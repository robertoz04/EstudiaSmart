package com.estudiasmart.app.data

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.estudiasmart.app.model.IntentoQuiz
import com.estudiasmart.app.model.PreguntaQuiz
import com.estudiasmart.app.model.Quiz
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions

object QuizRepository {

    // =========================================================
    // FIREBASE
    // =========================================================

    private val auth =
        FirebaseAuth.getInstance()

    private val firestore =
        FirebaseFirestore.getInstance()

    private var quizzesListener:
            ListenerRegistration? = null

    private var intentosListener:
            ListenerRegistration? = null

    private var uidSincronizado:
            String? = null

    // =========================================================
    // LISTAS LOCALES
    // =========================================================

    private val _quizzes =
        mutableStateListOf<Quiz>()

    val quizzes: List<Quiz>
        get() {
            asegurarSincronizacionFirestore()
            return _quizzes
        }

    private val _intentos =
        mutableStateListOf<IntentoQuiz>()

    val intentos: List<IntentoQuiz>
        get() {
            asegurarSincronizacionFirestore()
            return _intentos
        }

    private var siguienteQuizId = 1
    private var siguientePreguntaId = 1
    private var siguienteIntentoId = 1

    // =========================================================
    // SINCRONIZACIÓN
    // =========================================================

    private fun asegurarSincronizacionFirestore() {

        val uidActual =
            auth.currentUser?.uid

        if (uidActual == null) {

            if (uidSincronizado != null) {
                limpiarDatosLocales()
            }

            return
        }

        if (
            uidSincronizado == uidActual &&
            quizzesListener != null &&
            intentosListener != null
        ) {
            return
        }

        iniciarSincronizacionUsuario(
            uidActual
        )
    }

    private fun iniciarSincronizacionUsuario(
        uid: String
    ) {

        quizzesListener?.remove()
        intentosListener?.remove()

        quizzesListener = null
        intentosListener = null

        uidSincronizado = uid

        _quizzes.clear()
        _intentos.clear()

        siguienteQuizId = 1
        siguientePreguntaId = 1
        siguienteIntentoId = 1

        escucharQuizzes(uid)
        escucharIntentos(uid)
    }

    // =========================================================
    // LISTENER DE QUIZZES
    // =========================================================

    private fun escucharQuizzes(
        uid: String
    ) {

        quizzesListener =
            referenciaQuizzes(uid)
                .orderBy("id")
                .addSnapshotListener { snapshot, error ->

                    if (error != null) {

                        Log.e(
                            "QuizRepository",
                            "Error cargando quizzes",
                            error
                        )

                        return@addSnapshotListener
                    }

                    if (snapshot == null) {
                        return@addSnapshotListener
                    }

                    val lista =
                        snapshot.documents.mapNotNull { documento ->

                            val id =
                                documento
                                    .getLong("id")
                                    ?.toInt()
                                    ?: documento.id.toIntOrNull()
                                    ?: return@mapNotNull null

                            val temaId =
                                documento
                                    .getLong("temaId")
                                    ?.toInt()
                                    ?: return@mapNotNull null

                            val titulo =
                                documento
                                    .getString("titulo")
                                    ?: ""

                            val preguntasRaw =
                                documento.get("preguntas")
                                        as? List<*>
                                    ?: emptyList<Any>()

                            val preguntas =
                                preguntasRaw.mapNotNull { elemento ->

                                    val mapa =
                                        elemento as? Map<*, *>
                                            ?: return@mapNotNull null

                                    val preguntaId =
                                        (
                                                mapa["id"]
                                                        as? Number
                                                )?.toInt()
                                            ?: return@mapNotNull null

                                    val enunciado =
                                        mapa["enunciado"]
                                                as? String
                                            ?: ""

                                    val opcionesRaw =
                                        mapa["opciones"]
                                                as? List<*>
                                            ?: emptyList<Any>()

                                    val opciones =
                                        opcionesRaw.mapNotNull {
                                            it as? String
                                        }

                                    val respuestaCorrecta =
                                        (
                                                mapa["respuestaCorrecta"]
                                                        as? Number
                                                )?.toInt()
                                            ?: 0

                                    PreguntaQuiz(
                                        id = preguntaId,
                                        enunciado = enunciado,
                                        opciones = opciones,
                                        respuestaCorrecta =
                                            respuestaCorrecta
                                    )
                                }

                            Quiz(
                                id = id,
                                temaId = temaId,
                                titulo = titulo,
                                preguntas = preguntas
                            )
                        }

                    _quizzes.clear()
                    _quizzes.addAll(lista)

                    siguienteQuizId =
                        (
                                _quizzes.maxOfOrNull {
                                    it.id
                                } ?: 0
                                ) + 1

                    siguientePreguntaId =
                        (
                                _quizzes
                                    .flatMap {
                                        it.preguntas
                                    }
                                    .maxOfOrNull {
                                        it.id
                                    }
                                    ?: 0
                                ) + 1
                }
    }

    // =========================================================
    // LISTENER DEL HISTORIAL
    // =========================================================

    private fun escucharIntentos(
        uid: String
    ) {

        intentosListener =
            referenciaHistorial(uid)
                .orderBy("id")
                .addSnapshotListener { snapshot, error ->

                    if (error != null) {

                        Log.e(
                            "QuizRepository",
                            "Error cargando historial de quizzes",
                            error
                        )

                        return@addSnapshotListener
                    }

                    if (snapshot == null) {
                        return@addSnapshotListener
                    }

                    val lista =
                        snapshot.documents.mapNotNull { documento ->

                            val id =
                                documento
                                    .getLong("id")
                                    ?.toInt()
                                    ?: documento.id.toIntOrNull()
                                    ?: return@mapNotNull null

                            val quizId =
                                documento
                                    .getLong("quizId")
                                    ?.toInt()
                                    ?: return@mapNotNull null

                            val respuestasCorrectas =
                                documento
                                    .getLong(
                                        "respuestasCorrectas"
                                    )
                                    ?.toInt()
                                    ?: 0

                            val totalPreguntas =
                                documento
                                    .getLong(
                                        "totalPreguntas"
                                    )
                                    ?.toInt()
                                    ?: 0

                            val porcentaje =
                                documento
                                    .getDouble("porcentaje")
                                    ?: 0.0

                            val fechaMillis =
                                documento
                                    .getLong("fechaMillis")
                                    ?: System.currentTimeMillis()

                            IntentoQuiz(
                                id = id,
                                quizId = quizId,
                                respuestasCorrectas =
                                    respuestasCorrectas,
                                totalPreguntas =
                                    totalPreguntas,
                                porcentaje = porcentaje,
                                fechaMillis = fechaMillis
                            )
                        }

                    _intentos.clear()
                    _intentos.addAll(lista)

                    siguienteIntentoId =
                        (
                                _intentos.maxOfOrNull {
                                    it.id
                                } ?: 0
                                ) + 1
                }
    }

    // =========================================================
    // REFERENCIAS FIRESTORE
    // =========================================================

    private fun referenciaQuizzes(
        uid: String
    ) =
        firestore
            .collection("usuarios")
            .document(uid)
            .collection("quizzes")

    private fun referenciaHistorial(
        uid: String
    ) =
        firestore
            .collection("usuarios")
            .document(uid)
            .collection("historialQuiz")

    // =========================================================
    // GUARDAR QUIZ
    // =========================================================

    private fun guardarQuizFirestore(
        uid: String,
        quiz: Quiz,
        esNuevo: Boolean
    ) {

        val preguntasFirestore =
            quiz.preguntas.map { pregunta ->

                hashMapOf<String, Any>(
                    "id" to pregunta.id,
                    "enunciado" to pregunta.enunciado,
                    "opciones" to pregunta.opciones,
                    "respuestaCorrecta" to
                            pregunta.respuestaCorrecta
                )
            }

        val datos =
            hashMapOf<String, Any>(
                "id" to quiz.id,
                "temaId" to quiz.temaId,
                "titulo" to quiz.titulo,
                "preguntas" to preguntasFirestore,
                "actualizadoEn" to
                        FieldValue.serverTimestamp()
            )

        if (esNuevo) {

            datos["creadoEn"] =
                FieldValue.serverTimestamp()
        }

        referenciaQuizzes(uid)
            .document(
                quiz.id.toString()
            )
            .set(
                datos,
                SetOptions.merge()
            )
            .addOnFailureListener { error ->

                Log.e(
                    "QuizRepository",
                    "Error guardando quiz",
                    error
                )
            }
    }

    // =========================================================
    // GUARDAR INTENTO / HISTORIAL
    // =========================================================

    private fun guardarIntentoFirestore(
        uid: String,
        intento: IntentoQuiz
    ) {

        val datos =
            hashMapOf<String, Any>(
                "id" to intento.id,
                "quizId" to intento.quizId,
                "respuestasCorrectas" to
                        intento.respuestasCorrectas,
                "totalPreguntas" to
                        intento.totalPreguntas,
                "porcentaje" to
                        intento.porcentaje,
                "fechaMillis" to
                        intento.fechaMillis,
                "creadoEn" to
                        FieldValue.serverTimestamp()
            )

        referenciaHistorial(uid)
            .document(
                intento.id.toString()
            )
            .set(datos)
            .addOnFailureListener { error ->

                Log.e(
                    "QuizRepository",
                    "Error guardando intento de quiz",
                    error
                )
            }
    }

    // =========================================================
    // LIMPIAR DATOS LOCALES
    // =========================================================

    fun limpiarDatosLocales() {

        quizzesListener?.remove()
        intentosListener?.remove()

        quizzesListener = null
        intentosListener = null

        uidSincronizado = null

        _quizzes.clear()
        _intentos.clear()

        siguienteQuizId = 1
        siguientePreguntaId = 1
        siguienteIntentoId = 1
    }

    // =========================================================
    // CREAR QUIZ
    // =========================================================

    fun crearQuiz(
        temaId: Int,
        titulo: String
    ): String? {

        asegurarSincronizacionFirestore()

        val uid =
            auth.currentUser?.uid
                ?: return "Debes iniciar sesión para crear un quiz."

        val tituloLimpio =
            titulo.trim()

        if (
            AcademicRepository
                .obtenerTema(temaId) == null
        ) {
            return "El tema seleccionado no existe."
        }

        if (tituloLimpio.isBlank()) {
            return "El título del quiz es obligatorio."
        }

        if (tituloLimpio.length < 3) {
            return "El título debe tener al menos 3 caracteres."
        }

        val duplicado =
            _quizzes.any {

                it.temaId == temaId &&
                        it.titulo.equals(
                            tituloLimpio,
                            ignoreCase = true
                        )
            }

        if (duplicado) {
            return "Ya existe un quiz con ese título en este tema."
        }

        val nuevoId =
            maxOf(
                siguienteQuizId,
                (
                        _quizzes.maxOfOrNull {
                            it.id
                        } ?: 0
                        ) + 1
            )

        siguienteQuizId =
            nuevoId + 1

        val quiz =
            Quiz(
                id = nuevoId,
                temaId = temaId,
                titulo = tituloLimpio
            )

        _quizzes.add(
            quiz
        )

        guardarQuizFirestore(
            uid = uid,
            quiz = quiz,
            esNuevo = true
        )

        return null
    }

    // =========================================================
    // AGREGAR PREGUNTA
    // =========================================================

    fun agregarPregunta(
        quizId: Int,
        enunciado: String,
        opcion1: String,
        opcion2: String,
        opcion3: String,
        opcion4: String,
        respuestaCorrecta: Int
    ): String? {

        asegurarSincronizacionFirestore()

        val uid =
            auth.currentUser?.uid
                ?: return "Debes iniciar sesión para agregar preguntas."

        val indiceQuiz =
            _quizzes.indexOfFirst {
                it.id == quizId
            }

        if (indiceQuiz == -1) {
            return "Quiz no encontrado."
        }

        val preguntaLimpia =
            enunciado.trim()

        val opciones =
            listOf(
                opcion1.trim(),
                opcion2.trim(),
                opcion3.trim(),
                opcion4.trim()
            )

        if (preguntaLimpia.isBlank()) {
            return "La pregunta es obligatoria."
        }

        if (
            opciones.any {
                it.isBlank()
            }
        ) {
            return "Debes completar las cuatro opciones."
        }

        if (respuestaCorrecta !in 0..3) {
            return "Selecciona una respuesta correcta."
        }

        val quizActual =
            _quizzes[indiceQuiz]

        if (
            quizActual.preguntas.any {

                it.enunciado.equals(
                    preguntaLimpia,
                    ignoreCase = true
                )
            }
        ) {
            return "Esta pregunta ya existe en el quiz."
        }

        val nuevoPreguntaId =
            maxOf(
                siguientePreguntaId,
                (
                        _quizzes
                            .flatMap {
                                it.preguntas
                            }
                            .maxOfOrNull {
                                it.id
                            }
                            ?: 0
                        ) + 1
            )

        siguientePreguntaId =
            nuevoPreguntaId + 1

        val nuevaPregunta =
            PreguntaQuiz(
                id = nuevoPreguntaId,
                enunciado = preguntaLimpia,
                opciones = opciones,
                respuestaCorrecta =
                    respuestaCorrecta
            )

        val quizActualizado =
            quizActual.copy(
                preguntas =
                    quizActual.preguntas +
                            nuevaPregunta
            )

        _quizzes[indiceQuiz] =
            quizActualizado

        guardarQuizFirestore(
            uid = uid,
            quiz = quizActualizado,
            esNuevo = false
        )

        return null
    }

    // =========================================================
    // ELIMINAR PREGUNTA
    // =========================================================

    fun eliminarPregunta(
        quizId: Int,
        preguntaId: Int
    ): Boolean {

        asegurarSincronizacionFirestore()

        val uid =
            auth.currentUser?.uid
                ?: return false

        val indiceQuiz =
            _quizzes.indexOfFirst {
                it.id == quizId
            }

        if (indiceQuiz == -1) {
            return false
        }

        val quizActual =
            _quizzes[indiceQuiz]

        val nuevasPreguntas =
            quizActual.preguntas.filter {
                it.id != preguntaId
            }

        if (
            nuevasPreguntas.size ==
            quizActual.preguntas.size
        ) {
            return false
        }

        val quizActualizado =
            quizActual.copy(
                preguntas = nuevasPreguntas
            )

        _quizzes[indiceQuiz] =
            quizActualizado

        guardarQuizFirestore(
            uid = uid,
            quiz = quizActualizado,
            esNuevo = false
        )

        return true
    }

    // =========================================================
    // ELIMINAR QUIZ
    // =========================================================

    fun eliminarQuiz(
        quizId: Int
    ): Boolean {

        asegurarSincronizacionFirestore()

        val uid =
            auth.currentUser?.uid
                ?: return false

        val intentosRelacionados =
            _intentos.filter {
                it.quizId == quizId
            }

        intentosRelacionados.forEach { intento ->

            referenciaHistorial(uid)
                .document(
                    intento.id.toString()
                )
                .delete()
                .addOnFailureListener { error ->

                    Log.e(
                        "QuizRepository",
                        "Error eliminando intento relacionado",
                        error
                    )
                }
        }

        _intentos.removeAll {
            it.quizId == quizId
        }

        val eliminado =
            _quizzes.removeAll {
                it.id == quizId
            }

        if (eliminado) {

            referenciaQuizzes(uid)
                .document(
                    quizId.toString()
                )
                .delete()
                .addOnFailureListener { error ->

                    Log.e(
                        "QuizRepository",
                        "Error eliminando quiz",
                        error
                    )
                }
        }

        return eliminado
    }

    // =========================================================
    // ELIMINAR QUIZZES DE UN TEMA
    // =========================================================

    fun eliminarPorTema(
        temaId: Int
    ) {

        asegurarSincronizacionFirestore()

        val quizzesRelacionados =
            _quizzes
                .filter {
                    it.temaId == temaId
                }
                .map {
                    it.id
                }

        quizzesRelacionados.forEach { quizId ->

            eliminarQuiz(
                quizId
            )
        }
    }

    // =========================================================
    // OBTENER QUIZ
    // =========================================================

    fun obtenerQuiz(
        quizId: Int
    ): Quiz? {

        asegurarSincronizacionFirestore()

        return _quizzes.find {
            it.id == quizId
        }
    }

    // =========================================================
    // QUIZZES POR TEMA
    // =========================================================

    fun quizzesPorTema(
        temaId: Int
    ): List<Quiz> {

        asegurarSincronizacionFirestore()

        return _quizzes.filter {
            it.temaId == temaId
        }
    }

    // =========================================================
    // REGISTRAR INTENTO
    // =========================================================

    fun registrarIntento(
        quizId: Int,
        respuestas: Map<Int, Int>
    ): IntentoQuiz? {

        asegurarSincronizacionFirestore()

        val uid =
            auth.currentUser?.uid
                ?: return null

        val quiz =
            obtenerQuiz(quizId)
                ?: return null

        if (quiz.preguntas.isEmpty()) {
            return null
        }

        val correctas =
            quiz.preguntas.count { pregunta ->

                respuestas[pregunta.id] ==
                        pregunta.respuestaCorrecta
            }

        val porcentaje =
            (
                    correctas.toDouble() /
                            quiz.preguntas.size.toDouble()
                    ) * 100.0

        val nuevoId =
            maxOf(
                siguienteIntentoId,
                (
                        _intentos.maxOfOrNull {
                            it.id
                        } ?: 0
                        ) + 1
            )

        siguienteIntentoId =
            nuevoId + 1

        val intento =
            IntentoQuiz(
                id = nuevoId,
                quizId = quizId,
                respuestasCorrectas =
                    correctas,
                totalPreguntas =
                    quiz.preguntas.size,
                porcentaje =
                    porcentaje
            )

        _intentos.add(
            intento
        )

        guardarIntentoFirestore(
            uid = uid,
            intento = intento
        )

        return intento
    }

    // =========================================================
    // HISTORIAL DE UN QUIZ
    // =========================================================

    fun intentosDeQuiz(
        quizId: Int
    ): List<IntentoQuiz> {

        asegurarSincronizacionFirestore()

        return _intentos
            .filter {
                it.quizId == quizId
            }
            .sortedByDescending {
                it.fechaMillis
            }
    }

    // =========================================================
    // MEJOR NOTA
    // =========================================================

    fun mejorNota(
        quizId: Int
    ): Double? {

        asegurarSincronizacionFirestore()

        return intentosDeQuiz(
            quizId
        ).maxOfOrNull {
            it.porcentaje
        }
    }
}
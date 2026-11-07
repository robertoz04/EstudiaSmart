package com.estudiasmart.app.data

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.estudiasmart.app.model.Dificultad
import com.estudiasmart.app.model.Flashcard
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions

object FlashcardRepository {

    // =========================
    // FIREBASE
    // =========================

    private val auth =
        FirebaseAuth.getInstance()

    private val firestore =
        FirebaseFirestore.getInstance()

    private var flashcardsListener:
            ListenerRegistration? = null

    private var uidSincronizado:
            String? = null

    // =========================
    // LISTA LOCAL
    // =========================

    private val _flashcards =
        mutableStateListOf<Flashcard>()

    val flashcards: List<Flashcard>
        get() {
            asegurarSincronizacionFirestore()
            return _flashcards
        }

    private var siguienteId = 1

    // =========================================================
    // SINCRONIZACIÓN FIRESTORE
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
            flashcardsListener != null
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

        flashcardsListener?.remove()

        flashcardsListener = null

        uidSincronizado = uid

        _flashcards.clear()

        siguienteId = 1

        escucharFlashcards(uid)
    }

    // =========================================================
    // LISTENER FIRESTORE
    // =========================================================

    private fun escucharFlashcards(
        uid: String
    ) {

        flashcardsListener =
            referenciaFlashcards(uid)
                .orderBy("id")
                .addSnapshotListener { snapshot, error ->

                    if (error != null) {

                        Log.e(
                            "FlashcardRepository",
                            "Error cargando flashcards",
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

                            val pregunta =
                                documento
                                    .getString("pregunta")
                                    ?: ""

                            val respuesta =
                                documento
                                    .getString("respuesta")
                                    ?: ""

                            val dificultadTexto =
                                documento
                                    .getString("dificultad")
                                    ?: Dificultad.REGULAR.name

                            val dificultad =
                                try {
                                    Dificultad.valueOf(
                                        dificultadTexto
                                    )
                                } catch (
                                    exception: Exception
                                ) {
                                    Dificultad.REGULAR
                                }

                            val racha =
                                documento
                                    .getLong("racha")
                                    ?.toInt()
                                    ?: 0

                            val revisiones =
                                documento
                                    .getLong("revisiones")
                                    ?.toInt()
                                    ?: 0

                            val proximaRevisionMillis =
                                documento
                                    .getLong(
                                        "proximaRevisionMillis"
                                    )
                                    ?: System.currentTimeMillis()

                            Flashcard(
                                id = id,
                                temaId = temaId,
                                pregunta = pregunta,
                                respuesta = respuesta,
                                dificultad = dificultad,
                                racha = racha,
                                revisiones = revisiones,
                                proximaRevisionMillis =
                                    proximaRevisionMillis
                            )
                        }

                    _flashcards.clear()

                    _flashcards.addAll(
                        lista
                    )

                    siguienteId =
                        (
                                _flashcards
                                    .maxOfOrNull {
                                        it.id
                                    }
                                    ?: 0
                                ) + 1
                }
    }

    // =========================================================
    // REFERENCIA FIRESTORE
    // =========================================================

    private fun referenciaFlashcards(
        uid: String
    ) =
        firestore
            .collection("usuarios")
            .document(uid)
            .collection("flashcards")

    // =========================================================
    // GUARDAR FIRESTORE
    // =========================================================

    private fun guardarFlashcardFirestore(
        uid: String,
        flashcard: Flashcard,
        esNueva: Boolean
    ) {

        val datos =
            hashMapOf<String, Any>(
                "id" to flashcard.id,
                "temaId" to flashcard.temaId,
                "pregunta" to flashcard.pregunta,
                "respuesta" to flashcard.respuesta,
                "dificultad" to flashcard.dificultad.name,
                "racha" to flashcard.racha,
                "revisiones" to flashcard.revisiones,
                "proximaRevisionMillis" to
                        flashcard.proximaRevisionMillis,
                "actualizadoEn" to
                        FieldValue.serverTimestamp()
            )

        if (esNueva) {

            datos["creadoEn"] =
                FieldValue.serverTimestamp()
        }

        referenciaFlashcards(uid)
            .document(
                flashcard.id.toString()
            )
            .set(
                datos,
                SetOptions.merge()
            )
            .addOnFailureListener { error ->

                Log.e(
                    "FlashcardRepository",
                    "Error guardando flashcard",
                    error
                )
            }
    }

    // =========================================================
    // LIMPIEZA
    // =========================================================

    fun limpiarDatosLocales() {

        flashcardsListener?.remove()

        flashcardsListener = null

        uidSincronizado = null

        _flashcards.clear()

        siguienteId = 1
    }

    // =========================================================
    // CREAR
    // =========================================================

    fun crearFlashcard(
        temaId: Int,
        pregunta: String,
        respuesta: String
    ): String? {

        asegurarSincronizacionFirestore()

        val uid =
            auth.currentUser?.uid
                ?: return "Debes iniciar sesión para crear una flashcard."

        val preguntaLimpia =
            pregunta.trim()

        val respuestaLimpia =
            respuesta.trim()

        if (
            AcademicRepository
                .obtenerTema(temaId) == null
        ) {
            return "El tema seleccionado no existe."
        }

        if (preguntaLimpia.isBlank()) {
            return "La pregunta es obligatoria."
        }

        if (preguntaLimpia.length < 3) {
            return "La pregunta debe tener al menos 3 caracteres."
        }

        if (respuestaLimpia.isBlank()) {
            return "La respuesta es obligatoria."
        }

        val duplicada =
            _flashcards.any {

                it.temaId == temaId &&

                        it.pregunta.equals(
                            preguntaLimpia,
                            ignoreCase = true
                        )
            }

        if (duplicada) {
            return "Ya existe una flashcard con esa pregunta."
        }

        val nuevoId =
            maxOf(
                siguienteId,
                (
                        _flashcards
                            .maxOfOrNull {
                                it.id
                            }
                            ?: 0
                        ) + 1
            )

        siguienteId =
            nuevoId + 1

        val flashcard =
            Flashcard(
                id = nuevoId,
                temaId = temaId,
                pregunta = preguntaLimpia,
                respuesta = respuestaLimpia
            )

        _flashcards.add(
            flashcard
        )

        guardarFlashcardFirestore(
            uid = uid,
            flashcard = flashcard,
            esNueva = true
        )

        return null
    }

    // =========================================================
    // ACTUALIZAR
    // =========================================================

    fun actualizarFlashcard(
        id: Int,
        temaId: Int,
        pregunta: String,
        respuesta: String
    ): String? {

        asegurarSincronizacionFirestore()

        val uid =
            auth.currentUser?.uid
                ?: return "Debes iniciar sesión para actualizar una flashcard."

        val preguntaLimpia =
            pregunta.trim()

        val respuestaLimpia =
            respuesta.trim()

        if (
            AcademicRepository
                .obtenerTema(temaId) == null
        ) {
            return "El tema seleccionado no existe."
        }

        if (preguntaLimpia.isBlank()) {
            return "La pregunta es obligatoria."
        }

        if (preguntaLimpia.length < 3) {
            return "La pregunta debe tener al menos 3 caracteres."
        }

        if (respuestaLimpia.isBlank()) {
            return "La respuesta es obligatoria."
        }

        val indice =
            _flashcards.indexOfFirst {
                it.id == id
            }

        if (indice == -1) {
            return "Flashcard no encontrada."
        }

        val duplicada =
            _flashcards.any {

                it.id != id &&
                        it.temaId == temaId &&

                        it.pregunta.equals(
                            preguntaLimpia,
                            ignoreCase = true
                        )
            }

        if (duplicada) {
            return "Ya existe otra flashcard con esa pregunta."
        }

        val actual =
            _flashcards[indice]

        val actualizada =
            actual.copy(
                temaId = temaId,
                pregunta = preguntaLimpia,
                respuesta = respuestaLimpia
            )

        _flashcards[indice] =
            actualizada

        guardarFlashcardFirestore(
            uid = uid,
            flashcard = actualizada,
            esNueva = false
        )

        return null
    }

    // =========================================================
    // ELIMINAR
    // =========================================================

    fun eliminarFlashcard(
        id: Int
    ): Boolean {

        asegurarSincronizacionFirestore()

        val uid =
            auth.currentUser?.uid
                ?: return false

        val eliminada =
            _flashcards.removeAll {
                it.id == id
            }

        if (eliminada) {

            referenciaFlashcards(uid)
                .document(
                    id.toString()
                )
                .delete()
                .addOnFailureListener { error ->

                    Log.e(
                        "FlashcardRepository",
                        "Error eliminando flashcard",
                        error
                    )
                }
        }

        return eliminada
    }

    // =========================================================
    // REGISTRAR REPASO
    // =========================================================

    fun registrarRepaso(
        id: Int,
        dificultad: Dificultad
    ): String? {

        asegurarSincronizacionFirestore()

        val uid =
            auth.currentUser?.uid
                ?: return "Debes iniciar sesión para registrar el repaso."

        val indice =
            _flashcards.indexOfFirst {
                it.id == id
            }

        if (indice == -1) {
            return "Flashcard no encontrada."
        }

        val actual =
            _flashcards[indice]

        val nuevaRacha =
            when (dificultad) {

                Dificultad.DIFICIL ->
                    0

                Dificultad.REGULAR,
                Dificultad.FACIL ->
                    actual.racha + 1
            }

        val multiplicador =
            maxOf(
                1,
                nuevaRacha
            )

        val dias =
            dificultad.diasBase *
                    multiplicador

        val milisegundosPorDia =
            24L *
                    60L *
                    60L *
                    1000L

        val proximaRevision =
            System.currentTimeMillis() +
                    (dias * milisegundosPorDia)

        val actualizada =
            actual.copy(
                dificultad = dificultad,
                racha = nuevaRacha,
                revisiones =
                    actual.revisiones + 1,
                proximaRevisionMillis =
                    proximaRevision
            )

        _flashcards[indice] =
            actualizada

        guardarFlashcardFirestore(
            uid = uid,
            flashcard = actualizada,
            esNueva = false
        )

        return null
    }

    // =========================================================
    // CONSULTAS
    // =========================================================

    fun pendientesHoy(): List<Flashcard> {

        asegurarSincronizacionFirestore()

        val ahora =
            System.currentTimeMillis()

        return _flashcards.filter {
            it.proximaRevisionMillis <= ahora
        }
    }

    fun flashcardsPorTema(
        temaId: Int
    ): List<Flashcard> {

        asegurarSincronizacionFirestore()

        return _flashcards.filter {
            it.temaId == temaId
        }
    }

    fun obtenerFlashcard(
        id: Int
    ): Flashcard? {

        asegurarSincronizacionFirestore()

        return _flashcards.find {
            it.id == id
        }
    }

    // =========================================================
    // ELIMINAR FLASHCARDS DE UN TEMA
    // =========================================================

    fun eliminarPorTema(
        temaId: Int
    ) {

        asegurarSincronizacionFirestore()

        val uid =
            auth.currentUser?.uid

        val relacionadas =
            _flashcards.filter {
                it.temaId == temaId
            }

        if (uid != null) {

            relacionadas.forEach { flashcard ->

                referenciaFlashcards(uid)
                    .document(
                        flashcard.id.toString()
                    )
                    .delete()
                    .addOnFailureListener { error ->

                        Log.e(
                            "FlashcardRepository",
                            "Error eliminando flashcard relacionada",
                            error
                        )
                    }
            }
        }

        _flashcards.removeAll {
            it.temaId == temaId
        }
    }

    // =========================================================
    // ESTADÍSTICAS
    // =========================================================

    fun totalRevisiones(): Int {

        asegurarSincronizacionFirestore()

        return _flashcards.sumOf {
            it.revisiones
        }
    }

    fun flashcardsEstudiadas(): Int {

        asegurarSincronizacionFirestore()

        return _flashcards.count {
            it.revisiones > 0
        }
    }
}
package com.estudiasmart.app.data

import androidx.compose.runtime.mutableStateListOf
import com.estudiasmart.app.model.IntentoQuiz
import com.estudiasmart.app.model.PreguntaQuiz
import com.estudiasmart.app.model.Quiz

object QuizRepository {

    private val _quizzes =
        mutableStateListOf<Quiz>()

    val quizzes: List<Quiz>
        get() = _quizzes

    private val _intentos =
        mutableStateListOf<IntentoQuiz>()

    val intentos: List<IntentoQuiz>
        get() = _intentos

    private var siguienteQuizId = 1
    private var siguientePreguntaId = 1
    private var siguienteIntentoId = 1

    fun crearQuiz(
        temaId: Int,
        titulo: String
    ): String? {

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

        _quizzes.add(
            Quiz(
                id = siguienteQuizId++,
                temaId = temaId,
                titulo = tituloLimpio
            )
        )

        return null
    }

    fun agregarPregunta(
        quizId: Int,
        enunciado: String,
        opcion1: String,
        opcion2: String,
        opcion3: String,
        opcion4: String,
        respuestaCorrecta: Int
    ): String? {

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

        val nuevaPregunta =
            PreguntaQuiz(
                id = siguientePreguntaId++,
                enunciado = preguntaLimpia,
                opciones = opciones,
                respuestaCorrecta = respuestaCorrecta
            )

        _quizzes[indiceQuiz] =
            quizActual.copy(
                preguntas =
                    quizActual.preguntas +
                            nuevaPregunta
            )

        return null
    }

    fun eliminarPregunta(
        quizId: Int,
        preguntaId: Int
    ): Boolean {

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

        _quizzes[indiceQuiz] =
            quizActual.copy(
                preguntas = nuevasPreguntas
            )

        return true
    }

    fun eliminarQuiz(
        quizId: Int
    ): Boolean {

        _intentos.removeAll {
            it.quizId == quizId
        }

        return _quizzes.removeAll {
            it.id == quizId
        }
    }

    fun obtenerQuiz(
        quizId: Int
    ): Quiz? {

        return _quizzes.find {
            it.id == quizId
        }
    }

    fun quizzesPorTema(
        temaId: Int
    ): List<Quiz> {

        return _quizzes.filter {
            it.temaId == temaId
        }
    }

    fun registrarIntento(
        quizId: Int,
        respuestas: Map<Int, Int>
    ): IntentoQuiz? {

        val quiz =
            obtenerQuiz(quizId)
                ?: return null

        if (quiz.preguntas.isEmpty()) {
            return null
        }

        val correctas =
            quiz.preguntas.count {
                    pregunta ->

                respuestas[pregunta.id] ==
                        pregunta.respuestaCorrecta
            }

        val porcentaje =
            (
                    correctas.toDouble() /
                            quiz.preguntas.size.toDouble()
                    ) * 100.0

        val intento =
            IntentoQuiz(
                id = siguienteIntentoId++,
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

        return intento
    }

    fun intentosDeQuiz(
        quizId: Int
    ): List<IntentoQuiz> {

        return _intentos.filter {
            it.quizId == quizId
        }
    }

    fun mejorNota(
        quizId: Int
    ): Double? {

        return intentosDeQuiz(
            quizId
        )
            .maxOfOrNull {
                it.porcentaje
            }
    }
}
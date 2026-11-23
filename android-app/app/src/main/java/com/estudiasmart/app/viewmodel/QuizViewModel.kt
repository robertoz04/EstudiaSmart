package com.estudiasmart.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.estudiasmart.app.data.AcademicRepository
import com.estudiasmart.app.data.QuizRepository
import com.estudiasmart.app.model.Quiz
import com.estudiasmart.app.model.Tema
import java.util.Locale

class QuizViewModel : ViewModel() {

    // =========================================================
    // DATOS
    // =========================================================

    val temas: List<Tema>
        get() = AcademicRepository.temas

    val quizzes: List<Quiz>
        get() = QuizRepository.quizzes

    fun nombreTema(
        temaId: Int
    ): String {

        return AcademicRepository
            .obtenerTema(temaId)
            ?.nombre
            ?: "Tema"
    }

    fun mejorNota(
        quizId: Int
    ): Double? {

        return QuizRepository
            .mejorNota(quizId)
    }

    // =========================================================
    // CREAR QUIZ
    // =========================================================

    var tituloQuiz by mutableStateOf("")
        private set

    private var _temaSeleccionadoId by
    mutableStateOf<Int?>(null)

    val temaSeleccionadoId: Int?
        get() =
            _temaSeleccionadoId
                ?: temas.firstOrNull()?.id

    var errorQuiz by mutableStateOf<String?>(null)
        private set

    fun cambiarTituloQuiz(
        valor: String
    ) {

        tituloQuiz = valor
        errorQuiz = null
    }

    fun seleccionarTema(
        temaId: Int
    ) {

        _temaSeleccionadoId = temaId
        errorQuiz = null
    }

    fun crearQuiz() {

        val temaId =
            temaSeleccionadoId

        if (temaId == null) {

            errorQuiz =
                "Selecciona un tema."

            return
        }

        val resultado =
            QuizRepository.crearQuiz(
                temaId = temaId,
                titulo = tituloQuiz
            )

        if (resultado == null) {

            tituloQuiz = ""
            errorQuiz = null

        } else {

            errorQuiz = resultado
        }
    }

    // =========================================================
    // ADMINISTRAR / RESOLVER
    // =========================================================

    var quizAdministrandoId by
    mutableStateOf<Int?>(null)
        private set

    var quizResolviendoId by
    mutableStateOf<Int?>(null)
        private set

    val quizAdministrando: Quiz?
        get() =
            quizAdministrandoId?.let {
                QuizRepository
                    .obtenerQuiz(it)
            }

    val quizResolviendo: Quiz?
        get() =
            quizResolviendoId?.let {
                QuizRepository
                    .obtenerQuiz(it)
            }

    fun administrarQuiz(
        quiz: Quiz
    ) {

        quizAdministrandoId =
            quiz.id

        quizResolviendoId =
            null

        limpiarFormularioPregunta()

        respuestasUsuario =
            emptyMap()

        resultadoTexto =
            null
    }

    fun resolverQuiz(
        quiz: Quiz
    ) {

        quizResolviendoId =
            quiz.id

        quizAdministrandoId =
            null

        respuestasUsuario =
            emptyMap()

        resultadoTexto =
            null
    }

    // =========================================================
    // PREGUNTAS
    // =========================================================

    var enunciado by mutableStateOf("")
        private set

    var opcion1 by mutableStateOf("")
        private set

    var opcion2 by mutableStateOf("")
        private set

    var opcion3 by mutableStateOf("")
        private set

    var opcion4 by mutableStateOf("")
        private set

    var respuestaCorrecta by
    mutableStateOf<Int?>(null)
        private set

    var errorPregunta by
    mutableStateOf<String?>(null)
        private set

    fun cambiarEnunciado(
        valor: String
    ) {

        enunciado = valor
        errorPregunta = null
    }

    fun cambiarOpcion1(
        valor: String
    ) {

        opcion1 = valor
        errorPregunta = null
    }

    fun cambiarOpcion2(
        valor: String
    ) {

        opcion2 = valor
        errorPregunta = null
    }

    fun cambiarOpcion3(
        valor: String
    ) {

        opcion3 = valor
        errorPregunta = null
    }

    fun cambiarOpcion4(
        valor: String
    ) {

        opcion4 = valor
        errorPregunta = null
    }

    fun seleccionarRespuestaCorrecta(
        indice: Int
    ) {

        respuestaCorrecta = indice
        errorPregunta = null
    }

    fun agregarPregunta() {

        val quiz =
            quizAdministrando

        if (quiz == null) {

            errorPregunta =
                "Selecciona un quiz para administrar."

            return
        }

        val correcta =
            respuestaCorrecta

        if (correcta == null) {

            errorPregunta =
                "Selecciona la respuesta correcta."

            return
        }

        val resultado =
            QuizRepository
                .agregarPregunta(
                    quizId = quiz.id,
                    enunciado = enunciado,
                    opcion1 = opcion1,
                    opcion2 = opcion2,
                    opcion3 = opcion3,
                    opcion4 = opcion4,
                    respuestaCorrecta =
                        correcta
                )

        if (resultado == null) {

            limpiarFormularioPregunta()

        } else {

            errorPregunta =
                resultado
        }
    }

    fun eliminarPregunta(
        quizId: Int,
        preguntaId: Int
    ) {

        QuizRepository
            .eliminarPregunta(
                quizId = quizId,
                preguntaId = preguntaId
            )
    }

    private fun limpiarFormularioPregunta() {

        enunciado = ""
        opcion1 = ""
        opcion2 = ""
        opcion3 = ""
        opcion4 = ""

        respuestaCorrecta = null
        errorPregunta = null
    }

    // =========================================================
    // RESOLVER QUIZ
    // =========================================================

    var respuestasUsuario by
    mutableStateOf<Map<Int, Int>>(
        emptyMap()
    )
        private set

    var resultadoTexto by
    mutableStateOf<String?>(null)
        private set

    fun seleccionarRespuesta(
        preguntaId: Int,
        opcion: Int
    ) {

        respuestasUsuario =
            respuestasUsuario +
                    (preguntaId to opcion)

        resultadoTexto = null
    }

    fun finalizarQuiz() {

        val quiz =
            quizResolviendo

        if (quiz == null) {

            resultadoTexto =
                "No se encontró el quiz."

            return
        }

        if (
            quiz.preguntas.isEmpty()
        ) {

            resultadoTexto =
                "Este quiz todavía no tiene preguntas."

            return
        }

        if (
            respuestasUsuario.size <
            quiz.preguntas.size
        ) {

            resultadoTexto =
                "Debes responder todas las preguntas."

            return
        }

        val intento =
            QuizRepository
                .registrarIntento(
                    quizId = quiz.id,
                    respuestas =
                        respuestasUsuario
                )

        if (intento != null) {

            resultadoTexto =
                "Resultado: ${
                    String.format(
                        Locale.getDefault(),
                        "%.0f",
                        intento.porcentaje
                    )
                }% - ${intento.respuestasCorrectas}/${intento.totalPreguntas} correctas"

        } else {

            resultadoTexto =
                "No se pudo registrar el resultado."
        }
    }

    // =========================================================
    // ELIMINAR QUIZ
    // =========================================================

    var quizAEliminar by
    mutableStateOf<Quiz?>(null)
        private set

    fun solicitarEliminarQuiz(
        quiz: Quiz
    ) {

        quizAEliminar = quiz
    }

    fun cancelarEliminarQuiz() {

        quizAEliminar = null
    }

    fun confirmarEliminarQuiz() {

        val quiz =
            quizAEliminar
                ?: return

        val id =
            quiz.id

        QuizRepository
            .eliminarQuiz(id)

        if (
            quizAdministrandoId ==
            id
        ) {

            quizAdministrandoId =
                null
        }

        if (
            quizResolviendoId ==
            id
        ) {

            quizResolviendoId =
                null
        }

        limpiarFormularioPregunta()

        respuestasUsuario =
            emptyMap()

        resultadoTexto =
            null

        quizAEliminar =
            null
    }
}
package com.estudiasmart.app.data

import com.estudiasmart.app.model.Dificultad
import com.estudiasmart.app.model.EstadoTema
import com.estudiasmart.app.model.ProgresoTema

object ProgresoService {

    fun calcularProgresoTema(
        temaId: Int
    ): ProgresoTema? {

        val tema =
            AcademicRepository
                .obtenerTema(temaId)
                ?: return null

        val porcentajeQuiz =
            calcularQuiz(
                temaId
            )

        val porcentajeFlashcards =
            calcularFlashcards(
                temaId
            )

        val dominioTotal =
            (porcentajeQuiz * 0.70) +
                    (porcentajeFlashcards * 0.30)

        val estado =
            when {

                dominioTotal < 60.0 ->
                    EstadoTema.REQUIERE_REFUERZO

                dominioTotal < 80.0 ->
                    EstadoTema.EN_PROGRESO

                else ->
                    EstadoTema.DOMINIO_ALTO
            }

        return ProgresoTema(
            temaId = tema.id,
            nombreTema = tema.nombre,
            porcentajeQuiz = porcentajeQuiz,
            porcentajeFlashcards = porcentajeFlashcards,
            dominioTotal = dominioTotal,
            estado = estado
        )
    }

    private fun calcularQuiz(
        temaId: Int
    ): Double {

        val quizzes =
            QuizRepository
                .quizzesPorTema(
                    temaId
                )

        if (quizzes.isEmpty()) {
            return 0.0
        }

        val mejoresNotas =
            quizzes.mapNotNull { quiz ->

                QuizRepository
                    .mejorNota(
                        quiz.id
                    )
            }

        if (mejoresNotas.isEmpty()) {
            return 0.0
        }

        return mejoresNotas.average()
    }

    private fun calcularFlashcards(
        temaId: Int
    ): Double {

        val flashcards =
            FlashcardRepository
                .flashcardsPorTema(
                    temaId
                )

        if (flashcards.isEmpty()) {
            return 0.0
        }

        val valores =
            flashcards.map { flashcard ->

                if (flashcard.revisiones == 0) {

                    0.0

                } else {

                    when (
                        flashcard.dificultad
                    ) {

                        Dificultad.DIFICIL ->
                            40.0

                        Dificultad.REGULAR ->
                            70.0

                        Dificultad.FACIL ->
                            100.0
                    }
                }
            }

        return valores.average()
    }

    fun obtenerTodosLosProgresos():
            List<ProgresoTema> {

        return AcademicRepository
            .temas
            .mapNotNull { tema ->

                calcularProgresoTema(
                    tema.id
                )
            }
    }

    fun calcularPromedioGeneral():
            Double {

        val progresos =
            obtenerTodosLosProgresos()

        if (progresos.isEmpty()) {
            return 0.0
        }

        return progresos
            .map {
                it.dominioTotal
            }
            .average()
    }

    fun temasRequierenRefuerzo():
            List<ProgresoTema> {

        return obtenerTodosLosProgresos()
            .filter {
                it.estado ==
                        EstadoTema.REQUIERE_REFUERZO
            }
    }

    fun temasEnProgreso():
            List<ProgresoTema> {

        return obtenerTodosLosProgresos()
            .filter {
                it.estado ==
                        EstadoTema.EN_PROGRESO
            }
    }

    fun temasDominioAlto():
            List<ProgresoTema> {

        return obtenerTodosLosProgresos()
            .filter {
                it.estado ==
                        EstadoTema.DOMINIO_ALTO
            }
    }
}
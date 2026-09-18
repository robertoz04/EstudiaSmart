package service

import model.IntentoQuiz
import model.Quiz

class QuizService {

    fun evaluarQuiz(
        quiz: Quiz,
        respuestasUsuario: Map<Int, Char>,
        idIntento: Int
    ): IntentoQuiz {

        if (quiz.preguntas.isEmpty()) {
            throw IllegalArgumentException(
                "El quiz debe contener al menos una pregunta."
            )
        }

        var correctas = 0
        val falladas = mutableListOf<Int>()

        quiz.preguntas.forEach { pregunta ->

            val respuestaUsuario =
                respuestasUsuario[pregunta.id]?.uppercaseChar()

            if (respuestaUsuario == pregunta.respuestaCorrecta.uppercaseChar()) {
                correctas++
            } else {
                falladas.add(pregunta.id)
            }
        }

        val puntuacion =
            (correctas.toDouble() / quiz.preguntas.size) * 100

        return IntentoQuiz(
            id = idIntento,
            quizId = quiz.id,
            respuestasCorrectas = correctas,
            totalPreguntas = quiz.preguntas.size,
            puntuacion = puntuacion,
            preguntasFalladas = falladas
        )
    }

    fun obtenerNivelDominio(puntuacion: Double): String {
        return when {
            puntuacion < 60 -> "REQUIERE REFUERZO"
            puntuacion < 80 -> "EN PROGRESO"
            else -> "DOMINIO ALTO"
        }
    }
}
package model

import java.time.LocalDateTime

data class IntentoQuiz(
    val id: Int,
    val quizId: Int,
    val respuestasCorrectas: Int,
    val totalPreguntas: Int,
    val puntuacion: Double,
    val preguntasFalladas: List<Int>,
    val fecha: LocalDateTime = LocalDateTime.now()
)
package com.estudiasmart.app.model

data class IntentoQuiz(
    val id: Int,
    val quizId: Int,
    val respuestasCorrectas: Int,
    val totalPreguntas: Int,
    val porcentaje: Double,
    val fechaMillis: Long = System.currentTimeMillis()
)
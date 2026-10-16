package com.estudiasmart.app.model

data class Quiz(
    val id: Int,
    val temaId: Int,
    val titulo: String,
    val preguntas: List<PreguntaQuiz> = emptyList()
)
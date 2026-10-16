package com.estudiasmart.app.model

data class ProgresoTema(
    val temaId: Int,
    val nombreTema: String,
    val porcentajeQuiz: Double,
    val porcentajeFlashcards: Double,
    val dominioTotal: Double,
    val estado: EstadoTema
)
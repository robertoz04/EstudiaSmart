package com.estudiasmart.app.model

data class Flashcard(
    val id: Int,
    val temaId: Int,
    val pregunta: String,
    val respuesta: String,
    val dificultad: Dificultad = Dificultad.REGULAR,
    val racha: Int = 0,
    val proximaRevisionMillis: Long =
        System.currentTimeMillis()
)
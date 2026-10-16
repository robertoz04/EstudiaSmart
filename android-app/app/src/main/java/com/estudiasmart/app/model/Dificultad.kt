package com.estudiasmart.app.model

enum class Dificultad(
    val diasBase: Int,
    val etiqueta: String
) {
    DIFICIL(
        diasBase = 1,
        etiqueta = "Difícil"
    ),

    REGULAR(
        diasBase = 3,
        etiqueta = "Regular"
    ),

    FACIL(
        diasBase = 7,
        etiqueta = "Fácil"
    )
}
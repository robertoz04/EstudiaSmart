package model

data class Pregunta(
    val id: Int,
    val quizId: Int,
    var enunciado: String,
    var opcionA: String,
    var opcionB: String,
    var opcionC: String,
    var opcionD: String,
    var respuestaCorrecta: Char
)
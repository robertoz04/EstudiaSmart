package com.estudiasmart.app.model

data class PreguntaQuiz(
    val id: Int,
    val enunciado: String,
    val opciones: List<String>,
    val respuestaCorrecta: Int
)
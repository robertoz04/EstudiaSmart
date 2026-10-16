package com.estudiasmart.app.model

data class RecomendacionEstudio(
    val temaId: Int,
    val nombreTema: String,
    val dominioActual: Double,
    val debilidadDominio: Double,
    val urgenciaRepaso: Double,
    val dificultad: Double,
    val puntuacion: Double,
    val prioridad: PrioridadRecomendacion,
    val motivo: String
)
package model

data class RecomendacionEstudio(
    val temaId: Int,
    val nombreTema: String,
    val dominio: Double,
    val diasSinRepaso: Long,
    val dificultad: Dificultad,
    val prioridad: Double
)
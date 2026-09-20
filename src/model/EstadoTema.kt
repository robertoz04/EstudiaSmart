package model

import java.time.LocalDate

data class EstadoTema(
    val temaId: Int,
    val nombreTema: String,
    val dominio: Double,
    val ultimaRevision: LocalDate?,
    val dificultad: Dificultad
)
package model

data class Quiz(
    val id: Int,
    val temaId: Int,
    var nombre: String,
    val preguntas: MutableList<Pregunta> = mutableListOf()
)
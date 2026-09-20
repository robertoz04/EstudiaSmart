package model

data class Materia(
    val id: Int,
    val estudianteId: Int,
    var nombre: String,
    var descripcion: String
)
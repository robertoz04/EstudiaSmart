package model

data class Tema(
    val id: Int,
    val materiaId: Int,
    val estudianteId: Int,
    var nombre: String,
    var descripcion: String
)
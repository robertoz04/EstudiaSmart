package model

data class Material(
    val id: Int,
    val temaId: Int,
    val estudianteId: Int,
    var titulo: String,
    var contenido: String
)
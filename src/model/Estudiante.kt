package model

class Estudiante(
    id: Int,
    nombre: String,
    correo: String,
    contrasena: String,
    activo: Boolean = true
) : Usuario(
    id,
    nombre,
    correo,
    contrasena,
    activo
) {

    override val rol: String = "ESTUDIANTE"

    override fun descripcionRol(): String {
        return "Estudiante: puede gestionar materias, temas, materiales y actividades de estudio."
    }
}
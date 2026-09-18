package model

class Administrador(
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

    override val rol: String = "ADMINISTRADOR"

    override fun descripcionRol(): String {
        return "Administrador: puede gestionar usuarios y configuraciones generales del sistema."
    }
}
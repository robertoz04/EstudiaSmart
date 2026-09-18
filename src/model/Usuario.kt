package model

abstract class Usuario(
    val id: Int,
    var nombre: String,
    var correo: String,
    private var contrasena: String,
    var activo: Boolean = true
) {

    abstract val rol: String

    abstract fun descripcionRol(): String

    fun validarContrasena(contrasenaIngresada: String): Boolean {
        return contrasena == contrasenaIngresada
    }

    fun cambiarContrasena(nuevaContrasena: String) {
        require(nuevaContrasena.length >= 6) {
            "La contraseña debe contener al menos 6 caracteres."
        }

        contrasena = nuevaContrasena
    }
}
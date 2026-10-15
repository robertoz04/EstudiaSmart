package com.estudiasmart.app.data

data class AppUser(
    val nombre: String,
    val correo: String,
    val contrasena: String
)

object AuthRepository {

    private val usuarios = mutableListOf<AppUser>()

    var usuarioActual: AppUser? = null
        private set

    init {
        usuarios.add(
            AppUser(
                nombre = "Administrador",
                correo = "admin@estudiasmart.com",
                contrasena = "admin123"
            )
        )
    }

    fun registrar(
        nombre: String,
        correo: String,
        contrasena: String
    ): String? {

        val correoNormalizado =
            correo.trim().lowercase()

        if (
            usuarios.any {
                it.correo.equals(
                    correoNormalizado,
                    ignoreCase = true
                )
            }
        ) {
            return "Ya existe una cuenta con este correo."
        }

        val usuario = AppUser(
            nombre = nombre.trim(),
            correo = correoNormalizado,
            contrasena = contrasena
        )

        usuarios.add(usuario)
        usuarioActual = usuario

        return null
    }

    fun iniciarSesion(
        correo: String,
        contrasena: String
    ): String? {

        val usuario = usuarios.find {
            it.correo.equals(
                correo.trim(),
                ignoreCase = true
            )
        } ?: return "No existe una cuenta con este correo."

        if (usuario.contrasena != contrasena) {
            return "La contraseña es incorrecta."
        }

        usuarioActual = usuario

        return null
    }

    fun cerrarSesion() {
        usuarioActual = null
    }
}
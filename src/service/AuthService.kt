package service

import model.Administrador
import model.Estudiante
import model.Usuario

class AuthService {

    private val usuarios = mutableListOf<Usuario>()
    private var siguienteId = 1

    fun registrarEstudiante(
        nombre: String,
        correo: String,
        contrasena: String
    ): Estudiante {

        require(nombre.isNotBlank()) {
            "El nombre no puede estar vacío."
        }

        require(correo.contains("@") && correo.contains(".")) {
            "El correo ingresado no es válido."
        }

        require(contrasena.length >= 6) {
            "La contraseña debe contener al menos 6 caracteres."
        }

        if (usuarios.any { it.correo.equals(correo, ignoreCase = true) }) {
            throw IllegalArgumentException(
                "Ya existe un usuario registrado con ese correo."
            )
        }

        val estudiante = Estudiante(
            id = siguienteId++,
            nombre = nombre,
            correo = correo,
            contrasena = contrasena
        )

        usuarios.add(estudiante)

        return estudiante
    }

    fun registrarAdministrador(
        nombre: String,
        correo: String,
        contrasena: String
    ): Administrador {

        val administrador = Administrador(
            id = siguienteId++,
            nombre = nombre,
            correo = correo,
            contrasena = contrasena
        )

        usuarios.add(administrador)

        return administrador
    }

    fun iniciarSesion(
        correo: String,
        contrasena: String
    ): Usuario? {

        val usuario = usuarios.find {
            it.correo.equals(correo, ignoreCase = true)
        }

        if (usuario == null) {
            return null
        }

        if (!usuario.activo) {
            return null
        }

        return if (usuario.validarContrasena(contrasena)) {
            usuario
        } else {
            null
        }
    }

    fun listarUsuarios(): List<Usuario> {
        return usuarios.toList()
    }
}
package util

object Validador {

    fun validarTexto(
        texto: String,
        nombreCampo: String
    ) {
        require(texto.isNotBlank()) {
            "$nombreCampo no puede estar vacío."
        }
    }

    fun validarCorreo(correo: String) {
        require(
            correo.contains("@") &&
            correo.substringAfter("@").contains(".")
        ) {
            "El correo ingresado no es válido."
        }
    }

    fun validarContrasena(contrasena: String) {
        require(contrasena.length >= 6) {
            "La contraseña debe contener al menos 6 caracteres."
        }
    }

    fun validarId(id: Int) {
        require(id > 0) {
            "El identificador debe ser mayor que cero."
        }
    }
}
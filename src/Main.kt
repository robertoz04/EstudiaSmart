import service.AuthService

fun main() {

    println("================================")
    println("          ESTUDIASMART")
    println("================================")

    val authService = AuthService()

    authService.registrarAdministrador(
        nombre = "Administrador",
        correo = "admin@estudiasmart.com",
        contrasena = "admin123"
    )

    try {

        val estudiante = authService.registrarEstudiante(
            nombre = "Josseline Perez",
            correo = "josseline@estudiasmart.com",
            contrasena = "123456"
        )

        println("\nREGISTRO")
        println("--------------------------------")
        println("Usuario registrado correctamente.")
        println("Nombre: ${estudiante.nombre}")
        println("Correo: ${estudiante.correo}")
        println("Rol: ${estudiante.rol}")

    } catch (error: IllegalArgumentException) {

        println("Error al registrar usuario:")
        println(error.message)
    }

    println("\nINICIO DE SESIÓN")
    println("--------------------------------")

    val usuario = authService.iniciarSesion(
        correo = "josseline@estudiasmart.com",
        contrasena = "123456"
    )

    if (usuario != null) {

        println("Inicio de sesión correcto.")
        println("Bienvenida, ${usuario.nombre}")
        println("Rol: ${usuario.rol}")

    } else {

        println("Correo o contraseña incorrectos.")
    }

    println("\nUSUARIOS REGISTRADOS")
    println("--------------------------------")

    authService.listarUsuarios().forEach { usuarioRegistrado ->
        println(
            "${usuarioRegistrado.id} - " +
            "${usuarioRegistrado.nombre} - " +
            usuarioRegistrado.rol
        )
    }
}
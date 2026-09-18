import model.Administrador
import model.Estudiante
import model.Usuario

fun main() {

    println("================================")
    println("          ESTUDIASMART")
    println("================================")

    val estudiante = Estudiante(
        id = 1,
        nombre = "Josseline Perez",
        correo = "josseline@estudiasmart.com",
        contrasena = "123456"
    )

    val administrador = Administrador(
        id = 2,
        nombre = "Administrador",
        correo = "admin@estudiasmart.com",
        contrasena = "admin123"
    )

    val usuarios: List<Usuario> = listOf(
        estudiante,
        administrador
    )

    println("\nUSUARIOS DEL SISTEMA")
    println("--------------------------------")

    usuarios.forEach { usuario ->
        println("Nombre: ${usuario.nombre}")
        println("Correo: ${usuario.correo}")
        println("Rol: ${usuario.rol}")
        println(usuario.descripcionRol())
        println()
    }
}
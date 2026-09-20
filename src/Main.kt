import service.MaterialService
import util.ErrorLogger

fun main() {

    println("================================")
    println("          ESTUDIASMART")
    println("================================")

    val materialService = MaterialService()

    println("\nPRUEBA DE VALIDACIÓN")
    println("--------------------------------")

    try {

        materialService.crearMaterial(
            temaId = 1,
            estudianteId = 1,
            titulo = "",
            contenido = "Contenido de prueba"
        )

    } catch (error: IllegalArgumentException) {

        println("Se produjo un error controlado:")
        println(error.message)

        ErrorLogger.registrarError(error)

        println("El error fue registrado en logs/errors.txt")
    }
}
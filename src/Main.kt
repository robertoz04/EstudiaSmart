import model.ProgresoTema
import service.ProgresoService

fun main() {

    println("================================")
    println("          ESTUDIASMART")
    println("================================")

    val progreso = ProgresoTema(
        temaId = 1
    )

    val progresoService = ProgresoService()

    val precisionQuiz = 80.0
    val rendimientoFlashcards = 90.0

    progresoService.actualizarProgreso(
        progreso,
        precisionQuiz,
        rendimientoFlashcards
    )

    println("\nPROGRESO DEL TEMA")
    println("--------------------------------")
    println("Rendimiento Quiz: ${progreso.precisionQuiz}%")
    println("Rendimiento Flashcards: ${progreso.rendimientoFlashcards}%")
    println("Dominio total: ${progreso.dominio}%")
    println(
        "Nivel: ${progresoService.obtenerNivelDominio(progreso.dominio)}"
    )
}
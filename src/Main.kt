import model.Dificultad
import model.Flashcard
import service.FlashcardService

fun main() {

    println("================================")
    println("          ESTUDIASMART")
    println("================================")

    val flashcard = Flashcard(
        id = 1,
        temaId = 1,
        pregunta = "¿Qué es la herencia?",
        respuesta = "Es un mecanismo de POO que permite heredar características de otra clase."
    )

    val flashcardService = FlashcardService()

    println("\nFlashcard:")
    println(flashcard.pregunta)

    println("\nRespuesta:")
    println(flashcard.respuesta)

    flashcardService.revisarFlashcard(
        flashcard,
        Dificultad.REGULAR,
        true
    )

    println("\nResultado de la revisión")
    println("Dificultad: ${flashcard.dificultad}")
    println("Racha de aciertos: ${flashcard.rachaAciertos}")
    println("Última revisión: ${flashcard.ultimaRevision}")
    println("Próxima revisión: ${flashcard.proximaRevision}")
}
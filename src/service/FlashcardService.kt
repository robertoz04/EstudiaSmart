package service

import model.Dificultad
import model.Flashcard
import java.time.LocalDate

class FlashcardService {

    fun revisarFlashcard(
        flashcard: Flashcard,
        dificultadSeleccionada: Dificultad,
        acierto: Boolean
    ) {
        flashcard.ultimaRevision = LocalDate.now()
        flashcard.dificultad = dificultadSeleccionada

        if (acierto) {
            flashcard.rachaAciertos++
        } else {
            flashcard.rachaAciertos = 0
        }

        val intervaloBase = when (dificultadSeleccionada) {
            Dificultad.DIFICIL -> 1
            Dificultad.REGULAR -> 3
            Dificultad.FACIL -> 7
        }

        // Si existe una racha de aciertos, el intervalo aumenta.
        val multiplicador = if (flashcard.rachaAciertos > 0) {
            flashcard.rachaAciertos
        } else {
            1
        }

        val diasParaProximaRevision = intervaloBase * multiplicador

        flashcard.proximaRevision =
            LocalDate.now().plusDays(diasParaProximaRevision.toLong())
    }

    fun obtenerFlashcardsPendientes(
        flashcards: List<Flashcard>
    ): List<Flashcard> {

        val hoy = LocalDate.now()

        return flashcards.filter { flashcard ->
            flashcard.proximaRevision == null ||
            !flashcard.proximaRevision!!.isAfter(hoy)
        }
    }
}
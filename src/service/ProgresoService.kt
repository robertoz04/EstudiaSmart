package service

import model.ProgresoTema

class ProgresoService {

    fun calcularDominio(
        precisionQuiz: Double,
        rendimientoFlashcards: Double
    ): Double {

        require(precisionQuiz in 0.0..100.0) {
            "La precisión del quiz debe estar entre 0 y 100."
        }

        require(rendimientoFlashcards in 0.0..100.0) {
            "El rendimiento de flashcards debe estar entre 0 y 100."
        }

        return (precisionQuiz * 0.70) +
               (rendimientoFlashcards * 0.30)
    }

    fun actualizarProgreso(
        progreso: ProgresoTema,
        precisionQuiz: Double,
        rendimientoFlashcards: Double
    ) {
        progreso.precisionQuiz = precisionQuiz
        progreso.rendimientoFlashcards = rendimientoFlashcards

        progreso.dominio = calcularDominio(
            precisionQuiz,
            rendimientoFlashcards
        )
    }

    fun obtenerNivelDominio(dominio: Double): String {
        return when {
            dominio < 60 -> "REQUIERE REFUERZO"
            dominio < 80 -> "EN PROGRESO"
            else -> "DOMINIO ALTO"
        }
    }
}
package service

import model.Flashcard
import model.IntentoQuiz
import model.ProgresoTema

class ReporteService {

    fun generarReporte(
        intentos: List<IntentoQuiz>,
        flashcards: List<Flashcard>,
        progresos: List<ProgresoTema>
    ): String {

        val quizzesRealizados = intentos.size

        val promedioQuizzes = if (intentos.isNotEmpty()) {
            intentos.map { it.puntuacion }.average()
        } else {
            0.0
        }

        val flashcardsEstudiadas =
            flashcards.count {
                it.ultimaRevision != null
            }

        val dominioAlto =
            progresos.count {
                it.dominio >= 80
            }

        val enProgreso =
            progresos.count {
                it.dominio >= 60 &&
                it.dominio < 80
            }

        val requiereRefuerzo =
            progresos.count {
                it.dominio < 60
            }

        return """
            =================================
            REPORTE GENERAL ESTUDIASMART
            =================================
            
            Quizzes realizados: $quizzesRealizados
            Promedio de quizzes: ${"%.1f".format(promedioQuizzes)}%
            
            Flashcards registradas: ${flashcards.size}
            Flashcards estudiadas: $flashcardsEstudiadas
            
            Temas con dominio alto: $dominioAlto
            Temas en progreso: $enProgreso
            Temas que requieren refuerzo: $requiereRefuerzo
            =================================
        """.trimIndent()
    }
}
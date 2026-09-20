import model.ActividadEstudio
import model.Dificultad
import model.Flashcard
import model.IntentoQuiz
import model.ProgresoTema
import model.Quiz
import service.ReporteService
import java.time.LocalDate
import java.time.LocalDateTime

fun main() {

    println("================================")
    println("          ESTUDIASMART")
    println("================================")

    val flashcard1 = Flashcard(
        id = 1,
        temaId = 1,
        pregunta = "¿Qué es la herencia?",
        respuesta = "Mecanismo de POO.",
        dificultad = Dificultad.REGULAR,
        rachaAciertos = 2,
        ultimaRevision = LocalDate.now()
    )

    val flashcard2 = Flashcard(
        id = 2,
        temaId = 1,
        pregunta = "¿Qué es una interfaz?",
        respuesta = "Contrato que define comportamientos.",
        dificultad = Dificultad.DIFICIL
    )

    val quiz = Quiz(
        id = 1,
        temaId = 1,
        nombre = "Quiz de POO"
    )

    println("\nACTIVIDADES DE ESTUDIO")
    println("--------------------------------")

    val actividades: List<ActividadEstudio> =
        listOf(
            flashcard1,
            flashcard2,
            quiz
        )

    actividades.forEach { actividad ->
        println(actividad.obtenerResumen())
    }

    val intentos = listOf(
        IntentoQuiz(
            id = 1,
            quizId = 1,
            respuestasCorrectas = 8,
            totalPreguntas = 10,
            puntuacion = 80.0,
            preguntasFalladas = listOf(3, 7),
            fecha = LocalDateTime.now()
        ),

        IntentoQuiz(
            id = 2,
            quizId = 1,
            respuestasCorrectas = 9,
            totalPreguntas = 10,
            puntuacion = 90.0,
            preguntasFalladas = listOf(4),
            fecha = LocalDateTime.now()
        )
    )

    val progresos = listOf(
        ProgresoTema(
            temaId = 1,
            precisionQuiz = 85.0,
            rendimientoFlashcards = 90.0,
            dominio = 86.5
        ),

        ProgresoTema(
            temaId = 2,
            precisionQuiz = 70.0,
            rendimientoFlashcards = 60.0,
            dominio = 67.0
        ),

        ProgresoTema(
            temaId = 3,
            precisionQuiz = 45.0,
            rendimientoFlashcards = 50.0,
            dominio = 46.5
        )
    )

    val reporteService = ReporteService()

    println()
    println(
        reporteService.generarReporte(
            intentos = intentos,
            flashcards = listOf(
                flashcard1,
                flashcard2
            ),
            progresos = progresos
        )
    )
}
import model.Pregunta
import model.Quiz
import service.QuizService

fun main() {

    println("================================")
    println("          ESTUDIASMART")
    println("================================")

    val pregunta1 = Pregunta(
        id = 1,
        quizId = 1,
        enunciado = "¿Qué concepto permite que una clase herede de otra?",
        opcionA = "Encapsulamiento",
        opcionB = "Herencia",
        opcionC = "Abstracción",
        opcionD = "Sobrecarga",
        respuestaCorrecta = 'B'
    )

    val pregunta2 = Pregunta(
        id = 2,
        quizId = 1,
        enunciado = "¿Cuál palabra se utiliza para declarar una clase en Kotlin?",
        opcionA = "class",
        opcionB = "object",
        opcionC = "struct",
        opcionD = "model",
        respuestaCorrecta = 'A'
    )

    val quiz = Quiz(
        id = 1,
        temaId = 1,
        nombre = "Quiz de Programación Orientada a Objetos"
    )

    quiz.preguntas.add(pregunta1)
    quiz.preguntas.add(pregunta2)

    val respuestas = mapOf(
        1 to 'B',
        2 to 'C'
    )

    val quizService = QuizService()

    val resultado = quizService.evaluarQuiz(
        quiz = quiz,
        respuestasUsuario = respuestas,
        idIntento = 1
    )

    println("\nResultado del Quiz")
    println("--------------------------------")
    println("Correctas: ${resultado.respuestasCorrectas}")
    println("Total: ${resultado.totalPreguntas}")
    println("Puntuación: ${resultado.puntuacion}%")
    println(
        "Nivel: ${quizService.obtenerNivelDominio(resultado.puntuacion)}"
    )
    println("Preguntas falladas: ${resultado.preguntasFalladas}")
}
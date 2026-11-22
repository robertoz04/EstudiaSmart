package com.estudiasmart.app.navigation

object Routes {

    const val LOGIN =
        "login"

    const val REGISTER =
        "register"

    const val HOME =
        "home"

    const val MATERIAS =
        "materias"

    const val FLASHCARDS =
        "flashcards"

    const val QUIZ =
        "quiz"

    const val PROGRESO =
        "progreso"

    const val RECOMENDACIONES =
        "recomendaciones"

    const val TEMAS =
        "temas/{materiaId}"

    const val MATERIALES =
        "materiales/{temaId}"

    fun temas(
        materiaId: Int
    ): String {

        return "temas/$materiaId"
    }

    fun materiales(
        temaId: Int
    ): String {

        return "materiales/$temaId"
    }
}
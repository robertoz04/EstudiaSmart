package com.estudiasmart.app.viewmodel

import androidx.lifecycle.ViewModel
import com.estudiasmart.app.data.AcademicRepository
import com.estudiasmart.app.data.AuthRepository
import com.estudiasmart.app.data.FlashcardRepository
import com.estudiasmart.app.data.ProgresoService
import com.estudiasmart.app.data.QuizRepository

class HomeViewModel : ViewModel() {

    /**
     * Nombre del usuario autenticado.
     */
    val nombreUsuario: String
        get() =
            AuthRepository.usuarioActual
                ?.nombre
                ?: "Estudiante"

    /**
     * Cantidad total de materias del usuario.
     */
    val totalMaterias: Int
        get() =
            AcademicRepository
                .materias
                .size

    /**
     * Cantidad total de temas registrados.
     */
    val totalTemas: Int
        get() =
            AcademicRepository
                .temas
                .size

    /**
     * Cantidad de flashcards registradas.
     */
    val totalFlashcards: Int
        get() =
            FlashcardRepository
                .flashcards
                .size

    /**
     * Cantidad de quizzes registrados.
     */
    val totalQuizzes: Int
        get() =
            QuizRepository
                .quizzes
                .size

    /**
     * Progreso académico general.
     */
    val progresoGeneral: Double
        get() =
            ProgresoService
                .calcularPromedioGeneral()
}
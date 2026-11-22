package com.estudiasmart.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.estudiasmart.app.data.AcademicRepository
import com.estudiasmart.app.data.FlashcardRepository
import com.estudiasmart.app.model.Dificultad
import com.estudiasmart.app.model.Flashcard
import com.estudiasmart.app.model.Tema

class FlashcardViewModel : ViewModel() {

    // =========================================================
    // DATOS
    // =========================================================

    val temas: List<Tema>
        get() =
            AcademicRepository.temas

    val flashcards: List<Flashcard>
        get() =
            FlashcardRepository.flashcards

    fun obtenerTema(
        temaId: Int
    ): Tema? {

        return AcademicRepository
            .obtenerTema(
                temaId
            )
    }

    // =========================================================
    // FORMULARIO
    // =========================================================

    var pregunta by
    mutableStateOf("")
        private set

    var respuesta by
    mutableStateOf("")
        private set

    private var _temaSeleccionadoId by
    mutableStateOf<Int?>(null)

    val temaSeleccionadoId: Int?
        get() =
            _temaSeleccionadoId
                ?: temas.firstOrNull()?.id

    var error by
    mutableStateOf<String?>(null)
        private set

    var flashcardEditando by
    mutableStateOf<Flashcard?>(
        null
    )
        private set

    var flashcardAEliminar by
    mutableStateOf<Flashcard?>(
        null
    )
        private set

    // =========================================================
    // CAMBIOS DEL FORMULARIO
    // =========================================================

    fun cambiarPregunta(
        valor: String
    ) {

        pregunta = valor
        error = null
    }

    fun cambiarRespuesta(
        valor: String
    ) {

        respuesta = valor
        error = null
    }

    fun seleccionarTema(
        temaId: Int
    ) {

        _temaSeleccionadoId =
            temaId

        error = null
    }

    // =========================================================
    // GUARDAR
    // =========================================================

    fun guardarFlashcard() {

        val temaId =
            temaSeleccionadoId

        if (temaId == null) {

            error =
                "Selecciona un tema."

            return
        }

        val resultado =
            if (
                flashcardEditando ==
                null
            ) {

                FlashcardRepository
                    .crearFlashcard(
                        temaId =
                            temaId,
                        pregunta =
                            pregunta,
                        respuesta =
                            respuesta
                    )

            } else {

                FlashcardRepository
                    .actualizarFlashcard(
                        id =
                            flashcardEditando!!
                                .id,
                        temaId =
                            temaId,
                        pregunta =
                            pregunta,
                        respuesta =
                            respuesta
                    )
            }

        if (resultado == null) {

            limpiarFormulario()

        } else {

            error =
                resultado
        }
    }

    // =========================================================
    // EDITAR
    // =========================================================

    fun iniciarEdicion(
        flashcard: Flashcard
    ) {

        flashcardEditando =
            flashcard

        _temaSeleccionadoId =
            flashcard.temaId

        pregunta =
            flashcard.pregunta

        respuesta =
            flashcard.respuesta

        error = null
    }

    fun cancelarEdicion() {

        limpiarFormulario()
    }

    private fun limpiarFormulario() {

        pregunta = ""
        respuesta = ""
        flashcardEditando = null
        error = null
    }

    // =========================================================
    // ELIMINAR
    // =========================================================

    fun solicitarEliminar(
        flashcard: Flashcard
    ) {

        flashcardAEliminar =
            flashcard
    }

    fun cancelarEliminar() {

        flashcardAEliminar =
            null
    }

    fun confirmarEliminar() {

        val flashcard =
            flashcardAEliminar
                ?: return

        val id =
            flashcard.id

        FlashcardRepository
            .eliminarFlashcard(
                id
            )

        if (
            flashcardEditando?.id ==
            id
        ) {

            limpiarFormulario()
        }

        flashcardAEliminar =
            null
    }

    // =========================================================
    // REPETICIÓN ESPACIADA
    // =========================================================

    fun registrarRepaso(
        flashcardId: Int,
        dificultad: Dificultad
    ) {

        val resultado =
            FlashcardRepository
                .registrarRepaso(
                    id =
                        flashcardId,
                    dificultad =
                        dificultad
                )

        if (resultado != null) {

            error =
                resultado
        }
    }

    // =========================================================
    // ESTADÍSTICAS
    // =========================================================

    val totalFlashcards: Int
        get() =
            flashcards.size

    val totalRevisiones: Int
        get() =
            FlashcardRepository
                .totalRevisiones()

    val flashcardsEstudiadas: Int
        get() =
            FlashcardRepository
                .flashcardsEstudiadas()
}
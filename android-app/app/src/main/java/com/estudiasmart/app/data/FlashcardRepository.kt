package com.estudiasmart.app.data

import androidx.compose.runtime.mutableStateListOf
import com.estudiasmart.app.model.Dificultad
import com.estudiasmart.app.model.Flashcard

object FlashcardRepository {

    private val _flashcards =
        mutableStateListOf<Flashcard>()

    val flashcards: List<Flashcard>
        get() = _flashcards

    private var siguienteId = 1

    fun crearFlashcard(
        temaId: Int,
        pregunta: String,
        respuesta: String
    ): String? {

        val preguntaLimpia =
            pregunta.trim()

        val respuestaLimpia =
            respuesta.trim()

        if (
            AcademicRepository
                .obtenerTema(temaId) == null
        ) {
            return "El tema seleccionado no existe."
        }

        if (preguntaLimpia.isBlank()) {
            return "La pregunta es obligatoria."
        }

        if (preguntaLimpia.length < 3) {
            return "La pregunta debe tener al menos 3 caracteres."
        }

        if (respuestaLimpia.isBlank()) {
            return "La respuesta es obligatoria."
        }

        val duplicada =
            _flashcards.any {
                it.temaId == temaId &&
                        it.pregunta.equals(
                            preguntaLimpia,
                            ignoreCase = true
                        )
            }

        if (duplicada) {
            return "Ya existe una flashcard con esa pregunta."
        }

        val flashcard =
            Flashcard(
                id = siguienteId++,
                temaId = temaId,
                pregunta = preguntaLimpia,
                respuesta = respuestaLimpia
            )

        _flashcards.add(
            flashcard
        )

        return null
    }

    fun actualizarFlashcard(
        id: Int,
        temaId: Int,
        pregunta: String,
        respuesta: String
    ): String? {

        val preguntaLimpia =
            pregunta.trim()

        val respuestaLimpia =
            respuesta.trim()

        if (
            AcademicRepository
                .obtenerTema(temaId) == null
        ) {
            return "El tema seleccionado no existe."
        }

        if (preguntaLimpia.isBlank()) {
            return "La pregunta es obligatoria."
        }

        if (preguntaLimpia.length < 3) {
            return "La pregunta debe tener al menos 3 caracteres."
        }

        if (respuestaLimpia.isBlank()) {
            return "La respuesta es obligatoria."
        }

        val indice =
            _flashcards.indexOfFirst {
                it.id == id
            }

        if (indice == -1) {
            return "Flashcard no encontrada."
        }

        val duplicada =
            _flashcards.any {
                it.id != id &&
                        it.temaId == temaId &&
                        it.pregunta.equals(
                            preguntaLimpia,
                            ignoreCase = true
                        )
            }

        if (duplicada) {
            return "Ya existe otra flashcard con esa pregunta."
        }

        val actual =
            _flashcards[indice]

        _flashcards[indice] =
            actual.copy(
                temaId = temaId,
                pregunta = preguntaLimpia,
                respuesta = respuestaLimpia
            )

        return null
    }

    fun eliminarFlashcard(
        id: Int
    ): Boolean {

        return _flashcards.removeAll {
            it.id == id
        }
    }

    fun registrarRepaso(
        id: Int,
        dificultad: Dificultad
    ): String? {

        val indice =
            _flashcards.indexOfFirst {
                it.id == id
            }

        if (indice == -1) {
            return "Flashcard no encontrada."
        }

        val actual =
            _flashcards[indice]

        val nuevaRacha =
            when (dificultad) {

                Dificultad.DIFICIL ->
                    0

                Dificultad.REGULAR,
                Dificultad.FACIL ->
                    actual.racha + 1
            }

        val multiplicador =
            maxOf(
                1,
                nuevaRacha
            )

        val dias =
            dificultad.diasBase *
                    multiplicador

        val milisegundosPorDia =
            24L *
                    60L *
                    60L *
                    1000L

        val proximaRevision =
            System.currentTimeMillis() +
                    (dias * milisegundosPorDia)

        _flashcards[indice] =
            actual.copy(
                dificultad = dificultad,
                racha = nuevaRacha,
                revisiones =
                    actual.revisiones + 1,
                proximaRevisionMillis =
                    proximaRevision
            )

        return null
    }

    fun pendientesHoy(): List<Flashcard> {

        val ahora =
            System.currentTimeMillis()

        return _flashcards.filter {
            it.proximaRevisionMillis <= ahora
        }
    }

    fun flashcardsPorTema(
        temaId: Int
    ): List<Flashcard> {

        return _flashcards.filter {
            it.temaId == temaId
        }
    }

    fun obtenerFlashcard(
        id: Int
    ): Flashcard? {

        return _flashcards.find {
            it.id == id
        }
    }

    fun eliminarPorTema(
        temaId: Int
    ) {

        _flashcards.removeAll {
            it.temaId == temaId
        }
    }

    fun totalRevisiones(): Int {

        return _flashcards.sumOf {
            it.revisiones
        }
    }

    fun flashcardsEstudiadas(): Int {

        return _flashcards.count {
            it.revisiones > 0
        }
    }
}
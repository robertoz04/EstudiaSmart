package model

import java.time.LocalDate

data class Flashcard(
    val id: Int,
    val temaId: Int,
    var pregunta: String,
    var respuesta: String,
    var dificultad: Dificultad = Dificultad.REGULAR,
    var rachaAciertos: Int = 0,
    var ultimaRevision: LocalDate? = null,
    var proximaRevision: LocalDate? = null
)
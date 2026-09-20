package model

data class ProgresoTema(
    val temaId: Int,
    var precisionQuiz: Double = 0.0,
    var rendimientoFlashcards: Double = 0.0,
    var dominio: Double = 0.0
)
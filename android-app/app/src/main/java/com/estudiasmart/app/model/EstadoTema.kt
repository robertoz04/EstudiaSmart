package com.estudiasmart.app.model

enum class EstadoTema(
    val etiqueta: String
) {

    REQUIERE_REFUERZO(
        "Requiere refuerzo"
    ),

    EN_PROGRESO(
        "En progreso"
    ),

    DOMINIO_ALTO(
        "Dominio alto"
    )
}
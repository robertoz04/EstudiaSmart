package com.estudiasmart.app.data

import com.estudiasmart.app.model.Dificultad
import com.estudiasmart.app.model.EstadoTema
import com.estudiasmart.app.model.PrioridadRecomendacion
import com.estudiasmart.app.model.RecomendacionEstudio

object RecomendacionService {

    fun obtenerRecomendaciones():
            List<RecomendacionEstudio> {

        return AcademicRepository
            .temas
            .mapNotNull { tema ->

                crearRecomendacion(
                    tema.id
                )
            }
            .sortedByDescending {
                it.puntuacion
            }
            .take(3)
    }

    fun crearRecomendacion(
        temaId: Int
    ): RecomendacionEstudio? {

        val progreso =
            ProgresoService
                .calcularProgresoTema(
                    temaId
                )
                ?: return null

        val debilidadDominio =
            (
                    100.0 -
                            progreso.dominioTotal
                    )
                .coerceIn(
                    0.0,
                    100.0
                )

        val urgenciaRepaso =
            calcularUrgenciaRepaso(
                temaId
            )

        val dificultad =
            calcularDificultad(
                temaId
            )

        val puntuacion =
            (
                    debilidadDominio *
                            0.50
                    ) +
                    (
                            urgenciaRepaso *
                                    0.30
                            ) +
                    (
                            dificultad *
                                    0.20
                            )

        val prioridad =
            determinarPrioridad(
                puntuacion
            )

        val motivo =
            crearMotivo(
                estado =
                    progreso.estado,
                urgenciaRepaso =
                    urgenciaRepaso,
                dificultad =
                    dificultad
            )

        return RecomendacionEstudio(
            temaId =
                progreso.temaId,
            nombreTema =
                progreso.nombreTema,
            dominioActual =
                progreso.dominioTotal,
            debilidadDominio =
                debilidadDominio,
            urgenciaRepaso =
                urgenciaRepaso,
            dificultad =
                dificultad,
            puntuacion =
                puntuacion,
            prioridad =
                prioridad,
            motivo =
                motivo
        )
    }

    private fun calcularUrgenciaRepaso(
        temaId: Int
    ): Double {

        val flashcards =
            FlashcardRepository
                .flashcardsPorTema(
                    temaId
                )

        if (flashcards.isEmpty()) {

            return 100.0
        }

        val ahora =
            System.currentTimeMillis()

        val pendientes =
            flashcards.count {
                it.proximaRevisionMillis <=
                        ahora
            }

        return (
                pendientes.toDouble() /
                        flashcards.size.toDouble()
                ) * 100.0
    }

    private fun calcularDificultad(
        temaId: Int
    ): Double {

        val flashcards =
            FlashcardRepository
                .flashcardsPorTema(
                    temaId
                )

        if (flashcards.isEmpty()) {

            return 60.0
        }

        val valores =
            flashcards.map {
                    flashcard ->

                when (
                    flashcard.dificultad
                ) {

                    Dificultad.DIFICIL ->
                        100.0

                    Dificultad.REGULAR ->
                        60.0

                    Dificultad.FACIL ->
                        30.0
                }
            }

        return valores.average()
    }

    private fun determinarPrioridad(
        puntuacion: Double
    ): PrioridadRecomendacion {

        return when {

            puntuacion >= 70.0 ->
                PrioridadRecomendacion
                    .MUY_ALTA

            puntuacion >= 50.0 ->
                PrioridadRecomendacion
                    .ALTA

            puntuacion >= 30.0 ->
                PrioridadRecomendacion
                    .MEDIA

            else ->
                PrioridadRecomendacion
                    .BAJA
        }
    }

    private fun crearMotivo(
        estado: EstadoTema,
        urgenciaRepaso: Double,
        dificultad: Double
    ): String {

        return when {

            estado ==
                    EstadoTema.REQUIERE_REFUERZO ->

                "Este tema necesita refuerzo porque su nivel de dominio todavía es bajo."

            urgenciaRepaso >= 50.0 ->

                "Tienes contenido pendiente de repaso en este tema."

            dificultad >= 80.0 ->

                "Este tema contiene conceptos que has marcado como difíciles."

            estado ==
                    EstadoTema.EN_PROGRESO ->

                "Vas avanzando, pero todavía puedes mejorar tu dominio."

            else ->

                "Tienes buen dominio. Repásalo para mantener tus conocimientos."
        }
    }
}
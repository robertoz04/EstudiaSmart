package service

import model.Dificultad
import model.EstadoTema
import model.RecomendacionEstudio
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class RecomendacionService {

    fun generarRecomendaciones(
        temas: List<EstadoTema>,
        limite: Int = 3
    ): List<RecomendacionEstudio> {

        val hoy = LocalDate.now()

        return temas.map { tema ->

            require(tema.dominio in 0.0..100.0) {
                "El dominio debe estar entre 0 y 100."
            }

            val diasSinRepaso = if (tema.ultimaRevision != null) {
                ChronoUnit.DAYS
                    .between(tema.ultimaRevision, hoy)
                    .coerceAtLeast(0)
            } else {
                30
            }

            val debilidad = 100.0 - tema.dominio

            // Se limita a 100 para trabajar todos
            // los factores sobre la misma escala.
            val factorTiempo =
                (diasSinRepaso * 10.0).coerceAtMost(100.0)

            val factorDificultad = when (tema.dificultad) {
                Dificultad.DIFICIL -> 100.0
                Dificultad.REGULAR -> 60.0
                Dificultad.FACIL -> 30.0
            }

            val prioridad =
                (debilidad * 0.50) +
                (factorTiempo * 0.30) +
                (factorDificultad * 0.20)

            RecomendacionEstudio(
                temaId = tema.temaId,
                nombreTema = tema.nombreTema,
                dominio = tema.dominio,
                diasSinRepaso = diasSinRepaso,
                dificultad = tema.dificultad,
                prioridad = prioridad
            )

        }.sortedByDescending {
            it.prioridad
        }.take(limite)
    }
}
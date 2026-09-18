import model.Dificultad
import model.EstadoTema
import service.RecomendacionService
import java.time.LocalDate

fun main() {

    println("================================")
    println("          ESTUDIASMART")
    println("================================")

    val temas = listOf(

        EstadoTema(
            temaId = 1,
            nombreTema = "Herencia",
            dominio = 83.0,
            ultimaRevision = LocalDate.now().minusDays(2),
            dificultad = Dificultad.REGULAR
        ),

        EstadoTema(
            temaId = 2,
            nombreTema = "Interfaces",
            dominio = 45.0,
            ultimaRevision = LocalDate.now().minusDays(8),
            dificultad = Dificultad.DIFICIL
        ),

        EstadoTema(
            temaId = 3,
            nombreTema = "Colecciones",
            dominio = 65.0,
            ultimaRevision = LocalDate.now().minusDays(5),
            dificultad = Dificultad.REGULAR
        ),

        EstadoTema(
            temaId = 4,
            nombreTema = "Encapsulamiento",
            dominio = 90.0,
            ultimaRevision = LocalDate.now().minusDays(1),
            dificultad = Dificultad.FACIL
        )
    )

    val recomendacionService = RecomendacionService()

    val recomendaciones =
        recomendacionService.generarRecomendaciones(temas)

    println("\n¿QUÉ DEBO ESTUDIAR HOY?")
    println("--------------------------------")

    recomendaciones.forEachIndexed { indice, recomendacion ->

        println("${indice + 1}. ${recomendacion.nombreTema}")
        println("   Dominio: ${recomendacion.dominio}%")
        println("   Días sin repaso: ${recomendacion.diasSinRepaso}")
        println("   Dificultad: ${recomendacion.dificultad}")
        println(
            "   Prioridad: ${
                "%.1f".format(recomendacion.prioridad)
            }"
        )
        println()
    }
}
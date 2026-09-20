import service.MateriaService
import service.TemaService

fun main() {

    println("================================")
    println("          ESTUDIASMART")
    println("================================")

    val materiaService = MateriaService()
    val temaService = TemaService()

    val estudianteId = 1

    val materia = materiaService.crearMateria(
        estudianteId = estudianteId,
        nombre = "Programación III",
        descripcion = "Programación orientada a objetos"
    )

    println("\nMATERIA")
    println("--------------------------------")
    println("${materia.id}. ${materia.nombre}")

    println("\nCREAR TEMAS")
    println("--------------------------------")

    val temaHerencia = temaService.crearTema(
        materiaId = materia.id,
        estudianteId = estudianteId,
        nombre = "Herencia",
        descripcion = "Herencia de clases en Kotlin"
    )

    temaService.crearTema(
        materiaId = materia.id,
        estudianteId = estudianteId,
        nombre = "Interfaces",
        descripcion = "Uso de interfaces en Kotlin"
    )

    println("Temas creados correctamente.")

    println("\nLISTAR TEMAS")
    println("--------------------------------")

    temaService
        .listarTemas(
            materiaId = materia.id,
            estudianteId = estudianteId
        )
        .forEach { tema ->

            println("${tema.id}. ${tema.nombre}")
            println("   ${tema.descripcion}")
        }

    println("\nACTUALIZAR TEMA")
    println("--------------------------------")

    val actualizado = temaService.actualizarTema(
        id = temaHerencia.id,
        estudianteId = estudianteId,
        nuevoNombre = "Herencia en Kotlin",
        nuevaDescripcion = "Clases base y clases derivadas"
    )

    println(
        if (actualizado)
            "Tema actualizado correctamente."
        else
            "No se encontró el tema."
    )

    println("\nELIMINAR TEMA")
    println("--------------------------------")

    val eliminado = temaService.eliminarTema(
        id = 2,
        estudianteId = estudianteId
    )

    println(
        if (eliminado)
            "Tema eliminado correctamente."
        else
            "No se encontró el tema."
    )

    println("\nTEMAS FINALES")
    println("--------------------------------")

    temaService
        .listarTemas(
            materiaId = materia.id,
            estudianteId = estudianteId
        )
        .forEach { tema ->

            println("${tema.id}. ${tema.nombre}")
            println("   ${tema.descripcion}")
        }
}
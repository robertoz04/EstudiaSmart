import service.MateriaService

fun main() {

    println("================================")
    println("          ESTUDIASMART")
    println("================================")

    val materiaService = MateriaService()

    val estudianteId = 1

    println("\nCREAR MATERIAS")
    println("--------------------------------")

    val programacion = materiaService.crearMateria(
        estudianteId,
        "Programación III",
        "Programación orientada a objetos"
    )

    materiaService.crearMateria(
        estudianteId,
        "Matemáticas",
        "Matemática universitaria"
    )

    println("Materias creadas correctamente.")

    println("\nLISTAR MATERIAS")
    println("--------------------------------")

    materiaService
        .listarMaterias(estudianteId)
        .forEach { materia ->

            println("${materia.id}. ${materia.nombre}")
            println("   ${materia.descripcion}")
        }

    println("\nACTUALIZAR MATERIA")
    println("--------------------------------")

    val actualizada = materiaService.actualizarMateria(
        id = programacion.id,
        estudianteId = estudianteId,
        nuevoNombre = "Programación Orientada a Objetos",
        nuevaDescripcion = "POO utilizando Kotlin"
    )

    println(
        if (actualizada)
            "Materia actualizada correctamente."
        else
            "No se encontró la materia."
    )

    println("\nELIMINAR MATERIA")
    println("--------------------------------")

    val eliminada = materiaService.eliminarMateria(
        id = 2,
        estudianteId = estudianteId
    )

    println(
        if (eliminada)
            "Materia eliminada correctamente."
        else
            "No se encontró la materia."
    )

    println("\nMATERIAS FINALES")
    println("--------------------------------")

    materiaService
        .listarMaterias(estudianteId)
        .forEach { materia ->

            println("${materia.id}. ${materia.nombre}")
            println("   ${materia.descripcion}")
        }
}
import service.MateriaService
import service.TemaService
import service.MaterialService

fun main() {

    println("================================")
    println("          ESTUDIASMART")
    println("================================")

    val materiaService = MateriaService()
    val temaService = TemaService()
    val materialService = MaterialService()

    val estudianteId = 1

    val materia = materiaService.crearMateria(
        estudianteId = estudianteId,
        nombre = "Programación III",
        descripcion = "Programación orientada a objetos"
    )

    val tema = temaService.crearTema(
        materiaId = materia.id,
        estudianteId = estudianteId,
        nombre = "Herencia",
        descripcion = "Herencia en Kotlin"
    )

    println("\nCREAR MATERIALES")
    println("--------------------------------")

    val material1 = materialService.crearMaterial(
        temaId = tema.id,
        estudianteId = estudianteId,
        titulo = "Apunte sobre herencia",
        contenido = "La herencia permite que una clase adquiera características de otra."
    )

    materialService.crearMaterial(
        temaId = tema.id,
        estudianteId = estudianteId,
        titulo = "Ejemplo Kotlin",
        contenido = "Una clase puede heredar utilizando dos puntos."
    )

    println("Materiales creados correctamente.")

    println("\nLISTAR MATERIALES")
    println("--------------------------------")

    materialService
        .listarMateriales(
            temaId = tema.id,
            estudianteId = estudianteId
        )
        .forEach { material ->

            println("${material.id}. ${material.titulo}")
            println("   ${material.contenido}")
        }

    println("\nACTUALIZAR MATERIAL")
    println("--------------------------------")

    val actualizado = materialService.actualizarMaterial(
        id = material1.id,
        estudianteId = estudianteId,
        nuevoTitulo = "Herencia en Kotlin",
        nuevoContenido = "Una clase puede heredar propiedades y métodos de otra clase."
    )

    println(
        if (actualizado)
            "Material actualizado correctamente."
        else
            "No se encontró el material."
    )

    println("\nELIMINAR MATERIAL")
    println("--------------------------------")

    val eliminado = materialService.eliminarMaterial(
        id = 2,
        estudianteId = estudianteId
    )

    println(
        if (eliminado)
            "Material eliminado correctamente."
        else
            "No se encontró el material."
    )

    println("\nMATERIALES FINALES")
    println("--------------------------------")

    materialService
        .listarMateriales(
            temaId = tema.id,
            estudianteId = estudianteId
        )
        .forEach { material ->

            println("${material.id}. ${material.titulo}")
            println("   ${material.contenido}")
        }
}
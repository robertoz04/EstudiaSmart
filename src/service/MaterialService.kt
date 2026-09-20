package service

import model.Material

class MaterialService {

    private val materiales = mutableListOf<Material>()
    private var siguienteId = 1

    fun crearMaterial(
        temaId: Int,
        estudianteId: Int,
        titulo: String,
        contenido: String
    ): Material {

        require(temaId > 0) {
            "El ID del tema debe ser válido."
        }

        require(estudianteId > 0) {
            "El ID del estudiante debe ser válido."
        }

        require(titulo.isNotBlank()) {
            "El título no puede estar vacío."
        }

        require(contenido.isNotBlank()) {
            "El contenido no puede estar vacío."
        }

        val material = Material(
            id = siguienteId++,
            temaId = temaId,
            estudianteId = estudianteId,
            titulo = titulo,
            contenido = contenido
        )

        materiales.add(material)

        return material
    }

    fun listarMateriales(
        temaId: Int,
        estudianteId: Int
    ): List<Material> {

        return materiales.filter {
            it.temaId == temaId &&
            it.estudianteId == estudianteId
        }
    }

    fun buscarMaterial(
        id: Int,
        estudianteId: Int
    ): Material? {

        return materiales.find {
            it.id == id &&
            it.estudianteId == estudianteId
        }
    }

    fun actualizarMaterial(
        id: Int,
        estudianteId: Int,
        nuevoTitulo: String,
        nuevoContenido: String
    ): Boolean {

        require(nuevoTitulo.isNotBlank()) {
            "El título no puede estar vacío."
        }

        require(nuevoContenido.isNotBlank()) {
            "El contenido no puede estar vacío."
        }

        val material = buscarMaterial(
            id,
            estudianteId
        ) ?: return false

        material.titulo = nuevoTitulo
        material.contenido = nuevoContenido

        return true
    }

    fun eliminarMaterial(
        id: Int,
        estudianteId: Int
    ): Boolean {

        val material = buscarMaterial(
            id,
            estudianteId
        ) ?: return false

        return materiales.remove(material)
    }
}
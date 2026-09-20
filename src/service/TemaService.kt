package service

import model.Tema

class TemaService {

    private val temas = mutableListOf<Tema>()
    private var siguienteId = 1

    fun crearTema(
        materiaId: Int,
        estudianteId: Int,
        nombre: String,
        descripcion: String
    ): Tema {

        require(materiaId > 0) {
            "El ID de la materia debe ser válido."
        }

        require(estudianteId > 0) {
            "El ID del estudiante debe ser válido."
        }

        require(nombre.isNotBlank()) {
            "El nombre del tema no puede estar vacío."
        }

        val tema = Tema(
            id = siguienteId++,
            materiaId = materiaId,
            estudianteId = estudianteId,
            nombre = nombre,
            descripcion = descripcion
        )

        temas.add(tema)

        return tema
    }

    fun listarTemas(
        materiaId: Int,
        estudianteId: Int
    ): List<Tema> {

        return temas.filter {
            it.materiaId == materiaId &&
            it.estudianteId == estudianteId
        }
    }

    fun buscarTema(
        id: Int,
        estudianteId: Int
    ): Tema? {

        return temas.find {
            it.id == id &&
            it.estudianteId == estudianteId
        }
    }

    fun actualizarTema(
        id: Int,
        estudianteId: Int,
        nuevoNombre: String,
        nuevaDescripcion: String
    ): Boolean {

        require(nuevoNombre.isNotBlank()) {
            "El nombre del tema no puede estar vacío."
        }

        val tema = buscarTema(
            id,
            estudianteId
        ) ?: return false

        tema.nombre = nuevoNombre
        tema.descripcion = nuevaDescripcion

        return true
    }

    fun eliminarTema(
        id: Int,
        estudianteId: Int
    ): Boolean {

        val tema = buscarTema(
            id,
            estudianteId
        ) ?: return false

        return temas.remove(tema)
    }
}
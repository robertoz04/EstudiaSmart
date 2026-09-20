package service

import model.Materia

class MateriaService {

    private val materias = mutableListOf<Materia>()
    private var siguienteId = 1

    fun crearMateria(
        estudianteId: Int,
        nombre: String,
        descripcion: String
    ): Materia {

        require(nombre.isNotBlank()) {
            "El nombre de la materia no puede estar vacío."
        }

        val materia = Materia(
            id = siguienteId++,
            estudianteId = estudianteId,
            nombre = nombre,
            descripcion = descripcion
        )

        materias.add(materia)

        return materia
    }

    fun listarMaterias(estudianteId: Int): List<Materia> {
        return materias.filter {
            it.estudianteId == estudianteId
        }
    }

    fun buscarMateria(
        id: Int,
        estudianteId: Int
    ): Materia? {

        return materias.find {
            it.id == id &&
            it.estudianteId == estudianteId
        }
    }

    fun actualizarMateria(
        id: Int,
        estudianteId: Int,
        nuevoNombre: String,
        nuevaDescripcion: String
    ): Boolean {

        require(nuevoNombre.isNotBlank()) {
            "El nombre no puede estar vacío."
        }

        val materia = buscarMateria(
            id,
            estudianteId
        ) ?: return false

        materia.nombre = nuevoNombre
        materia.descripcion = nuevaDescripcion

        return true
    }

    fun eliminarMateria(
        id: Int,
        estudianteId: Int
    ): Boolean {

        val materia = buscarMateria(
            id,
            estudianteId
        ) ?: return false

        return materias.remove(materia)
    }
}
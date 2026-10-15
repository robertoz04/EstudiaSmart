package com.estudiasmart.app.data

import androidx.compose.runtime.mutableStateListOf
import com.estudiasmart.app.model.Materia

object AcademicRepository {

    private val _materias =
        mutableStateListOf<Materia>()

    val materias: List<Materia>
        get() = _materias

    private var siguienteMateriaId = 1

    fun crearMateria(
        nombre: String,
        descripcion: String
    ): String? {

        if (nombre.isBlank()) {
            return "El nombre de la materia es obligatorio."
        }

        if (nombre.trim().length < 3) {
            return "El nombre debe tener al menos 3 caracteres."
        }

        if (
            _materias.any {
                it.nombre.equals(
                    nombre.trim(),
                    ignoreCase = true
                )
            }
        ) {
            return "Ya existe una materia con ese nombre."
        }

        val materia = Materia(
            id = siguienteMateriaId++,
            nombre = nombre.trim(),
            descripcion = descripcion.trim()
        )

        _materias.add(materia)

        return null
    }

    fun actualizarMateria(
        id: Int,
        nombre: String,
        descripcion: String
    ): String? {

        val nombreLimpio = nombre.trim()
        val descripcionLimpia = descripcion.trim()

        if (nombreLimpio.isBlank()) {
            return "El nombre no puede estar vacío."
        }

        if (nombreLimpio.length < 3) {
            return "El nombre debe tener al menos 3 caracteres."
        }

        val indice =
            _materias.indexOfFirst {
                it.id == id
            }

        if (indice == -1) {
            return "Materia no encontrada."
        }

        val nombreDuplicado =
            _materias.any {
                it.id != id &&
                        it.nombre.equals(
                            nombreLimpio,
                            ignoreCase = true
                        )
            }

        if (nombreDuplicado) {
            return "Ya existe otra materia con ese nombre."
        }

        val materiaActual =
            _materias[indice]

        _materias[indice] =
            materiaActual.copy(
                nombre = nombreLimpio,
                descripcion = descripcionLimpia
            )

        return null
    }

    fun eliminarMateria(
        id: Int
    ): Boolean {

        return _materias.removeAll {
            it.id == id
        }
    }
}
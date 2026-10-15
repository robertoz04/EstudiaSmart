package com.estudiasmart.app.data

import androidx.compose.runtime.mutableStateListOf
import com.estudiasmart.app.model.Materia
import com.estudiasmart.app.model.Tema

object AcademicRepository {

    private val _materias =
        mutableStateListOf<Materia>()

    val materias: List<Materia>
        get() = _materias

    private val _temas =
        mutableStateListOf<Tema>()

    val temas: List<Tema>
        get() = _temas

    private var siguienteMateriaId = 1
    private var siguienteTemaId = 1

    // =========================
    // MATERIAS
    // =========================

    fun crearMateria(
        nombre: String,
        descripcion: String
    ): String? {

        val nombreLimpio = nombre.trim()
        val descripcionLimpia = descripcion.trim()

        if (nombreLimpio.isBlank()) {
            return "El nombre de la materia es obligatorio."
        }

        if (nombreLimpio.length < 3) {
            return "El nombre debe tener al menos 3 caracteres."
        }

        if (
            _materias.any {
                it.nombre.equals(
                    nombreLimpio,
                    ignoreCase = true
                )
            }
        ) {
            return "Ya existe una materia con ese nombre."
        }

        val materia = Materia(
            id = siguienteMateriaId++,
            nombre = nombreLimpio,
            descripcion = descripcionLimpia
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

        val eliminada =
            _materias.removeAll {
                it.id == id
            }

        if (eliminada) {

            _temas.removeAll {
                it.materiaId == id
            }
        }

        return eliminada
    }

    fun obtenerMateria(
        id: Int
    ): Materia? {

        return _materias.find {
            it.id == id
        }
    }

    // =========================
    // TEMAS
    // =========================

    fun temasPorMateria(
        materiaId: Int
    ): List<Tema> {

        return _temas.filter {
            it.materiaId == materiaId
        }
    }

    fun crearTema(
        materiaId: Int,
        nombre: String,
        descripcion: String
    ): String? {

        val nombreLimpio = nombre.trim()
        val descripcionLimpia = descripcion.trim()

        if (nombreLimpio.isBlank()) {
            return "El nombre del tema es obligatorio."
        }

        if (nombreLimpio.length < 3) {
            return "El nombre debe tener al menos 3 caracteres."
        }

        if (obtenerMateria(materiaId) == null) {
            return "La materia no existe."
        }

        val duplicado =
            _temas.any {
                it.materiaId == materiaId &&
                        it.nombre.equals(
                            nombreLimpio,
                            ignoreCase = true
                        )
            }

        if (duplicado) {
            return "Ya existe un tema con ese nombre en esta materia."
        }

        val tema = Tema(
            id = siguienteTemaId++,
            materiaId = materiaId,
            nombre = nombreLimpio,
            descripcion = descripcionLimpia
        )

        _temas.add(tema)

        return null
    }

    fun actualizarTema(
        id: Int,
        nombre: String,
        descripcion: String
    ): String? {

        val nombreLimpio = nombre.trim()
        val descripcionLimpia = descripcion.trim()

        if (nombreLimpio.isBlank()) {
            return "El nombre del tema es obligatorio."
        }

        if (nombreLimpio.length < 3) {
            return "El nombre debe tener al menos 3 caracteres."
        }

        val indice =
            _temas.indexOfFirst {
                it.id == id
            }

        if (indice == -1) {
            return "Tema no encontrado."
        }

        val temaActual =
            _temas[indice]

        val duplicado =
            _temas.any {
                it.id != id &&
                        it.materiaId == temaActual.materiaId &&
                        it.nombre.equals(
                            nombreLimpio,
                            ignoreCase = true
                        )
            }

        if (duplicado) {
            return "Ya existe otro tema con ese nombre."
        }

        _temas[indice] =
            temaActual.copy(
                nombre = nombreLimpio,
                descripcion = descripcionLimpia
            )

        return null
    }

    fun eliminarTema(
        id: Int
    ): Boolean {

        return _temas.removeAll {
            it.id == id
        }
    }
}
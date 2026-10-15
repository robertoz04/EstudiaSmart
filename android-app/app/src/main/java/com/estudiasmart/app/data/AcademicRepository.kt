package com.estudiasmart.app.data

import androidx.compose.runtime.mutableStateListOf
import com.estudiasmart.app.model.Material
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

    private val _materiales =
        mutableStateListOf<Material>()

    val materiales: List<Material>
        get() = _materiales

    private var siguienteMateriaId = 1
    private var siguienteTemaId = 1
    private var siguienteMaterialId = 1

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

        val temasRelacionados =
            _temas.filter {
                it.materiaId == id
            }.map {
                it.id
            }

        _materiales.removeAll {
            it.temaId in temasRelacionados
        }

        _temas.removeAll {
            it.materiaId == id
        }

        return _materias.removeAll {
            it.id == id
        }
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

    fun obtenerTema(
        id: Int
    ): Tema? {

        return _temas.find {
            it.id == id
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

        _materiales.removeAll {
            it.temaId == id
        }

        return _temas.removeAll {
            it.id == id
        }
    }

    // =========================
    // MATERIALES
    // =========================

    fun materialesPorTema(
        temaId: Int
    ): List<Material> {

        return _materiales.filter {
            it.temaId == temaId
        }
    }

    fun crearMaterial(
        temaId: Int,
        titulo: String,
        contenido: String
    ): String? {

        val tituloLimpio = titulo.trim()
        val contenidoLimpio = contenido.trim()

        if (tituloLimpio.isBlank()) {
            return "El título del material es obligatorio."
        }

        if (tituloLimpio.length < 3) {
            return "El título debe tener al menos 3 caracteres."
        }

        if (contenidoLimpio.isBlank()) {
            return "El contenido del material es obligatorio."
        }

        if (obtenerTema(temaId) == null) {
            return "El tema no existe."
        }

        val duplicado =
            _materiales.any {
                it.temaId == temaId &&
                        it.titulo.equals(
                            tituloLimpio,
                            ignoreCase = true
                        )
            }

        if (duplicado) {
            return "Ya existe un material con ese título."
        }

        val material = Material(
            id = siguienteMaterialId++,
            temaId = temaId,
            titulo = tituloLimpio,
            contenido = contenidoLimpio
        )

        _materiales.add(material)

        return null
    }

    fun actualizarMaterial(
        id: Int,
        titulo: String,
        contenido: String
    ): String? {

        val tituloLimpio = titulo.trim()
        val contenidoLimpio = contenido.trim()

        if (tituloLimpio.isBlank()) {
            return "El título del material es obligatorio."
        }

        if (tituloLimpio.length < 3) {
            return "El título debe tener al menos 3 caracteres."
        }

        if (contenidoLimpio.isBlank()) {
            return "El contenido del material es obligatorio."
        }

        val indice =
            _materiales.indexOfFirst {
                it.id == id
            }

        if (indice == -1) {
            return "Material no encontrado."
        }

        val materialActual =
            _materiales[indice]

        val duplicado =
            _materiales.any {
                it.id != id &&
                        it.temaId == materialActual.temaId &&
                        it.titulo.equals(
                            tituloLimpio,
                            ignoreCase = true
                        )
            }

        if (duplicado) {
            return "Ya existe otro material con ese título."
        }

        _materiales[indice] =
            materialActual.copy(
                titulo = tituloLimpio,
                contenido = contenidoLimpio
            )

        return null
    }

    fun eliminarMaterial(
        id: Int
    ): Boolean {

        return _materiales.removeAll {
            it.id == id
        }
    }
}
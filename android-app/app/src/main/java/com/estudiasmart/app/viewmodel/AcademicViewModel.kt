package com.estudiasmart.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.estudiasmart.app.data.AcademicRepository
import com.estudiasmart.app.model.Material
import com.estudiasmart.app.model.Materia
import com.estudiasmart.app.model.Tema

class AcademicViewModel : ViewModel() {

    // =========================================================
    // DATOS
    // =========================================================

    val materias: List<Materia>
        get() =
            AcademicRepository.materias

    fun obtenerMateria(
        id: Int
    ): Materia? =
        AcademicRepository
            .obtenerMateria(id)

    fun temasPorMateria(
        materiaId: Int
    ): List<Tema> =
        AcademicRepository
            .temasPorMateria(
                materiaId
            )

    fun obtenerTema(
        id: Int
    ): Tema? =
        AcademicRepository
            .obtenerTema(id)

    fun materialesPorTema(
        temaId: Int
    ): List<Material> =
        AcademicRepository
            .materialesPorTema(
                temaId
            )

    // =========================================================
    // MATERIAS
    // =========================================================

    var nombreMateria by
    mutableStateOf("")
        private set

    var descripcionMateria by
    mutableStateOf("")
        private set

    var errorMateria by
    mutableStateOf<String?>(
        null
    )
        private set

    var materiaEditando by
    mutableStateOf<Materia?>(
        null
    )
        private set

    var materiaAEliminar by
    mutableStateOf<Materia?>(
        null
    )
        private set

    fun cambiarNombreMateria(
        valor: String
    ) {

        nombreMateria = valor
        errorMateria = null
    }

    fun cambiarDescripcionMateria(
        valor: String
    ) {

        descripcionMateria = valor
        errorMateria = null
    }

    fun guardarMateria() {

        val resultado =
            if (
                materiaEditando ==
                null
            ) {

                AcademicRepository
                    .crearMateria(
                        nombre =
                            nombreMateria,
                        descripcion =
                            descripcionMateria
                    )

            } else {

                AcademicRepository
                    .actualizarMateria(
                        id =
                            materiaEditando!!
                                .id,
                        nombre =
                            nombreMateria,
                        descripcion =
                            descripcionMateria
                    )
            }

        if (resultado == null) {

            limpiarFormularioMateria()

        } else {

            errorMateria =
                resultado
        }
    }

    fun iniciarEdicionMateria(
        materia: Materia
    ) {

        materiaEditando =
            materia

        nombreMateria =
            materia.nombre

        descripcionMateria =
            materia.descripcion

        errorMateria = null
    }

    fun cancelarEdicionMateria() {

        limpiarFormularioMateria()
    }

    fun solicitarEliminarMateria(
        materia: Materia
    ) {

        materiaAEliminar =
            materia
    }

    fun cancelarEliminarMateria() {

        materiaAEliminar =
            null
    }

    fun confirmarEliminarMateria() {

        val materia =
            materiaAEliminar
                ?: return

        val id =
            materia.id

        AcademicRepository
            .eliminarMateria(id)

        if (
            materiaEditando?.id ==
            id
        ) {

            limpiarFormularioMateria()
        }

        materiaAEliminar =
            null
    }

    private fun limpiarFormularioMateria() {

        nombreMateria = ""
        descripcionMateria = ""
        materiaEditando = null
        errorMateria = null
    }

    // =========================================================
    // TEMAS
    // =========================================================

    var nombreTema by
    mutableStateOf("")
        private set

    var descripcionTema by
    mutableStateOf("")
        private set

    var errorTema by
    mutableStateOf<String?>(
        null
    )
        private set

    var temaEditando by
    mutableStateOf<Tema?>(
        null
    )
        private set

    var temaAEliminar by
    mutableStateOf<Tema?>(
        null
    )
        private set

    fun cambiarNombreTema(
        valor: String
    ) {

        nombreTema = valor
        errorTema = null
    }

    fun cambiarDescripcionTema(
        valor: String
    ) {

        descripcionTema = valor
        errorTema = null
    }

    fun guardarTema(
        materiaId: Int
    ) {

        val resultado =
            if (
                temaEditando ==
                null
            ) {

                AcademicRepository
                    .crearTema(
                        materiaId =
                            materiaId,
                        nombre =
                            nombreTema,
                        descripcion =
                            descripcionTema
                    )

            } else {

                AcademicRepository
                    .actualizarTema(
                        id =
                            temaEditando!!
                                .id,
                        nombre =
                            nombreTema,
                        descripcion =
                            descripcionTema
                    )
            }

        if (resultado == null) {

            limpiarFormularioTema()

        } else {

            errorTema =
                resultado
        }
    }

    fun iniciarEdicionTema(
        tema: Tema
    ) {

        temaEditando =
            tema

        nombreTema =
            tema.nombre

        descripcionTema =
            tema.descripcion

        errorTema = null
    }

    fun cancelarEdicionTema() {

        limpiarFormularioTema()
    }

    fun solicitarEliminarTema(
        tema: Tema
    ) {

        temaAEliminar =
            tema
    }

    fun cancelarEliminarTema() {

        temaAEliminar =
            null
    }

    fun confirmarEliminarTema() {

        val tema =
            temaAEliminar
                ?: return

        val id =
            tema.id

        AcademicRepository
            .eliminarTema(id)

        if (
            temaEditando?.id ==
            id
        ) {

            limpiarFormularioTema()
        }

        temaAEliminar =
            null
    }

    private fun limpiarFormularioTema() {

        nombreTema = ""
        descripcionTema = ""
        temaEditando = null
        errorTema = null
    }

    // =========================================================
    // MATERIALES
    // =========================================================

    var tituloMaterial by
    mutableStateOf("")
        private set

    var contenidoMaterial by
    mutableStateOf("")
        private set

    var errorMaterial by
    mutableStateOf<String?>(
        null
    )
        private set

    var materialEditando by
    mutableStateOf<Material?>(
        null
    )
        private set

    var materialAEliminar by
    mutableStateOf<Material?>(
        null
    )
        private set

    fun cambiarTituloMaterial(
        valor: String
    ) {

        tituloMaterial = valor
        errorMaterial = null
    }

    fun cambiarContenidoMaterial(
        valor: String
    ) {

        contenidoMaterial = valor
        errorMaterial = null
    }

    fun guardarMaterial(
        temaId: Int
    ) {

        val resultado =
            if (
                materialEditando ==
                null
            ) {

                AcademicRepository
                    .crearMaterial(
                        temaId =
                            temaId,
                        titulo =
                            tituloMaterial,
                        contenido =
                            contenidoMaterial
                    )

            } else {

                AcademicRepository
                    .actualizarMaterial(
                        id =
                            materialEditando!!
                                .id,
                        titulo =
                            tituloMaterial,
                        contenido =
                            contenidoMaterial
                    )
            }

        if (resultado == null) {

            limpiarFormularioMaterial()

        } else {

            errorMaterial =
                resultado
        }
    }

    fun iniciarEdicionMaterial(
        material: Material
    ) {

        materialEditando =
            material

        tituloMaterial =
            material.titulo

        contenidoMaterial =
            material.contenido

        errorMaterial = null
    }

    fun cancelarEdicionMaterial() {

        limpiarFormularioMaterial()
    }

    fun solicitarEliminarMaterial(
        material: Material
    ) {

        materialAEliminar =
            material
    }

    fun cancelarEliminarMaterial() {

        materialAEliminar =
            null
    }

    fun confirmarEliminarMaterial() {

        val material =
            materialAEliminar
                ?: return

        val id =
            material.id

        AcademicRepository
            .eliminarMaterial(id)

        if (
            materialEditando?.id ==
            id
        ) {

            limpiarFormularioMaterial()
        }

        materialAEliminar =
            null
    }

    private fun limpiarFormularioMaterial() {

        tituloMaterial = ""
        contenidoMaterial = ""
        materialEditando = null
        errorMaterial = null
    }
}
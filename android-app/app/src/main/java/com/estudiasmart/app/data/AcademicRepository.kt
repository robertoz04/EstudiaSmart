package com.estudiasmart.app.data

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.estudiasmart.app.model.Material
import com.estudiasmart.app.model.Materia
import com.estudiasmart.app.model.Tema
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions

object AcademicRepository {

    // =========================
    // FIREBASE
    // =========================

    private val auth =
        FirebaseAuth.getInstance()

    private val firestore =
        FirebaseFirestore.getInstance()

    private var materiasListener:
            ListenerRegistration? = null

    private var uidSincronizado:
            String? = null

    // =========================
    // LISTAS LOCALES
    // =========================

    private val _materias =
        mutableStateListOf<Materia>()

    val materias: List<Materia>
        get() {
            asegurarSincronizacionMaterias()
            return _materias
        }

    private val _temas =
        mutableStateListOf<Tema>()

    val temas: List<Tema>
        get() {
            asegurarSincronizacionMaterias()
            return _temas
        }

    private val _materiales =
        mutableStateListOf<Material>()

    val materiales: List<Material>
        get() {
            asegurarSincronizacionMaterias()
            return _materiales
        }

    private var siguienteMateriaId = 1
    private var siguienteTemaId = 1
    private var siguienteMaterialId = 1

    // =========================================================
    // SINCRONIZACIÓN DE MATERIAS CON FIRESTORE
    // =========================================================

    private fun asegurarSincronizacionMaterias() {

        val uidActual =
            auth.currentUser?.uid

        if (uidActual == null) {

            if (uidSincronizado != null) {
                limpiarDatosLocales()
            }

            return
        }

        if (
            uidSincronizado == uidActual &&
            materiasListener != null
        ) {
            return
        }

        materiasListener?.remove()

        uidSincronizado = uidActual

        _materias.clear()
        _temas.clear()
        _materiales.clear()

        materiasListener =
            firestore
                .collection("usuarios")
                .document(uidActual)
                .collection("materias")
                .orderBy("id")
                .addSnapshotListener { snapshot, error ->

                    if (error != null) {

                        Log.e(
                            "AcademicRepository",
                            "Error cargando materias desde Firestore",
                            error
                        )

                        return@addSnapshotListener
                    }

                    if (snapshot == null) {
                        return@addSnapshotListener
                    }

                    val materiasFirestore =
                        snapshot.documents.mapNotNull { documento ->

                            val id =
                                documento
                                    .getLong("id")
                                    ?.toInt()
                                    ?: documento.id.toIntOrNull()
                                    ?: return@mapNotNull null

                            val nombre =
                                documento
                                    .getString("nombre")
                                    ?: ""

                            val descripcion =
                                documento
                                    .getString("descripcion")
                                    ?: ""

                            Materia(
                                id = id,
                                nombre = nombre,
                                descripcion = descripcion
                            )
                        }

                    _materias.clear()
                    _materias.addAll(
                        materiasFirestore
                    )

                    siguienteMateriaId =
                        (
                                _materias
                                    .maxOfOrNull {
                                        it.id
                                    }
                                    ?: 0
                                ) + 1
                }
    }

    private fun referenciaMaterias(
        uid: String
    ) =
        firestore
            .collection("usuarios")
            .document(uid)
            .collection("materias")

    private fun guardarMateriaFirestore(
        uid: String,
        materia: Materia,
        esNueva: Boolean
    ) {

        val datos =
            hashMapOf<String, Any>(
                "id" to materia.id,
                "nombre" to materia.nombre,
                "descripcion" to materia.descripcion,
                "actualizadoEn" to FieldValue.serverTimestamp()
            )

        if (esNueva) {
            datos["creadoEn"] =
                FieldValue.serverTimestamp()
        }

        referenciaMaterias(uid)
            .document(
                materia.id.toString()
            )
            .set(
                datos,
                SetOptions.merge()
            )
            .addOnFailureListener { error ->

                Log.e(
                    "AcademicRepository",
                    "Error guardando materia en Firestore",
                    error
                )
            }
    }

    // =========================================================
    // LIMPIEZA DE DATOS
    // =========================================================

    fun limpiarDatosLocales() {

        materiasListener?.remove()
        materiasListener = null

        uidSincronizado = null

        _materias.clear()
        _temas.clear()
        _materiales.clear()

        siguienteMateriaId = 1
        siguienteTemaId = 1
        siguienteMaterialId = 1
    }

    // =========================
    // MATERIAS
    // =========================

    fun crearMateria(
        nombre: String,
        descripcion: String
    ): String? {

        asegurarSincronizacionMaterias()

        val uid =
            auth.currentUser?.uid
                ?: return "Debes iniciar sesión para crear una materia."

        val nombreLimpio =
            nombre.trim()

        val descripcionLimpia =
            descripcion.trim()

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

        val nuevoId =
            maxOf(
                siguienteMateriaId,
                (
                        _materias
                            .maxOfOrNull {
                                it.id
                            }
                            ?: 0
                        ) + 1
            )

        siguienteMateriaId =
            nuevoId + 1

        val materia =
            Materia(
                id = nuevoId,
                nombre = nombreLimpio,
                descripcion = descripcionLimpia
            )

        _materias.add(
            materia
        )

        guardarMateriaFirestore(
            uid = uid,
            materia = materia,
            esNueva = true
        )

        return null
    }

    fun actualizarMateria(
        id: Int,
        nombre: String,
        descripcion: String
    ): String? {

        asegurarSincronizacionMaterias()

        val uid =
            auth.currentUser?.uid
                ?: return "Debes iniciar sesión para actualizar una materia."

        val nombreLimpio =
            nombre.trim()

        val descripcionLimpia =
            descripcion.trim()

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

        val materiaActualizada =
            materiaActual.copy(
                nombre = nombreLimpio,
                descripcion = descripcionLimpia
            )

        _materias[indice] =
            materiaActualizada

        guardarMateriaFirestore(
            uid = uid,
            materia = materiaActualizada,
            esNueva = false
        )

        return null
    }

    fun eliminarMateria(
        id: Int
    ): Boolean {

        asegurarSincronizacionMaterias()

        val uid =
            auth.currentUser?.uid
                ?: return false

        val temasRelacionados =
            _temas
                .filter {
                    it.materiaId == id
                }
                .map {
                    it.id
                }

        _materiales.removeAll {
            it.temaId in temasRelacionados
        }

        _temas.removeAll {
            it.materiaId == id
        }

        val eliminada =
            _materias.removeAll {
                it.id == id
            }

        if (eliminada) {

            referenciaMaterias(uid)
                .document(
                    id.toString()
                )
                .delete()
                .addOnFailureListener { error ->

                    Log.e(
                        "AcademicRepository",
                        "Error eliminando materia de Firestore",
                        error
                    )
                }
        }

        return eliminada
    }

    fun obtenerMateria(
        id: Int
    ): Materia? {

        asegurarSincronizacionMaterias()

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

        val nombreLimpio =
            nombre.trim()

        val descripcionLimpia =
            descripcion.trim()

        if (nombreLimpio.isBlank()) {
            return "El nombre del tema es obligatorio."
        }

        if (nombreLimpio.length < 3) {
            return "El nombre debe tener al menos 3 caracteres."
        }

        if (
            obtenerMateria(
                materiaId
            ) == null
        ) {
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

        val tema =
            Tema(
                id = siguienteTemaId++,
                materiaId = materiaId,
                nombre = nombreLimpio,
                descripcion = descripcionLimpia
            )

        _temas.add(
            tema
        )

        return null
    }

    fun actualizarTema(
        id: Int,
        nombre: String,
        descripcion: String
    ): String? {

        val nombreLimpio =
            nombre.trim()

        val descripcionLimpia =
            descripcion.trim()

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

        val tituloLimpio =
            titulo.trim()

        val contenidoLimpio =
            contenido.trim()

        if (tituloLimpio.isBlank()) {
            return "El título del material es obligatorio."
        }

        if (tituloLimpio.length < 3) {
            return "El título debe tener al menos 3 caracteres."
        }

        if (contenidoLimpio.isBlank()) {
            return "El contenido del material es obligatorio."
        }

        if (
            obtenerTema(
                temaId
            ) == null
        ) {
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

        val material =
            Material(
                id = siguienteMaterialId++,
                temaId = temaId,
                titulo = tituloLimpio,
                contenido = contenidoLimpio
            )

        _materiales.add(
            material
        )

        return null
    }

    fun actualizarMaterial(
        id: Int,
        titulo: String,
        contenido: String
    ): String? {

        val tituloLimpio =
            titulo.trim()

        val contenidoLimpio =
            contenido.trim()

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
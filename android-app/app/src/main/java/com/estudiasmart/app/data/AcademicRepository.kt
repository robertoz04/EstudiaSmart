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

    private var temasListener:
            ListenerRegistration? = null

    private var materialesListener:
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
            asegurarSincronizacionFirestore()
            return _materias
        }

    private val _temas =
        mutableStateListOf<Tema>()

    val temas: List<Tema>
        get() {
            asegurarSincronizacionFirestore()
            return _temas
        }

    private val _materiales =
        mutableStateListOf<Material>()

    val materiales: List<Material>
        get() {
            asegurarSincronizacionFirestore()
            return _materiales
        }

    private var siguienteMateriaId = 1
    private var siguienteTemaId = 1
    private var siguienteMaterialId = 1

    // =========================================================
    // SINCRONIZACIÓN GENERAL
    // =========================================================

    private fun asegurarSincronizacionFirestore() {

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
            materiasListener != null &&
            temasListener != null &&
            materialesListener != null
        ) {
            return
        }

        iniciarSincronizacionUsuario(
            uidActual
        )
    }

    private fun iniciarSincronizacionUsuario(
        uid: String
    ) {

        materiasListener?.remove()
        temasListener?.remove()
        materialesListener?.remove()

        materiasListener = null
        temasListener = null
        materialesListener = null

        uidSincronizado = uid

        crearOActualizarPerfilUsuario(uid)

        _materias.clear()
        _temas.clear()
        _materiales.clear()

        siguienteMateriaId = 1
        siguienteTemaId = 1
        siguienteMaterialId = 1

        escucharMaterias(uid)
        escucharTemas(uid)
        escucharMateriales(uid)
    }

    // =========================================================
    // PERFIL DEL USUARIO
    // =========================================================

    private fun crearOActualizarPerfilUsuario(
        uid: String
    ) {

        val usuarioFirebase =
            auth.currentUser ?: return

        val datosUsuario =
            hashMapOf<String, Any>(
                "uid" to uid,
                "correo" to (
                        usuarioFirebase.email ?: ""
                        ),
                "nombre" to (
                        usuarioFirebase.displayName
                            ?: usuarioFirebase.email
                                ?.substringBefore("@")
                            ?: "Usuario"
                        ),
                "ultimoAcceso" to
                        FieldValue.serverTimestamp()
            )

        firestore
            .collection("usuarios")
            .document(uid)
            .set(
                datosUsuario,
                SetOptions.merge()
            )
            .addOnFailureListener { error ->

                Log.e(
                    "AcademicRepository",
                    "Error creando perfil de usuario",
                    error
                )
            }
    }

    // =========================================================
    // LISTENER MATERIAS
    // =========================================================

    private fun escucharMaterias(
        uid: String
    ) {

        materiasListener =
            referenciaMaterias(uid)
                .orderBy("id")
                .addSnapshotListener { snapshot, error ->

                    if (error != null) {

                        Log.e(
                            "AcademicRepository",
                            "Error cargando materias",
                            error
                        )

                        return@addSnapshotListener
                    }

                    if (snapshot == null) {
                        return@addSnapshotListener
                    }

                    val lista =
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
                    _materias.addAll(lista)

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

    // =========================================================
    // LISTENER TEMAS
    // =========================================================

    private fun escucharTemas(
        uid: String
    ) {

        temasListener =
            referenciaTemas(uid)
                .orderBy("id")
                .addSnapshotListener { snapshot, error ->

                    if (error != null) {

                        Log.e(
                            "AcademicRepository",
                            "Error cargando temas",
                            error
                        )

                        return@addSnapshotListener
                    }

                    if (snapshot == null) {
                        return@addSnapshotListener
                    }

                    val lista =
                        snapshot.documents.mapNotNull { documento ->

                            val id =
                                documento
                                    .getLong("id")
                                    ?.toInt()
                                    ?: documento.id.toIntOrNull()
                                    ?: return@mapNotNull null

                            val materiaId =
                                documento
                                    .getLong("materiaId")
                                    ?.toInt()
                                    ?: return@mapNotNull null

                            val nombre =
                                documento
                                    .getString("nombre")
                                    ?: ""

                            val descripcion =
                                documento
                                    .getString("descripcion")
                                    ?: ""

                            Tema(
                                id = id,
                                materiaId = materiaId,
                                nombre = nombre,
                                descripcion = descripcion
                            )
                        }

                    _temas.clear()
                    _temas.addAll(lista)

                    siguienteTemaId =
                        (
                                _temas
                                    .maxOfOrNull {
                                        it.id
                                    }
                                    ?: 0
                                ) + 1
                }
    }

    // =========================================================
    // LISTENER MATERIALES
    // =========================================================

    private fun escucharMateriales(
        uid: String
    ) {

        materialesListener =
            referenciaMateriales(uid)
                .orderBy("id")
                .addSnapshotListener { snapshot, error ->

                    if (error != null) {

                        Log.e(
                            "AcademicRepository",
                            "Error cargando materiales",
                            error
                        )

                        return@addSnapshotListener
                    }

                    if (snapshot == null) {
                        return@addSnapshotListener
                    }

                    val lista =
                        snapshot.documents.mapNotNull { documento ->

                            val id =
                                documento
                                    .getLong("id")
                                    ?.toInt()
                                    ?: documento.id.toIntOrNull()
                                    ?: return@mapNotNull null

                            val temaId =
                                documento
                                    .getLong("temaId")
                                    ?.toInt()
                                    ?: return@mapNotNull null

                            val titulo =
                                documento
                                    .getString("titulo")
                                    ?: ""

                            val contenido =
                                documento
                                    .getString("contenido")
                                    ?: ""

                            Material(
                                id = id,
                                temaId = temaId,
                                titulo = titulo,
                                contenido = contenido
                            )
                        }

                    _materiales.clear()
                    _materiales.addAll(lista)

                    siguienteMaterialId =
                        (
                                _materiales
                                    .maxOfOrNull {
                                        it.id
                                    }
                                    ?: 0
                                ) + 1
                }
    }

    // =========================================================
    // REFERENCIAS FIRESTORE
    // =========================================================

    private fun referenciaMaterias(
        uid: String
    ) =
        firestore
            .collection("usuarios")
            .document(uid)
            .collection("materias")

    private fun referenciaTemas(
        uid: String
    ) =
        firestore
            .collection("usuarios")
            .document(uid)
            .collection("temas")

    private fun referenciaMateriales(
        uid: String
    ) =
        firestore
            .collection("usuarios")
            .document(uid)
            .collection("materiales")

    // =========================================================
    // GUARDAR MATERIA
    // =========================================================

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
                "actualizadoEn" to
                        FieldValue.serverTimestamp()
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
                    "Error guardando materia",
                    error
                )
            }
    }

    // =========================================================
    // GUARDAR TEMA
    // =========================================================

    private fun guardarTemaFirestore(
        uid: String,
        tema: Tema,
        esNuevo: Boolean
    ) {

        val datos =
            hashMapOf<String, Any>(
                "id" to tema.id,
                "materiaId" to tema.materiaId,
                "nombre" to tema.nombre,
                "descripcion" to tema.descripcion,
                "actualizadoEn" to
                        FieldValue.serverTimestamp()
            )

        if (esNuevo) {

            datos["creadoEn"] =
                FieldValue.serverTimestamp()
        }

        referenciaTemas(uid)
            .document(
                tema.id.toString()
            )
            .set(
                datos,
                SetOptions.merge()
            )
            .addOnFailureListener { error ->

                Log.e(
                    "AcademicRepository",
                    "Error guardando tema",
                    error
                )
            }
    }

    // =========================================================
    // GUARDAR MATERIAL
    // =========================================================

    private fun guardarMaterialFirestore(
        uid: String,
        material: Material,
        esNuevo: Boolean
    ) {

        val datos =
            hashMapOf<String, Any>(
                "id" to material.id,
                "temaId" to material.temaId,
                "titulo" to material.titulo,
                "contenido" to material.contenido,
                "actualizadoEn" to
                        FieldValue.serverTimestamp()
            )

        if (esNuevo) {

            datos["creadoEn"] =
                FieldValue.serverTimestamp()
        }

        referenciaMateriales(uid)
            .document(
                material.id.toString()
            )
            .set(
                datos,
                SetOptions.merge()
            )
            .addOnFailureListener { error ->

                Log.e(
                    "AcademicRepository",
                    "Error guardando material",
                    error
                )
            }
    }

    // =========================================================
    // LIMPIEZA
    // =========================================================

    fun limpiarDatosLocales() {

        materiasListener?.remove()
        temasListener?.remove()
        materialesListener?.remove()

        materiasListener = null
        temasListener = null
        materialesListener = null

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

        asegurarSincronizacionFirestore()

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
                (_materias.maxOfOrNull { it.id } ?: 0) + 1
            )

        siguienteMateriaId =
            nuevoId + 1

        val materia =
            Materia(
                id = nuevoId,
                nombre = nombreLimpio,
                descripcion = descripcionLimpia
            )

        _materias.add(materia)

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

        asegurarSincronizacionFirestore()

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

        val duplicado =
            _materias.any {

                it.id != id &&
                        it.nombre.equals(
                            nombreLimpio,
                            ignoreCase = true
                        )
            }

        if (duplicado) {
            return "Ya existe otra materia con ese nombre."
        }

        val materia =
            _materias[indice].copy(
                nombre = nombreLimpio,
                descripcion = descripcionLimpia
            )

        _materias[indice] =
            materia

        guardarMateriaFirestore(
            uid = uid,
            materia = materia,
            esNueva = false
        )

        return null
    }

    fun eliminarMateria(
        id: Int
    ): Boolean {

        asegurarSincronizacionFirestore()

        val uid =
            auth.currentUser?.uid
                ?: return false

        val temasRelacionados =
            _temas.filter {
                it.materiaId == id
            }

        val idsTemas =
            temasRelacionados.map {
                it.id
            }

        val materialesRelacionados =
            _materiales.filter {
                it.temaId in idsTemas
            }

        // Eliminar materiales relacionados
        materialesRelacionados.forEach { material ->

            referenciaMateriales(uid)
                .document(
                    material.id.toString()
                )
                .delete()
        }

        _materiales.removeAll {
            it.temaId in idsTemas
        }

        // Eliminar temas relacionados
        temasRelacionados.forEach { tema ->

            referenciaTemas(uid)
                .document(
                    tema.id.toString()
                )
                .delete()
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
        }

        return eliminada
    }

    fun obtenerMateria(
        id: Int
    ): Materia? {

        asegurarSincronizacionFirestore()

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

        asegurarSincronizacionFirestore()

        return _temas.filter {
            it.materiaId == materiaId
        }
    }

    fun obtenerTema(
        id: Int
    ): Tema? {

        asegurarSincronizacionFirestore()

        return _temas.find {
            it.id == id
        }
    }

    fun crearTema(
        materiaId: Int,
        nombre: String,
        descripcion: String
    ): String? {

        asegurarSincronizacionFirestore()

        val uid =
            auth.currentUser?.uid
                ?: return "Debes iniciar sesión para crear un tema."

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

        val nuevoId =
            maxOf(
                siguienteTemaId,
                (_temas.maxOfOrNull { it.id } ?: 0) + 1
            )

        siguienteTemaId =
            nuevoId + 1

        val tema =
            Tema(
                id = nuevoId,
                materiaId = materiaId,
                nombre = nombreLimpio,
                descripcion = descripcionLimpia
            )

        _temas.add(tema)

        guardarTemaFirestore(
            uid = uid,
            tema = tema,
            esNuevo = true
        )

        return null
    }

    fun actualizarTema(
        id: Int,
        nombre: String,
        descripcion: String
    ): String? {

        asegurarSincronizacionFirestore()

        val uid =
            auth.currentUser?.uid
                ?: return "Debes iniciar sesión para actualizar un tema."

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

        val tema =
            temaActual.copy(
                nombre = nombreLimpio,
                descripcion = descripcionLimpia
            )

        _temas[indice] =
            tema

        guardarTemaFirestore(
            uid = uid,
            tema = tema,
            esNuevo = false
        )

        return null
    }

    fun eliminarTema(
        id: Int
    ): Boolean {

        asegurarSincronizacionFirestore()

        val uid =
            auth.currentUser?.uid
                ?: return false

        val materialesRelacionados =
            _materiales.filter {
                it.temaId == id
            }

        materialesRelacionados.forEach { material ->

            referenciaMateriales(uid)
                .document(
                    material.id.toString()
                )
                .delete()
        }

        _materiales.removeAll {
            it.temaId == id
        }

        val eliminado =
            _temas.removeAll {
                it.id == id
            }

        if (eliminado) {

            referenciaTemas(uid)
                .document(
                    id.toString()
                )
                .delete()
        }

        return eliminado
    }

    // =========================
    // MATERIALES
    // =========================

    fun materialesPorTema(
        temaId: Int
    ): List<Material> {

        asegurarSincronizacionFirestore()

        return _materiales.filter {
            it.temaId == temaId
        }
    }

    fun crearMaterial(
        temaId: Int,
        titulo: String,
        contenido: String
    ): String? {

        asegurarSincronizacionFirestore()

        val uid =
            auth.currentUser?.uid
                ?: return "Debes iniciar sesión para crear un material."

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

        val nuevoId =
            maxOf(
                siguienteMaterialId,
                (_materiales.maxOfOrNull { it.id } ?: 0) + 1
            )

        siguienteMaterialId =
            nuevoId + 1

        val material =
            Material(
                id = nuevoId,
                temaId = temaId,
                titulo = tituloLimpio,
                contenido = contenidoLimpio
            )

        _materiales.add(
            material
        )

        guardarMaterialFirestore(
            uid = uid,
            material = material,
            esNuevo = true
        )

        return null
    }

    fun actualizarMaterial(
        id: Int,
        titulo: String,
        contenido: String
    ): String? {

        asegurarSincronizacionFirestore()

        val uid =
            auth.currentUser?.uid
                ?: return "Debes iniciar sesión para actualizar un material."

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

        val material =
            materialActual.copy(
                titulo = tituloLimpio,
                contenido = contenidoLimpio
            )

        _materiales[indice] =
            material

        guardarMaterialFirestore(
            uid = uid,
            material = material,
            esNuevo = false
        )

        return null
    }

    fun eliminarMaterial(
        id: Int
    ): Boolean {

        asegurarSincronizacionFirestore()

        val uid =
            auth.currentUser?.uid
                ?: return false

        val eliminado =
            _materiales.removeAll {
                it.id == id
            }

        if (eliminado) {

            referenciaMateriales(uid)
                .document(
                    id.toString()
                )
                .delete()
                .addOnFailureListener { error ->

                    Log.e(
                        "AcademicRepository",
                        "Error eliminando material",
                        error
                    )
                }
        }

        return eliminado
    }
}
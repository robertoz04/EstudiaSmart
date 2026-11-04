package com.estudiasmart.app.data

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

data class AppUser(
    val nombre: String,
    val correo: String,
    val contrasena: String = "",
    val uid: String = ""
)

object AuthRepository {

    private val auth: FirebaseAuth =
        FirebaseAuth.getInstance()

    var usuarioActual: AppUser? =
        convertirUsuario(
            auth.currentUser
        )
        private set

    // =========================
    // REGISTRO CON FIREBASE
    // =========================

    fun registrar(
        nombre: String,
        correo: String,
        contrasena: String,
        onResult: (String?) -> Unit
    ) {

        val nombreLimpio =
            nombre.trim()

        val correoNormalizado =
            correo.trim().lowercase()

        if (nombreLimpio.isBlank()) {

            onResult(
                "Ingresa tu nombre."
            )

            return
        }

        if (correoNormalizado.isBlank()) {

            onResult(
                "Ingresa tu correo electrónico."
            )

            return
        }

        if (contrasena.isBlank()) {

            onResult(
                "Ingresa una contraseña."
            )

            return
        }

        auth
            .createUserWithEmailAndPassword(
                correoNormalizado,
                contrasena
            )
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    val firebaseUser =
                        auth.currentUser

                    if (firebaseUser == null) {

                        onResult(
                            "La cuenta fue creada, pero no se pudo obtener el usuario."
                        )

                        return@addOnCompleteListener
                    }

                    val actualizacionPerfil =
                        UserProfileChangeRequest
                            .Builder()
                            .setDisplayName(
                                nombreLimpio
                            )
                            .build()

                    firebaseUser
                        .updateProfile(
                            actualizacionPerfil
                        )
                        .addOnCompleteListener { profileTask ->

                            if (
                                profileTask.isSuccessful
                            ) {

                                usuarioActual =
                                    AppUser(
                                        nombre =
                                            nombreLimpio,
                                        correo =
                                            firebaseUser.email
                                                ?: correoNormalizado,
                                        uid =
                                            firebaseUser.uid
                                    )

                                onResult(null)

                            } else {

                                usuarioActual =
                                    AppUser(
                                        nombre =
                                            nombreLimpio,
                                        correo =
                                            firebaseUser.email
                                                ?: correoNormalizado,
                                        uid =
                                            firebaseUser.uid
                                    )

                                onResult(null)
                            }
                        }

                } else {

                    val mensaje =
                        obtenerMensajeError(
                            task.exception
                        )

                    onResult(mensaje)
                }
            }
    }

    // =========================
    // LOGIN CON FIREBASE
    // =========================

    fun iniciarSesion(
        correo: String,
        contrasena: String,
        onResult: (String?) -> Unit
    ) {

        val correoNormalizado =
            correo.trim().lowercase()

        if (correoNormalizado.isBlank()) {

            onResult(
                "Ingresa tu correo electrónico."
            )

            return
        }

        if (contrasena.isBlank()) {

            onResult(
                "Ingresa tu contraseña."
            )

            return
        }

        auth
            .signInWithEmailAndPassword(
                correoNormalizado,
                contrasena
            )
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    usuarioActual =
                        convertirUsuario(
                            auth.currentUser
                        )

                    onResult(null)

                } else {

                    val mensaje =
                        obtenerMensajeError(
                            task.exception
                        )

                    onResult(mensaje)
                }
            }
    }

    // =========================
    // CERRAR SESIÓN
    // =========================

    fun cerrarSesion() {

        auth.signOut()

        usuarioActual =
            null
    }

    // =========================
    // VERIFICAR SESIÓN
    // =========================

    fun haySesionActiva(): Boolean {

        return auth.currentUser != null
    }

    // =========================
    // ACTUALIZAR USUARIO ACTUAL
    // =========================

    fun sincronizarUsuarioActual() {

        usuarioActual =
            convertirUsuario(
                auth.currentUser
            )
    }

    // =========================
    // CONVERTIR FIREBASE USER
    // =========================

    private fun convertirUsuario(
        firebaseUser: FirebaseUser?
    ): AppUser? {

        if (firebaseUser == null) {

            return null
        }

        val correo =
            firebaseUser.email.orEmpty()

        val nombre =
            firebaseUser.displayName
                ?.takeIf {
                    it.isNotBlank()
                }
                ?: correo
                    .substringBefore("@")
                    .ifBlank {
                        "Usuario"
                    }

        return AppUser(
            nombre = nombre,
            correo = correo,
            uid = firebaseUser.uid
        )
    }

    // =========================
    // MENSAJES DE ERROR
    // =========================

    private fun obtenerMensajeError(
        exception: Exception?
    ): String {

        return when (exception) {

            is FirebaseAuthUserCollisionException ->

                "Ya existe una cuenta con este correo."

            is FirebaseAuthWeakPasswordException ->

                "La contraseña es demasiado débil. Utiliza al menos 6 caracteres."

            is FirebaseAuthInvalidUserException ->

                "No existe una cuenta con este correo."

            is FirebaseAuthInvalidCredentialsException ->

                "Correo o contraseña incorrectos."

            is FirebaseNetworkException ->

                "No se pudo conectar con Firebase. Verifica tu conexión a internet."

            else ->

                exception?.localizedMessage
                    ?: "Ocurrió un error durante la autenticación."
        }
    }
}
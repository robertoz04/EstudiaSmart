package com.estudiasmart.app.data

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.estudiasmart.app.R
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

data class AppUser(
    val nombre: String,
    val correo: String,
    val contrasena: String = "",
    val uid: String = ""
)

object AuthRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    var usuarioActual: AppUser? = convertirUsuario(auth.currentUser)
        private set

    // =========================================================
    // REGISTRO CON CORREO Y CONTRASEÑA
    // =========================================================

    fun registrar(
        nombre: String,
        correo: String,
        contrasena: String,
        onResult: (String?) -> Unit
    ) {
        val nombreLimpio = nombre.trim()
        val correoNormalizado = correo.trim().lowercase()

        if (nombreLimpio.isBlank()) {
            onResult("Ingresa tu nombre.")
            return
        }

        if (correoNormalizado.isBlank()) {
            onResult("Ingresa tu correo electrónico.")
            return
        }

        if (contrasena.isBlank()) {
            onResult("Ingresa una contraseña.")
            return
        }

        auth.createUserWithEmailAndPassword(
            correoNormalizado,
            contrasena
        ).addOnCompleteListener { task ->

            if (task.isSuccessful) {

                val firebaseUser = auth.currentUser

                if (firebaseUser == null) {
                    onResult(
                        "La cuenta fue creada, pero no se pudo obtener el usuario."
                    )
                    return@addOnCompleteListener
                }

                val actualizacionPerfil =
                    UserProfileChangeRequest.Builder()
                        .setDisplayName(nombreLimpio)
                        .build()

                firebaseUser.updateProfile(actualizacionPerfil)
                    .addOnCompleteListener {

                        usuarioActual = AppUser(
                            nombre = nombreLimpio,
                            correo = firebaseUser.email
                                ?: correoNormalizado,
                            uid = firebaseUser.uid
                        )

                        onResult(null)
                    }

            } else {
                onResult(
                    obtenerMensajeError(task.exception)
                )
            }
        }
    }

    // =========================================================
    // INICIAR SESIÓN CON CORREO Y CONTRASEÑA
    // =========================================================

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

        auth.signInWithEmailAndPassword(
            correoNormalizado,
            contrasena
        ).addOnCompleteListener { task ->

            if (task.isSuccessful) {

                usuarioActual =
                    convertirUsuario(auth.currentUser)

                onResult(null)

            } else {

                onResult(
                    obtenerMensajeError(task.exception)
                )
            }
        }
    }

    // =========================================================
    // INICIAR SESIÓN CON GOOGLE
    // =========================================================

    suspend fun iniciarSesionConGoogle(
        context: Context
    ): String? {

        return try {

            val credentialManager =
                CredentialManager.create(context)

            /*
             * default_web_client_id es generado automáticamente
             * a partir del google-services.json actualizado.
             *
             * IMPORTANTE:
             * aquí se utiliza el CLIENT ID WEB y no el Android.
             */
            val webClientId =
                context.getString(
                    R.string.default_web_client_id
                )

            /*
             * Como este flujo será iniciado mediante un botón
             * "Continuar con Google", utilizamos
             * GetSignInWithGoogleOption.
             */
            val googleOption =
                GetSignInWithGoogleOption.Builder(
                    webClientId
                ).build()

            val request =
                GetCredentialRequest.Builder()
                    .addCredentialOption(
                        googleOption
                    )
                    .build()

            /*
             * Credential Manager mostrará el selector
             * de cuentas Google disponibles en el teléfono.
             */
            val resultado =
                credentialManager.getCredential(
                    context = context,
                    request = request
                )

            val credential =
                resultado.credential

            if (
                credential is CustomCredential &&
                credential.type ==
                GoogleIdTokenCredential
                    .TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {

                val googleCredential =
                    GoogleIdTokenCredential
                        .createFrom(
                            credential.data
                        )

                /*
                 * Convertimos el token de Google
                 * en una credencial válida de Firebase.
                 */
                autenticarGoogleEnFirebase(
                    googleCredential.idToken
                )

            } else {

                "No se pudo obtener una credencial válida de Google."
            }

        } catch (
            e: GetCredentialCancellationException
        ) {

            "Inicio de sesión con Google cancelado."

        } catch (
            e: GetCredentialException
        ) {

            e.localizedMessage
                ?: "No se pudo iniciar sesión con Google."

        } catch (
            e: Exception
        ) {

            e.localizedMessage
                ?: "Ocurrió un error al iniciar sesión con Google."
        }
    }

    // =========================================================
    // AUTENTICAR TOKEN DE GOOGLE EN FIREBASE
    // =========================================================

    private suspend fun autenticarGoogleEnFirebase(
        idToken: String
    ): String? {

        return suspendCoroutine { continuation ->

            val firebaseCredential =
                GoogleAuthProvider.getCredential(
                    idToken,
                    null
                )

            auth.signInWithCredential(
                firebaseCredential
            ).addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    usuarioActual =
                        convertirUsuario(
                            auth.currentUser
                        )

                    continuation.resume(null)

                } else {

                    continuation.resume(
                        obtenerMensajeError(
                            task.exception
                        )
                    )
                }
            }
        }
    }

    // =========================================================
    // CERRAR SESIÓN
    // =========================================================

    fun cerrarSesion() {
        auth.signOut()
        usuarioActual = null
    }

    // =========================================================
    // VERIFICAR SESIÓN ACTIVA
    // =========================================================

    fun haySesionActiva(): Boolean {
        return auth.currentUser != null
    }

    // =========================================================
    // SINCRONIZAR USUARIO ACTUAL
    // =========================================================

    fun sincronizarUsuarioActual() {
        usuarioActual =
            convertirUsuario(
                auth.currentUser
            )
    }

    // =========================================================
    // CONVERTIR FIREBASE USER → APP USER
    // =========================================================

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

    // =========================================================
    // MENSAJES DE ERROR
    // =========================================================

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
                "Las credenciales proporcionadas no son válidas."

            is FirebaseNetworkException ->
                "No se pudo conectar con Firebase. Verifica tu conexión a internet."

            else ->
                exception?.localizedMessage
                    ?: "Ocurrió un error durante la autenticación."
        }
    }
}
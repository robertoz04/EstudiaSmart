package com.estudiasmart.app.viewmodel

import android.content.Context
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.estudiasmart.app.data.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    // =========================================================
    // LOGIN
    // =========================================================

    var correoLogin by mutableStateOf("")
        private set

    var contrasenaLogin by mutableStateOf("")
        private set

    var errorCorreoLogin by mutableStateOf<String?>(null)
        private set

    var errorContrasenaLogin by mutableStateOf<String?>(null)
        private set

    var errorLoginGeneral by mutableStateOf<String?>(null)
        private set

    var cargandoLogin by mutableStateOf(false)
        private set

    fun cambiarCorreoLogin(valor: String) {

        correoLogin = valor

        errorCorreoLogin = null
        errorLoginGeneral = null
    }

    fun cambiarContrasenaLogin(valor: String) {

        contrasenaLogin = valor

        errorContrasenaLogin = null
        errorLoginGeneral = null
    }

    fun iniciarSesion(
        onSuccess: () -> Unit
    ) {

        errorCorreoLogin = null
        errorContrasenaLogin = null
        errorLoginGeneral = null

        val correoLimpio =
            correoLogin
                .trim()
                .lowercase()

        var formularioValido = true

        // =========================
        // VALIDAR CORREO
        // =========================

        if (correoLimpio.isBlank()) {

            errorCorreoLogin =
                "Ingresa tu correo electrónico."

            formularioValido = false

        } else if (
            !Patterns.EMAIL_ADDRESS
                .matcher(correoLimpio)
                .matches()
        ) {

            errorCorreoLogin =
                "Ingresa un correo electrónico válido."

            formularioValido = false
        }

        // =========================
        // VALIDAR CONTRASEÑA
        // =========================

        if (contrasenaLogin.isBlank()) {

            errorContrasenaLogin =
                "Ingresa tu contraseña."

            formularioValido = false
        }

        if (!formularioValido) {
            return
        }

        // =========================
        // FIREBASE
        // =========================

        cargandoLogin = true

        AuthRepository.iniciarSesion(
            correo = correoLimpio,
            contrasena = contrasenaLogin
        ) { resultado ->

            cargandoLogin = false

            if (resultado == null) {

                onSuccess()

            } else {

                errorLoginGeneral =
                    resultado
            }
        }
    }

    // =========================================================
    // LOGIN GOOGLE
    // =========================================================

    fun iniciarSesionConGoogle(
        context: Context,
        onSuccess: () -> Unit
    ) {

        errorCorreoLogin = null
        errorContrasenaLogin = null
        errorLoginGeneral = null

        cargandoLogin = true

        viewModelScope.launch {

            val resultado =
                AuthRepository
                    .iniciarSesionConGoogle(
                        context
                    )

            cargandoLogin = false

            if (resultado == null) {

                onSuccess()

            } else {

                errorLoginGeneral =
                    resultado
            }
        }
    }

    // =========================================================
    // REGISTRO
    // =========================================================

    var nombreRegistro by mutableStateOf("")
        private set

    var correoRegistro by mutableStateOf("")
        private set

    var contrasenaRegistro by mutableStateOf("")
        private set

    var confirmarContrasenaRegistro by mutableStateOf("")
        private set

    var errorNombreRegistro by mutableStateOf<String?>(null)
        private set

    var errorCorreoRegistro by mutableStateOf<String?>(null)
        private set

    var errorContrasenaRegistro by mutableStateOf<String?>(null)
        private set

    var errorConfirmacionRegistro by mutableStateOf<String?>(null)
        private set

    var errorRegistroGeneral by mutableStateOf<String?>(null)
        private set

    var cargandoRegistro by mutableStateOf(false)
        private set

    fun cambiarNombreRegistro(
        valor: String
    ) {

        nombreRegistro = valor

        errorNombreRegistro = null
        errorRegistroGeneral = null
    }

    fun cambiarCorreoRegistro(
        valor: String
    ) {

        correoRegistro = valor

        errorCorreoRegistro = null
        errorRegistroGeneral = null
    }

    fun cambiarContrasenaRegistro(
        valor: String
    ) {

        contrasenaRegistro = valor

        errorContrasenaRegistro = null
        errorConfirmacionRegistro = null
        errorRegistroGeneral = null
    }

    fun cambiarConfirmacionRegistro(
        valor: String
    ) {

        confirmarContrasenaRegistro = valor

        errorConfirmacionRegistro = null
        errorRegistroGeneral = null
    }

    fun registrar(
        onSuccess: () -> Unit
    ) {

        errorNombreRegistro = null
        errorCorreoRegistro = null
        errorContrasenaRegistro = null
        errorConfirmacionRegistro = null
        errorRegistroGeneral = null

        val nombreLimpio =
            nombreRegistro.trim()

        val correoLimpio =
            correoRegistro
                .trim()
                .lowercase()

        var formularioValido = true

        // =========================
        // VALIDAR NOMBRE
        // =========================

        if (nombreLimpio.isBlank()) {

            errorNombreRegistro =
                "El nombre es obligatorio."

            formularioValido = false

        } else if (
            nombreLimpio.length < 3
        ) {

            errorNombreRegistro =
                "El nombre debe tener al menos 3 caracteres."

            formularioValido = false
        }

        // =========================
        // VALIDAR CORREO
        // =========================

        if (correoLimpio.isBlank()) {

            errorCorreoRegistro =
                "El correo es obligatorio."

            formularioValido = false

        } else if (
            !Patterns.EMAIL_ADDRESS
                .matcher(correoLimpio)
                .matches()
        ) {

            errorCorreoRegistro =
                "Ingrese un correo válido."

            formularioValido = false
        }

        // =========================
        // VALIDAR CONTRASEÑA
        // =========================

        if (contrasenaRegistro.isBlank()) {

            errorContrasenaRegistro =
                "La contraseña es obligatoria."

            formularioValido = false

        } else if (
            contrasenaRegistro.length < 6
        ) {

            errorContrasenaRegistro =
                "Debe tener al menos 6 caracteres."

            formularioValido = false
        }

        // =========================
        // CONFIRMAR CONTRASEÑA
        // =========================

        if (
            confirmarContrasenaRegistro
                .isBlank()
        ) {

            errorConfirmacionRegistro =
                "Confirme su contraseña."

            formularioValido = false

        } else if (
            confirmarContrasenaRegistro !=
            contrasenaRegistro
        ) {

            errorConfirmacionRegistro =
                "Las contraseñas no coinciden."

            formularioValido = false
        }

        if (!formularioValido) {
            return
        }

        // =========================
        // FIREBASE
        // =========================

        cargandoRegistro = true

        AuthRepository.registrar(
            nombre = nombreLimpio,
            correo = correoLimpio,
            contrasena = contrasenaRegistro
        ) { resultado ->

            cargandoRegistro = false

            if (resultado == null) {

                onSuccess()

            } else {

                errorRegistroGeneral =
                    resultado
            }
        }
    }
}
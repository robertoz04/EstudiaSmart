package com.estudiasmart.app.ui.screens

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.estudiasmart.app.data.AuthRepository

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit
) {

    var nombre by remember {
        mutableStateOf("")
    }

    var correo by remember {
        mutableStateOf("")
    }

    var contrasena by remember {
        mutableStateOf("")
    }

    var confirmarContrasena by remember {
        mutableStateOf("")
    }

    var errorNombre by remember {
        mutableStateOf<String?>(null)
    }

    var errorCorreo by remember {
        mutableStateOf<String?>(null)
    }

    var errorContrasena by remember {
        mutableStateOf<String?>(null)
    }

    var errorConfirmacion by remember {
        mutableStateOf<String?>(null)
    }

    var errorGeneral by remember {
        mutableStateOf<String?>(null)
    }

    val colorPrincipal = Color(0xFF4F46E5)
    val fondo = Color(0xFFF6F7FB)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(fondo)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "EstudiaSmart",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = colorPrincipal
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Crea tu cuenta y comienza a estudiar",
            fontSize = 15.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {

            Column(
                modifier = Modifier.padding(24.dp)
            ) {

                Text(
                    text = "Crear cuenta",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = "Completa tus datos para continuar.",
                    color = Color.Gray
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                OutlinedTextField(
                    value = nombre,
                    onValueChange = {
                        nombre = it
                        errorNombre = null
                        errorGeneral = null
                    },
                    label = {
                        Text("Nombre completo")
                    },
                    placeholder = {
                        Text("Ej. Beatriz Pérez")
                    },
                    isError = errorNombre != null,
                    supportingText = {
                        errorNombre?.let {
                            Text(it)
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = correo,
                    onValueChange = {
                        correo = it
                        errorCorreo = null
                        errorGeneral = null
                    },
                    label = {
                        Text("Correo electrónico")
                    },
                    placeholder = {
                        Text("correo@ejemplo.com")
                    },
                    isError = errorCorreo != null,
                    supportingText = {
                        errorCorreo?.let {
                            Text(it)
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = contrasena,
                    onValueChange = {
                        contrasena = it
                        errorContrasena = null
                        errorConfirmacion = null
                        errorGeneral = null
                    },
                    label = {
                        Text("Contraseña")
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = errorContrasena != null,
                    supportingText = {
                        errorContrasena?.let {
                            Text(it)
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = confirmarContrasena,
                    onValueChange = {
                        confirmarContrasena = it
                        errorConfirmacion = null
                        errorGeneral = null
                    },
                    label = {
                        Text("Confirmar contraseña")
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = errorConfirmacion != null,
                    supportingText = {
                        errorConfirmacion?.let {
                            Text(it)
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                if (errorGeneral != null) {

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = errorGeneral!!,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Button(
                    onClick = {

                        errorNombre = null
                        errorCorreo = null
                        errorContrasena = null
                        errorConfirmacion = null
                        errorGeneral = null

                        var formularioValido = true

                        if (nombre.isBlank()) {

                            errorNombre =
                                "El nombre es obligatorio."

                            formularioValido = false

                        } else if (nombre.trim().length < 3) {

                            errorNombre =
                                "El nombre debe tener al menos 3 caracteres."

                            formularioValido = false
                        }

                        if (correo.isBlank()) {

                            errorCorreo =
                                "El correo es obligatorio."

                            formularioValido = false

                        } else if (
                            !Patterns.EMAIL_ADDRESS
                                .matcher(correo)
                                .matches()
                        ) {

                            errorCorreo =
                                "Ingrese un correo válido."

                            formularioValido = false
                        }

                        if (contrasena.isBlank()) {

                            errorContrasena =
                                "La contraseña es obligatoria."

                            formularioValido = false

                        } else if (contrasena.length < 6) {

                            errorContrasena =
                                "Debe tener al menos 6 caracteres."

                            formularioValido = false
                        }

                        if (confirmarContrasena.isBlank()) {

                            errorConfirmacion =
                                "Confirme su contraseña."

                            formularioValido = false

                        } else if (
                            confirmarContrasena != contrasena
                        ) {

                            errorConfirmacion =
                                "Las contraseñas no coinciden."

                            formularioValido = false
                        }

                        if (formularioValido) {

                            val resultado =
                                AuthRepository.registrar(
                                    nombre = nombre,
                                    correo = correo,
                                    contrasena = contrasena
                                )

                            if (resultado == null) {

                                onRegisterSuccess()

                            } else {

                                errorGeneral = resultado
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorPrincipal
                    )
                ) {

                    Text(
                        text = "CREAR CUENTA",
                        modifier = Modifier.padding(6.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                TextButton(
                    onClick = onBackToLogin,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        text =
                            "¿Ya tienes una cuenta? Inicia sesión"
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )
    }
}
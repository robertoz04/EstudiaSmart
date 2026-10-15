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
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {

    var correo by remember {
        mutableStateOf("")
    }

    var contrasena by remember {
        mutableStateOf("")
    }

    var errorCorreo by remember {
        mutableStateOf<String?>(null)
    }

    var errorContrasena by remember {
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
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "EstudiaSmart",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            color = colorPrincipal
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Aprende a tu propio ritmo",
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(
            modifier = Modifier.height(32.dp)
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
                    text = "Iniciar sesión",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "Ingresa tus datos para continuar.",
                    color = Color.Gray
                )

                Spacer(
                    modifier = Modifier.height(24.dp)
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

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                OutlinedTextField(
                    value = contrasena,
                    onValueChange = {
                        contrasena = it
                        errorContrasena = null
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
                    modifier = Modifier.height(24.dp)
                )

                Button(
                    onClick = {

                        errorCorreo = null
                        errorContrasena = null
                        errorGeneral = null

                        var formularioValido = true

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
                        }

                        if (formularioValido) {

                            val resultado =
                                AuthRepository.iniciarSesion(
                                    correo = correo,
                                    contrasena = contrasena
                                )

                            if (resultado == null) {

                                onLoginSuccess()

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
                        text = "INICIAR SESIÓN",
                        modifier = Modifier.padding(6.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                TextButton(
                    onClick = onRegisterClick,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        text =
                            "¿No tienes una cuenta? Regístrate"
                    )
                }
            }
        }
    }
}
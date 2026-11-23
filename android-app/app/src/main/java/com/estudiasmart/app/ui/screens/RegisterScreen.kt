package com.estudiasmart.app.ui.screens

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.estudiasmart.app.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {

    val colorPrincipal =
        Color(0xFF4F46E5)

    val fondo =
        Color(0xFFF6F7FB)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(fondo)
            .verticalScroll(
                rememberScrollState()
            )
            .padding(24.dp),
        verticalArrangement =
            Arrangement.Center,
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        Text(
            text = "EstudiaSmart",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = colorPrincipal
        )

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )

        Text(
            text =
                "Crea tu cuenta y comienza a estudiar",
            fontSize = 15.sp,
            color = Color.Gray,
            textAlign =
                TextAlign.Center
        )

        Spacer(
            modifier =
                Modifier.height(24.dp)
        )

        Card(
            modifier =
                Modifier.fillMaxWidth(),
            shape =
                RoundedCornerShape(24.dp),
            colors =
                CardDefaults.cardColors(
                    containerColor =
                        Color.White
                ),
            elevation =
                CardDefaults.cardElevation(
                    defaultElevation =
                        4.dp
                )
        ) {

            Column(
                modifier =
                    Modifier.padding(24.dp)
            ) {

                Text(
                    text =
                        "Crear cuenta",
                    style =
                        MaterialTheme
                            .typography
                            .headlineSmall,
                    fontWeight =
                        FontWeight.Bold
                )

                Spacer(
                    modifier =
                        Modifier.height(6.dp)
                )

                Text(
                    text =
                        "Completa tus datos para continuar.",
                    color = Color.Gray
                )

                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )

                // =============================================
                // NOMBRE
                // =============================================

                OutlinedTextField(
                    value =
                        authViewModel
                            .nombreRegistro,
                    onValueChange = {

                        authViewModel
                            .cambiarNombreRegistro(
                                it
                            )
                    },
                    label = {
                        Text(
                            "Nombre completo"
                        )
                    },
                    placeholder = {
                        Text(
                            "Ej. Beatriz Pérez"
                        )
                    },
                    enabled =
                        !authViewModel
                            .cargandoRegistro,
                    isError =
                        authViewModel
                            .errorNombreRegistro !=
                                null,
                    supportingText = {

                        authViewModel
                            .errorNombreRegistro
                            ?.let {

                                Text(it)
                            }
                    },
                    singleLine = true,
                    modifier =
                        Modifier.fillMaxWidth()
                )

                // =============================================
                // CORREO
                // =============================================

                OutlinedTextField(
                    value =
                        authViewModel
                            .correoRegistro,
                    onValueChange = {

                        authViewModel
                            .cambiarCorreoRegistro(
                                it
                            )
                    },
                    label = {
                        Text(
                            "Correo electrónico"
                        )
                    },
                    placeholder = {
                        Text(
                            "correo@ejemplo.com"
                        )
                    },
                    enabled =
                        !authViewModel
                            .cargandoRegistro,
                    isError =
                        authViewModel
                            .errorCorreoRegistro !=
                                null,
                    supportingText = {

                        authViewModel
                            .errorCorreoRegistro
                            ?.let {

                                Text(it)
                            }
                    },
                    singleLine = true,
                    modifier =
                        Modifier.fillMaxWidth()
                )

                // =============================================
                // CONTRASEÑA
                // =============================================

                OutlinedTextField(
                    value =
                        authViewModel
                            .contrasenaRegistro,
                    onValueChange = {

                        authViewModel
                            .cambiarContrasenaRegistro(
                                it
                            )
                    },
                    label = {
                        Text(
                            "Contraseña"
                        )
                    },
                    visualTransformation =
                        PasswordVisualTransformation(),
                    enabled =
                        !authViewModel
                            .cargandoRegistro,
                    isError =
                        authViewModel
                            .errorContrasenaRegistro !=
                                null,
                    supportingText = {

                        authViewModel
                            .errorContrasenaRegistro
                            ?.let {

                                Text(it)
                            }
                    },
                    singleLine = true,
                    modifier =
                        Modifier.fillMaxWidth()
                )

                // =============================================
                // CONFIRMAR CONTRASEÑA
                // =============================================

                OutlinedTextField(
                    value =
                        authViewModel
                            .confirmarContrasenaRegistro,
                    onValueChange = {

                        authViewModel
                            .cambiarConfirmacionRegistro(
                                it
                            )
                    },
                    label = {
                        Text(
                            "Confirmar contraseña"
                        )
                    },
                    visualTransformation =
                        PasswordVisualTransformation(),
                    enabled =
                        !authViewModel
                            .cargandoRegistro,
                    isError =
                        authViewModel
                            .errorConfirmacionRegistro !=
                                null,
                    supportingText = {

                        authViewModel
                            .errorConfirmacionRegistro
                            ?.let {

                                Text(it)
                            }
                    },
                    singleLine = true,
                    modifier =
                        Modifier.fillMaxWidth()
                )

                // =============================================
                // ERROR GENERAL
                // =============================================

                authViewModel
                    .errorRegistroGeneral
                    ?.let {

                        Spacer(
                            modifier =
                                Modifier.height(
                                    8.dp
                                )
                        )

                        Text(
                            text = it,
                            color =
                                MaterialTheme
                                    .colorScheme
                                    .error,
                            modifier =
                                Modifier
                                    .fillMaxWidth(),
                            textAlign =
                                TextAlign.Center
                        )
                    }

                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )

                // =============================================
                // CREAR CUENTA
                // =============================================

                Button(
                    onClick = {

                        authViewModel
                            .registrar(
                                onSuccess =
                                    onRegisterSuccess
                            )
                    },
                    enabled =
                        !authViewModel
                            .cargandoRegistro,
                    modifier =
                        Modifier.fillMaxWidth(),
                    shape =
                        RoundedCornerShape(
                            12.dp
                        ),
                    colors =
                        ButtonDefaults
                            .buttonColors(
                                containerColor =
                                    colorPrincipal
                            )
                ) {

                    if (
                        authViewModel
                            .cargandoRegistro
                    ) {

                        CircularProgressIndicator(
                            modifier =
                                Modifier
                                    .height(22.dp)
                                    .padding(
                                        end =
                                            6.dp
                                    ),
                            strokeWidth =
                                2.dp,
                            color =
                                Color.White
                        )

                        Text(
                            text =
                                "CREANDO CUENTA..."
                        )

                    } else {

                        Text(
                            text =
                                "CREAR CUENTA",
                            modifier =
                                Modifier.padding(
                                    6.dp
                                )
                        )
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                TextButton(
                    onClick =
                        onBackToLogin,
                    enabled =
                        !authViewModel
                            .cargandoRegistro,
                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    Text(
                        text =
                            "¿Ya tienes una cuenta? Inicia sesión"
                    )
                }
            }
        }

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )
    }
}
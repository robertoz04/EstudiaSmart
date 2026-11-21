package com.estudiasmart.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.estudiasmart.app.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {

    val context =
        LocalContext.current

    val colorPrincipal =
        Color(0xFF4F46E5)

    val colorTextoSecundario =
        Color(0xFF6B7280)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(
                horizontal = 24.dp,
                vertical = 32.dp
            ),
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        Text(
            text = "EstudiaSmart",
            fontSize = 38.sp,
            fontWeight = FontWeight.Bold,
            color = colorPrincipal
        )

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )

        Text(
            text =
                "Organiza tu estudio y alcanza tus metas",
            fontSize = 16.sp,
            color = colorTextoSecundario
        )

        Spacer(
            modifier =
                Modifier.height(32.dp)
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
                    defaultElevation = 4.dp
                )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Text(
                    text =
                        "Iniciar sesión",
                    fontSize = 26.sp,
                    fontWeight =
                        FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                Text(
                    text =
                        "Ingresa tus datos para continuar",
                    fontSize = 14.sp,
                    color =
                        colorTextoSecundario
                )

                Spacer(
                    modifier =
                        Modifier.height(24.dp)
                )

                // =============================================
                // CORREO
                // =============================================

                OutlinedTextField(
                    value =
                        authViewModel
                            .correoLogin,
                    onValueChange = {
                        authViewModel
                            .cambiarCorreoLogin(it)
                    },
                    label = {
                        Text(
                            "Correo electrónico"
                        )
                    },
                    enabled =
                        !authViewModel
                            .cargandoLogin,
                    singleLine = true,
                    isError =
                        authViewModel
                            .errorCorreoLogin !=
                                null,
                    supportingText = {

                        authViewModel
                            .errorCorreoLogin
                            ?.let {

                                Text(
                                    text = it,
                                    color =
                                        MaterialTheme
                                            .colorScheme
                                            .error
                                )
                            }
                    },
                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType =
                                KeyboardType.Email,
                            imeAction =
                                ImeAction.Next
                        ),
                    modifier =
                        Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )

                // =============================================
                // CONTRASEÑA
                // =============================================

                OutlinedTextField(
                    value =
                        authViewModel
                            .contrasenaLogin,
                    onValueChange = {
                        authViewModel
                            .cambiarContrasenaLogin(
                                it
                            )
                    },
                    label = {
                        Text(
                            "Contraseña"
                        )
                    },
                    enabled =
                        !authViewModel
                            .cargandoLogin,
                    singleLine = true,
                    visualTransformation =
                        PasswordVisualTransformation(),
                    isError =
                        authViewModel
                            .errorContrasenaLogin !=
                                null,
                    supportingText = {

                        authViewModel
                            .errorContrasenaLogin
                            ?.let {

                                Text(
                                    text = it,
                                    color =
                                        MaterialTheme
                                            .colorScheme
                                            .error
                                )
                            }
                    },
                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType =
                                KeyboardType.Password,
                            imeAction =
                                ImeAction.Done
                        ),
                    modifier =
                        Modifier.fillMaxWidth()
                )

                // =============================================
                // ERROR GENERAL
                // =============================================

                authViewModel
                    .errorLoginGeneral
                    ?.let {

                        Spacer(
                            modifier =
                                Modifier
                                    .height(8.dp)
                        )

                        Text(
                            text = it,
                            color =
                                MaterialTheme
                                    .colorScheme
                                    .error,
                            fontSize = 14.sp
                        )
                    }

                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )

                // =============================================
                // LOGIN CORREO
                // =============================================

                Button(
                    onClick = {

                        authViewModel
                            .iniciarSesion(
                                onSuccess =
                                    onLoginSuccess
                            )
                    },
                    enabled =
                        !authViewModel
                            .cargandoLogin,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape =
                        RoundedCornerShape(
                            14.dp
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
                            .cargandoLogin
                    ) {

                        CircularProgressIndicator(
                            modifier =
                                Modifier
                                    .height(24.dp),
                            color =
                                Color.White,
                            strokeWidth =
                                2.dp
                        )

                    } else {

                        Text(
                            text =
                                "INICIAR SESIÓN",
                            fontWeight =
                                FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.Center,
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(
                        text = "o",
                        color =
                            colorTextoSecundario,
                        fontSize = 14.sp
                    )
                }

                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )

                // =============================================
                // GOOGLE
                // =============================================

                OutlinedButton(
                    onClick = {

                        authViewModel
                            .iniciarSesionConGoogle(
                                context = context,
                                onSuccess =
                                    onLoginSuccess
                            )
                    },
                    enabled =
                        !authViewModel
                            .cargandoLogin,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape =
                        RoundedCornerShape(
                            14.dp
                        ),
                    colors =
                        ButtonDefaults
                            .outlinedButtonColors(
                                contentColor =
                                    Color.Black
                            )
                ) {

                    Text(
                        text =
                            "G   Continuar con Google",
                        fontWeight =
                            FontWeight.SemiBold,
                        fontSize = 15.sp
                    )
                }

                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically,
                    horizontalArrangement =
                        Arrangement.Center
                ) {

                    Text(
                        text =
                            "¿No tienes una cuenta?",
                        color =
                            colorTextoSecundario,
                        fontSize = 14.sp
                    )

                    TextButton(
                        onClick =
                            onRegisterClick,
                        enabled =
                            !authViewModel
                                .cargandoLogin
                    ) {

                        Text(
                            text =
                                "Regístrate",
                            color =
                                colorPrincipal,
                            fontWeight =
                                FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(
            modifier =
                Modifier.height(24.dp)
        )
    }
}
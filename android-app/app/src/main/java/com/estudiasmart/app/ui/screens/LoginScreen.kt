package com.estudiasmart.app.ui.screens

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.estudiasmart.app.data.AuthRepository
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {

    // =========================================================
    // ESTADOS
    // =========================================================

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

    var cargando by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // =========================================================
    // COLORES
    // =========================================================

    val colorPrincipal = Color(0xFF4F46E5)
    val colorFondo = Color(0xFFF6F7FB)
    val colorTextoSecundario = Color(0xFF6B7280)

    // =========================================================
    // CONTENIDO
    // =========================================================

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
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // =====================================================
        // TÍTULO
        // =====================================================

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "EstudiaSmart",
            fontSize = 38.sp,
            fontWeight = FontWeight.Bold,
            color = colorPrincipal
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Organiza tu estudio y alcanza tus metas",
            fontSize = 16.sp,
            color = colorTextoSecundario
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        // =====================================================
        // TARJETA DE LOGIN
        // =====================================================

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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Iniciar sesión",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "Ingresa tus datos para continuar",
                    fontSize = 14.sp,
                    color = colorTextoSecundario
                )

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                // =============================================
                // CORREO
                // =============================================

                OutlinedTextField(
                    value = correo,
                    onValueChange = {
                        correo = it
                        errorCorreo = null
                        errorGeneral = null
                    },
                    label = {
                        Text(
                            "Correo electrónico"
                        )
                    },
                    enabled = !cargando,
                    singleLine = true,
                    isError = errorCorreo != null,
                    supportingText = {
                        if (errorCorreo != null) {
                            Text(
                                text = errorCorreo!!,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                // =============================================
                // CONTRASEÑA
                // =============================================

                OutlinedTextField(
                    value = contrasena,
                    onValueChange = {
                        contrasena = it
                        errorContrasena = null
                        errorGeneral = null
                    },
                    label = {
                        Text(
                            "Contraseña"
                        )
                    },
                    enabled = !cargando,
                    singleLine = true,
                    visualTransformation =
                        PasswordVisualTransformation(),
                    isError = errorContrasena != null,
                    supportingText = {
                        if (errorContrasena != null) {
                            Text(
                                text = errorContrasena!!,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // =============================================
                // ERROR GENERAL
                // =============================================

                if (errorGeneral != null) {

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = errorGeneral!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp
                    )
                }

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                // =============================================
                // BOTÓN LOGIN CORREO
                // =============================================

                Button(
                    onClick = {

                        errorCorreo = null
                        errorContrasena = null
                        errorGeneral = null

                        val correoLimpio =
                            correo.trim()

                        var formularioValido = true

                        if (correoLimpio.isBlank()) {

                            errorCorreo =
                                "Ingresa tu correo electrónico."

                            formularioValido = false

                        } else if (
                            !Patterns.EMAIL_ADDRESS
                                .matcher(correoLimpio)
                                .matches()
                        ) {

                            errorCorreo =
                                "Ingresa un correo electrónico válido."

                            formularioValido = false
                        }

                        if (contrasena.isBlank()) {

                            errorContrasena =
                                "Ingresa tu contraseña."

                            formularioValido = false
                        }

                        if (formularioValido) {

                            cargando = true

                            AuthRepository.iniciarSesion(
                                correo = correoLimpio,
                                contrasena = contrasena
                            ) { resultado ->

                                cargando = false

                                if (resultado == null) {

                                    onLoginSuccess()

                                } else {

                                    errorGeneral =
                                        resultado
                                }
                            }
                        }
                    },
                    enabled = !cargando,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorPrincipal
                    )
                ) {

                    if (cargando) {

                        CircularProgressIndicator(
                            modifier = Modifier.height(
                                24.dp
                            ),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )

                    } else {

                        Text(
                            text = "INICIAR SESIÓN",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }

                // =============================================
                // SEPARADOR
                // =============================================

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.Center,
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(
                        text = "o",
                        color = colorTextoSecundario,
                        fontSize = 14.sp
                    )
                }

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                // =============================================
                // BOTÓN GOOGLE
                // =============================================

                OutlinedButton(
                    onClick = {

                        errorGeneral = null
                        errorCorreo = null
                        errorContrasena = null

                        cargando = true

                        scope.launch {

                            val resultado =
                                AuthRepository
                                    .iniciarSesionConGoogle(
                                        context
                                    )

                            cargando = false

                            if (resultado == null) {

                                onLoginSuccess()

                            } else {

                                errorGeneral =
                                    resultado
                            }
                        }
                    },
                    enabled = !cargando,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors =
                        ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.Black
                        )
                ) {

                    Text(
                        text = "G   Continuar con Google",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )
                }

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                // =============================================
                // REGISTRO
                // =============================================

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically,
                    horizontalArrangement =
                        Arrangement.Center
                ) {

                    Text(
                        text = "¿No tienes una cuenta?",
                        color = colorTextoSecundario,
                        fontSize = 14.sp
                    )

                    TextButton(
                        onClick = onRegisterClick,
                        enabled = !cargando
                    ) {

                        Text(
                            text = "Regístrate",
                            color = colorPrincipal,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )
    }
}
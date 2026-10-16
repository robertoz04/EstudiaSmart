package com.estudiasmart.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.estudiasmart.app.data.AcademicRepository
import com.estudiasmart.app.data.FlashcardRepository
import com.estudiasmart.app.model.Dificultad
import com.estudiasmart.app.model.Flashcard
import com.estudiasmart.app.ui.components.ConfirmDeleteDialog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun FlashcardsScreen(
    onBack: () -> Unit
) {

    val temas =
        AcademicRepository.temas

    if (temas.isEmpty()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color(0xFFF6F7FB)
                )
                .padding(24.dp)
        ) {

            TextButton(
                onClick = onBack
            ) {
                Text("← Volver")
            }

            Text(
                text = "Flashcards",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4F46E5)
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text =
                    "Primero debes crear una materia y al menos un tema para poder agregar flashcards.",
                color = Color.Gray
            )
        }

        return
    }

    var pregunta by remember {
        mutableStateOf("")
    }

    var respuesta by remember {
        mutableStateOf("")
    }

    var temaSeleccionadoId by remember {
        mutableStateOf<Int?>(
            temas.firstOrNull()?.id
        )
    }

    var menuTemasAbierto by remember {
        mutableStateOf(false)
    }

    var error by remember {
        mutableStateOf<String?>(null)
    }

    var flashcardEditando by remember {
        mutableStateOf<Flashcard?>(null)
    }

    var flashcardAEliminar by remember {
        mutableStateOf<Flashcard?>(null)
    }

    val colorPrincipal =
        Color(0xFF4F46E5)

    val fondo =
        Color(0xFFF6F7FB)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(fondo)
            .padding(
                horizontal = 20.dp
            ),
        verticalArrangement =
            Arrangement.spacedBy(12.dp)
    ) {

        item {

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            TextButton(
                onClick = onBack
            ) {
                Text("← Volver al inicio")
            }

            Text(
                text = "Flashcards",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = colorPrincipal
            )

            Text(
                text =
                    "Repasa tus conceptos con repetición espaciada.",
                color = Color.Gray
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Card(
                modifier =
                    Modifier.fillMaxWidth(),
                shape =
                    RoundedCornerShape(20.dp),
                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            Color.White
                    ),
                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation = 3.dp
                    )
            ) {

                Column(
                    modifier =
                        Modifier.padding(20.dp)
                ) {

                    Text(
                        text =
                            if (
                                flashcardEditando == null
                            )
                                "Nueva flashcard"
                            else
                                "Editar flashcard",
                        fontSize = 20.sp,
                        fontWeight =
                            FontWeight.Bold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(14.dp)
                    )

                    Text(
                        text = "Tema",
                        fontWeight =
                            FontWeight.Medium
                    )

                    Spacer(
                        modifier =
                            Modifier.height(6.dp)
                    )

                    Box(
                        modifier =
                            Modifier.fillMaxWidth()
                    ) {

                        OutlinedButton(
                            onClick = {
                                menuTemasAbierto =
                                    true
                            },
                            modifier =
                                Modifier.fillMaxWidth()
                        ) {

                            val tema =
                                temas.find {
                                    it.id ==
                                            temaSeleccionadoId
                                }

                            Text(
                                text =
                                    tema?.nombre
                                        ?: "Seleccionar tema"
                            )
                        }

                        DropdownMenu(
                            expanded =
                                menuTemasAbierto,
                            onDismissRequest = {
                                menuTemasAbierto =
                                    false
                            }
                        ) {

                            temas.forEach { tema ->

                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            tema.nombre
                                        )
                                    },
                                    onClick = {

                                        temaSeleccionadoId =
                                            tema.id

                                        menuTemasAbierto =
                                            false

                                        error = null
                                    }
                                )
                            }
                        }
                    }

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )

                    OutlinedTextField(
                        value = pregunta,
                        onValueChange = {
                            pregunta = it
                            error = null
                        },
                        label = {
                            Text("Pregunta")
                        },
                        placeholder = {
                            Text(
                                "Ej. ¿Qué es Jetpack Compose?"
                            )
                        },
                        modifier =
                            Modifier.fillMaxWidth()
                    )

                    Spacer(
                        modifier =
                            Modifier.height(10.dp)
                    )

                    OutlinedTextField(
                        value = respuesta,
                        onValueChange = {
                            respuesta = it
                            error = null
                        },
                        label = {
                            Text("Respuesta")
                        },
                        placeholder = {
                            Text(
                                "Escribe la respuesta..."
                            )
                        },
                        minLines = 2,
                        maxLines = 5,
                        modifier =
                            Modifier.fillMaxWidth()
                    )

                    if (error != null) {

                        Spacer(
                            modifier =
                                Modifier.height(8.dp)
                        )

                        Text(
                            text = error!!,
                            color =
                                Color(0xFFB3261E)
                        )
                    }

                    Spacer(
                        modifier =
                            Modifier.height(16.dp)
                    )

                    Button(
                        onClick = {

                            val temaId =
                                temaSeleccionadoId

                            if (temaId == null) {

                                error =
                                    "Selecciona un tema."

                                return@Button
                            }

                            if (
                                flashcardEditando == null
                            ) {

                                val resultado =
                                    FlashcardRepository
                                        .crearFlashcard(
                                            temaId =
                                                temaId,
                                            pregunta =
                                                pregunta,
                                            respuesta =
                                                respuesta
                                        )

                                if (
                                    resultado == null
                                ) {

                                    pregunta = ""
                                    respuesta = ""
                                    error = null

                                } else {

                                    error =
                                        resultado
                                }

                            } else {

                                val resultado =
                                    FlashcardRepository
                                        .actualizarFlashcard(
                                            id =
                                                flashcardEditando!!
                                                    .id,
                                            temaId =
                                                temaId,
                                            pregunta =
                                                pregunta,
                                            respuesta =
                                                respuesta
                                        )

                                if (
                                    resultado == null
                                ) {

                                    pregunta = ""
                                    respuesta = ""
                                    flashcardEditando =
                                        null
                                    error = null

                                } else {

                                    error =
                                        resultado
                                }
                            }
                        },
                        modifier =
                            Modifier.fillMaxWidth(),
                        colors =
                            ButtonDefaults
                                .buttonColors(
                                    containerColor =
                                        colorPrincipal
                                )
                    ) {

                        Text(
                            if (
                                flashcardEditando == null
                            )
                                "AGREGAR FLASHCARD"
                            else
                                "GUARDAR CAMBIOS"
                        )
                    }

                    if (
                        flashcardEditando != null
                    ) {

                        TextButton(
                            onClick = {

                                flashcardEditando =
                                    null

                                pregunta = ""
                                respuesta = ""
                                error = null
                            },
                            modifier =
                                Modifier.fillMaxWidth()
                        ) {

                            Text(
                                "Cancelar edición"
                            )
                        }
                    }
                }
            }
        }

        item {

            Spacer(
                modifier =
                    Modifier.height(6.dp)
            )

            Text(
                text =
                    "Flashcards registradas",
                fontSize = 20.sp,
                fontWeight =
                    FontWeight.Bold
            )

            Text(
                text =
                    "${FlashcardRepository.flashcards.size} flashcards en total",
                color = Color.Gray
            )
        }

        if (
            FlashcardRepository
                .flashcards
                .isEmpty()
        ) {

            item {

                Text(
                    text =
                        "Todavía no tienes flashcards.",
                    color = Color.Gray
                )
            }

        } else {

            items(
                items =
                    FlashcardRepository
                        .flashcards,
                key = {
                    it.id
                }
            ) { flashcard ->

                FlashcardCard(
                    flashcard =
                        flashcard,
                    onEditar = {

                        flashcardEditando =
                            flashcard

                        temaSeleccionadoId =
                            flashcard.temaId

                        pregunta =
                            flashcard.pregunta

                        respuesta =
                            flashcard.respuesta

                        error = null
                    },
                    onEliminar = {

                        flashcardAEliminar =
                            flashcard
                    }
                )
            }
        }

        item {

            Spacer(
                modifier =
                    Modifier.height(30.dp)
            )
        }
    }

    if (flashcardAEliminar != null) {

        ConfirmDeleteDialog(
            titulo =
                "Eliminar flashcard",
            mensaje =
                "¿Seguro que deseas eliminar esta flashcard?\n\n\"${flashcardAEliminar!!.pregunta}\"",
            onConfirm = {

                val id =
                    flashcardAEliminar!!.id

                FlashcardRepository
                    .eliminarFlashcard(
                        id
                    )

                if (
                    flashcardEditando?.id ==
                    id
                ) {

                    flashcardEditando =
                        null

                    pregunta = ""
                    respuesta = ""
                    error = null
                }

                flashcardAEliminar =
                    null
            },
            onDismiss = {

                flashcardAEliminar =
                    null
            }
        )
    }
}

@Composable
private fun FlashcardCard(
    flashcard: Flashcard,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {

    val tema =
        AcademicRepository
            .obtenerTema(
                flashcard.temaId
            )

    val formatoFecha =
        SimpleDateFormat(
            "dd/MM/yyyy",
            Locale.getDefault()
        )

    val fecha =
        formatoFecha.format(
            Date(
                flashcard
                    .proximaRevisionMillis
            )
        )

    Card(
        modifier =
            Modifier.fillMaxWidth(),
        shape =
            RoundedCornerShape(18.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            )
    ) {

        Column(
            modifier =
                Modifier.padding(16.dp)
        ) {

            Text(
                text =
                    tema?.nombre
                        ?: "Tema",
                color =
                    Color(0xFF4F46E5),
                fontWeight =
                    FontWeight.Medium
            )

            Spacer(
                modifier =
                    Modifier.height(6.dp)
            )

            Text(
                text =
                    flashcard.pregunta,
                fontSize = 18.sp,
                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(6.dp)
            )

            Text(
                text =
                    flashcard.respuesta,
                color = Color.Gray
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            Text(
                text =
                    "Dificultad: ${flashcard.dificultad.etiqueta}"
            )

            Text(
                text =
                    "Racha: ${flashcard.racha}"
            )

            Text(
                text =
                    "Revisiones: ${flashcard.revisiones}"
            )

            Text(
                text =
                    "Próximo repaso: $fecha",
                color = Color.Gray
            )

            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )

            Text(
                text =
                    "¿Cómo te fue?",
                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(6.dp)
            ) {

                OutlinedButton(
                    onClick = {

                        FlashcardRepository
                            .registrarRepaso(
                                flashcard.id,
                                Dificultad.DIFICIL
                            )
                    },
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text("Difícil")
                }

                OutlinedButton(
                    onClick = {

                        FlashcardRepository
                            .registrarRepaso(
                                flashcard.id,
                                Dificultad.REGULAR
                            )
                    },
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text("Regular")
                }

                OutlinedButton(
                    onClick = {

                        FlashcardRepository
                            .registrarRepaso(
                                flashcard.id,
                                Dificultad.FACIL
                            )
                    },
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text("Fácil")
                }
            }

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                OutlinedButton(
                    onClick =
                        onEditar,
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text("Editar")
                }

                OutlinedButton(
                    onClick =
                        onEliminar,
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text("Eliminar")
                }
            }
        }
    }
}
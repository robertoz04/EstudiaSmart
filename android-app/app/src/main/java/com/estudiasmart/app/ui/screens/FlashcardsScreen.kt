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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.estudiasmart.app.model.Dificultad
import com.estudiasmart.app.model.Flashcard
import com.estudiasmart.app.ui.components.ConfirmDeleteDialog
import com.estudiasmart.app.viewmodel.FlashcardViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun FlashcardsScreen(
    onBack: () -> Unit,
    flashcardViewModel:
    FlashcardViewModel =
        viewModel()
) {

    val temas =
        flashcardViewModel.temas

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
                fontWeight =
                    FontWeight.Bold,
                color =
                    Color(0xFF4F46E5)
            )

            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )

            Text(
                text =
                    "Primero debes crear una materia y al menos un tema para poder agregar flashcards.",
                color =
                    Color.Gray
            )
        }

        return
    }

    var menuTemasAbierto by remember {
        mutableStateOf(false)
    }

    val colorPrincipal =
        Color(0xFF4F46E5)

    val fondo =
        Color(0xFFF6F7FB)

    val flashcards =
        flashcardViewModel.flashcards

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(fondo)
            .padding(
                horizontal = 20.dp
            ),
        verticalArrangement =
            Arrangement.spacedBy(
                12.dp
            )
    ) {

        item {

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            TextButton(
                onClick = onBack
            ) {
                Text(
                    "← Volver al inicio"
                )
            }

            Text(
                text = "Flashcards",
                fontSize = 30.sp,
                fontWeight =
                    FontWeight.Bold,
                color =
                    colorPrincipal
            )

            Text(
                text =
                    "Repasa tus conceptos con repetición espaciada.",
                color =
                    Color.Gray
            )

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            Card(
                modifier =
                    Modifier.fillMaxWidth(),
                shape =
                    RoundedCornerShape(
                        20.dp
                    ),
                colors =
                    CardDefaults
                        .cardColors(
                            containerColor =
                                Color.White
                        ),
                elevation =
                    CardDefaults
                        .cardElevation(
                            defaultElevation =
                                3.dp
                        )
            ) {

                Column(
                    modifier =
                        Modifier.padding(
                            20.dp
                        )
                ) {

                    Text(
                        text =
                            if (
                                flashcardViewModel
                                    .flashcardEditando ==
                                null
                            )
                                "Nueva flashcard"
                            else
                                "Editar flashcard",
                        fontSize =
                            20.sp,
                        fontWeight =
                            FontWeight.Bold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(
                                14.dp
                            )
                    )

                    Text(
                        text =
                            "Tema",
                        fontWeight =
                            FontWeight.Medium
                    )

                    Spacer(
                        modifier =
                            Modifier.height(
                                6.dp
                            )
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
                                Modifier
                                    .fillMaxWidth()
                        ) {

                            val tema =
                                temas.find {

                                    it.id ==
                                            flashcardViewModel
                                                .temaSeleccionadoId
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

                                        flashcardViewModel
                                            .seleccionarTema(
                                                tema.id
                                            )

                                        menuTemasAbierto =
                                            false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(
                        modifier =
                            Modifier.height(
                                12.dp
                            )
                    )

                    OutlinedTextField(
                        value =
                            flashcardViewModel
                                .pregunta,
                        onValueChange = {

                            flashcardViewModel
                                .cambiarPregunta(
                                    it
                                )
                        },
                        label = {
                            Text(
                                "Pregunta"
                            )
                        },
                        placeholder = {
                            Text(
                                "Ej. ¿Qué es Jetpack Compose?"
                            )
                        },
                        modifier =
                            Modifier
                                .fillMaxWidth()
                    )

                    Spacer(
                        modifier =
                            Modifier.height(
                                10.dp
                            )
                    )

                    OutlinedTextField(
                        value =
                            flashcardViewModel
                                .respuesta,
                        onValueChange = {

                            flashcardViewModel
                                .cambiarRespuesta(
                                    it
                                )
                        },
                        label = {
                            Text(
                                "Respuesta"
                            )
                        },
                        placeholder = {
                            Text(
                                "Escribe la respuesta..."
                            )
                        },
                        minLines = 2,
                        maxLines = 5,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                    )

                    flashcardViewModel
                        .error
                        ?.let {

                            Spacer(
                                modifier =
                                    Modifier
                                        .height(
                                            8.dp
                                        )
                            )

                            Text(
                                text = it,
                                color =
                                    Color(
                                        0xFFB3261E
                                    )
                            )
                        }

                    Spacer(
                        modifier =
                            Modifier.height(
                                16.dp
                            )
                    )

                    Button(
                        onClick = {

                            flashcardViewModel
                                .guardarFlashcard()
                        },
                        modifier =
                            Modifier
                                .fillMaxWidth(),
                        colors =
                            ButtonDefaults
                                .buttonColors(
                                    containerColor =
                                        colorPrincipal
                                )
                    ) {

                        Text(
                            if (
                                flashcardViewModel
                                    .flashcardEditando ==
                                null
                            )
                                "AGREGAR FLASHCARD"
                            else
                                "GUARDAR CAMBIOS"
                        )
                    }

                    if (
                        flashcardViewModel
                            .flashcardEditando !=
                        null
                    ) {

                        TextButton(
                            onClick = {

                                flashcardViewModel
                                    .cancelarEdicion()
                            },
                            modifier =
                                Modifier
                                    .fillMaxWidth()
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
                fontSize =
                    20.sp,
                fontWeight =
                    FontWeight.Bold
            )

            Text(
                text =
                    "${flashcardViewModel.totalFlashcards} flashcards en total",
                color =
                    Color.Gray
            )
        }

        if (
            flashcards.isEmpty()
        ) {

            item {

                Text(
                    text =
                        "Todavía no tienes flashcards.",
                    color =
                        Color.Gray
                )
            }

        } else {

            items(
                items =
                    flashcards,
                key = {
                    it.id
                }
            ) { flashcard ->

                FlashcardCard(
                    flashcard =
                        flashcard,
                    nombreTema =
                        flashcardViewModel
                            .obtenerTema(
                                flashcard.temaId
                            )
                            ?.nombre
                            ?: "Tema",
                    onRepaso = {
                            dificultad ->

                        flashcardViewModel
                            .registrarRepaso(
                                flashcardId =
                                    flashcard.id,
                                dificultad =
                                    dificultad
                            )
                    },
                    onEditar = {

                        flashcardViewModel
                            .iniciarEdicion(
                                flashcard
                            )
                    },
                    onEliminar = {

                        flashcardViewModel
                            .solicitarEliminar(
                                flashcard
                            )
                    }
                )
            }
        }

        item {

            Spacer(
                modifier =
                    Modifier.height(
                        30.dp
                    )
            )
        }
    }

    val flashcardAEliminar =
        flashcardViewModel
            .flashcardAEliminar

    if (
        flashcardAEliminar !=
        null
    ) {

        ConfirmDeleteDialog(
            titulo =
                "Eliminar flashcard",
            mensaje =
                "¿Seguro que deseas eliminar esta flashcard?\n\n\"${flashcardAEliminar.pregunta}\"",
            onConfirm = {

                flashcardViewModel
                    .confirmarEliminar()
            },
            onDismiss = {

                flashcardViewModel
                    .cancelarEliminar()
            }
        )
    }
}

@Composable
private fun FlashcardCard(
    flashcard: Flashcard,
    nombreTema: String,
    onRepaso:
        (Dificultad) -> Unit,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {

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
            RoundedCornerShape(
                18.dp
            ),
        colors =
            CardDefaults
                .cardColors(
                    containerColor =
                        Color.White
                )
    ) {

        Column(
            modifier =
                Modifier.padding(
                    16.dp
                )
        ) {

            Text(
                text =
                    nombreTema,
                color =
                    Color(0xFF4F46E5),
                fontWeight =
                    FontWeight.Medium
            )

            Spacer(
                modifier =
                    Modifier.height(
                        6.dp
                    )
            )

            Text(
                text =
                    flashcard.pregunta,
                fontSize =
                    18.sp,
                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(
                        6.dp
                    )
            )

            Text(
                text =
                    flashcard.respuesta,
                color =
                    Color.Gray
            )

            Spacer(
                modifier =
                    Modifier.height(
                        12.dp
                    )
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
                color =
                    Color.Gray
            )

            Spacer(
                modifier =
                    Modifier.height(
                        14.dp
                    )
            )

            Text(
                text =
                    "¿Cómo te fue?",
                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(
                        8.dp
                    )
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(
                        6.dp
                    )
            ) {

                OutlinedButton(
                    onClick = {

                        onRepaso(
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

                        onRepaso(
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

                        onRepaso(
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
                    Modifier.height(
                        10.dp
                    )
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(
                        8.dp
                    )
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
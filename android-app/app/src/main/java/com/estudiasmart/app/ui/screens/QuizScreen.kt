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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import com.estudiasmart.app.model.Quiz
import com.estudiasmart.app.ui.components.ConfirmDeleteDialog
import com.estudiasmart.app.viewmodel.QuizViewModel
import java.util.Locale

@Composable
fun QuizScreen(
    onBack: () -> Unit,
    quizViewModel: QuizViewModel =
        viewModel()
) {

    val temas =
        quizViewModel.temas

    val quizzes =
        quizViewModel.quizzes

    val quizAdministrando =
        quizViewModel.quizAdministrando

    val quizResolviendo =
        quizViewModel.quizResolviendo

    var menuTemasAbierto by remember {
        mutableStateOf(false)
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
                text = "Quiz",
                fontSize = 30.sp,
                fontWeight =
                    FontWeight.Bold,
                color =
                    colorPrincipal
            )

            Text(
                text =
                    "Evalúa tus conocimientos y revisa tu progreso.",
                color =
                    Color.Gray
            )

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )
        }

        if (temas.isEmpty()) {

            item {

                Card(
                    modifier =
                        Modifier.fillMaxWidth(),
                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                Color.White
                        )
                ) {

                    Column(
                        modifier =
                            Modifier.padding(20.dp)
                    ) {

                        Text(
                            text =
                                "Primero debes crear una materia y al menos un tema.",
                            color =
                                Color.Gray
                        )
                    }
                }
            }

        } else {

            // =================================================
            // CREAR QUIZ
            // =================================================

            item {

                Card(
                    modifier =
                        Modifier.fillMaxWidth(),
                    shape =
                        RoundedCornerShape(
                            20.dp
                        ),
                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                Color.White
                        ),
                    elevation =
                        CardDefaults.cardElevation(
                            defaultElevation =
                                3.dp
                        )
                ) {

                    Column(
                        modifier =
                            Modifier.padding(20.dp)
                    ) {

                        Text(
                            text =
                                "Nuevo Quiz",
                            fontSize = 20.sp,
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
                            text = "Tema",
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
                                                quizViewModel
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

                                            quizViewModel
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
                                quizViewModel
                                    .tituloQuiz,
                            onValueChange = {

                                quizViewModel
                                    .cambiarTituloQuiz(
                                        it
                                    )
                            },
                            label = {

                                Text(
                                    "Título del Quiz"
                                )
                            },
                            placeholder = {

                                Text(
                                    "Ej. Evaluación de Compose"
                                )
                            },
                            singleLine = true,
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                        )

                        quizViewModel
                            .errorQuiz
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
                                            .error
                                )
                            }

                        Spacer(
                            modifier =
                                Modifier.height(
                                    14.dp
                                )
                        )

                        Button(
                            onClick = {

                                quizViewModel
                                    .crearQuiz()
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
                                "CREAR QUIZ"
                            )
                        }
                    }
                }
            }

            // =================================================
            // LISTA QUIZZES
            // =================================================

            item {

                Text(
                    text =
                        "Quizzes registrados",
                    fontSize =
                        20.sp,
                    fontWeight =
                        FontWeight.Bold
                )

                Text(
                    text =
                        "${quizzes.size} quizzes en total",
                    color =
                        Color.Gray
                )
            }

            if (quizzes.isEmpty()) {

                item {

                    Text(
                        text =
                            "Todavía no tienes quizzes.",
                        color =
                            Color.Gray
                    )
                }

            } else {

                items(
                    items = quizzes,
                    key = {
                        it.id
                    }
                ) { quiz ->

                    QuizCard(
                        quiz = quiz,
                        nombreTema =
                            quizViewModel
                                .nombreTema(
                                    quiz.temaId
                                ),
                        mejorNota =
                            quizViewModel
                                .mejorNota(
                                    quiz.id
                                ),
                        onAdministrar = {

                            quizViewModel
                                .administrarQuiz(
                                    quiz
                                )
                        },
                        onResolver = {

                            quizViewModel
                                .resolverQuiz(
                                    quiz
                                )
                        },
                        onEliminar = {

                            quizViewModel
                                .solicitarEliminarQuiz(
                                    quiz
                                )
                        }
                    )
                }
            }

            // =================================================
            // ADMINISTRAR PREGUNTAS
            // =================================================

            if (
                quizAdministrando !=
                null
            ) {

                item {

                    Card(
                        modifier =
                            Modifier.fillMaxWidth(),
                        shape =
                            RoundedCornerShape(
                                20.dp
                            ),
                        colors =
                            CardDefaults.cardColors(
                                containerColor =
                                    Color.White
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
                                    "Administrar Quiz",
                                fontSize =
                                    20.sp,
                                fontWeight =
                                    FontWeight.Bold
                            )

                            Text(
                                text =
                                    quizAdministrando
                                        .titulo,
                                color =
                                    colorPrincipal
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(
                                        14.dp
                                    )
                            )

                            OutlinedTextField(
                                value =
                                    quizViewModel
                                        .enunciado,
                                onValueChange = {

                                    quizViewModel
                                        .cambiarEnunciado(
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
                                        8.dp
                                    )
                            )

                            OutlinedTextField(
                                value =
                                    quizViewModel
                                        .opcion1,
                                onValueChange = {

                                    quizViewModel
                                        .cambiarOpcion1(
                                            it
                                        )
                                },
                                label = {
                                    Text(
                                        "Opción A"
                                    )
                                },
                                singleLine = true,
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                            )

                            OutlinedTextField(
                                value =
                                    quizViewModel
                                        .opcion2,
                                onValueChange = {

                                    quizViewModel
                                        .cambiarOpcion2(
                                            it
                                        )
                                },
                                label = {
                                    Text(
                                        "Opción B"
                                    )
                                },
                                singleLine = true,
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                            )

                            OutlinedTextField(
                                value =
                                    quizViewModel
                                        .opcion3,
                                onValueChange = {

                                    quizViewModel
                                        .cambiarOpcion3(
                                            it
                                        )
                                },
                                label = {
                                    Text(
                                        "Opción C"
                                    )
                                },
                                singleLine = true,
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                            )

                            OutlinedTextField(
                                value =
                                    quizViewModel
                                        .opcion4,
                                onValueChange = {

                                    quizViewModel
                                        .cambiarOpcion4(
                                            it
                                        )
                                },
                                label = {
                                    Text(
                                        "Opción D"
                                    )
                                },
                                singleLine = true,
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(
                                        12.dp
                                    )
                            )

                            Text(
                                text =
                                    "Respuesta correcta",
                                fontWeight =
                                    FontWeight.Bold
                            )

                            OpcionCorrecta(
                                texto = "A",
                                indice = 0,
                                seleccion =
                                    quizViewModel
                                        .respuestaCorrecta,
                                onSeleccionar = {

                                    quizViewModel
                                        .seleccionarRespuestaCorrecta(
                                            0
                                        )
                                }
                            )

                            OpcionCorrecta(
                                texto = "B",
                                indice = 1,
                                seleccion =
                                    quizViewModel
                                        .respuestaCorrecta,
                                onSeleccionar = {

                                    quizViewModel
                                        .seleccionarRespuestaCorrecta(
                                            1
                                        )
                                }
                            )

                            OpcionCorrecta(
                                texto = "C",
                                indice = 2,
                                seleccion =
                                    quizViewModel
                                        .respuestaCorrecta,
                                onSeleccionar = {

                                    quizViewModel
                                        .seleccionarRespuestaCorrecta(
                                            2
                                        )
                                }
                            )

                            OpcionCorrecta(
                                texto = "D",
                                indice = 3,
                                seleccion =
                                    quizViewModel
                                        .respuestaCorrecta,
                                onSeleccionar = {

                                    quizViewModel
                                        .seleccionarRespuestaCorrecta(
                                            3
                                        )
                                }
                            )

                            quizViewModel
                                .errorPregunta
                                ?.let {

                                    Text(
                                        text = it,
                                        color =
                                            MaterialTheme
                                                .colorScheme
                                                .error
                                    )
                                }

                            Spacer(
                                modifier =
                                    Modifier.height(
                                        12.dp
                                    )
                            )

                            Button(
                                onClick = {

                                    quizViewModel
                                        .agregarPregunta()
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
                                    "AGREGAR PREGUNTA"
                                )
                            }

                            Spacer(
                                modifier =
                                    Modifier.height(
                                        18.dp
                                    )
                            )

                            Text(
                                text =
                                    "Preguntas registradas: ${quizAdministrando.preguntas.size}",
                                fontWeight =
                                    FontWeight.Bold
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(
                                        8.dp
                                    )
                            )

                            quizAdministrando
                                .preguntas
                                .forEach { pregunta ->

                                    Card(
                                        modifier =
                                            Modifier
                                                .fillMaxWidth()
                                                .padding(
                                                    vertical =
                                                        4.dp
                                                ),
                                        colors =
                                            CardDefaults
                                                .cardColors(
                                                    containerColor =
                                                        Color(
                                                            0xFFF6F7FB
                                                        )
                                                )
                                    ) {

                                        Column(
                                            modifier =
                                                Modifier
                                                    .padding(
                                                        12.dp
                                                    )
                                        ) {

                                            Text(
                                                text =
                                                    pregunta
                                                        .enunciado,
                                                fontWeight =
                                                    FontWeight.Bold
                                            )

                                            Text(
                                                text =
                                                    "Correcta: ${
                                                        pregunta.opciones[
                                                            pregunta.respuestaCorrecta
                                                        ]
                                                    }",
                                                color =
                                                    Color.Gray
                                            )

                                            TextButton(
                                                onClick = {

                                                    quizViewModel
                                                        .eliminarPregunta(
                                                            quizId =
                                                                quizAdministrando.id,
                                                            preguntaId =
                                                                pregunta.id
                                                        )
                                                }
                                            ) {

                                                Text(
                                                    "Eliminar pregunta"
                                                )
                                            }
                                        }
                                    }
                                }
                        }
                    }
                }
            }

            // =================================================
            // RESOLVER QUIZ
            // =================================================

            if (
                quizResolviendo !=
                null
            ) {

                item {

                    Card(
                        modifier =
                            Modifier.fillMaxWidth(),
                        shape =
                            RoundedCornerShape(
                                20.dp
                            ),
                        colors =
                            CardDefaults.cardColors(
                                containerColor =
                                    Color.White
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
                                    "Resolver Quiz",
                                fontSize =
                                    20.sp,
                                fontWeight =
                                    FontWeight.Bold
                            )

                            Text(
                                text =
                                    quizResolviendo
                                        .titulo,
                                color =
                                    colorPrincipal
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(
                                        16.dp
                                    )
                            )

                            if (
                                quizResolviendo
                                    .preguntas
                                    .isEmpty()
                            ) {

                                Text(
                                    text =
                                        "Este quiz todavía no tiene preguntas.",
                                    color =
                                        Color.Gray
                                )

                            } else {

                                quizResolviendo
                                    .preguntas
                                    .forEachIndexed {
                                            indice,
                                            pregunta ->

                                        Text(
                                            text =
                                                "${indice + 1}. ${pregunta.enunciado}",
                                            fontWeight =
                                                FontWeight.Bold
                                        )

                                        pregunta
                                            .opciones
                                            .forEachIndexed {
                                                    indiceOpcion,
                                                    opcion ->

                                                Row(
                                                    modifier =
                                                        Modifier
                                                            .fillMaxWidth()
                                                ) {

                                                    RadioButton(
                                                        selected =
                                                            quizViewModel
                                                                .respuestasUsuario[
                                                                pregunta.id
                                                            ] ==
                                                                    indiceOpcion,
                                                        onClick = {

                                                            quizViewModel
                                                                .seleccionarRespuesta(
                                                                    preguntaId =
                                                                        pregunta.id,
                                                                    opcion =
                                                                        indiceOpcion
                                                                )
                                                        }
                                                    )

                                                    Text(
                                                        text =
                                                            opcion,
                                                        modifier =
                                                            Modifier
                                                                .padding(
                                                                    top =
                                                                        12.dp
                                                                )
                                                    )
                                                }
                                            }

                                        Spacer(
                                            modifier =
                                                Modifier
                                                    .height(
                                                        12.dp
                                                    )
                                        )
                                    }

                                Button(
                                    onClick = {

                                        quizViewModel
                                            .finalizarQuiz()
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
                                        "FINALIZAR QUIZ"
                                    )
                                }

                                quizViewModel
                                    .resultadoTexto
                                    ?.let { resultado ->

                                        Spacer(
                                            modifier =
                                                Modifier
                                                    .height(
                                                        12.dp
                                                    )
                                        )

                                        Text(
                                            text =
                                                resultado,
                                            fontSize =
                                                18.sp,
                                            fontWeight =
                                                FontWeight.Bold,
                                            color =
                                                if (
                                                    resultado
                                                        .startsWith(
                                                            "Resultado"
                                                        )
                                                )
                                                    colorPrincipal
                                                else
                                                    MaterialTheme
                                                        .colorScheme
                                                        .error
                                        )
                                    }
                            }
                        }
                    }
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
    }

    val quizAEliminar =
        quizViewModel.quizAEliminar

    if (
        quizAEliminar !=
        null
    ) {

        ConfirmDeleteDialog(
            titulo =
                "Eliminar Quiz",
            mensaje =
                "¿Seguro que deseas eliminar \"${quizAEliminar.titulo}\"? También se eliminarán sus preguntas e intentos registrados.",
            onConfirm = {

                quizViewModel
                    .confirmarEliminarQuiz()
            },
            onDismiss = {

                quizViewModel
                    .cancelarEliminarQuiz()
            }
        )
    }
}

@Composable
private fun OpcionCorrecta(
    texto: String,
    indice: Int,
    seleccion: Int?,
    onSeleccionar: () -> Unit
) {

    Row(
        modifier =
            Modifier.fillMaxWidth()
    ) {

        RadioButton(
            selected =
                seleccion == indice,
            onClick =
                onSeleccionar
        )

        Text(
            text =
                "Opción $texto",
            modifier =
                Modifier.padding(
                    top = 12.dp
                )
        )
    }
}

@Composable
private fun QuizCard(
    quiz: Quiz,
    nombreTema: String,
    mejorNota: Double?,
    onAdministrar: () -> Unit,
    onResolver: () -> Unit,
    onEliminar: () -> Unit
) {

    Card(
        modifier =
            Modifier.fillMaxWidth(),
        shape =
            RoundedCornerShape(
                18.dp
            ),
        colors =
            CardDefaults.cardColors(
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
                        4.dp
                    )
            )

            Text(
                text =
                    quiz.titulo,
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
                    "${quiz.preguntas.size} preguntas",
                color =
                    Color.Gray
            )

            if (
                mejorNota !=
                null
            ) {

                Text(
                    text =
                        "Mejor nota: ${
                            String.format(
                                Locale.getDefault(),
                                "%.0f%%",
                                mejorNota
                            )
                        }",
                    color =
                        Color.Gray
                )
            }

            Spacer(
                modifier =
                    Modifier.height(
                        12.dp
                    )
            )

            Button(
                onClick =
                    onAdministrar,
                modifier =
                    Modifier
                        .fillMaxWidth(),
                colors =
                    ButtonDefaults
                        .buttonColors(
                            containerColor =
                                Color(
                                    0xFF4F46E5
                                )
                        )
            ) {

                Text(
                    "ADMINISTRAR PREGUNTAS"
                )
            }

            Spacer(
                modifier =
                    Modifier.height(
                        8.dp
                    )
            )

            OutlinedButton(
                onClick =
                    onResolver,
                enabled =
                    quiz.preguntas
                        .isNotEmpty(),
                modifier =
                    Modifier
                        .fillMaxWidth()
            ) {

                Text(
                    "RESOLVER QUIZ"
                )
            }

            TextButton(
                onClick =
                    onEliminar,
                modifier =
                    Modifier
                        .fillMaxWidth()
            ) {

                Text(
                    "Eliminar Quiz"
                )
            }
        }
    }
}
package com.estudiasmart.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.estudiasmart.app.model.Tema
import com.estudiasmart.app.ui.components.ConfirmDeleteDialog
import com.estudiasmart.app.viewmodel.AcademicViewModel

@Composable
fun TemasScreen(
    materiaId: Int,
    onBack: () -> Unit,
    onVerMateriales: (Tema) -> Unit,
    academicViewModel:
    AcademicViewModel =
        viewModel()
) {

    val materia =
        academicViewModel
            .obtenerMateria(
                materiaId
            )

    if (materia == null) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {

            Text(
                text =
                    "Materia no encontrada."
            )

            TextButton(
                onClick = onBack
            ) {
                Text("Volver")
            }
        }

        return
    }

    val temas =
        academicViewModel
            .temasPorMateria(
                materiaId
            )

    val colorPrincipal =
        Color(0xFF4F46E5)

    val fondo =
        Color(0xFFF6F7FB)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(fondo)
            .padding(20.dp)
    ) {

        TextButton(
            onClick = onBack
        ) {
            Text(
                "← Volver a materias"
            )
        }

        Text(
            text = "Temas",
            fontSize = 30.sp,
            fontWeight =
                FontWeight.Bold,
            color =
                colorPrincipal
        )

        Text(
            text =
                materia.nombre,
            fontSize = 17.sp,
            fontWeight =
                FontWeight.Medium
        )

        Text(
            text =
                "Organiza los contenidos que estudiarás.",
            color =
                Color.Gray
        )

        Spacer(
            modifier =
                Modifier.height(18.dp)
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
                        if (
                            academicViewModel
                                .temaEditando ==
                            null
                        )
                            "Nuevo tema"
                        else
                            "Editar tema",
                    fontSize = 20.sp,
                    fontWeight =
                        FontWeight.Bold
                )

                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )

                OutlinedTextField(
                    value =
                        academicViewModel
                            .nombreTema,
                    onValueChange = {

                        academicViewModel
                            .cambiarNombreTema(
                                it
                            )
                    },
                    label = {
                        Text(
                            "Nombre del tema"
                        )
                    },
                    placeholder = {
                        Text(
                            "Ej. Jetpack Compose"
                        )
                    },
                    singleLine = true,
                    modifier =
                        Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )

                OutlinedTextField(
                    value =
                        academicViewModel
                            .descripcionTema,
                    onValueChange = {

                        academicViewModel
                            .cambiarDescripcionTema(
                                it
                            )
                    },
                    label = {
                        Text("Descripción")
                    },
                    placeholder = {
                        Text(
                            "Descripción del tema"
                        )
                    },
                    modifier =
                        Modifier.fillMaxWidth()
                )

                academicViewModel
                    .errorTema
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
                                Color(
                                    0xFFB3261E
                                )
                        )
                    }

                Spacer(
                    modifier =
                        Modifier.height(16.dp)
                )

                Button(
                    onClick = {

                        academicViewModel
                            .guardarTema(
                                materiaId
                            )
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
                            academicViewModel
                                .temaEditando ==
                            null
                        )
                            "AGREGAR TEMA"
                        else
                            "GUARDAR CAMBIOS"
                    )
                }

                if (
                    academicViewModel
                        .temaEditando !=
                    null
                ) {

                    TextButton(
                        onClick = {

                            academicViewModel
                                .cancelarEdicionTema()
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

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        Text(
            text =
                "Temas registrados",
            fontSize = 20.sp,
            fontWeight =
                FontWeight.Bold
        )

        Spacer(
            modifier =
                Modifier.height(10.dp)
        )

        if (
            temas.isEmpty()
        ) {

            Text(
                text =
                    "Todavía no hay temas en esta materia.",
                color =
                    Color.Gray
            )

        } else {

            LazyColumn(
                verticalArrangement =
                    Arrangement.spacedBy(
                        10.dp
                    )
            ) {

                items(
                    items =
                        temas,
                    key = {
                        it.id
                    }
                ) { tema ->

                    Card(
                        modifier =
                            Modifier.fillMaxWidth(),
                        shape =
                            RoundedCornerShape(
                                16.dp
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
                                    tema.nombre,
                                fontSize =
                                    18.sp,
                                fontWeight =
                                    FontWeight.Bold
                            )

                            if (
                                tema.descripcion
                                    .isNotBlank()
                            ) {

                                Spacer(
                                    modifier =
                                        Modifier.height(
                                            4.dp
                                        )
                                )

                                Text(
                                    text =
                                        tema.descripcion,
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
                                onClick = {

                                    onVerMateriales(
                                        tema
                                    )
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
                                    "VER MATERIALES"
                                )
                            }

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
                                        8.dp
                                    )
                            ) {

                                OutlinedButton(
                                    onClick = {

                                        academicViewModel
                                            .iniciarEdicionTema(
                                                tema
                                            )
                                    },
                                    modifier =
                                        Modifier.weight(
                                            1f
                                        )
                                ) {

                                    Text("Editar")
                                }

                                OutlinedButton(
                                    onClick = {

                                        academicViewModel
                                            .solicitarEliminarTema(
                                                tema
                                            )
                                    },
                                    modifier =
                                        Modifier.weight(
                                            1f
                                        )
                                ) {

                                    Text("Eliminar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    val temaAEliminar =
        academicViewModel
            .temaAEliminar

    if (
        temaAEliminar !=
        null
    ) {

        ConfirmDeleteDialog(
            titulo =
                "Eliminar tema",
            mensaje =
                "¿Seguro que deseas eliminar \"${temaAEliminar.nombre}\"? También se eliminarán los materiales asociados.",
            onConfirm = {

                academicViewModel
                    .confirmarEliminarTema()
            },
            onDismiss = {

                academicViewModel
                    .cancelarEliminarTema()
            }
        )
    }
}
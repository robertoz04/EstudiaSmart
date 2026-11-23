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
import com.estudiasmart.app.model.Materia
import com.estudiasmart.app.ui.components.ConfirmDeleteDialog
import com.estudiasmart.app.viewmodel.AcademicViewModel

@Composable
fun MateriasScreen(
    onBack: () -> Unit,
    onVerTemas: (Materia) -> Unit,
    academicViewModel: AcademicViewModel =
        viewModel()
) {

    val colorPrincipal =
        Color(0xFF4F46E5)

    val fondo =
        Color(0xFFF6F7FB)

    val materias =
        academicViewModel.materias

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(fondo)
            .padding(20.dp)
    ) {

        TextButton(
            onClick = onBack
        ) {
            Text("← Volver")
        }

        Text(
            text = "Mis materias",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = colorPrincipal
        )

        Text(
            text =
                "Organiza las materias que estás estudiando.",
            color = Color.Gray
        )

        Spacer(
            modifier =
                Modifier.height(20.dp)
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
                                .materiaEditando ==
                            null
                        )
                            "Nueva materia"
                        else
                            "Editar materia",
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
                            .nombreMateria,
                    onValueChange = {

                        academicViewModel
                            .cambiarNombreMateria(
                                it
                            )
                    },
                    label = {
                        Text("Nombre")
                    },
                    placeholder = {
                        Text(
                            "Ej. Programación III"
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
                            .descripcionMateria,
                    onValueChange = {

                        academicViewModel
                            .cambiarDescripcionMateria(
                                it
                            )
                    },
                    label = {
                        Text("Descripción")
                    },
                    placeholder = {
                        Text(
                            "Descripción de la materia"
                        )
                    },
                    modifier =
                        Modifier.fillMaxWidth()
                )

                academicViewModel
                    .errorMateria
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
                            .guardarMateria()
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
                        text =
                            if (
                                academicViewModel
                                    .materiaEditando ==
                                null
                            )
                                "AGREGAR MATERIA"
                            else
                                "GUARDAR CAMBIOS"
                    )
                }

                if (
                    academicViewModel
                        .materiaEditando !=
                    null
                ) {

                    TextButton(
                        onClick = {

                            academicViewModel
                                .cancelarEdicionMateria()
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
                "Materias registradas",
            fontSize = 20.sp,
            fontWeight =
                FontWeight.Bold
        )

        Spacer(
            modifier =
                Modifier.height(10.dp)
        )

        if (
            materias.isEmpty()
        ) {

            Text(
                text =
                    "Todavía no tienes materias registradas.",
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
                        materias,
                    key = {
                        it.id
                    }
                ) { materia ->

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
                                    materia.nombre,
                                fontSize =
                                    18.sp,
                                fontWeight =
                                    FontWeight.Bold
                            )

                            if (
                                materia.descripcion
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
                                        materia.descripcion,
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

                                    onVerTemas(
                                        materia
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
                                    "VER TEMAS"
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
                                            .iniciarEdicionMateria(
                                                materia
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
                                            .solicitarEliminarMateria(
                                                materia
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

    val materiaAEliminar =
        academicViewModel
            .materiaAEliminar

    if (
        materiaAEliminar !=
        null
    ) {

        ConfirmDeleteDialog(
            titulo =
                "Eliminar materia",
            mensaje =
                "¿Seguro que deseas eliminar \"${materiaAEliminar.nombre}\"? También se eliminarán sus temas y materiales.",
            onConfirm = {

                academicViewModel
                    .confirmarEliminarMateria()
            },
            onDismiss = {

                academicViewModel
                    .cancelarEliminarMateria()
            }
        )
    }
}
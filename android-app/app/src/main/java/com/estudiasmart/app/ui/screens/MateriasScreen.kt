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
import com.estudiasmart.app.model.Materia

@Composable
fun MateriasScreen(
    onBack: () -> Unit,
    onVerTemas: (Materia) -> Unit
) {

    var nombre by remember {
        mutableStateOf("")
    }

    var descripcion by remember {
        mutableStateOf("")
    }

    var error by remember {
        mutableStateOf<String?>(null)
    }

    var materiaEditando by remember {
        mutableStateOf<Materia?>(null)
    }

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
            Text("← Volver")
        }

        Text(
            text = "Mis materias",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = colorPrincipal
        )

        Text(
            text = "Organiza las materias que estás estudiando.",
            color = Color.Gray
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp
            )
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text =
                        if (materiaEditando == null)
                            "Nueva materia"
                        else
                            "Editar materia",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                OutlinedTextField(
                    value = nombre,
                    onValueChange = {
                        nombre = it
                        error = null
                    },
                    label = {
                        Text("Nombre")
                    },
                    placeholder = {
                        Text("Ej. Programación III")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = {
                        descripcion = it
                    },
                    label = {
                        Text("Descripción")
                    },
                    placeholder = {
                        Text("Descripción de la materia")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                if (error != null) {

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = error!!,
                        color = Color(0xFFB3261E)
                    )
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Button(
                    onClick = {

                        if (materiaEditando == null) {

                            val resultado =
                                AcademicRepository
                                    .crearMateria(
                                        nombre = nombre,
                                        descripcion = descripcion
                                    )

                            if (resultado == null) {

                                nombre = ""
                                descripcion = ""
                                error = null

                            } else {

                                error = resultado
                            }

                        } else {

                            val resultado =
                                AcademicRepository
                                    .actualizarMateria(
                                        id = materiaEditando!!.id,
                                        nombre = nombre,
                                        descripcion = descripcion
                                    )

                            if (resultado == null) {

                                nombre = ""
                                descripcion = ""
                                materiaEditando = null
                                error = null

                            } else {

                                error = resultado
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorPrincipal
                    )
                ) {

                    Text(
                        text =
                            if (materiaEditando == null)
                                "AGREGAR MATERIA"
                            else
                                "GUARDAR CAMBIOS"
                    )
                }

                if (materiaEditando != null) {

                    TextButton(
                        onClick = {

                            materiaEditando = null
                            nombre = ""
                            descripcion = ""
                            error = null
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Text("Cancelar edición")
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "Materias registradas",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        if (AcademicRepository.materias.isEmpty()) {

            Text(
                text = "Todavía no tienes materias registradas.",
                color = Color.Gray
            )

        } else {

            LazyColumn(
                verticalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                items(
                    items = AcademicRepository.materias,
                    key = {
                        it.id
                    }
                ) { materia ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                text = materia.nombre,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            if (
                                materia.descripcion
                                    .isNotBlank()
                            ) {

                                Spacer(
                                    modifier =
                                        Modifier.height(4.dp)
                                )

                                Text(
                                    text = materia.descripcion,
                                    color = Color.Gray
                                )
                            }

                            Spacer(
                                modifier =
                                    Modifier.height(12.dp)
                            )

                            Button(
                                onClick = {
                                    onVerTemas(materia)
                                },
                                modifier =
                                    Modifier.fillMaxWidth(),
                                colors =
                                    ButtonDefaults.buttonColors(
                                        containerColor =
                                            colorPrincipal
                                    )
                            ) {

                                Text("VER TEMAS")
                            }

                            Spacer(
                                modifier =
                                    Modifier.height(8.dp)
                            )

                            Row(
                                modifier =
                                    Modifier.fillMaxWidth(),
                                horizontalArrangement =
                                    Arrangement.spacedBy(8.dp)
                            ) {

                                OutlinedButton(
                                    onClick = {

                                        materiaEditando =
                                            materia

                                        nombre =
                                            materia.nombre

                                        descripcion =
                                            materia.descripcion

                                        error = null
                                    },
                                    modifier =
                                        Modifier.weight(1f)
                                ) {

                                    Text("Editar")
                                }

                                OutlinedButton(
                                    onClick = {

                                        AcademicRepository
                                            .eliminarMateria(
                                                materia.id
                                            )

                                        if (
                                            materiaEditando
                                                ?.id ==
                                            materia.id
                                        ) {

                                            materiaEditando =
                                                null

                                            nombre = ""
                                            descripcion = ""
                                            error = null
                                        }
                                    },
                                    modifier =
                                        Modifier.weight(1f)
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
}
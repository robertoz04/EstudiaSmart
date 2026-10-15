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
import com.estudiasmart.app.model.Material

@Composable
fun MaterialesScreen(
    temaId: Int,
    onBack: () -> Unit
) {

    val tema =
        AcademicRepository.obtenerTema(
            temaId
        )

    if (tema == null) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {

            Text("Tema no encontrado.")

            TextButton(
                onClick = onBack
            ) {
                Text("Volver")
            }
        }

        return
    }

    var titulo by remember {
        mutableStateOf("")
    }

    var contenido by remember {
        mutableStateOf("")
    }

    var error by remember {
        mutableStateOf<String?>(null)
    }

    var materialEditando by remember {
        mutableStateOf<Material?>(null)
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
            Text("← Volver a temas")
        }

        Text(
            text = "Materiales",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = colorPrincipal
        )

        Text(
            text = tema.nombre,
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium
        )

        Text(
            text = "Agrega recursos para estudiar este tema.",
            color = Color.Gray
        )

        Spacer(
            modifier = Modifier.height(18.dp)
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
                        if (materialEditando == null)
                            "Nuevo material"
                        else
                            "Editar material",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                OutlinedTextField(
                    value = titulo,
                    onValueChange = {
                        titulo = it
                        error = null
                    },
                    label = {
                        Text("Título")
                    },
                    placeholder = {
                        Text("Ej. Introducción a Compose")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                OutlinedTextField(
                    value = contenido,
                    onValueChange = {
                        contenido = it
                        error = null
                    },
                    label = {
                        Text("Contenido")
                    },
                    placeholder = {
                        Text(
                            "Escribe aquí apuntes, conceptos o información de estudio."
                        )
                    },
                    minLines = 3,
                    maxLines = 6,
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

                        if (materialEditando == null) {

                            val resultado =
                                AcademicRepository
                                    .crearMaterial(
                                        temaId = temaId,
                                        titulo = titulo,
                                        contenido = contenido
                                    )

                            if (resultado == null) {

                                titulo = ""
                                contenido = ""
                                error = null

                            } else {

                                error = resultado
                            }

                        } else {

                            val resultado =
                                AcademicRepository
                                    .actualizarMaterial(
                                        id =
                                            materialEditando!!.id,
                                        titulo = titulo,
                                        contenido = contenido
                                    )

                            if (resultado == null) {

                                titulo = ""
                                contenido = ""
                                materialEditando = null
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
                        if (materialEditando == null)
                            "AGREGAR MATERIAL"
                        else
                            "GUARDAR CAMBIOS"
                    )
                }

                if (materialEditando != null) {

                    TextButton(
                        onClick = {

                            materialEditando = null
                            titulo = ""
                            contenido = ""
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
            text = "Materiales registrados",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        val materiales =
            AcademicRepository
                .materialesPorTema(
                    temaId
                )

        if (materiales.isEmpty()) {

            Text(
                text =
                    "Todavía no hay materiales para este tema.",
                color = Color.Gray
            )

        } else {

            LazyColumn(
                verticalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                items(
                    items = materiales,
                    key = {
                        it.id
                    }
                ) { material ->

                    Card(
                        modifier =
                            Modifier.fillMaxWidth(),
                        shape =
                            RoundedCornerShape(16.dp),
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
                                text = material.titulo,
                                fontSize = 18.sp,
                                fontWeight =
                                    FontWeight.Bold
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(6.dp)
                            )

                            Text(
                                text = material.contenido,
                                color = Color.Gray
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(12.dp)
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

                                        materialEditando =
                                            material

                                        titulo =
                                            material.titulo

                                        contenido =
                                            material.contenido

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
                                            .eliminarMaterial(
                                                material.id
                                            )

                                        if (
                                            materialEditando
                                                ?.id ==
                                            material.id
                                        ) {

                                            materialEditando =
                                                null

                                            titulo = ""
                                            contenido = ""
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
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.estudiasmart.app.data.RecomendacionService
import com.estudiasmart.app.model.PrioridadRecomendacion
import com.estudiasmart.app.model.RecomendacionEstudio
import java.util.Locale

@Composable
fun RecomendacionesScreen(
    onBack: () -> Unit
) {

    val recomendaciones =
        RecomendacionService
            .obtenerRecomendaciones()

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
            Arrangement.spacedBy(
                12.dp
            )
    ) {

        item {

            Spacer(
                modifier =
                    Modifier.height(
                        12.dp
                    )
            )

            TextButton(
                onClick = onBack
            ) {

                Text(
                    "← Volver al inicio"
                )
            }

            Text(
                text =
                    "¿Qué debo estudiar hoy?",
                fontSize =
                    28.sp,
                fontWeight =
                    FontWeight.Bold,
                color =
                    colorPrincipal
            )

            Spacer(
                modifier =
                    Modifier.height(
                        6.dp
                    )
            )

            Text(
                text =
                    "EstudiaSmart analiza tu progreso y recomienda los temas que necesitan más atención.",
                color =
                    Color.Gray
            )

            Spacer(
                modifier =
                    Modifier.height(
                        16.dp
                    )
            )
        }

        if (
            recomendaciones.isEmpty()
        ) {

            item {

                Card(
                    modifier =
                        Modifier.fillMaxWidth(),
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
                                20.dp
                            )
                    ) {

                        Text(
                            text =
                                "Todavía no hay recomendaciones.",
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
                                "Crea materias y temas para que EstudiaSmart pueda analizar tu progreso.",
                            color =
                                Color.Gray
                        )
                    }
                }
            }

        } else {

            item {

                Text(
                    text =
                        "Recomendaciones de hoy",
                    fontSize =
                        20.sp,
                    fontWeight =
                        FontWeight.Bold
                )
            }

            itemsIndexed(
                items =
                    recomendaciones,
                key = {
                        _,
                        recomendacion ->

                    recomendacion.temaId
                }
            ) {
                    indice,
                    recomendacion ->

                RecomendacionCard(
                    posicion =
                        indice + 1,
                    recomendacion =
                        recomendacion
                )
            }
        }

        item {

            Spacer(
                modifier =
                    Modifier.height(
                        16.dp
                    )
            )

            Card(
                modifier =
                    Modifier.fillMaxWidth(),
                colors =
                    CardDefaults
                        .cardColors(
                            containerColor =
                                Color.White
                        ),
                shape =
                    RoundedCornerShape(
                        18.dp
                    )
            ) {

                Column(
                    modifier =
                        Modifier.padding(
                            18.dp
                        )
                ) {

                    Text(
                        text =
                            "¿Cómo se calcula?",
                        fontWeight =
                            FontWeight.Bold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(
                                8.dp
                            )
                    )

                    Text(
                        text =
                            "50% nivel de dominio",
                        color =
                            Color.Gray
                    )

                    Text(
                        text =
                            "30% necesidad de repaso",
                        color =
                            Color.Gray
                    )

                    Text(
                        text =
                            "20% dificultad",
                        color =
                            Color.Gray
                    )
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

@Composable
private fun RecomendacionCard(
    posicion: Int,
    recomendacion:
    RecomendacionEstudio
) {

    val colorPrioridad =
        when (
            recomendacion.prioridad
        ) {

            PrioridadRecomendacion
                .MUY_ALTA ->
                Color(0xFFB3261E)

            PrioridadRecomendacion
                .ALTA ->
                Color(0xFFE07A00)

            PrioridadRecomendacion
                .MEDIA ->
                Color(0xFF4F46E5)

            PrioridadRecomendacion
                .BAJA ->
                Color(0xFF2E7D32)
        }

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
                    2.dp
            )
    ) {

        Column(
            modifier =
                Modifier.padding(
                    18.dp
                )
        ) {

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement
                        .SpaceBetween
            ) {

                Text(
                    text =
                        "#$posicion",
                    fontSize =
                        22.sp,
                    fontWeight =
                        FontWeight.Bold,
                    color =
                        Color(
                            0xFF4F46E5
                        )
                )

                Card(
                    colors =
                        CardDefaults
                            .cardColors(
                                containerColor =
                                    colorPrioridad
                                        .copy(
                                            alpha =
                                                0.12f
                                        )
                            ),
                    shape =
                        RoundedCornerShape(
                            12.dp
                        )
                ) {

                    Text(
                        text =
                            recomendacion
                                .prioridad
                                .etiqueta
                                .uppercase(),
                        modifier =
                            Modifier.padding(
                                horizontal =
                                    12.dp,
                                vertical =
                                    6.dp
                            ),
                        color =
                            colorPrioridad,
                        fontWeight =
                            FontWeight.Bold,
                        fontSize =
                            12.sp
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(
                        10.dp
                    )
            )

            Text(
                text =
                    recomendacion
                        .nombreTema,
                fontSize =
                    21.sp,
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
                    recomendacion
                        .motivo,
                color =
                    Color.Gray
            )

            Spacer(
                modifier =
                    Modifier.height(
                        16.dp
                    )
            )

            DatoRecomendacion(
                titulo =
                    "Dominio actual",
                valor =
                    "${formatear(recomendacion.dominioActual)}%"
            )

            DatoRecomendacion(
                titulo =
                    "Necesidad de refuerzo",
                valor =
                    "${formatear(recomendacion.debilidadDominio)}%"
            )

            DatoRecomendacion(
                titulo =
                    "Urgencia de repaso",
                valor =
                    "${formatear(recomendacion.urgenciaRepaso)}%"
            )

            DatoRecomendacion(
                titulo =
                    "Dificultad",
                valor =
                    "${formatear(recomendacion.dificultad)}%"
            )

            Spacer(
                modifier =
                    Modifier.height(
                        12.dp
                    )
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement
                        .SpaceBetween
            ) {

                Text(
                    text =
                        "Puntuación",
                    fontWeight =
                        FontWeight.Bold
                )

                Text(
                    text =
                        formatear(
                            recomendacion
                                .puntuacion
                        ),
                    fontSize =
                        20.sp,
                    fontWeight =
                        FontWeight.Bold,
                    color =
                        colorPrioridad
                )
            }
        }
    }
}

@Composable
private fun DatoRecomendacion(
    titulo: String,
    valor: String
) {

    Row(
        modifier =
            Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {

        Text(
            text =
                titulo,
            color =
                Color.Gray
        )

        Text(
            text =
                valor,
            fontWeight =
                FontWeight.Medium
        )
    }

    Spacer(
        modifier =
            Modifier.height(
                5.dp
            )
    )
}

private fun formatear(
    valor: Double
): String {

    return String.format(
        Locale.getDefault(),
        "%.0f",
        valor
    )
}
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
import com.estudiasmart.app.data.ProgresoService
import com.estudiasmart.app.model.EstadoTema
import com.estudiasmart.app.model.ProgresoTema
import java.util.Locale

@Composable
fun ProgresoScreen(
    onBack: () -> Unit
) {

    val colorPrincipal =
        Color(0xFF4F46E5)

    val fondo =
        Color(0xFFF6F7FB)

    val progresos =
        ProgresoService
            .obtenerTodosLosProgresos()

    val promedioGeneral =
        ProgresoService
            .calcularPromedioGeneral()

    val requiereRefuerzo =
        progresos.count {
            it.estado ==
                    EstadoTema.REQUIERE_REFUERZO
        }

    val enProgreso =
        progresos.count {
            it.estado ==
                    EstadoTema.EN_PROGRESO
        }

    val dominioAlto =
        progresos.count {
            it.estado ==
                    EstadoTema.DOMINIO_ALTO
        }

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
                Text("← Volver al inicio")
            }

            Text(
                text = "Mi progreso",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = colorPrincipal
            )

            Text(
                text =
                    "Consulta tu nivel de dominio en cada tema.",
                color = Color.Gray
            )

            Spacer(
                modifier =
                    Modifier.height(18.dp)
            )
        }

        item {

            Card(
                modifier =
                    Modifier.fillMaxWidth(),
                shape =
                    RoundedCornerShape(22.dp),
                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            colorPrincipal
                    )
            ) {

                Column(
                    modifier =
                        Modifier.padding(22.dp)
                ) {

                    Text(
                        text =
                            "Promedio general",
                        color =
                            Color.White
                    )

                    Spacer(
                        modifier =
                            Modifier.height(4.dp)
                    )

                    Text(
                        text =
                            "${formatearPorcentaje(promedioGeneral)}%",
                        fontSize = 36.sp,
                        fontWeight =
                            FontWeight.Bold,
                        color =
                            Color.White
                    )

                    Text(
                        text =
                            mensajePromedio(
                                promedioGeneral
                            ),
                        color =
                            Color.White
                    )
                }
            }
        }

        item {

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(
                        8.dp
                    )
            ) {

                EstadoResumenCard(
                    titulo = "Refuerzo",
                    cantidad =
                        requiereRefuerzo,
                    modifier =
                        Modifier.weight(1f)
                )

                EstadoResumenCard(
                    titulo = "Progreso",
                    cantidad =
                        enProgreso,
                    modifier =
                        Modifier.weight(1f)
                )

                EstadoResumenCard(
                    titulo = "Dominio",
                    cantidad =
                        dominioAlto,
                    modifier =
                        Modifier.weight(1f)
                )
            }
        }

        item {

            Spacer(
                modifier =
                    Modifier.height(6.dp)
            )

            Text(
                text =
                    "Progreso por tema",
                fontSize = 21.sp,
                fontWeight =
                    FontWeight.Bold
            )
        }

        if (progresos.isEmpty()) {

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
                                "Todavía no hay temas para evaluar.",
                            fontWeight =
                                FontWeight.Bold
                        )

                        Spacer(
                            modifier =
                                Modifier.height(6.dp)
                        )

                        Text(
                            text =
                                "Crea una materia y un tema para comenzar a registrar tu progreso.",
                            color =
                                Color.Gray
                        )
                    }
                }
            }

        } else {

            items(
                items = progresos,
                key = {
                    it.temaId
                }
            ) { progreso ->

                ProgresoTemaCard(
                    progreso =
                        progreso
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
}

@Composable
private fun ProgresoTemaCard(
    progreso: ProgresoTema
) {

    val colorEstado =
        when (progreso.estado) {

            EstadoTema.REQUIERE_REFUERZO ->
                Color(0xFFB3261E)

            EstadoTema.EN_PROGRESO ->
                Color(0xFFE07A00)

            EstadoTema.DOMINIO_ALTO ->
                Color(0xFF2E7D32)
        }

    Card(
        modifier =
            Modifier.fillMaxWidth(),
        shape =
            RoundedCornerShape(18.dp),
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
                Modifier.padding(18.dp)
        ) {

            Text(
                text =
                    progreso.nombreTema,
                fontSize = 20.sp,
                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )

            FilaProgreso(
                titulo = "Quiz",
                porcentaje =
                    progreso
                        .porcentajeQuiz
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            FilaProgreso(
                titulo = "Flashcards",
                porcentaje =
                    progreso
                        .porcentajeFlashcards
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                Text(
                    text =
                        "Dominio total",
                    fontWeight =
                        FontWeight.Bold
                )

                Text(
                    text =
                        "${formatearPorcentaje(progreso.dominioTotal)}%",
                    fontSize = 20.sp,
                    fontWeight =
                        FontWeight.Bold,
                    color =
                        Color(0xFF4F46E5)
                )
            }

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            Card(
                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            colorEstado.copy(
                                alpha = 0.12f
                            )
                    ),
                shape =
                    RoundedCornerShape(12.dp)
            ) {

                Text(
                    text =
                        progreso.estado.etiqueta
                            .uppercase(),
                    modifier =
                        Modifier.padding(
                            horizontal = 14.dp,
                            vertical = 8.dp
                        ),
                    fontWeight =
                        FontWeight.Bold,
                    color =
                        colorEstado
                )
            }
        }
    }
}

@Composable
private fun FilaProgreso(
    titulo: String,
    porcentaje: Double
) {

    Row(
        modifier =
            Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {

        Text(
            text = titulo,
            color = Color.Gray
        )

        Text(
            text =
                "${formatearPorcentaje(porcentaje)}%",
            fontWeight =
                FontWeight.SemiBold
        )
    }
}

@Composable
private fun EstadoResumenCard(
    titulo: String,
    cantidad: Int,
    modifier: Modifier =
        Modifier
) {

    Card(
        modifier =
            modifier,
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
                Modifier.padding(14.dp)
        ) {

            Text(
                text =
                    cantidad.toString(),
                fontSize = 24.sp,
                fontWeight =
                    FontWeight.Bold,
                color =
                    Color(0xFF4F46E5)
            )

            Text(
                text = titulo,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

private fun formatearPorcentaje(
    valor: Double
): String {

    return String.format(
        Locale.getDefault(),
        "%.0f",
        valor
    )
}

private fun mensajePromedio(
    promedio: Double
): String {

    return when {

        promedio < 60.0 ->
            "Continúa practicando los temas que necesitan refuerzo."

        promedio < 80.0 ->
            "Vas avanzando. Sigue estudiando para aumentar tu dominio."

        else ->
            "Excelente avance. Mantén tu ritmo de estudio."
    }
}
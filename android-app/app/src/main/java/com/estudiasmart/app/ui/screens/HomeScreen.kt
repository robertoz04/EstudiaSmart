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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.estudiasmart.app.data.AcademicRepository
import com.estudiasmart.app.data.AuthRepository
import com.estudiasmart.app.data.FlashcardRepository
import com.estudiasmart.app.data.ProgresoService
import com.estudiasmart.app.data.QuizRepository
import java.util.Locale

@Composable
fun HomeScreen(
    onMateriasClick: () -> Unit,
    onFlashcardsClick: () -> Unit,
    onQuizClick: () -> Unit,
    onProgresoClick: () -> Unit
) {

    val colorPrincipal =
        Color(0xFF4F46E5)

    val fondo =
        Color(0xFFF6F7FB)

    val usuario =
        AuthRepository.usuarioActual

    val progresoGeneral =
        ProgresoService
            .calcularPromedioGeneral()

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
                    Modifier.height(18.dp)
            )

            Text(
                text = "EstudiaSmart",
                fontSize = 30.sp,
                fontWeight =
                    FontWeight.Bold,
                color =
                    colorPrincipal
            )

            Spacer(
                modifier =
                    Modifier.height(6.dp)
            )

            Text(
                text =
                    "Hola, ${usuario?.nombre ?: "Estudiante"} 👋",
                fontSize = 21.sp,
                fontWeight =
                    FontWeight.SemiBold
            )

            Text(
                text =
                    "¿Qué quieres estudiar hoy?",
                color =
                    Color.Gray
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )
        }

        item {

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(
                        12.dp
                    )
            ) {

                ResumenCard(
                    titulo = "Materias",
                    valor =
                        AcademicRepository
                            .materias
                            .size
                            .toString(),
                    modifier =
                        Modifier.weight(1f)
                )

                ResumenCard(
                    titulo = "Temas",
                    valor =
                        AcademicRepository
                            .temas
                            .size
                            .toString(),
                    modifier =
                        Modifier.weight(1f)
                )
            }
        }

        item {

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(
                        12.dp
                    )
            ) {

                ResumenCard(
                    titulo = "Flashcards",
                    valor =
                        FlashcardRepository
                            .flashcards
                            .size
                            .toString(),
                    modifier =
                        Modifier.weight(1f)
                )

                ResumenCard(
                    titulo = "Quiz",
                    valor =
                        QuizRepository
                            .quizzes
                            .size
                            .toString(),
                    modifier =
                        Modifier.weight(1f)
                )
            }
        }

        item {

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

                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                18.dp
                            ),
                    horizontalArrangement =
                        Arrangement.SpaceBetween
                ) {

                    Column {

                        Text(
                            text =
                                "Progreso general",
                            color =
                                Color.Gray
                        )

                        Text(
                            text =
                                "${String.format(
                                    Locale.getDefault(),
                                    "%.0f",
                                    progresoGeneral
                                )}%",
                            fontSize =
                                28.sp,
                            fontWeight =
                                FontWeight.Bold,
                            color =
                                colorPrincipal
                        )
                    }

                    Button(
                        onClick =
                            onProgresoClick,
                        colors =
                            ButtonDefaults
                                .buttonColors(
                                    containerColor =
                                        colorPrincipal
                                )
                    ) {

                        Text(
                            "VER PROGRESO"
                        )
                    }
                }
            }
        }

        item {

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(
                text =
                    "Tus herramientas de estudio",
                fontSize = 20.sp,
                fontWeight =
                    FontWeight.Bold
            )
        }

        item {

            ToolCard(
                titulo =
                    "📚 Mis materias",
                descripcion =
                    "Administra tus materias, temas y materiales.",
                boton =
                    "ABRIR MIS MATERIAS",
                onClick =
                    onMateriasClick
            )
        }

        item {

            ToolCard(
                titulo =
                    "🧠 Flashcards",
                descripcion =
                    "Repasa conceptos con repetición espaciada.",
                boton =
                    "ABRIR FLASHCARDS",
                onClick =
                    onFlashcardsClick
            )
        }

        item {

            ToolCard(
                titulo =
                    "📝 Quiz",
                descripcion =
                    "Evalúa tus conocimientos y revisa tus resultados.",
                boton =
                    "ABRIR QUIZ",
                onClick =
                    onQuizClick
            )
        }

        item {

            ToolCard(
                titulo =
                    "📊 Mi progreso",
                descripcion =
                    "Consulta tu dominio en cada tema según quizzes y flashcards.",
                boton =
                    "VER MI PROGRESO",
                onClick =
                    onProgresoClick
            )
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
private fun ToolCard(
    titulo: String,
    descripcion: String,
    boton: String,
    onClick: () -> Unit
) {

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
                text = titulo,
                fontSize =
                    18.sp,
                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(4.dp)
            )

            Text(
                text =
                    descripcion,
                color =
                    Color.Gray
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            Button(
                onClick =
                    onClick,
                modifier =
                    Modifier.fillMaxWidth(),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            Color(0xFF4F46E5)
                    )
            ) {

                Text(
                    boton
                )
            }
        }
    }
}

@Composable
private fun ResumenCard(
    titulo: String,
    valor: String,
    modifier: Modifier =
        Modifier
) {

    Card(
        modifier =
            modifier,
        shape =
            RoundedCornerShape(
                18.dp
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
                    16.dp
                )
        ) {

            Text(
                text =
                    valor,
                fontSize =
                    26.sp,
                fontWeight =
                    FontWeight.Bold,
                color =
                    Color(0xFF4F46E5)
            )

            Text(
                text =
                    titulo,
                fontSize =
                    14.sp,
                color =
                    Color.Gray
            )
        }
    }
}
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
import com.estudiasmart.app.data.QuizRepository

@Composable
fun HomeScreen(
    onMateriasClick: () -> Unit,
    onFlashcardsClick: () -> Unit,
    onQuizClick: () -> Unit
) {

    val colorPrincipal =
        Color(0xFF4F46E5)

    val fondo =
        Color(0xFFF6F7FB)

    val usuario =
        AuthRepository.usuarioActual

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(fondo)
            .padding(20.dp)
    ) {

        Spacer(
            modifier =
                Modifier.height(16.dp)
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
                Modifier.height(24.dp)
        )

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
                        .materias.size
                        .toString(),
                modifier =
                    Modifier.weight(1f)
            )

            ResumenCard(
                titulo = "Temas",
                valor =
                    AcademicRepository
                        .temas.size
                        .toString(),
                modifier =
                    Modifier.weight(1f)
            )
        }

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )

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
                        .flashcards.size
                        .toString(),
                modifier =
                    Modifier.weight(1f)
            )

            ResumenCard(
                titulo = "Quiz",
                valor =
                    QuizRepository
                        .quizzes.size
                        .toString(),
                modifier =
                    Modifier.weight(1f)
            )
        }

        Spacer(
            modifier =
                Modifier.height(28.dp)
        )

        Text(
            text =
                "Tus herramientas de estudio",
            fontSize = 20.sp,
            fontWeight =
                FontWeight.Bold
        )

        Spacer(
            modifier =
                Modifier.height(14.dp)
        )

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

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )

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

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )

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

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )
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

            Text(
                text =
                    descripcion,
                color =
                    Color.Gray
            )

            Spacer(
                modifier =
                    Modifier.height(
                        12.dp
                    )
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
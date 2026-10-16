package com.estudiasmart.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.estudiasmart.app.data.AuthRepository
import com.estudiasmart.app.ui.screens.FlashcardsScreen
import com.estudiasmart.app.ui.screens.HomeScreen
import com.estudiasmart.app.ui.screens.LoginScreen
import com.estudiasmart.app.ui.screens.MaterialesScreen
import com.estudiasmart.app.ui.screens.MateriasScreen
import com.estudiasmart.app.ui.screens.ProgresoScreen
import com.estudiasmart.app.ui.screens.QuizScreen
import com.estudiasmart.app.ui.screens.RecomendacionesScreen
import com.estudiasmart.app.ui.screens.RegisterScreen
import com.estudiasmart.app.ui.screens.TemasScreen

@Composable
fun AppNavigation() {

    val navController =
        rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        // =========================
        // LOGIN
        // =========================

        composable("login") {

            LoginScreen(
                onLoginSuccess = {

                    navController.navigate(
                        "home"
                    ) {

                        popUpTo(
                            "login"
                        ) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                },
                onRegisterClick = {

                    navController.navigate(
                        "register"
                    )
                }
            )
        }

        // =========================
        // REGISTRO
        // =========================

        composable("register") {

            RegisterScreen(
                onRegisterSuccess = {

                    navController.navigate(
                        "home"
                    ) {

                        popUpTo(
                            "register"
                        ) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                },
                onBackToLogin = {

                    navController.popBackStack()
                }
            )
        }

        // =========================
        // HOME / DASHBOARD
        // =========================

        composable("home") {

            HomeScreen(
                onMateriasClick = {

                    navController.navigate(
                        "materias"
                    )
                },
                onFlashcardsClick = {

                    navController.navigate(
                        "flashcards"
                    )
                },
                onQuizClick = {

                    navController.navigate(
                        "quiz"
                    )
                },
                onProgresoClick = {

                    navController.navigate(
                        "progreso"
                    )
                },
                onRecomendacionesClick = {

                    navController.navigate(
                        "recomendaciones"
                    )
                },
                onLogoutClick = {

                    AuthRepository.cerrarSesion()

                    navController.navigate(
                        "login"
                    ) {

                        popUpTo(
                            "home"
                        ) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                }
            )
        }

        // =========================
        // FLASHCARDS
        // =========================

        composable("flashcards") {

            FlashcardsScreen(
                onBack = {

                    navController.popBackStack()
                }
            )
        }

        // =========================
        // QUIZ
        // =========================

        composable("quiz") {

            QuizScreen(
                onBack = {

                    navController.popBackStack()
                }
            )
        }

        // =========================
        // PROGRESO
        // =========================

        composable("progreso") {

            ProgresoScreen(
                onBack = {

                    navController.popBackStack()
                }
            )
        }

        // =========================
        // RECOMENDACIONES
        // =========================

        composable("recomendaciones") {

            RecomendacionesScreen(
                onBack = {

                    navController.popBackStack()
                }
            )
        }

        // =========================
        // MATERIAS
        // =========================

        composable("materias") {

            MateriasScreen(
                onBack = {

                    navController.popBackStack()
                },
                onVerTemas = { materia ->

                    navController.navigate(
                        "temas/${materia.id}"
                    )
                }
            )
        }

        // =========================
        // TEMAS
        // =========================

        composable(
            route = "temas/{materiaId}"
        ) { backStackEntry ->

            val materiaId =
                backStackEntry
                    .arguments
                    ?.getString(
                        "materiaId"
                    )
                    ?.toIntOrNull()
                    ?: 0

            TemasScreen(
                materiaId = materiaId,
                onBack = {

                    navController.popBackStack()
                },
                onVerMateriales = { tema ->

                    navController.navigate(
                        "materiales/${tema.id}"
                    )
                }
            )
        }

        // =========================
        // MATERIALES
        // =========================

        composable(
            route = "materiales/{temaId}"
        ) { backStackEntry ->

            val temaId =
                backStackEntry
                    .arguments
                    ?.getString(
                        "temaId"
                    )
                    ?.toIntOrNull()
                    ?: 0

            MaterialesScreen(
                temaId = temaId,
                onBack = {

                    navController.popBackStack()
                }
            )
        }
    }
}
package com.estudiasmart.app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
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

    val startDestination =
        if (
            AuthRepository
                .haySesionActiva()
        ) {
            Routes.HOME
        } else {
            Routes.LOGIN
        }

    NavHost(
        navController =
            navController,
        startDestination =
            startDestination
    ) {

        // =====================================================
        // LOGIN
        // =====================================================

        composable(
            Routes.LOGIN
        ) {

            LoginScreen(
                onLoginSuccess = {

                    navController.navigate(
                        Routes.HOME
                    ) {

                        popUpTo(
                            Routes.LOGIN
                        ) {

                            inclusive =
                                true
                        }

                        launchSingleTop =
                            true
                    }
                },
                onRegisterClick = {

                    navController.navigate(
                        Routes.REGISTER
                    )
                }
            )
        }

        // =====================================================
        // REGISTRO
        // =====================================================

        composable(
            Routes.REGISTER
        ) {

            RegisterScreen(
                onRegisterSuccess = {

                    navController.navigate(
                        Routes.HOME
                    ) {

                        popUpTo(
                            Routes.REGISTER
                        ) {

                            inclusive =
                                true
                        }

                        launchSingleTop =
                            true
                    }
                },
                onBackToLogin = {

                    navController
                        .popBackStack()
                }
            )
        }

        // =====================================================
        // HOME
        // =====================================================

        composable(
            Routes.HOME
        ) {

            MainScreenLayout(
                navController =
                    navController
            ) {

                HomeScreen(
                    onMateriasClick = {

                        navegarPrincipal(
                            navController,
                            Routes.MATERIAS
                        )
                    },
                    onFlashcardsClick = {

                        navegarPrincipal(
                            navController,
                            Routes.FLASHCARDS
                        )
                    },
                    onQuizClick = {

                        navegarPrincipal(
                            navController,
                            Routes.QUIZ
                        )
                    },
                    onProgresoClick = {

                        navegarPrincipal(
                            navController,
                            Routes.PROGRESO
                        )
                    },
                    onRecomendacionesClick = {

                        navController.navigate(
                            Routes.RECOMENDACIONES
                        )
                    },
                    onLogoutClick = {

                        AuthRepository
                            .cerrarSesion()

                        navController.navigate(
                            Routes.LOGIN
                        ) {

                            popUpTo(
                                Routes.HOME
                            ) {

                                inclusive =
                                    true
                            }

                            launchSingleTop =
                                true
                        }
                    }
                )
            }
        }

        // =====================================================
        // MATERIAS
        // =====================================================

        composable(
            Routes.MATERIAS
        ) {

            MainScreenLayout(
                navController =
                    navController
            ) {

                MateriasScreen(
                    onBack = {

                        navegarPrincipal(
                            navController,
                            Routes.HOME
                        )
                    },
                    onVerTemas = {
                            materia ->

                        navController.navigate(
                            Routes.temas(
                                materia.id
                            )
                        )
                    }
                )
            }
        }

        // =====================================================
        // FLASHCARDS
        // =====================================================

        composable(
            Routes.FLASHCARDS
        ) {

            MainScreenLayout(
                navController =
                    navController
            ) {

                FlashcardsScreen(
                    onBack = {

                        navegarPrincipal(
                            navController,
                            Routes.HOME
                        )
                    }
                )
            }
        }

        // =====================================================
        // QUIZ
        // =====================================================

        composable(
            Routes.QUIZ
        ) {

            MainScreenLayout(
                navController =
                    navController
            ) {

                QuizScreen(
                    onBack = {

                        navegarPrincipal(
                            navController,
                            Routes.HOME
                        )
                    }
                )
            }
        }

        // =====================================================
        // PROGRESO
        // =====================================================

        composable(
            Routes.PROGRESO
        ) {

            MainScreenLayout(
                navController =
                    navController
            ) {

                ProgresoScreen(
                    onBack = {

                        navegarPrincipal(
                            navController,
                            Routes.HOME
                        )
                    }
                )
            }
        }

        // =====================================================
        // RECOMENDACIONES
        // =====================================================

        composable(
            Routes.RECOMENDACIONES
        ) {

            RecomendacionesScreen(
                onBack = {

                    navController
                        .popBackStack()
                }
            )
        }

        // =====================================================
        // TEMAS
        // =====================================================

        composable(
            route =
                Routes.TEMAS
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
                materiaId =
                    materiaId,
                onBack = {

                    navController
                        .popBackStack()
                },
                onVerMateriales = {
                        tema ->

                    navController.navigate(
                        Routes.materiales(
                            tema.id
                        )
                    )
                }
            )
        }

        // =====================================================
        // MATERIALES
        // =====================================================

        composable(
            route =
                Routes.MATERIALES
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
                temaId =
                    temaId,
                onBack = {

                    navController
                        .popBackStack()
                }
            )
        }
    }
}

// =============================================================
// CONTENEDOR DE LAS PANTALLAS PRINCIPALES
// =============================================================

@Composable
private fun MainScreenLayout(
    navController:
    NavHostController,
    content:
    @Composable () -> Unit
) {

    Scaffold(
        modifier =
            Modifier.fillMaxSize(),
        bottomBar = {

            MainBottomBar(
                navController =
                    navController
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    innerPadding
                )
        ) {

            content()
        }
    }
}

// =============================================================
// NAVEGACIÓN ENTRE MÓDULOS PRINCIPALES
// =============================================================

private fun navegarPrincipal(
    navController:
    NavHostController,
    route: String
) {

    navController.navigate(
        route
    ) {

        popUpTo(
            Routes.HOME
        ) {

            saveState =
                true
        }

        launchSingleTop =
            true

        restoreState =
            true
    }
}
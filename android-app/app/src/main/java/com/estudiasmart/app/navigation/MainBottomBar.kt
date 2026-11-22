package com.estudiasmart.app.navigation

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

private data class MainNavigationItem(
    val route: String,
    val icono: String,
    val titulo: String
)

@Composable
fun MainBottomBar(
    navController: NavHostController
) {

    val items =
        listOf(
            MainNavigationItem(
                route =
                    Routes.HOME,
                icono =
                    "⌂",
                titulo =
                    "Inicio"
            ),
            MainNavigationItem(
                route =
                    Routes.MATERIAS,
                icono =
                    "▤",
                titulo =
                    "Materias"
            ),
            MainNavigationItem(
                route =
                    Routes.FLASHCARDS,
                icono =
                    "◫",
                titulo =
                    "Flash"
            ),
            MainNavigationItem(
                route =
                    Routes.QUIZ,
                icono =
                    "✓",
                titulo =
                    "Quiz"
            ),
            MainNavigationItem(
                route =
                    Routes.PROGRESO,
                icono =
                    "▥",
                titulo =
                    "Progreso"
            )
        )

    val backStackEntry by
    navController
        .currentBackStackEntryAsState()

    val currentRoute =
        backStackEntry
            ?.destination
            ?.route

    NavigationBar {

        items.forEach { item ->

            NavigationBarItem(
                selected =
                    currentRoute ==
                            item.route,
                onClick = {

                    if (
                        currentRoute !=
                        item.route
                    ) {

                        navController.navigate(
                            item.route
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
                },
                icon = {

                    Text(
                        text =
                            item.icono
                    )
                },
                label = {

                    Text(
                        text =
                            item.titulo
                    )
                }
            )
        }
    }
}
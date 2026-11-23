package com.estudiasmart.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EstudiaSmartColorScheme =
    lightColorScheme(

        primary =
            EstudiaPrimary,

        onPrimary =
            Color.White,

        primaryContainer =
            Color(0xFFE0E0FF),

        onPrimaryContainer =
            EstudiaPrimaryDark,

        secondary =
            EstudiaPrimaryDark,

        onSecondary =
            Color.White,

        background =
            EstudiaBackground,

        onBackground =
            EstudiaText,

        surface =
            EstudiaSurface,

        onSurface =
            EstudiaText,

        surfaceVariant =
            Color(0xFFF1F2F6),

        onSurfaceVariant =
            EstudiaTextSecondary,

        error =
            EstudiaError,

        onError =
            Color.White
    )

@Composable
fun EstudiaSmartTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme =
            EstudiaSmartColorScheme,
        typography =
            Typography,
        content =
            content
    )
}
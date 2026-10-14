package com.estudiasmart.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.estudiasmart.app.navigation.AppNavigation
import com.estudiasmart.app.ui.theme.EstudiaSmartTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EstudiaSmartTheme {
                AppNavigation()
            }
        }
    }
}
package com.estudiasmart.app.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ConfirmDeleteDialog(
    titulo: String,
    mensaje: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = titulo
            )
        },
        text = {
            Text(
                text = mensaje
            )
        },
        confirmButton = {

            TextButton(
                onClick = onConfirm
            ) {

                Text(
                    text = "Eliminar",
                    color =
                        MaterialTheme
                            .colorScheme
                            .error
                )
            }
        },
        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {

                Text(
                    text = "Cancelar"
                )
            }
        }
    )
}
package util

import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object ErrorLogger {

    private val archivoLog = File("logs/errors.txt")

    fun registrarError(error: Exception) {

        if (!archivoLog.parentFile.exists()) {
            archivoLog.parentFile.mkdirs()
        }

        val fecha = LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        )

        val mensaje = """
            [$fecha]
            Tipo: ${error::class.simpleName}
            Mensaje: ${error.message}
            ----------------------------------------
            
        """.trimIndent()

        archivoLog.appendText(mensaje)
    }
}
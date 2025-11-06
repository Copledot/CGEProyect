package com.example.cgeproyect

import java.io.File
import java.io.FileOutputStream

actual fun getPlatformName(): String = "Desktop (JVM)"

actual fun savePdf(bytes: ByteArray, fileName: String): String {
    return try {
        val file = File(fileName) // Crea un archivo (ej: "boleta-11-1.pdf")
        FileOutputStream(file).use {
            it.write(bytes)
        }
        "PDF guardado con éxito como '${file.absolutePath}'"
    } catch (e: Exception) {
        "Error al guardar PDF: ${e.message}"
    }
}
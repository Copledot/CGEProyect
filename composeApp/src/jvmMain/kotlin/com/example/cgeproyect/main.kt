package com.example.cgeproyect

// Imports para ESCRITORIO
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.cgeproyect.App // Importa tu App compartida

// Punto de entrada para la aplicación de Escritorio
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "CGE Proyect Desktop"
    ) {
        App() // Llama a la UI compartida
    }
}
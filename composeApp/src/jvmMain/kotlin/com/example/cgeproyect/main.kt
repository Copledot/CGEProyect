package com.example.cgeproyect


import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.cgeproyect.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "CGE Proyect Desktop"
    ) {
        App() // Llama a la UI compartida
    }
}

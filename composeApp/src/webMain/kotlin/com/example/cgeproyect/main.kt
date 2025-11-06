package com.example.cgeproyect.web

// Imports para WEB (JS/WASM)
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.example.cgeproyect.App
import org.jetbrains.skiko.wasm.onWasmReady

// Punto de entrada para la aplicación Web
@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    onWasmReady {
        CanvasBasedWindow(canvasElementId = "composeCanvas") {
            App() // Llama a la UI compartida
        }
    }
}

annotation class CanvasBasedWindow(val canvasElementId: Any, val function: Any)

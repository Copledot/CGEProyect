package com.example.cgeproyect

expect fun getPlatformName(): String

// --- AÑADE ESTA FUNCIÓN ---
// Pide a la plataforma que guarde un archivo
expect fun savePdf(bytes: ByteArray, fileName: String): String
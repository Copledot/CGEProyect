package com.example.cgeproyect

expect fun getPlatformName(): String


// Pide a la plataforma que guarde un archivo
expect fun savePdf(bytes: ByteArray, fileName: String): String
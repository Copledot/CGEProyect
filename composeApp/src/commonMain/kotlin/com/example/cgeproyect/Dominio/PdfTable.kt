package com.example.cgeproyect.Dominio

// Clase de datos para estructurar la tabla del PDF [cite: 117]
data class PdfTable(
    val headers: List<String>,
    val rows: List<List<String>>
)
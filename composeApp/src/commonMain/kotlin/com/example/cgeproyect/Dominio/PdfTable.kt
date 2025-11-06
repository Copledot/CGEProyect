package com.example.cgeproyect.Dominio

// Clase de datos para estructurar la tabla del PDF
data class PdfTable(
    val headers: List<String>,
    val rows: List<List<String>>
)
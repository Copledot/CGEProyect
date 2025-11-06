package com.example.cgeproyect.Dominio

// Interfaz para objetos que pueden ser exportados a PDF
interface ExportablePDF {
    fun toPdfTable(): PdfTable
}
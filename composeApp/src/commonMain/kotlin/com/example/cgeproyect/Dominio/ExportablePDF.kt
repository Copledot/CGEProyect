package com.example.cgeproyect.Dominio

// Interfaz para objetos que pueden ser exportados [cite: 156]
interface ExportablePDF {
    fun toPdfTable(): PdfTable // [cite: 157]
}
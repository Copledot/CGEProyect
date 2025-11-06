package com.example.cgeproyect.servicios

import com.example.cgeproyect.Dominio.Boleta
import com.example.cgeproyect.Dominio.Cliente

// Servicio para generar PDFs
expect class PdfService() {
    fun generarBoletaPDF(boleta: Boleta, cliente: Cliente, historial: Map<Int, Double>, medidorCodigo: String): ByteArray
}
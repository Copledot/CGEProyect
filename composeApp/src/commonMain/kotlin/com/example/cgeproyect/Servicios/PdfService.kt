package com.example.cgeproyect.servicios

import com.example.cgeproyect.Dominio.Boleta
import com.example.cgeproyect.Dominio.Cliente

// Clase 'expect' (la definición compartida)
expect class PdfService() {
    // AHORA ACEPTA UN NUEVO PARÁMETRO: medidorCodigo
    fun generarBoletaPDF(boleta: Boleta, cliente: Cliente, historial: Map<Int, Double>, medidorCodigo: String): ByteArray
}
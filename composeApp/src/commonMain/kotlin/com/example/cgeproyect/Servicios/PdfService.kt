package com.example.cgeproyect.servicios // <-- Asegúrate que esté en minúscula

import com.example.cgeproyect.Dominio.Boleta
import com.example.cgeproyect.Dominio.Cliente

expect class PdfService() {
    fun generarBoletaPDF(boleta: Boleta, cliente: Cliente): ByteArray
}
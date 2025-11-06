package com.example.cgeproyect.servicios

import com.example.cgeproyect.Dominio.Boleta
import com.example.cgeproyect.Dominio.Cliente

// Implementacion 'actual' para JS (Web)
actual class PdfService {
    actual fun generarBoletaPDF(boleta: Boleta, cliente: Cliente, historial: Map<Int, Double>, medidorCodigo: String): ByteArray {
        val tabla = boleta.toPdfTable()
        val stringBuilder = StringBuilder()
        stringBuilder.append("--- BOLETA CGE ELECTRICIDAD ---\n")
        stringBuilder.append("Cliente: ${cliente.nombre}\n")
        stringBuilder.append("---------------------------------\n")

        tabla.headers.forEach { header -> stringBuilder.append("$header\t\t") }
        stringBuilder.append("\n")

        tabla.rows.forEach { row ->
            row.forEach { cell -> stringBuilder.append("$cell\t\t") }
            stringBuilder.append("\n")
        }
        stringBuilder.append("---------------------------------\n")

        return stringBuilder.toString().encodeToByteArray()
    }
}
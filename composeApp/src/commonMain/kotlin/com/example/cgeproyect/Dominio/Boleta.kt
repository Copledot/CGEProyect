package com.example.cgeproyect.Dominio

import com.example.cgeproyect.dominio.EstadoBoleta

// Clase Boleta que implementa la interfaz
data class Boleta(
    val rut: String,
    val anio: Int,
    val mes: Int,
    val kwhTotal: Double,
    val detalle: TarifaDetalle,
    var estado: EstadoBoleta = EstadoBoleta.EMITIDA
) : EntidadBase(), ExportablePDF {

    // Implementacion de la interfaz para generar tabla PDF
    override fun toPdfTable(): PdfTable {
        val headers = listOf("Concepto", "Valor")
        val rows = listOf(
            listOf("RUT Cliente", rut),
            listOf("Mes/Año", "$mes/$anio"),
            listOf("Consumo (kWh)", kwhTotal.toString()),
            listOf("Subtotal", "$${detalle.subtotal}"),
            listOf("Cargos", "$${detalle.cargos}"),
            listOf("IVA (19%)", "$${detalle.iva}"),
            listOf("Total a Pagar", "$${detalle.total}")
        )
        return PdfTable(headers, rows)
    }
}
package com.example.cgeproyect.Dominio

// Implementación de la tarifa comercial
class TarifaComercial : Tarifa {
    private val cargoFijo: Double = 5000.0
    private val precioKwh: Double = 110.0
    private val recargoComercial: Double = 0.05 // 5% recargo
    private val iva: Double = 0.19

    override fun nombre(): String = "Tarifa Comercial"
    // Cálculo del detalle de la tarifa comercial
    override fun calcular(kwh: Double): TarifaDetalle {
        val subtotal = kwh * precioKwh
        val recargo = (subtotal + cargoFijo) * recargoComercial
        val cargos = cargoFijo + recargo
        val ivaCalculado = (subtotal + cargos) * iva
        val total = subtotal + cargos + ivaCalculado
        return TarifaDetalle(kwh, subtotal, cargos, ivaCalculado, total)
    }
}
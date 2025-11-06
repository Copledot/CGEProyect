package com.example.cgeproyect.Dominio

// Implementación de Tarifa [cite: 131]
class TarifaComercial : Tarifa {
    private val cargoFijo: Double = 5000.0
    private val precioKwh: Double = 110.0
    private val recargoComercial: Double = 0.05 // 5% recargo
    private val iva: Double = 0.19

    override fun nombre(): String = "Tarifa Comercial"

    // Cálculo polimórfico [cite: 37]
    override fun calcular(kwh: Double): TarifaDetalle {
        val subtotal = kwh * precioKwh
        val recargo = (subtotal + cargoFijo) * recargoComercial
        val cargos = cargoFijo + recargo
        val ivaCalculado = (subtotal + cargos) * iva
        val total = subtotal + cargos + ivaCalculado
        return TarifaDetalle(kwh, subtotal, cargos, ivaCalculado, total)
    }
}
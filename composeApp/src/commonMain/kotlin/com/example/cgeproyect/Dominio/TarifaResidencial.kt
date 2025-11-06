package com.example.cgeproyect.Dominio

// Implementación de Tarifa [cite: 129]
class TarifaResidencial : Tarifa {
    private val cargoFijo: Double = 1500.0
    private val precioKwh: Double = 120.0
    private val iva: Double = 0.19

    override fun nombre(): String = "Tarifa Residencial"

    override fun calcular(kwh: Double): TarifaDetalle {
        val subtotal = kwh * precioKwh
        val cargos = cargoFijo
        val ivaCalculado = (subtotal + cargos) * iva
        val total = subtotal + cargos + ivaCalculado
        return TarifaDetalle(kwh, subtotal, cargos, ivaCalculado, total)
    }
}
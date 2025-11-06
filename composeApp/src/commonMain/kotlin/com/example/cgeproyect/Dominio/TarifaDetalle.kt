package com.example.cgeproyect.Dominio

// resultados del calculo
data class TarifaDetalle(
    val kwh: Double,
    val subtotal: Double,
    val cargos: Double,
    val iva: Double,
    val total: Double // [cite: 155]
)
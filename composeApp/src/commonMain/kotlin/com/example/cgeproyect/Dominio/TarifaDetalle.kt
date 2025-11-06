package com.example.cgeproyect.Dominio

// Clase de datos para los resultados del cálculo [cite: 153]
data class TarifaDetalle(
    val kwh: Double,
    val subtotal: Double,
    val cargos: Double,
    val iva: Double,
    val total: Double // [cite: 155]
)
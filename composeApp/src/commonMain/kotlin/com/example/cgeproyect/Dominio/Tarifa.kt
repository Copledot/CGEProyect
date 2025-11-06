package com.example.cgeproyect.Dominio

// Interfaz Tarifa, aplica Polimorfismo [cite: 105, 54]
interface Tarifa {
    fun nombre(): String // [cite: 106]
    fun calcular(kwh: Double): TarifaDetalle // [cite: 107]
}
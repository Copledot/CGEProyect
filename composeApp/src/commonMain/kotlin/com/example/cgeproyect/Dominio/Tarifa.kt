package com.example.cgeproyect.Dominio

// Interfaz para las tarifas de electricidad
interface Tarifa {
    fun nombre(): String
    fun calcular(kwh: Double): TarifaDetalle
}
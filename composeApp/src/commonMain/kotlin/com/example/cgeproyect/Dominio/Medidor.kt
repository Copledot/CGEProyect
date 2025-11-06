package com.example.cgeproyect.Dominio

// Clase abstracta Medidor, aplica Herencia [cite: 136, 54]
abstract class Medidor : EntidadBase() {
    abstract val codigo: String
    abstract val direccionSuministro: String
    var activo: Boolean = true

    abstract fun tipo(): String
}
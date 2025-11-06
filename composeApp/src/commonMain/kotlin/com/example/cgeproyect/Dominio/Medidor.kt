package com.example.cgeproyect.Dominio

// Clase abstracta que representa un medidor de consumo
abstract class Medidor : EntidadBase() {
    abstract val codigo: String
    abstract val direccionSuministro: String
    var activo: Boolean = true

    abstract fun tipo(): String
}
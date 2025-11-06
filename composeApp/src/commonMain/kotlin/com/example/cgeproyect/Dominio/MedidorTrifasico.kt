package com.example.cgeproyect.Dominio

// Subclase de Medidor trifasico
data class MedidorTrifasico(
    override val codigo: String,
    override val direccionSuministro: String,
    val potenciaMaxKw: Double,
    val factorPotencia: Double
) : Medidor() {
    override fun tipo(): String = "Trifásico"
}
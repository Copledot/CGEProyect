package com.example.cgeproyect.Dominio

// Subclase de Medidor [cite: 150]
data class MedidorMonofasico(
    override val codigo: String,
    override val direccionSuministro: String,
    val potenciaMaxKw: Double
) : Medidor() {
    override fun tipo(): String = "Monofásico" // Implementación [cite: 151]
}
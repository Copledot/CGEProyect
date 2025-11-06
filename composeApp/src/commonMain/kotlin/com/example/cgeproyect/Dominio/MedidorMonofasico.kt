package com.example.cgeproyect.Dominio

// subclase que representa un medidor monofásico
data class MedidorMonofasico(
    override val codigo: String,
    override val direccionSuministro: String,
    val potenciaMaxKw: Double
) : Medidor() {
    override fun tipo(): String = "Monofásico"
}
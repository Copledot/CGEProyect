package com.example.cgeproyect.Dominio

// Clase para registrar lecturas mensuales [cite: 146]
data class LecturaConsumo(
    val idMedidor: String, // Se vincula por el 'codigo' del medidor
    val anio: Int,
    val mes: Int,
    val kwhLeidos: Double
) : EntidadBase()
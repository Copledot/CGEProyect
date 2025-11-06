package com.example.cgeproyect.Dominio

// Representa una lectura de consumo de un medidor
data class LecturaConsumo(
    val idMedidor: String, // Se vincula por el 'codigo' del medidor
    val anio: Int,
    val mes: Int,
    val kwhLeidos: Double
) : EntidadBase()
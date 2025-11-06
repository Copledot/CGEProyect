package com.example.cgeproyect.Dominio

// Hereda de la EntidadBase (ahora corregida)
abstract class Persona : EntidadBase() {
    abstract val rut: String
    abstract val nombre: String
    abstract val email: String
}
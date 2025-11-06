package com.example.cgeproyect.Dominio

// Clase abstracta que representa una persona
abstract class Persona : EntidadBase() {
    abstract val rut: String
    abstract val nombre: String
    abstract val email: String
}
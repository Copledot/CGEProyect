package com.example.cgeproyect.Dominio

// Clase Operador que hereda de Persona [cite: 127]
data class Operador(
    override val rut: String,
    override val nombre: String,
    override val email: String,
    val perfil: String
) : Persona()
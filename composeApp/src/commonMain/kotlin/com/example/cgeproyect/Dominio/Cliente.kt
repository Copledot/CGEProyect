package com.example.cgeproyect.Dominio

data class Cliente(
    override val rut: String,
    override val nombre: String,
    override val email: String,
    val direccionFacturacion: String,
    var estado: EstadoCliente = EstadoCliente.ACTIVO,
    val tipoTarifa: String = "Residencial" // <-- AÑADE ESTA LÍNEA
) : Persona()
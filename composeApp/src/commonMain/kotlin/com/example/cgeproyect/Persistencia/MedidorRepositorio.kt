package com.example.cgeproyect.Persistencia

import com.example.cgeproyect.Dominio.Medidor

// Esta es la interfaz (el "contrato")
interface MedidorRepositorio {
    // ESTAS DOS FUNCIONES FALTABAN O ESTABAN MAL ESCRITAS
    fun addMedidor(medidor: Medidor, rutCliente: String)
    fun findMedidorByCliente(rutCliente: String): Medidor?
}
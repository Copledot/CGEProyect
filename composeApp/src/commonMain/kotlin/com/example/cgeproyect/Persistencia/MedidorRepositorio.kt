package com.example.cgeproyect.Persistencia

import com.example.cgeproyect.Dominio.Medidor

// Repositorio para gestionar Medidores
interface MedidorRepositorio {
    fun addMedidor(medidor: Medidor, rutCliente: String)
    fun findMedidorByCliente(rutCliente: String): Medidor?
}
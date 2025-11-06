package com.example.cgeproyect.Persistencia

import com.example.cgeproyect.Dominio.Medidor

interface MedidorRepositorio {
    fun addMedidor(medidor: Medidor, rutCliente: String)
    fun findMedidorByCliente(rutCliente: String): Medidor?
}
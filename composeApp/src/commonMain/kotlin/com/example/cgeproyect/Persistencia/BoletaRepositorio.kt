package com.example.cgeproyect.Persistencia

import com.example.cgeproyect.Dominio.Boleta

// Repositorio para gestionar las boletas
interface BoletaRepositorio {
    fun addBoleta(boleta: Boleta)
    fun findBoleta(rutCliente: String, anio: Int, mes: Int): Boleta?
}
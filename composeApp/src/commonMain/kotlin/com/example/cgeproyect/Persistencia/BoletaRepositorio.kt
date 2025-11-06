package com.example.cgeproyect.Persistencia

import com.example.cgeproyect.Dominio.Boleta

interface BoletaRepositorio {
    fun addBoleta(boleta: Boleta)
    fun findBoleta(rutCliente: String, anio: Int, mes: Int): Boleta?
}
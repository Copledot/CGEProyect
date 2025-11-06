package com.example.cgeproyect.servicios

import com.example.cgeproyect.Dominio.Boleta
import com.example.cgeproyect.Dominio.Cliente
import com.example.cgeproyect.Dominio.LecturaConsumo
import com.example.cgeproyect.Dominio.Medidor
import com.example.cgeproyect.dominio.*

interface ClienteRepositorio {
    fun findById(rut: String): Cliente?
    fun getAll(): Map<String, Cliente>
    fun save(cliente: Cliente): Cliente
}

interface MedidorRepositorio {
    fun findByClienteId(idCliente: String): List<Medidor>
    fun save(medidor: Medidor): Medidor
}

interface LecturaRepositorio {
    fun findByMedidorIdAndPeriodo(idMedidor: String, anio: Int, mes: Int): LecturaConsumo?
    fun save(lectura: LecturaConsumo): LecturaConsumo
}

interface BoletaRepositorio {
    fun save(boleta: Boleta): Boleta
    fun findByClienteAndPeriodo(rutCliente: String, anio: Int, mes: Int): Boleta?
    fun findByCliente(rutCliente: String): List<Boleta>
}
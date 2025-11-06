package com.example.cgeproyect.Persistencia

import com.example.cgeproyect.Dominio.Cliente

interface ClienteRepositorio {
    fun addCliente(cliente: Cliente)
    fun findClienteByRut(rut: String): Cliente?
    fun getAllClientes(): List<Cliente>
}
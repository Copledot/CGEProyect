package com.example.cgeproyect.Servicios

import com.example.cgeproyect.Dominio.*

// Servicio para determinar la tarifa
class TarifaService {
    // Determina la tarifa para un cliente
    fun tarifaParaCliente(cliente: Cliente): Tarifa {
        // --- LÓGICA ACTUALIZADA ---
        // Ahora lee la propiedad guardada en el cliente
        return if (cliente.tipoTarifa.equals("Comercial", ignoreCase = true)) {
            TarifaComercial()
        } else {
            TarifaResidencial()
        }
    }
}
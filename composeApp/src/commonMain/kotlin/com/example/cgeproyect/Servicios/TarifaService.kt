package com.example.cgeproyect.servicios

// --- IMPORTACIONES DEL DOMINIO ---
import com.example.cgeproyect.Dominio.Cliente
import com.example.cgeproyect.Dominio.Tarifa
import com.example.cgeproyect.Dominio.TarifaComercial
import com.example.cgeproyect.Dominio.TarifaResidencial

// Esta es la clase que no se está encontrando
class TarifaService {

    // Esta es la función que no se está encontrando
    fun tarifaParaCliente(cliente: Cliente): Tarifa {
        // Lee la propiedad guardada en el cliente
        return if (cliente.tipoTarifa.equals("Comercial", ignoreCase = true)) {
            TarifaComercial()
        } else {
            TarifaResidencial()
        }
    }
}
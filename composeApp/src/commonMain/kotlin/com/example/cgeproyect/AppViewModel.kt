package com.example.cgeproyect

import com.example.cgeproyect.Dominio.*
import com.example.cgeproyect.Persistencia.*
import com.example.cgeproyect.servicios.BoletaService
import com.example.cgeproyect.servicios.PdfService
import com.example.cgeproyect.servicios.TarifaService // <-- IMPORT FALTANTE

object AppViewModel {

    private val storageDriver = StorageDriver()

    val tarifaService = TarifaService() // <-- Error era aquí
    val pdfService = PdfService()

    val boletaService = BoletaService(
        clienteRepo = storageDriver,
        medidorRepo = storageDriver, // <-- Error era aquí
        lecturaRepo = storageDriver,
        boletaRepo = storageDriver,
        tarifaService = tarifaService
    )

    val clienteRepo: ClienteRepositorio = storageDriver
    val lecturaRepo: LecturaRepositorio = storageDriver
    val medidorRepo: MedidorRepositorio = storageDriver // <-- Error era aquí

    // ... (El resto del archivo init{} no cambia) ...
    init {
        val cliente1 = Cliente("11-1", "Juan Perez", "juan@mail.com", "Av. Siempre Viva 123", tipoTarifa = "Residencial")
        val cliente2 = Cliente("22-2", "Ana Lopez", "ana@mail.com", "Oficina Comercial 456", tipoTarifa = "Comercial")
        storageDriver.addCliente(cliente1)
        storageDriver.addCliente(cliente2)

        val medidor1 = MedidorMonofasico("MONO-001", cliente1.direccionFacturacion, 15.0)
        val medidor2 = MedidorTrifasico("TRI-002", cliente2.direccionFacturacion, 50.0, 0.95)
        storageDriver.addMedidor(medidor1, cliente1.rut)
        storageDriver.addMedidor(medidor2, cliente2.rut)

        storageDriver.addLectura(LecturaConsumo("MONO-001", 2025, 9, 1500.0))
        storageDriver.addLectura(LecturaConsumo("MONO-001", 2025, 10, 1650.0))
        storageDriver.addLectura(LecturaConsumo("TRI-002", 2025, 9, 8000.0))
        storageDriver.addLectura(LecturaConsumo("TRI-002", 2025, 10, 8500.0))
    }
}
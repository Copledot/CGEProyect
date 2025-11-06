package com.example.cgeproyect

import com.example.cgeproyect.Dominio.*
import com.example.cgeproyect.Persistencia.*
import com.example.cgeproyect.Servicios.*
import com.example.cgeproyect.servicios.PdfService

// ViewModel/Singleton para mantener el estado y la lógica
object AppViewModel {

    // 1. Instanciamos el StorageDriver CONCRETO
    private val storageDriver = StorageDriver()

    // 2. Instanciamos los servicios, pasando el StorageDriver
    //    (ya que implementa todas las interfaces de repo)
    val tarifaService = TarifaService()
    val pdfService = PdfService()
    val boletaService = BoletaService(
        clienteRepo = storageDriver, //
        medidorRepo = storageDriver, //
        lecturaRepo = storageDriver, //
        boletaRepo = storageDriver,  //
        tarifaService = tarifaService
    )

    // 3. Exponemos el repositorio de clientes para la UI
    //    (o podríamos exponer el storageDriver completo si fuera necesario)
    val clienteRepo: ClienteRepositorio = storageDriver
    val lecturaRepo: LecturaRepositorio = storageDriver
    val medidorRepo: MedidorRepositorio = storageDriver

    // Datos de ejemplo
    init {
        val cliente1 = Cliente("11-1", "Juan Perez", "juan@mail.com", "Av. Siempre Viva 123")
        val cliente2 = Cliente("22-2", "Ana Lopez", "ana@mail.com", "Oficina Comercial 456")
        storageDriver.addCliente(cliente1)
        storageDriver.addCliente(cliente2)

        val medidor1 = MedidorMonofasico("MONO-001", cliente1.direccionFacturacion, 15.0)
        val medidor2 = MedidorTrifasico("TRI-002", cliente2.direccionFacturacion, 50.0, 0.95)
        storageDriver.addMedidor(medidor1, cliente1.rut)
        storageDriver.addMedidor(medidor2, cliente2.rut)

        // Lecturas de ejemplo
        storageDriver.addLectura(LecturaConsumo("MONO-001", 2025, 9, 1500.0))
        storageDriver.addLectura(LecturaConsumo("MONO-001", 2025, 10, 1650.0)) // 150 kWh
        storageDriver.addLectura(LecturaConsumo("TRI-002", 2025, 9, 8000.0))
        storageDriver.addLectura(LecturaConsumo("TRI-002", 2025, 10, 8500.0)) // 500 kWh
    }
}
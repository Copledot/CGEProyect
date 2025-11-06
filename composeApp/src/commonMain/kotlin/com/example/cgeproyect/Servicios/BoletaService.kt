package com.example.cgeproyect.servicios

// --- IMPORTACIONES DEL DOMINIO ---
import com.example.cgeproyect.Dominio.Boleta
import com.example.cgeproyect.Dominio.*

// --- IMPORTACIONES DE PERSISTENCIA (Estas faltaban) ---

import com.example.cgeproyect.Persistencia.BoletaRepositorio
import com.example.cgeproyect.Persistencia.ClienteRepositorio
import com.example.cgeproyect.Persistencia.LecturaRepositorio
import com.example.cgeproyect.Persistencia.MedidorRepositorio

// --- IMPORTACIONES DE SERVICIOS (Estas faltaban) ---

import com.example.cgeproyect.servicios.PdfService
import com.example.cgeproyect.servicios.TarifaService

class BoletaService(
    private val clienteRepo: ClienteRepositorio,
    private val medidorRepo: MedidorRepositorio,
    private val lecturaRepo: LecturaRepositorio,
    private val boletaRepo: BoletaRepositorio,
    private val tarifaService: TarifaService
) {

    // Calcula el consumo en kWh del mes
    fun calcularKwhClienteMes(rutCliente: String, anio: Int, mes: Int): Double {
        val medidor = medidorRepo.findMedidorByCliente(rutCliente)
            ?: throw Exception("Cliente sin medidor")

        val (lecturaActual, lecturaAnterior) = lecturaRepo.findLecturasMes(medidor.codigo, anio, mes)

        if (lecturaActual == null) throw Exception("No hay lectura para $mes/$anio")

        return lecturaActual.kwhLeidos - (lecturaAnterior?.kwhLeidos ?: 0.0)
    }

    // Emite la boleta mensual
    fun emitirBoletaMensual(rutCliente: String, anio: Int, mes: Int): Boleta {
        val cliente = clienteRepo.findClienteByRut(rutCliente)
            ?: throw Exception("Cliente no encontrado")

        val kwhConsumidos = calcularKwhClienteMes(rutCliente, anio, mes)

        val tarifaCliente = tarifaService.tarifaParaCliente(cliente)
        val detalleCalculado = tarifaCliente.calcular(kwhConsumidos)

        val boleta = Boleta(
            rut = rutCliente,
            anio = anio,
            mes = mes,
            kwhTotal = kwhConsumidos,
            detalle = detalleCalculado
        )

        boletaRepo.addBoleta(boleta)
        return boleta
    }

    // Exporta la boleta a PDF
    fun exportarPdfClienteMes(rutCliente: String, anio: Int, mes: Int, pdfService: PdfService): ByteArray {
        val boleta = boletaRepo.findBoleta(rutCliente, anio, mes)
            ?: throw Exception("Boleta no encontrada")
        val cliente = clienteRepo.findClienteByRut(rutCliente)
            ?: throw Exception("Cliente no encontrado")

        val medidor = medidorRepo.findMedidorByCliente(cliente.rut)
        val medidorCodigo = medidor?.codigo ?: "N/A"

        val historialConsumo = mutableMapOf<Int, Double>()
        val anioParaHistorial = boleta.anio

        for (mesHistorial in 1..12) {
            try {
                val consumoMes = calcularKwhClienteMes(rutCliente, anioParaHistorial, mesHistorial)
                historialConsumo[mesHistorial] = consumoMes
            } catch (e: Exception) {
                historialConsumo[mesHistorial] = 0.0
            }
        }

        return pdfService.generarBoletaPDF(boleta, cliente, historialConsumo, medidorCodigo)
    }
}
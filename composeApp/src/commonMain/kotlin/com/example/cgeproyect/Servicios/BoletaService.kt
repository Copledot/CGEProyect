package com.example.cgeproyect.Servicios

import com.example.cgeproyect.Dominio.Boleta
import com.example.cgeproyect.Persistencia.*
import com.example.cgeproyect.Dominio.*
import com.example.cgeproyect.servicios.PdfService
class BoletaService(
    private val clienteRepo: ClienteRepositorio,
    private val medidorRepo: MedidorRepositorio,
    private val lecturaRepo: LecturaRepositorio,
    private val boletaRepo: BoletaRepositorio,
    private val tarifaService: TarifaService
) {
    // ... (El resto de las funciones de BoletaService que te di
    //      (calcularKwhClienteMes, emitirBoletaMensual, exportarPdfClienteMes)
    //      deberían funcionar sin cambios, ya que usan los métodos de la interfaz)

    // ... (incluyo las funciones aquí para que esté completo)

    fun calcularKwhClienteMes(rutCliente: String, anio: Int, mes: Int): Double {
        val medidor = medidorRepo.findMedidorByCliente(rutCliente)
            ?: throw Exception("Cliente sin medidor")

        val (lecturaActual, lecturaAnterior) = lecturaRepo.findLecturasMes(medidor.codigo, anio, mes)

        if (lecturaActual == null) throw Exception("No hay lectura para $mes/$anio")

        return lecturaActual.kwhLeidos - (lecturaAnterior?.kwhLeidos ?: 0.0)
    }

    fun emitirBoletaMensual(rutCliente: String, anio: Int, mes: Int): Boleta {
        val cliente = clienteRepo.findClienteByRut(rutCliente)
            ?: throw Exception("Cliente no encontrado")

        val kwhConsumidos = calcularKwhClienteMes(rutCliente, anio, mes)

        val tarifaCliente = tarifaService.tarifaParaCliente(cliente)
        val detalleCalculado = tarifaCliente.calcular(kwhConsumidos)

        val boleta = Boleta(
            idCliente = rutCliente,
            anio = anio,
            mes = mes,
            kwhTotal = kwhConsumidos,
            detalle = detalleCalculado
        )

        boletaRepo.addBoleta(boleta)
        return boleta
    }

    fun exportarPdfClienteMes(rutCliente: String, anio: Int, mes: Int, pdfService: PdfService): ByteArray {
        val boleta = boletaRepo.findBoleta(rutCliente, anio, mes)
            ?: throw Exception("Boleta no encontrada")
        val cliente = clienteRepo.findClienteByRut(rutCliente)
            ?: throw Exception("Cliente no encontrado")

        return pdfService.generarBoletaPDF(boleta, cliente)
    }
}
package com.example.cgeproyect.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cgeproyect.Dominio.Boleta
import com.example.cgeproyect.servicios.BoletaService
import com.example.cgeproyect.AppViewModel
import com.example.cgeproyect.savePdf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoletaScreen(boletaService: BoletaService) {
    // --- Variables de estado ---
    var rutCliente by remember { mutableStateOf("11-1") }
    var anio by remember { mutableStateOf("2025") }
    var mes by remember { mutableStateOf("10") }
    var mensaje by remember { mutableStateOf("") }
    var boletaGenerada by remember { mutableStateOf<Boleta?>(null) }

    val mesInt = mes.toIntOrNull()
    val isMonthError = mes.isEmpty() || mesInt == null || mesInt !in 1..12

    // Box para centrar el contenido
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.widthIn(max = 600.dp), // Máximo 600.dp de ancho
            verticalArrangement = Arrangement.spacedBy(16.dp) // Espacio de 16.dp
        ) {
            Text("Emitir Boletas Mensuales", style = MaterialTheme.typography.headlineMedium)

            OutlinedTextField(rutCliente, { rutCliente = it }, label = { Text("RUT Cliente") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(anio, { anio = it }, label = { Text("Año (Ej: 2025)") }, modifier = Modifier.fillMaxWidth())

            // --- Campo de Mes ---
            OutlinedTextField(
                value = mes,
                onValueChange = { newValue ->
                    if (newValue.isEmpty()) {
                        mes = ""
                    } else if (newValue.all { it.isDigit() } && newValue.length <= 2) {
                        val intValue = newValue.toIntOrNull()
                        if (intValue == null) {
                            if (newValue == "0") mes = "0"
                        } else if (intValue in 0..12) {
                            mes = newValue
                        }
                    }
                },
                label = { Text("Mes (1-12)") },
                isError = isMonthError,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            if (isMonthError) {
                Text(
                    "El mes debe ser un número entre 1 y 12",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall
                )
            }


            Button(
                onClick = {
                    try {
                        boletaGenerada = null
                        val boleta = boletaService.emitirBoletaMensual(rutCliente, anio.toInt(), mes.toInt())
                        boletaGenerada = boleta
                        mensaje = "Boleta generada con éxito. Ya puedes exportar."
                    } catch (e: Exception) {
                        mensaje = "Error al emitir: ${e.message}"
                        println(e)
                    }
                },
                enabled = !isMonthError,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Emitir Boleta")
            }

            Text(mensaje, color = MaterialTheme.colorScheme.primary)

            boletaGenerada?.let { boleta ->
                Card(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), shape = MaterialTheme.shapes.large) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { // Espacio en la Card
                        Text("Detalle Boleta ${boleta.mes}/${boleta.anio}", style = MaterialTheme.typography.titleMedium)
                        Text("Cliente RUT: ${boleta.rut}")
                        Text("Consumo: ${boleta.kwhTotal} kWh")
                        Divider()
                        Text("Subtotal: $${boleta.detalle.subtotal}")
                        Text("Cargos: $${boleta.detalle.cargos}")
                        Text("IVA: $${boleta.detalle.iva}")
                        Text("TOTAL: $${boleta.detalle.total}", style = MaterialTheme.typography.titleLarge)

                        Button(
                            onClick = {
                                try {
                                    val bytes = boletaService.exportarPdfClienteMes(
                                        boleta.rut, boleta.anio, boleta.mes,
                                        AppViewModel.pdfService
                                    )
                                    val fileName = "boleta-${boleta.rut}-${boleta.mes}-${boleta.anio}.pdf"
                                    mensaje = savePdf(bytes, fileName)
                                } catch (e: Exception) {
                                    mensaje = "Error al exportar: ${e.message}"
                                }
                            },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text("Exportar a PDF")
                        }
                    }
                }
            }
        }
    }
}
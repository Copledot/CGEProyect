package com.example.cgeproyect.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cgeproyect.Dominio.Boleta
import com.example.cgeproyect.Servicios.BoletaService
import com.example.cgeproyect.AppViewModel
import com.example.cgeproyect.savePdf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoletaScreen(boletaService: BoletaService) {
    var rutCliente by remember { mutableStateOf("11-1") }
    var anio by remember { mutableStateOf("2025") }
    var mes by remember { mutableStateOf("10") } // Estado para el mes
    var mensaje by remember { mutableStateOf("") }
    var boletaGenerada by remember { mutableStateOf<Boleta?>(null) }

    // --- Lógica de validación para el mes ---
    val mesInt = mes.toIntOrNull()
    val isMonthError = mes.isEmpty() || mesInt == null || mesInt !in 1..12

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Emitir Boletas Mensuales", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(rutCliente, { rutCliente = it }, label = { Text("RUT Cliente") })
        OutlinedTextField(anio, { anio = it }, label = { Text("Año (Ej: 2025)") })

        // --- Campo de Mes Modificado ---
        OutlinedTextField(
            value = mes,
            onValueChange = { newValue ->
                if (newValue.isEmpty()) {
                    mes = "" // 1. Permite borrar
                } else if (newValue.all { it.isDigit() } && newValue.length <= 2) {
                    // 2. Solo permite 2 dígitos numéricos
                    val intValue = newValue.toIntOrNull()
                    if (intValue == null) {
                        if (newValue == "0") mes = "0" // Permite escribir "0" (para "01")
                    } else if (intValue in 0..12) { // 3. Permite números entre 0 y 12
                        mes = newValue
                    }
                }
            },
            label = { Text("Mes (1-12)") },
            isError = isMonthError, // 4. Muestra el error
            singleLine = true
        )
        if (isMonthError) {
            Text(
                "El mes debe ser un número entre 1 y 12",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall
            )
        }
        // --- Fin del Campo de Mes ---

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
            enabled = !isMonthError // 5. El botón se desactiva si hay error
        ) {
            Text("Emitir Boleta")
        }

        Text(mensaje, color = MaterialTheme.colorScheme.primary)

        boletaGenerada?.let { boleta ->
            // ... (El resto de la Card para mostrar la boleta) ...
            Card(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Text("Detalle Boleta ${boleta.mes}/${boleta.anio}", style = MaterialTheme.typography.titleMedium)
                    Text("Cliente RUT: ${boleta.idCliente}")
                    Text("Consumo: ${boleta.kwhTotal} kWh")
                    Divider(Modifier.padding(vertical = 8.dp))
                    Text("Subtotal: $${boleta.detalle.subtotal}")
                    Text("Cargos: $${boleta.detalle.cargos}")
                    Text("IVA: $${boleta.detalle.iva}")
                    Text("TOTAL: $${boleta.detalle.total}", style = MaterialTheme.typography.titleLarge)

                    Button(
                        onClick = {
                            try {
                                val bytes = boletaService.exportarPdfClienteMes(
                                    boleta.idCliente, boleta.anio, boleta.mes,
                                    AppViewModel.pdfService
                                )
                                val fileName = "boleta-${boleta.idCliente}-${boleta.mes}-${boleta.anio}.pdf"
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
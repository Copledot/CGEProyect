package com.example.cgeproyect.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cgeproyect.Dominio.LecturaConsumo
import com.example.cgeproyect.Persistencia.LecturaRepositorio
import com.example.cgeproyect.Persistencia.MedidorRepositorio

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LecturaScreen(lecturaRepo: LecturaRepositorio, medidorRepo: MedidorRepositorio) {
    // --- Variables de estado ---
    var rutCliente by remember { mutableStateOf("") }
    var anio by remember { mutableStateOf("2025") }
    var mes by remember { mutableStateOf("10") }
    var kwh by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    val mesInt = mes.toIntOrNull()
    val isMonthError = mes.isEmpty() || mesInt == null || mesInt !in 1..12

    // Box para centrar el contenido
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.widthIn(max = 600.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp), //
        ) {
            Text("Registrar Lectura Mensual", style = MaterialTheme.typography.headlineMedium)

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


            OutlinedTextField(kwh, { kwh = it }, label = { Text("kWh Leídos (Ej: 1800.0)") }, modifier = Modifier.fillMaxWidth())

            Button(
                onClick = {
                    try {
                        val medidor = medidorRepo.findMedidorByCliente(rutCliente)
                        if (medidor == null) {
                            mensaje = "Error: Cliente no tiene medidor."
                        } else {
                            // Crear la lectura y guardarla
                            val lectura = LecturaConsumo(
                                idMedidor = medidor.codigo,
                                anio = anio.toInt(),
                                mes = mes.toInt(),
                                kwhLeidos = kwh.toDouble()
                            )


                            lecturaRepo.addLectura(lectura)
                            mensaje = "Lectura registrada con éxito para ${medidor.codigo}"
                        }
                    } catch (e: Exception) {
                        mensaje = "Error: ${e.message}"
                    }
                },
                enabled = !isMonthError,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar Lectura")
            }

            Text(mensaje, color = MaterialTheme.colorScheme.primary)
        }
    }
}
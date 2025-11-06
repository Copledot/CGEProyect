package com.example.cgeproyect.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cgeproyect.Dominio.LecturaConsumo
import com.example.cgeproyect.Persistencia.LecturaRepositorio
import com.example.cgeproyect.Persistencia.MedidorRepositorio

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LecturaScreen(lecturaRepo: LecturaRepositorio, medidorRepo: MedidorRepositorio) {
    var rutCliente by remember { mutableStateOf("") }
    var anio by remember { mutableStateOf("2025") }
    var mes by remember { mutableStateOf("10") } // Estado para el mes
    var kwh by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    // --- Lógica de validación para el mes ---
    val mesInt = mes.toIntOrNull()
    val isMonthError = mes.isEmpty() || mesInt == null || mesInt !in 1..12

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Registrar Lectura Mensual", style = MaterialTheme.typography.headlineMedium)

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
                    // Cualquier otra cosa (como "13" o "20") se ignora
                    // porque no actualiza el estado 'mes'.
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

        OutlinedTextField(kwh, { kwh = it }, label = { Text("kWh Leídos (Ej: 1800.0)") })

        Button(
            onClick = {
                try {
                    val medidor = medidorRepo.findMedidorByCliente(rutCliente)
                    if (medidor == null) {
                        mensaje = "Error: Cliente no tiene medidor."
                    } else {
                        val lectura = LecturaConsumo(
                            idMedidor = medidor.codigo,
                            anio = anio.toInt(),
                            mes = mes.toInt(), // Se usa el 'mes' validado
                            kwhLeidos = kwh.toDouble()
                        )
                        lecturaRepo.addLectura(lectura)
                        mensaje = "Lectura registrada con éxito para ${medidor.codigo}"
                    }
                } catch (e: Exception) {
                    mensaje = "Error: ${e.message}"
                }
            },
            enabled = !isMonthError // 5. El botón se desactiva si hay error
        ) {
            Text("Registrar Lectura")
        }

        Text(mensaje, color = MaterialTheme.colorScheme.primary)
    }
}
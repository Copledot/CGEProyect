package com.example.cgeproyect.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cgeproyect.Persistencia.ClienteRepositorio
import com.example.cgeproyect.Persistencia.MedidorRepositorio
import com.example.cgeproyect.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteScreen(
    clienteRepo: ClienteRepositorio,
    medidorRepo: MedidorRepositorio
) {
    // --- 1. ESTADO PARA EL FILTRO ---
    var filtroTexto by remember { mutableStateOf("") }

    val todosLosClientes = clienteRepo.getAllClientes()

    // --- 2. LÓGICA DE FILTRADO ---
    val clientesFiltrados = if (filtroTexto.isBlank()) {
        todosLosClientes // Si no hay filtro, muestra todos
    } else {
        todosLosClientes.filter { cliente ->
            // Revisa si el nombre O el RUT contienen el texto
            cliente.nombre.contains(filtroTexto, ignoreCase = true) ||
                    cliente.rut.contains(filtroTexto, ignoreCase = true)
        }
    }

    Column(Modifier.fillMaxSize()) {
        Text(
            "Clientes Existentes",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // --- 3. CAMPO DE TEXTO PARA FILTRAR ---
        OutlinedTextField(
            value = filtroTexto,
            onValueChange = { filtroTexto = it },
            label = { Text("Filtrar por Nombre o RUT") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            singleLine = true
        )

        // --- 4. LISTA FILTRADA ---
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(clientesFiltrados) { cliente ->
                val medidor = medidorRepo.findMedidorByCliente(cliente.rut)

                Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large) {
                    Column(Modifier.padding(16.dp)) {
                        Text(cliente.nombre, style = MaterialTheme.typography.titleMedium)
                        Text("RUT: ${cliente.rut}")
                        Text("Dirección: ${cliente.direccionFacturacion}")

                        Text(
                            "Tarifa: ${cliente.tipoTarifa}",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary
                        )

                        if (medidor != null) {
                            Text(
                                "Medidor: ${medidor.codigo} (Tipo: ${medidor.tipo()})",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        } else {
                            Text("Medidor: No asignado", color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}
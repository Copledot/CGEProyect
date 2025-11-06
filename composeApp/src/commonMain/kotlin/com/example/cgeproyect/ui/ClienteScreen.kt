package com.example.cgeproyect.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cgeproyect.Persistencia.ClienteRepositorio
import com.example.cgeproyect.Persistencia.MedidorRepositorio
import com.example.cgeproyect.AppViewModel

@Composable
fun ClienteScreen(
    clienteRepo: ClienteRepositorio,
    medidorRepo: MedidorRepositorio
) {
    // Obtenemos todos los clientes.
    // Para ver los nuevos, tendrás que reiniciar la app (o implementar un estado de refresh más complejo)
    val clientes = clienteRepo.getAllClientes()

    Column(Modifier.fillMaxSize()) {
        Text("Clientes Existentes", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(clientes) { cliente ->
                // Buscamos el medidor de este cliente
                val medidor = medidorRepo.findMedidorByCliente(cliente.rut)

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text(cliente.nombre, style = MaterialTheme.typography.titleMedium)
                        Text("RUT: ${cliente.rut}")
                        Text("Dirección: ${cliente.direccionFacturacion}")

                        // Muestra la tarifa guardada
                        Text(
                            "Tarifa: ${cliente.tipoTarifa}",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary
                        )

                        // Muestra el medidor asignado
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
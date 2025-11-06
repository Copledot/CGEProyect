package com.example.cgeproyect.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cgeproyect.Dominio.*
import com.example.cgeproyect.Persistencia.ClienteRepositorio
import com.example.cgeproyect.Persistencia.MedidorRepositorio

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrarClienteScreen(
    clienteRepo: ClienteRepositorio,
    medidorRepo: MedidorRepositorio
) {
    // Variables de estado para el formulario
    var rut by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var tipoTarifa by remember { mutableStateOf("Residencial") } // Estado para Tarifa
    var tipoMedidor by remember { mutableStateOf("Monofásico") } // Estado para Medidor
    var codigoMedidor by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") } // Para el feedback

    // Usamos LazyColumn para que el formulario sea "scrollable"
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            Text("Registrar Nuevo Cliente", style = MaterialTheme.typography.headlineMedium)
        }

        // --- Campos del Cliente ---
        item {
            OutlinedTextField(rut, { rut = it }, label = { Text("RUT (ej: 11-1)") }, modifier = Modifier.fillMaxWidth())
        }
        item {
            OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre Completo") }, modifier = Modifier.fillMaxWidth())
        }
        item {
            OutlinedTextField(email, { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        }
        item {
            OutlinedTextField(direccion, { direccion = it }, label = { Text("Dirección de Suministro") }, modifier = Modifier.fillMaxWidth())
        }

        // --- Selección de Tarifa ---
        item {
            Text("Tipo de Tarifa", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))
        }
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = tipoTarifa == "Residencial", onClick = { tipoTarifa = "Residencial" })
                Text("Residencial", Modifier.padding(end = 16.dp))
                RadioButton(selected = tipoTarifa == "Comercial", onClick = { tipoTarifa = "Comercial" })
                Text("Comercial")
            }
        }

        // --- Asignación de Medidor ---
        item {
            Text("Asignar Medidor", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))
        }
        item {
            OutlinedTextField(codigoMedidor, { codigoMedidor = it }, label = { Text("Código de Medidor (ej: MONO-123)") }, modifier = Modifier.fillMaxWidth())
        }
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = tipoMedidor == "Monofásico", onClick = { tipoMedidor = "Monofásico" })
                Text("Monofásico", Modifier.padding(end = 16.dp))
                RadioButton(selected = tipoMedidor == "Trifásico", onClick = { tipoMedidor = "Trifásico" })
                Text("Trifásico")
            }
        }

        // --- Botón de Guardar y Feedback ---
        item {
            Button(
                onClick = {
                    try {
                        // 1. Creamos el Cliente con su tipo de tarifa
                        val cliente = Cliente(
                            rut = rut,
                            nombre = nombre,
                            email = email,
                            direccionFacturacion = direccion,
                            estado = EstadoCliente.ACTIVO,
                            tipoTarifa = tipoTarifa // <-- Guardamos la tarifa seleccionada
                        )

                        // 2. Creamos el Medidor
                        val medidor = if (tipoMedidor == "Monofásico") {
                            MedidorMonofasico(codigoMedidor, direccion, 15.0) // Potencia default
                        } else {
                            MedidorTrifasico(codigoMedidor, direccion, 50.0, 0.95) // Defaults
                        }

                        // 3. Guardamos ambos en los repositorios
                        clienteRepo.addCliente(cliente)
                        medidorRepo.addMedidor(medidor, rut)

                        // 4. Damos FEEDBACK de éxito
                        mensaje = "Cliente '$nombre' y medidor '$codigoMedidor' registrados."

                        // 5. Limpiamos los campos
                        rut = ""; nombre = ""; email = ""; direccion = ""; codigoMedidor = ""

                    } catch (e: Exception) {
                        // 4. Damos FEEDBACK de error
                        mensaje = "Error al registrar: ${e.message}"
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
            ) {
                Text("Registrar Cliente y Medidor")
            }
        }

        // --- Mensaje de Feedback ---
        item {
            Text(mensaje, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleMedium)
        }
    }
}
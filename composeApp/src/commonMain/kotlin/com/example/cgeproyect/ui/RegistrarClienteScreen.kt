package com.example.cgeproyect.ui

import androidx.compose.animation.*
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
    // --- Variables de estado ---
    var rut by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var tipoTarifa by remember { mutableStateOf("Residencial") }
    var tipoMedidor by remember { mutableStateOf("Monofásico") }
    var codigoMedidor by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    // --- 1. LOGICA DE VALIDACION ---
    val isRutError = rut.isBlank()
    val isNombreError = nombre.isBlank()
    val isCodigoError = codigoMedidor.isBlank()

    // El boton solo se activa si los campos principales están llenos
    val isButtonEnabled = !isRutError && !isNombreError && !isCodigoError

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier.widthIn(max = 600.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                Text("Registrar Nuevo Cliente", style = MaterialTheme.typography.headlineMedium)
            }

            // --- 2. CAMPOS CON MARCA DE ERROR ---
            item {
                OutlinedTextField(
                    value = rut,
                    onValueChange = { rut = it },
                    label = { Text("RUT (ej: 11-1)") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = isRutError // Se marca en rojo si está vacio
                )
            }
            item {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre Completo") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = isNombreError // Se marca en rojo si esta vacio
                )
            }
            item {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email (Opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = direccion,
                    onValueChange = { direccion = it },
                    label = { Text("Dirección de Suministro (Opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }


            item {
                Column(Modifier.fillMaxWidth()) {
                    Text("Tipo de Tarifa", style = MaterialTheme.typography.titleMedium)
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                        RadioButton(selected = tipoTarifa == "Residencial", onClick = { tipoTarifa = "Residencial" })
                        Text("Residencial", Modifier.padding(end = 16.dp))
                        RadioButton(selected = tipoTarifa == "Comercial", onClick = { tipoTarifa = "Comercial" })
                        Text("Comercial")
                    }
                }
            }

            // --- 3. CAMPO DE MEDIDOR CON MARCA DE ERROR ---
            item {
                Column(Modifier.fillMaxWidth()) {
                    Text("Asignar Medidor", style = MaterialTheme.typography.titleMedium)
                    OutlinedTextField(
                        value = codigoMedidor,
                        onValueChange = { codigoMedidor = it },
                        label = { Text("Código de Medidor (ej: MONO-123)") },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        isError = isCodigoError // Se marca en rojo si esta vacio
                    )
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                        RadioButton(selected = tipoMedidor == "Monofásico", onClick = { tipoMedidor = "Monofásico" })
                        Text("Monofásico", Modifier.padding(end = 16.dp))
                        RadioButton(selected = tipoMedidor == "Trifásico", onClick = { tipoMedidor = "Trifásico" })
                        Text("Trifásico")
                    }
                }
            }

            // --- 4. BOTON DESACTIVADO ---
            item {
                Button(
                    onClick = {
                        // Doble chequeo por si acaso, aunque el boton estaro desactivado
                        if (!isButtonEnabled) {
                            mensaje = "Error: RUT, Nombre y Código de Medidor son obligatorios."
                            return@Button
                        }

                        try {
                            val cliente = Cliente(
                                rut = rut,
                                nombre = nombre,
                                email = email,
                                direccionFacturacion = direccion,
                                estado = EstadoCliente.ACTIVO,
                                tipoTarifa = tipoTarifa
                            )
                            val medidor = if (tipoMedidor == "Monofásico") {
                                MedidorMonofasico(codigoMedidor, direccion, 15.0)
                            } else {
                                MedidorTrifasico(codigoMedidor, direccion, 50.0, 0.95)
                            }
                            clienteRepo.addCliente(cliente)
                            medidorRepo.addMedidor(medidor, rut)
                            mensaje = "Cliente '$nombre' y medidor '$codigoMedidor' registrados."
                            rut = ""; nombre = ""; email = ""; direccion = ""; codigoMedidor = ""
                        } catch (e: Exception) {
                            mensaje = "Error al registrar: ${e.message}"
                        }
                    },
                    enabled = isButtonEnabled, // El botón se activa/desactiva
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
                ) {
                    Text("Registrar Cliente y Medidor")
                }
            }

            // Animacion de mensaje
            item {
                AnimatedVisibility(
                    visible = mensaje.isNotEmpty(),
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    Text(
                        text = mensaje,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}
package com.example.cgeproyect

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cgeproyect.theme.AppTheme
import com.example.cgeproyect.ui.BoletaScreen
import com.example.cgeproyect.ui.ClienteScreen
import com.example.cgeproyect.ui.LecturaScreen
import com.example.cgeproyect.ui.RegistrarClienteScreen // <-- Importa la nueva pantalla

// Enum de navegación actualizado
enum class Pantalla {
    REGISTRAR,
    LISTA_CLIENTES, // Renombrada de "CLIENTES"
    LECTURAS,
    BOLETAS
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    AppTheme {
        var pantallaActual by remember { mutableStateOf(Pantalla.REGISTRAR) } // Inicia en Registrar

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("CGE Electricidad - Gestión") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            },
            bottomBar = {
                NavigationBar {
                    // Pestaña 1: Registrar
                    NavigationBarItem(
                        icon = { Spacer(Modifier.size(24.dp)) }, // Usamos Spacer
                        label = { Text("Registrar") },
                        selected = pantallaActual == Pantalla.REGISTRAR,
                        onClick = { pantallaActual = Pantalla.REGISTRAR }
                    )
                    // Pestaña 2: Lista de Clientes
                    NavigationBarItem(
                        icon = { Spacer(Modifier.size(24.dp)) },
                        label = { Text("Clientes") },
                        selected = pantallaActual == Pantalla.LISTA_CLIENTES,
                        onClick = { pantallaActual = Pantalla.LISTA_CLIENTES }
                    )
                    // Pestaña 3: Lecturas
                    NavigationBarItem(
                        icon = { Spacer(Modifier.size(24.dp)) },
                        label = { Text("Lecturas") },
                        selected = pantallaActual == Pantalla.LECTURAS,
                        onClick = { pantallaActual = Pantalla.LECTURAS }
                    )
                    // Pestaña 4: Boletas
                    NavigationBarItem(
                        icon = { Spacer(Modifier.size(24.dp)) },
                        label = { Text("Boletas") },
                        selected = pantallaActual == Pantalla.BOLETAS,
                        onClick = { pantallaActual = Pantalla.BOLETAS }
                    )
                }
            }
        ) { innerPadding ->
            Box(Modifier.padding(innerPadding).fillMaxSize().padding(16.dp)) {
                // Contenido de la pantalla actual
                when (pantallaActual) {
                    Pantalla.REGISTRAR -> RegistrarClienteScreen(AppViewModel.clienteRepo, AppViewModel.medidorRepo)
                    Pantalla.LISTA_CLIENTES -> ClienteScreen(AppViewModel.clienteRepo, AppViewModel.medidorRepo)
                    Pantalla.LECTURAS -> LecturaScreen(AppViewModel.lecturaRepo, AppViewModel.medidorRepo)
                    Pantalla.BOLETAS -> BoletaScreen(AppViewModel.boletaService)
                }
            }
        }
    }
}
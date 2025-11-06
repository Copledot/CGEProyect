package com.example.cgeproyect

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Speed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.* // (para remember, mutableStateOf, etc.)
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cgeproyect.theme.AppTheme
import com.example.cgeproyect.ui.BoletaScreen
import com.example.cgeproyect.ui.ClienteScreen
import com.example.cgeproyect.ui.LecturaScreen
import com.example.cgeproyect.ui.RegistrarClienteScreen

enum class Pantalla {
    REGISTRAR,
    LISTA_CLIENTES,
    LECTURAS,
    BOLETAS
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {

    AppTheme {
        var pantallaActual by remember { mutableStateOf(Pantalla.REGISTRAR) }

        Scaffold(
            topBar = {
                TopAppBar( //Titulo del programa
                    title = { Text("CGE Electricidad - Gestión") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                )
            },
            bottomBar = {
                NavigationBar {

                    NavigationBarItem( //Pestanias de los menus
                        icon = { Icon(Icons.Filled.AddCircleOutline, contentDescription = "Registrar") },
                        label = { Text("Registrar") },
                        selected = pantallaActual == Pantalla.REGISTRAR,
                        onClick = { pantallaActual = Pantalla.REGISTRAR }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.ListAlt, contentDescription = "Clientes") },
                        label = { Text("Clientes") },
                        selected = pantallaActual == Pantalla.LISTA_CLIENTES,
                        onClick = { pantallaActual = Pantalla.LISTA_CLIENTES }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Speed, contentDescription = "Lecturas") },
                        label = { Text("Lecturas") },
                        selected = pantallaActual == Pantalla.LECTURAS,
                        onClick = { pantallaActual = Pantalla.LECTURAS }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Article, contentDescription = "Boletas") },
                        label = { Text("Boletas") },
                        selected = pantallaActual == Pantalla.BOLETAS,
                        onClick = { pantallaActual = Pantalla.BOLETAS }
                    )
                }
            }
        ) { innerPadding ->

            Box(Modifier.fillMaxSize().padding(innerPadding).padding(16.dp)) {
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
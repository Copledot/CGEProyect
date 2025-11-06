package com.example.cgeproyect.Dominio

import kotlin.time.Clock
import kotlin.time.ExperimentalTime

// No se necesitan imports de kotlinx.datetime

abstract class EntidadBase {
    // Usamos System.currentTimeMillis() que es tradicional
    @OptIn(ExperimentalTime::class)
    val id: String = "id_${Clock.System.currentTimeMillis()}"

    // Usamos Long (un número largo) para guardar la fecha
    @OptIn(ExperimentalTime::class)
    val createdAt: String = Clock.System.currentTimeMillis()
    var updatedAt: String = createdAt
}

@OptIn(ExperimentalTime::class)
private fun Clock.System.currentTimeMillis(): String {
    return this.now().toEpochMilliseconds().toString()
}

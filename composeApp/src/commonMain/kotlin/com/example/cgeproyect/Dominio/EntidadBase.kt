package com.example.cgeproyect.Dominio

import kotlin.time.Clock
import kotlin.time.ExperimentalTime


// Clase base para las entidades del dominio
abstract class EntidadBase {
    @OptIn(ExperimentalTime::class)
    val id: String = "id_${Clock.System.currentTimeMillis()}"

    @OptIn(ExperimentalTime::class)
    val createdAt: String = Clock.System.currentTimeMillis()
    var updatedAt: String = createdAt
}

@OptIn(ExperimentalTime::class)
private fun Clock.System.currentTimeMillis(): String {
    return this.now().toEpochMilliseconds().toString()
}

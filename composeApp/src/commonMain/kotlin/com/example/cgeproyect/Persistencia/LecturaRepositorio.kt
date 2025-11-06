package com.example.cgeproyect.Persistencia

import com.example.cgeproyect.Dominio.LecturaConsumo

interface LecturaRepositorio {
    fun addLectura(lectura: LecturaConsumo)
    fun findLecturasMes(idMedidor: String, anio: Int, mes: Int): Pair<LecturaConsumo?, LecturaConsumo?>
}
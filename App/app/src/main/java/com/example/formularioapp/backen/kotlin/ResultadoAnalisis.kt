package com.example.formularioapp.backen.kotlin

import com.example.formularioapp.backen.java.ErroresDeAnalizadores
import java.io.Serializable

data class ResultadoAnalisis(
    val errores: List<ErroresDeAnalizadores>,
    val resultado: Any? = null
) : Serializable
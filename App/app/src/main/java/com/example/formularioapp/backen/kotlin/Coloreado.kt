package com.example.formularioapp.backen.kotlin

import android.graphics.Color
import android.text.Editable
import android.text.style.ForegroundColorSpan
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Coloreado {

    // Contadores
    data class Estadisticas(
        var secciones: Int = 0,
        var abiertas: Int = 0,
        var desplegables: Int = 0,
        var seleccion: Int = 0,
        var multiples: Int = 0
    )

    // Reporte de metadata
    fun obtenerEstadisticas(texto: String): Estadisticas {
        val stats = Estadisticas()
        var i = 0

        while (i < texto.length) {
            val c = texto[i]

            // Ignoramos cadenas
            if (c == '"') {
                i++
                while (i < texto.length && texto[i] != '"') i++
                if (i < texto.length) i++
                continue
            }

            // Si es una letra, extraer la palabra completa
            if (c.isLetter()) {
                val inicio = i
                while (i < texto.length && (texto[i].isLetterOrDigit() || texto[i] == '_')) i++

                val palabra = texto.substring(inicio, i)

                // Comprobamos poalabra resrvada
                when (palabra) {
                    "SECTION" -> stats.secciones++
                    "OPEN_QUESTION" -> stats.abiertas++
                    "DROP_QUESTION" -> stats.desplegables++
                    "SELECT_QUESTION" -> stats.seleccion++
                    "MULTIPLE_QUESTION" -> stats.multiples++
                }
                continue
            }
            i++
        }
        return stats
    }

    // encabezado de estadistica
    fun generarEncabezado(autor: String, descripcion: String, stats: Estadisticas): String {
        val fecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        val hora = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val totalPreguntas = stats.abiertas + stats.desplegables + stats.seleccion + stats.multiples

        val descFinal = if (descripcion.isBlank()) "Sin descripción" else descripcion

        return """
            ###
            Author: $autor
            Fecha: $fecha
            Hora: $hora
            Description: $descFinal
            Total de Secciones: ${stats.secciones}
            Total de Preguntas: $totalPreguntas
                Abiertas: ${stats.abiertas}
                Desplegables: ${stats.desplegables}
                Selección: ${stats.seleccion}
                Múltiples: ${stats.multiples}
            ###
            
        """.trimIndent()
    }

    // coloreado
    fun aplicarColores(editable: Editable) {
        val spans = editable.getSpans(0, editable.length, ForegroundColorSpan::class.java)
        for (span in spans) {
            editable.removeSpan(span)
        }

        val texto = editable.toString()
        val palabrasReservadas = setOf(
            "number","string","special","SECTION","TABLE","TEXT",
            "OPEN_QUESTION","DROP_QUESTION","SELECT_QUESTION","MULTIPLE_QUESTION",
            "IF","ELSE","WHILE","DO","FOR"
        )

        var i = 0
        while (i < texto.length) {
            val c = texto[i]

            if (c == '"') {
                val inicio = i
                i++
                while (i < texto.length && texto[i] != '"') i++
                if (i < texto.length) i++
                editable.setSpan(ForegroundColorSpan(Color.parseColor("#FFA500")), inicio, i, Editable.SPAN_EXCLUSIVE_EXCLUSIVE)
                continue
            }

            if (c.isDigit()) {
                val inicio = i
                while (i < texto.length && (texto[i].isDigit() || texto[i] == '.')) i++
                editable.setSpan(ForegroundColorSpan(Color.parseColor("#00BCD4")), inicio, i, Editable.SPAN_EXCLUSIVE_EXCLUSIVE)
                continue
            }

            if (c.isLetter()) {
                val inicio = i
                while (i < texto.length && (texto[i].isLetterOrDigit() || texto[i] == '_')) i++
                val palabra = texto.substring(inicio, i)
                if (palabrasReservadas.contains(palabra)) {
                    editable.setSpan(ForegroundColorSpan(Color.parseColor("#9C27B0")), inicio, i, Editable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                continue
            }

            if ("+-*/%^".contains(c)) {
                editable.setSpan(ForegroundColorSpan(Color.GREEN), i, i + 1, Editable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            if ("(){}[]".contains(c)) {
                editable.setSpan(ForegroundColorSpan(Color.BLUE), i, i + 1, Editable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            i++
        }
    }
}
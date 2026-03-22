package com.example.formularioapp.backen.kotlin

import android.graphics.Color
import android.text.Editable
import android.text.style.ForegroundColorSpan

class Coloreado {

    fun aplicarColores(editable: Editable) {

        // limpar estilos anteriores
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

            // ========================
            // STRINGS "..."
            // ========================
            if (c == '"') {
                val inicio = i
                i++
                while (i < texto.length && texto[i] != '"') i++
                if (i < texto.length) i++

                editable.setSpan(
                    ForegroundColorSpan(Color.parseColor("#FFA500")), // naranja
                    inicio,
                    i,
                    Editable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                continue
            }

            // ========================
            // NÚMEROS
            // ========================
            if (c.isDigit()) {
                val inicio = i
                while (i < texto.length && (texto[i].isDigit() || texto[i] == '.')) i++

                editable.setSpan(
                    ForegroundColorSpan(Color.parseColor("#00BCD4")), // celeste
                    inicio,
                    i,
                    Editable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                continue
            }

            // ========================
            // IDENTIFICADORES / PALABRAS
            // ========================
            if (c.isLetter()) {
                val inicio = i
                while (i < texto.length && (texto[i].isLetterOrDigit() || texto[i] == '_')) i++

                val palabra = texto.substring(inicio, i)

                if (palabrasReservadas.contains(palabra)) {
                    editable.setSpan(
                        ForegroundColorSpan(Color.parseColor("#9C27B0")), // morado
                        inicio,
                        i,
                        Editable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                continue
            }

            // ========================
            // OPERADORES
            // ========================
            if ("+-*/%^".contains(c)) {
                editable.setSpan(
                    ForegroundColorSpan(Color.GREEN),
                    i,
                    i + 1,
                    Editable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            // ========================
            // LLAVES / PARÉNTESIS
            // ========================
            if ("(){}[]".contains(c)) {
                editable.setSpan(
                    ForegroundColorSpan(Color.BLUE),
                    i,
                    i + 1,
                    Editable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            i++
        }
    }
}
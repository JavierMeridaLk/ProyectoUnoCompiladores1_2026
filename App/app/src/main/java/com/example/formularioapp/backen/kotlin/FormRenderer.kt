package com.example.formularioapp.backen.kotlin

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import android.widget.*
import com.example.formularioapp.backen.java.ast.*

class FormRenderer(private val context: Context) {

    fun render(nodo: Nodo, contenedor: LinearLayout) {

        when (nodo) {

            is NodoPrograma -> {
                nodo.instrucciones.forEach {
                    render(it, contenedor)
                }
            }

            is NodoSeccion -> {
                val layout = LinearLayout(context)

                val orientation = nodo.atributos["orientation"] as? String ?: "VERTICAL"
                layout.orientation =
                    if (orientation == "HORIZONTAL") LinearLayout.HORIZONTAL
                    else LinearLayout.VERTICAL

                val width = (nodo.atributos["width"] as? Number)?.toInt()
                val height = (nodo.atributos["height"] as? Number)?.toInt()

                layout.layoutParams = LinearLayout.LayoutParams(
                    width ?: ViewGroup.LayoutParams.MATCH_PARENT,
                    height ?: ViewGroup.LayoutParams.WRAP_CONTENT
                )

                aplicarEstilos(layout, nodo.estilo)

                nodo.elementos.forEach {
                    render(it, layout)
                }

                contenedor.addView(layout)
            }

            is NodoTexto -> {
                val tv = TextView(context)
                tv.text = nodo.contenido

                aplicarEstilos(tv, nodo.estilo)

                contenedor.addView(tv)
            }

            is NodoPreguntaAbierta -> {
                val layout = LinearLayout(context)
                layout.orientation = LinearLayout.VERTICAL

                val label = TextView(context)
                label.text = nodo.label

                val input = EditText(context)

                aplicarEstilos(label, nodo.estilo)
                aplicarEstilos(input, nodo.estilo)

                layout.addView(label)
                layout.addView(input)

                contenedor.addView(layout)
            }

            is NodoPreguntaSeleccion -> {
                val layout = LinearLayout(context)
                layout.orientation = LinearLayout.VERTICAL

                val label = TextView(context)
                label.text = nodo.label

                val spinner = Spinner(context)

                val opciones = nodo.opciones ?: listOf()

                val adapter = ArrayAdapter(
                    context,
                    android.R.layout.simple_spinner_item,
                    opciones
                )

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter

                layout.addView(label)
                layout.addView(spinner)

                contenedor.addView(layout)
            }

            is NodoTabla -> {
                val tableLayout = TableLayout(context)

                nodo.filas.forEach { fila ->

                    val tableRow = TableRow(context)

                    fila.forEach { celda ->
                        val celdaLayout = LinearLayout(context)
                        celdaLayout.orientation = LinearLayout.VERTICAL

                        render(celda, celdaLayout)

                        tableRow.addView(celdaLayout)
                    }

                    tableLayout.addView(tableRow)
                }

                contenedor.addView(tableLayout)
            }
        }
    }

    private fun aplicarEstilos(view: android.view.View, estilo: NodoEstilo?) {

        estilo?.atributos?.forEach { (key, value) ->

            when (key) {

                "background" -> {
                    if (value is NodoColor) {
                        view.setBackgroundColor(parseColor(value.valor))
                    }
                }

                "textColor" -> {
                    if (view is TextView && value is NodoColor) {
                        view.setTextColor(parseColor(value.valor))
                    }
                }

                "fontSize" -> {
                    if (view is TextView && value is Number) {
                        view.textSize = value.toFloat()
                    }
                }
            }
        }
    }

    private fun parseColor(valor: String): Int {
        return try {
            when {
                valor.startsWith("#") -> Color.parseColor(valor)
                valor.startsWith("(") -> {
                    val parts = valor.replace("(", "").replace(")", "").split(",")
                    Color.rgb(parts[0].toInt(), parts[1].toInt(), parts[2].toInt())
                }
                valor.startsWith("<") -> Color.GRAY // simplificado
                else -> when (valor) {
                    "RED" -> Color.RED
                    "BLUE" -> Color.BLUE
                    "GREEN" -> Color.GREEN
                    "BLACK" -> Color.BLACK
                    "WHITE" -> Color.WHITE
                    else -> Color.GRAY
                }
            }
        } catch (e: Exception) {
            Color.GRAY
        }
    }
}
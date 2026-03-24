package com.example.formularioapp.backen.kotlin

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.example.formularioapp.backen.java.ast.*

class FormRenderer(private val context: Context) {

    fun render(nodo: Nodo, contenedor: LinearLayout) {
        when (nodo) {
            is NodoPrograma -> {
                nodo.instrucciones.forEach { child ->
                    render(child, contenedor)
                }
            }

            is NodoComponente -> renderComponente(nodo, contenedor)

            else -> {}
        }
    }

    private fun renderComponente(nodo: NodoComponente, contenedor: LinearLayout) {
        when (nodo) {
            is NodoTexto -> {
                val tv = TextView(context)
                tv.text = nodo.contenido
                aplicarEstilo(tv, nodo.estilo)
                contenedor.addView(tv)
            }

            is NodoPreguntaAbierta -> {
                val layout = LinearLayout(context)
                layout.orientation = LinearLayout.VERTICAL
                layout.layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

                val label = TextView(context)
                label.text = nodo.label
                aplicarEstilo(label, nodo.estilo)

                val input = EditText(context)
                aplicarEstilo(input, nodo.estilo)

                layout.addView(label)
                layout.addView(input)
                contenedor.addView(layout)
            }

            is NodoPreguntaSeleccion -> {
                val layout = LinearLayout(context)
                layout.orientation = LinearLayout.VERTICAL
                layout.layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

                val label = TextView(context)
                label.text = nodo.label
                aplicarEstilo(label, nodo.estilo)
                layout.addView(label)

                nodo.opciones.forEach { opcion ->
                    val tvOpcion = TextView(context)
                    tvOpcion.text = opcion
                    aplicarEstilo(tvOpcion, nodo.estilo)
                    layout.addView(tvOpcion)
                }

                contenedor.addView(layout)
            }

            is NodoSeccion -> {
                val sectionLayout = LinearLayout(context)
                sectionLayout.orientation = LinearLayout.VERTICAL
                sectionLayout.layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

                // Título de la sección
                val titulo = nodo.atributos["label"] as? String
                if (!titulo.isNullOrEmpty()) {
                    val tvTitulo = TextView(context)
                    tvTitulo.text = titulo
                    tvTitulo.textSize = 18f
                    aplicarEstilo(tvTitulo, nodo.estilo)
                    sectionLayout.addView(tvTitulo)
                }

                nodo.elementos.forEach { child ->
                    render(child, sectionLayout)
                }

                contenedor.addView(sectionLayout)
            }

            is NodoTabla -> {
                val tableLayout = LinearLayout(context)
                tableLayout.orientation = LinearLayout.VERTICAL
                tableLayout.layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

                nodo.filas.forEach { fila ->
                    val filaLayout = LinearLayout(context)
                    filaLayout.orientation = LinearLayout.HORIZONTAL
                    filaLayout.layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )

                    fila.forEach { celda ->
                        render(celda, filaLayout)
                    }

                    tableLayout.addView(filaLayout)
                }

                contenedor.addView(tableLayout)
            }

            else -> {}
        }
    }

    private fun aplicarEstilo(view: TextView, estilo: NodoEstilo?) {
        estilo?.atributos?.let { attrs ->
            // Fuente
            when (attrs["font"] as? String) {
                "MONO" -> view.typeface = android.graphics.Typeface.MONOSPACE
                "SANS_SERIF" -> view.typeface = android.graphics.Typeface.SANS_SERIF
                "CURSIVE" -> view.typeface = android.graphics.Typeface.create("cursive", android.graphics.Typeface.NORMAL)
            }

            // Color de texto
            val colorNodo = attrs["color"] as? NodoColor
            colorNodo?.let {
                try {
                    view.setTextColor(Color.parseColor(it.valor))
                } catch (_: Exception) {}
            }

            // Tamaño del borde o grosor (solo como ejemplo para textos, si es necesario)
            val border = attrs["border"] as? String
            val thickness = attrs["thickness"] as? Number
            if (border != null && thickness != null) {
                view.setPadding(8, 8, 8, 8)
                // Aquí podríamos crear un Drawable de borde si quieres
            }
        }
    }

    private fun aplicarEstilo(view: EditText, estilo: NodoEstilo?) {
        // Reutilizamos la función para TextView, ya que EditText hereda de TextView
        aplicarEstilo(view as TextView, estilo)
    }
}
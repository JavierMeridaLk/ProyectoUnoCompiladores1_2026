package com.example.formularioapp.backen.kotlin

import android.content.Context
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.example.formularioapp.backen.java.ast.NodoComponente
import com.example.formularioapp.backen.java.ast.NodoPreguntaAbierta
import com.example.formularioapp.backen.java.ast.NodoTexto

class FormRenderer(private val context: Context) {

    fun render(nodo: NodoComponente, contenedor: LinearLayout) {

        when (nodo) {

            is NodoTexto -> {
                val tv = TextView(context)
                tv.text = nodo.contenido
                contenedor.addView(tv)
            }

            is NodoPreguntaAbierta -> {
                val layout = LinearLayout(context)
                layout.orientation = LinearLayout.VERTICAL

                val label = TextView(context)
                label.text = nodo.label

                val input = EditText(context)

                layout.addView(label)
                layout.addView(input)

                contenedor.addView(layout)
            }
        }
    }
}
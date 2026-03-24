package com.example.formularioapp.backen.kotlin

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.formularioapp.backen.java.ast.*
import kotlinx.coroutines.*

class FormRenderer(private val context: Context) {

    private val density = context.resources.displayMetrics.density
    private val mainScope = CoroutineScope(Dispatchers.Main + Job())

    fun render(nodo: Any, contenedor: ViewGroup, estiloPadre: NodoEstilo? = null) {
        val estiloActual = if (nodo is NodoComponente) (nodo.estilo ?: estiloPadre) else estiloPadre

        when (nodo) {
            is NodoPrograma -> nodo.instrucciones.forEach { render(it, contenedor) }

            is NodoSeccion -> {
                val layout = LinearLayout(context).apply {
                    orientation = if (nodo.atributos["orientation"]?.toString() == "HORIZONTAL")
                        LinearLayout.HORIZONTAL else LinearLayout.VERTICAL
                }
                aplicarRectangulo(layout, nodo.atributos)
                aplicarEstilos(layout, estiloActual)
                nodo.elementos.forEach { render(it, layout, estiloActual) }
                contenedor.addView(layout)
            }

            is NodoTexto -> {
                val tv = TextView(context).apply {
                    text = nodo.atributos["content"]?.toString() ?: ""
                    setTextColor(Color.BLACK)
                }
                aplicarRectangulo(tv, nodo.atributos, forzarWrap = true)
                aplicarEstilos(tv, estiloActual)
                contenedor.addView(tv)
            }

            // OPEN_QUESTION
            is NodoPreguntaAbierta -> {
                val block = crearBloqueVertical()
                val label = TextView(context).apply {
                    text = nodo.label ?: "Pregunta:"
                    setTextColor(Color.BLACK)
                    textSize = 16f
                }
                block.addView(label)

                val input = EditText(context).apply {
                    hint = "Escriba aquí..."
                    setTextColor(Color.BLACK)
                    setHintTextColor(Color.GRAY)
                    background = GradientDrawable().apply {
                        setColor(Color.parseColor("#F9F9F9"))
                        setStroke(2, Color.LTGRAY)
                        cornerRadius = 8 * density
                    }
                    setPadding((12 * density).toInt(), (10 * density).toInt(), (12 * density).toInt(), (10 * density).toInt())
                }
                block.addView(input)

                aplicarRectangulo(block, nodo.atributos, forzarWrap = true)
                contenedor.addView(block)
            }

            // PREGUNTAS DE SELECCIÓN
            is NodoPreguntaSeleccion -> {
                val block = crearBloqueVertical()
                val label = TextView(context).apply {
                    text = nodo.label ?: ""
                    setTextColor(Color.BLACK)
                }
                block.addView(label)

                val tipoSeleccion = nodo.tipo?.toString()?.uppercase() ?: "RADIO"
                val opcionesRaw = nodo.opciones

                // Validacion de la pokeapi
                if (tipoSeleccion == "DROP" && opcionesRaw is NodoPokemonRango) {
                    val tvLoading = TextView(context).apply {
                        text = "Cargando Pokédex..."
                        setTextColor(Color.DKGRAY)
                    }
                    block.addView(tvLoading)

                    mainScope.launch {
                        val listaPokemons = PokemonManager.obtenerPokemons(opcionesRaw.inicio, opcionesRaw.fin)
                        block.removeView(tvLoading)
                        if (listaPokemons.isNotEmpty()) {
                            renderizarOpciones("DROP", listaPokemons, block)
                        } else {
                            val tvError = TextView(context).apply {
                                text = "Error de conexión con PokéAPI"
                                setTextColor(Color.RED)
                            }
                            block.addView(tvError)
                        }
                    }
                } else {
                    //lista de strings normal
                    val listaOpciones = when (opcionesRaw) {
                        is List<*> -> opcionesRaw.filterIsInstance<String>()
                        else -> emptyList()
                    }
                    renderizarOpciones(tipoSeleccion, listaOpciones, block)
                }

                aplicarRectangulo(block, nodo.atributos, forzarWrap = true)
                contenedor.addView(block)
            }
            //TABLE
            is NodoTabla -> {
                val table = TableLayout(context).apply {
                    isStretchAllColumns = true
                    background = GradientDrawable().apply { setStroke(2, Color.BLACK) }
                }
                aplicarRectangulo(table, nodo.atributos, forzarWrap = true)
                nodo.filas.forEach { fila ->
                    val row = TableRow(context)
                    fila.forEach { celda ->
                        val cellBox = LinearLayout(context).apply {
                            orientation = LinearLayout.VERTICAL
                            background = GradientDrawable().apply { setStroke(1, Color.GRAY) }
                            setPadding(10, 10, 10, 10)
                        }
                        render(celda, cellBox, estiloActual)
                        row.addView(cellBox)
                    }
                    table.addView(row)
                }
                contenedor.addView(table)
            }
        }
    }

    private fun renderizarOpciones(tipo: String, opciones: List<String>, bloque: LinearLayout) {
        if (tipo == "DROP") {
            val sp = Spinner(context).apply {
                setBackgroundColor(Color.parseColor("#E0E0E0"))
            }
            val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, opciones)
            sp.adapter = adapter
            bloque.addView(sp)
        } else {
            val container = if (tipo == "MULTIPLE") bloque else RadioGroup(context).also { bloque.addView(it) }
            val colorStateList = ColorStateList.valueOf(Color.BLACK)

            opciones.forEach { opt ->
                val view = if (tipo == "MULTIPLE") CheckBox(context) else RadioButton(context)
                view.text = opt
                view.setTextColor(Color.BLACK)
                view.buttonTintList = colorStateList
                if (container is RadioGroup) container.addView(view) else container.addView(view)
            }
        }
    }

    private fun aplicarRectangulo(view: View, attrs: Map<String, Any>, forzarWrap: Boolean = false) {
        val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        lp.setMargins(0, (12 * density).toInt(), 0, (12 * density).toInt())
        view.layoutParams = lp
    }

    private fun aplicarEstilos(view: View, estilo: NodoEstilo?) {
        if (estilo == null) return
        val gd = GradientDrawable()
        var hasStyle = false
        estilo.atributos.forEach { (k, v) ->
            when (k.lowercase().trim()) {
                "color" -> if (view is TextView) view.setTextColor(procesarColor(v))
                "background" -> { gd.setColor(procesarColor(v)); hasStyle = true }
                "border" -> { gd.setStroke(3, Color.BLACK); hasStyle = true }
            }
        }
        if (hasStyle) view.background = gd
    }

    private fun procesarColor(v: Any?): Int = try {
        val hex = if (v is NodoColor) v.valor else v.toString()
        Color.parseColor(if (hex.startsWith("#")) hex else "#$hex")
    } catch (e: Exception) { Color.BLACK }

    private fun crearBloqueVertical() = LinearLayout(context).apply { orientation = LinearLayout.VERTICAL }
}
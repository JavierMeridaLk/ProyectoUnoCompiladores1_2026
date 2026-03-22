package com.example.formularioapp.backen.kotlin

import android.graphics.Color
import android.os.Bundle
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.formularioapp.R
import com.example.formularioapp.backen.java.ErroresDeAnalizadores

class ReportesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reportes)

        val tabla = findViewById<TableLayout>(R.id.tableErrores)

        val errores = intent.getSerializableExtra("listaErrores") as? List<ErroresDeAnalizadores> ?: emptyList()

        if (errores.isEmpty()) {
            Toast.makeText(this, "No se encontró ningún error", Toast.LENGTH_SHORT).show()
        } else {
            errores.forEachIndexed { index, error ->

                val row = TableRow(this)

                val colorFondo = if (index % 2 == 0)
                    Color.parseColor("#F8FAFC")
                else
                    Color.WHITE

                row.setBackgroundColor(colorFondo)

                row.addView(createCell(error.getLexema()))
                row.addView(createCell(error.getLine().toString()))
                row.addView(createCell(error.getColm().toString()))
                row.addView(createCell(error.getTipo()))
                row.addView(createCell(error.getLenguaje()))
                row.addView(createCell(error.getDescripcion()))

                tabla.addView(row)
            }
        }
    }

    private fun createCell(text: String): TextView {
        return TextView(this).apply {
            this.text = text
            setPadding(12, 12, 12, 12)
            setTextColor(Color.BLACK)
        }
    }
}
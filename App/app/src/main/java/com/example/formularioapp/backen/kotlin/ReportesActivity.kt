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

        val errores = intent.getSerializableExtra("listaErrores") as? ArrayList<ErroresDeAnalizadores> ?: arrayListOf()

        if (errores.isEmpty()) {
            val row = TableRow(this)
            val cell = TextView(this)

            cell.text = "No se encontraron errores"
            cell.setPadding(16, 16, 16, 16)
            cell.setTextColor(Color.BLACK)

            row.addView(cell)
            tabla.addView(row)

        } else {
            for (error in errores) {
                val row = TableRow(this)

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
        val tv = TextView(this)
        tv.text = text
        tv.setPadding(12, 12, 12, 12)
        tv.setTextColor(Color.BLACK)
        return tv
    }
}
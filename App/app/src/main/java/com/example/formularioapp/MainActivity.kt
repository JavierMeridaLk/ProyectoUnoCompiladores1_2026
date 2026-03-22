package com.example.formularioapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.example.formularioapp.backen.java.ErroresDeAnalizadores
import com.example.formularioapp.backen.kotlin.AnalizadorManager
import com.example.formularioapp.backen.kotlin.ReportesActivity

class MainActivity : AppCompatActivity() {

    private lateinit var editor: EditText
    private lateinit var lineNumbers: TextView
    private lateinit var btnGenerar: MaterialButton
    private lateinit var btnSubir: MaterialButton
    private lateinit var btnReportes: MaterialButton
    private lateinit var btnGuardarForm: MaterialButton
    private lateinit var btnGuardarPKM: MaterialButton
    private val FILE_REQUEST_CODE = 1001
    private val CREATE_FILE_FORM = 2001
    private val CREATE_FILE_PKM = 2002
    private var erroresGenerados: ArrayList<ErroresDeAnalizadores> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editor = findViewById(R.id.editor)
        lineNumbers = findViewById(R.id.lineNumbers)

        btnGenerar = findViewById(R.id.btnGenerar)
        btnSubir = findViewById(R.id.btnSubir)
        btnReportes = findViewById(R.id.btnReportes)
        btnGuardarForm = findViewById(R.id.btnGuardarForm)
        btnGuardarPKM = findViewById(R.id.btnGuardarPKM)

        setupEditor()

        // GENERAR
        btnGenerar.setOnClickListener {
            val codigo = editor.text.toString()

            try {
                val analizadorManager = AnalizadorManager()
                val resultadoAnalisis = analizadorManager.analizarCodigo(codigo)

                erroresGenerados = ArrayList(resultadoAnalisis.errores ?: emptyList())

                if (erroresGenerados.isEmpty()) {
                    Toast.makeText(this, "No se encontró ningún error", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Se encontraron ${erroresGenerados.size} errores", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Toast.makeText(this, "Error al analizar el código", Toast.LENGTH_LONG).show()
                erroresGenerados.clear()
            }
        }

        // SUBIR ARCHIVO
        btnSubir.setOnClickListener {
            abrirSelectorDeArchivo()
        }

        // REPORTES
        btnReportes.setOnClickListener {
            if (erroresGenerados.isEmpty()) {
                Toast.makeText(this, "No se encontró ningún error", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, ReportesActivity::class.java)
                intent.putExtra("listaErrores", erroresGenerados)
                startActivity(intent)
            }
        }

        // GUARDAR FORM
        btnGuardarForm.setOnClickListener {
            guardarArchivo(".form", CREATE_FILE_FORM)
        }

        // GUARDAR PKM
        btnGuardarPKM.setOnClickListener {
            guardarArchivo(".pkm", CREATE_FILE_PKM)
        }
    }
    //Editor
    private fun setupEditor() {
        editor.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { updateLineNumbers() }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        updateLineNumbers()
    }

    private fun updateLineNumbers() {
        val lines = if (editor.text.isEmpty()) 1 else editor.text.toString().split("\n").size
        lineNumbers.text = (1..lines).joinToString("\n")
    }

//Abrir archivos
    private fun abrirSelectorDeArchivo() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }
        startActivityForResult(intent, FILE_REQUEST_CODE)
    }
//Guardar archivois
    private fun guardarArchivo(extension: String, requestCode: Int) {

        val contenido = editor.text.toString()

        if (contenido.isBlank()) {
            Toast.makeText(this, "El editor está vacío", Toast.LENGTH_SHORT).show()
            return
        }

        val nombreSugerido = if (extension == ".form") {
            "archivo.form"
        } else {
            "archivo.pkm"
        }

        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*" // 🔥 IMPORTANTE: evita que fuerce .txt
            putExtra(Intent.EXTRA_TITLE, nombreSugerido)
        }

        startActivityForResult(intent, requestCode)
    }

//Resultados
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // CARGAR ARCHIVO
        if (requestCode == FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val uri: Uri? = data?.data

            uri?.let {
                val nombreArchivo = it.lastPathSegment ?: "archivo desconocido"

                if (nombreArchivo.endsWith(".form") || nombreArchivo.endsWith(".pkm")) {
                    val contenido = contentResolver.openInputStream(it)
                        ?.bufferedReader()
                        .use { reader -> reader?.readText() } ?: ""

                    editor.setText(contenido)
                    Toast.makeText(this, "Archivo cargado: $nombreArchivo", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Solo se permiten archivos .form o .pkm", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // GUARDAR ARCHIVO
        if ((requestCode == CREATE_FILE_FORM || requestCode == CREATE_FILE_PKM)
            && resultCode == Activity.RESULT_OK) {

            val uri: Uri? = data?.data

            uri?.let {
                try {
                    val contenido = editor.text.toString()

                    contentResolver.openOutputStream(it)?.use { outputStream ->
                        outputStream.write(contenido.toByteArray())
                    }

                    Toast.makeText(this, "Archivo guardado correctamente", Toast.LENGTH_SHORT).show()

                } catch (e: Exception) {
                    Toast.makeText(this, "Error al guardar el archivo", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
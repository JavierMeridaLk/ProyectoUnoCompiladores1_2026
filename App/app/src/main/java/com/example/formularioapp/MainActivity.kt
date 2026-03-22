package com.example.formularioapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.example.formularioapp.backen.java.ErroresDeAnalizadores
import com.example.formularioapp.backen.kotlin.*

class MainActivity : AppCompatActivity() {

    private lateinit var editor: EditText
    private lateinit var lineNumbers: TextView
    private lateinit var scrollEditor: ScrollView

    private lateinit var btnGenerar: MaterialButton
    private lateinit var btnSubir: MaterialButton
    private lateinit var btnReportes: MaterialButton
    private lateinit var btnGuardarForm: MaterialButton
    private lateinit var btnGuardarPKM: MaterialButton

    private val FILE_REQUEST_CODE = 1001
    private val CREATE_FILE_FORM = 2001
    private val CREATE_FILE_PKM = 2002

    private var erroresGenerados = arrayListOf<ErroresDeAnalizadores>()

    private val coloreado = Coloreado()
    private var isColoring = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editor = findViewById(R.id.editor)
        lineNumbers = findViewById(R.id.lineNumbers)
        scrollEditor = findViewById(R.id.scrollEditor)

        btnGenerar = findViewById(R.id.btnGenerar)
        btnSubir = findViewById(R.id.btnSubir)
        btnReportes = findViewById(R.id.btnReportes)
        btnGuardarForm = findViewById(R.id.btnGuardarForm)
        btnGuardarPKM = findViewById(R.id.btnGuardarPKM)

        setupEditor()

        btnGenerar.setOnClickListener {
            val resultado = AnalizadorManager().analizarCodigo(editor.text.toString())
            erroresGenerados = ArrayList(resultado.errores)

            Toast.makeText(
                this,
                if (erroresGenerados.isEmpty()) "Sin errores"
                else "${erroresGenerados.size} errores encontrados",
                Toast.LENGTH_SHORT
            ).show()
        }

        btnReportes.setOnClickListener {
            if (erroresGenerados.isEmpty()) {
                Toast.makeText(this, "No hay errores", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, ReportesActivity::class.java)
                intent.putExtra("listaErrores", erroresGenerados)
                startActivity(intent)
            }
        }

        btnSubir.setOnClickListener { abrirSelectorDeArchivo() }
        btnGuardarForm.setOnClickListener { guardarArchivo(".form", CREATE_FILE_FORM) }
        btnGuardarPKM.setOnClickListener { guardarArchivo(".pkm", CREATE_FILE_PKM) }
    }

    private fun setupEditor() {

        editor.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                if (isColoring) return
                isColoring = true

                val cursor = editor.selectionStart

                s?.let { coloreado.aplicarColores(it) }

                editor.setSelection(cursor.coerceAtMost(editor.text.length))

                updateLineNumbers()

                isColoring = false
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // 🔥 SINCRONIZACIÓN REAL
        editor.viewTreeObserver.addOnScrollChangedListener {
            lineNumbers.scrollTo(0, editor.scrollY)
        }

        updateLineNumbers()
    }

    private fun updateLineNumbers() {
        val text = editor.text.toString()
        val lines = if (text.isEmpty()) 1 else text.count { it == '\n' } + 1
        lineNumbers.text = (1..lines).joinToString("\n")
    }

    private fun abrirSelectorDeArchivo() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "*/*"
        startActivityForResult(intent, FILE_REQUEST_CODE)
    }

    private fun guardarArchivo(extension: String, requestCode: Int) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_TITLE, "archivo$extension")
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val uri = data?.data ?: return

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {

                FILE_REQUEST_CODE -> {
                    val contenido = contentResolver.openInputStream(uri)
                        ?.bufferedReader().use { it?.readText() } ?: ""
                    editor.setText(contenido)
                }

                CREATE_FILE_FORM, CREATE_FILE_PKM -> {
                    val contenido = editor.text.toString()
                    contentResolver.openOutputStream(uri)?.use {
                        it.write(contenido.toByteArray())
                    }
                    Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
package com.example.formularioapp

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import com.example.formularioapp.backen.java.ErroresDeAnalizadores
import com.example.formularioapp.backen.kotlin.*
import com.example.formularioapp.backen.java.ast.*

class MainActivity : AppCompatActivity() {

    private lateinit var editor: EditText
    private lateinit var lineNumbers: TextView
    private lateinit var scrollEditor: ScrollView
    private lateinit var previewContainer: FrameLayout

    private lateinit var btnGenerar: MaterialButton
    private lateinit var btnSubir: MaterialButton
    private lateinit var btnReportes: MaterialButton
    private lateinit var btnGuardarForm: MaterialButton
    private lateinit var btnGuardarPKM: MaterialButton
    private lateinit var btnColorPicker: MaterialButton

    private val FILE_REQUEST_CODE = 1001
    private val CREATE_FILE_FORM = 2001
    private val CREATE_FILE_PKM = 2002

    private var erroresGenerados = arrayListOf<ErroresDeAnalizadores>()

    private val coloreado = Coloreado()
    private var isColoring = false

    private val analizadorManager = AnalizadorManager()
    private lateinit var formRenderer: FormRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editor = findViewById(R.id.editor)
        lineNumbers = findViewById(R.id.lineNumbers)
        scrollEditor = findViewById(R.id.scrollEditor)
        previewContainer = findViewById(R.id.previewContainer)

        btnGenerar = findViewById(R.id.btnGenerar)
        btnSubir = findViewById(R.id.btnSubir)
        btnReportes = findViewById(R.id.btnReportes)
        btnGuardarForm = findViewById(R.id.btnGuardarForm)
        btnGuardarPKM = findViewById(R.id.btnGuardarPKM)
        btnColorPicker = findViewById(R.id.btnColorPicker)

        formRenderer = FormRenderer(this)

        setupEditor()

        btnGenerar.setOnClickListener { generarFormulario() }

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
        btnColorPicker.setOnClickListener { abrirColorPicker() }
        val test = TextView(this)
        test.text = "PRUEBA DIRECTA DESDE ONCREATE"
        test.textSize = 20f

        previewContainer.addView(test)
    }

    // 🚀 GENERAR FORMULARIO
    private fun generarFormulario() {

        val resultado = analizadorManager.analizarCodigo(editor.text.toString())
        erroresGenerados = ArrayList(resultado.errores)

        val ast = resultado.resultado

        // 🔥 Mostrar AST
        val astTexto = imprimirAST(ast)
        mostrarASTDialog(astTexto)

        // 🔥 Limpiar preview
        previewContainer.removeAllViews()

        val contenedor = LinearLayout(this)
        contenedor.orientation = LinearLayout.VERTICAL
        previewContainer.addView(contenedor)

        val test = TextView(this)
        test.text = "TEST PREVIEW"
        contenedor.addView(test)

        // 🔥 Renderizar TODO el AST directamente
        if (ast is Nodo) {
            formRenderer.render(ast, contenedor)
        } else {
            Toast.makeText(this, "AST inválido", Toast.LENGTH_SHORT).show()
        }

        Toast.makeText(
            this,
            if (erroresGenerados.isEmpty()) "Formulario generado"
            else "${erroresGenerados.size} errores encontrados",
            Toast.LENGTH_SHORT
        ).show()
    }

    // 🎨 COLOR PICKER
    private fun abrirColorPicker() {
        ColorPickerDialog.Builder(this)
            .setTitle("Selecciona un color")
            .setPositiveButton("Seleccionar",
                ColorEnvelopeListener { envelope, _ ->

                    val color = envelope.color
                    val hex = "#%06X".format(0xFFFFFF and color)

                    insertarEnEditor(hex)
                })
            .setNegativeButton("Cancelar") { d, _ -> d.dismiss() }
            .show()
    }

    private fun insertarEnEditor(texto: String) {
        val start = editor.selectionStart
        editor.text.insert(start, texto)
    }

    // ✍️ EDITOR
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

    // 📂 ARCHIVOS
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

    // 🌳 AST VISUAL
    private fun imprimirAST(nodo: Any?, indent: String = ""): String {
        if (nodo == null) return "${indent}null\n"

        return when (nodo) {

            is NodoPrograma -> {
                var res = "${indent}NodoPrograma\n"
                nodo.instrucciones.forEach {
                    res += imprimirAST(it, indent + "  ")
                }
                res
            }

            is NodoSeccion -> {
                var res = "${indent}NodoSeccion\n"

                res += "${indent}  Atributos:\n"
                nodo.atributos.forEach { (k, v) ->
                    res += "${indent}    $k = ${formatearValor(v, indent + "    ")}\n"
                }

                res += "${indent}  Elementos:\n"
                nodo.elementos.forEach {
                    res += imprimirAST(it, indent + "    ")
                }

                res
            }

            is NodoTexto -> "${indent}Texto: ${nodo.contenido}\n"

            is NodoPreguntaAbierta -> "${indent}Pregunta: ${nodo.label}\n"

            is NodoPreguntaSeleccion -> {
                "${indent}Select: ${nodo.label} -> ${nodo.opciones}\n"
            }

            is NodoTabla -> {
                var res = "${indent}Tabla\n"
                nodo.filas.forEach {
                    it.forEach { celda ->
                        res += imprimirAST(celda, indent + "  ")
                    }
                }
                res
            }

            else -> "${indent}${nodo::class.java.simpleName}\n"
        }
    }

    private fun mostrarASTDialog(texto: String) {
        val tv = TextView(this)
        tv.text = texto
        tv.setPadding(20, 20, 20, 20)

        val scroll = ScrollView(this)
        scroll.addView(tv)

        AlertDialog.Builder(this)
            .setTitle("AST")
            .setView(scroll)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun formatearValor(valor: Any?, indent: String): String {
        return when (valor) {
            is Map<*, *> -> valor.entries.joinToString("\n") {
                "$indent${it.key} = ${formatearValor(it.value, indent + "  ")}"
            }
            is List<*> -> valor.joinToString(", ")
            else -> valor.toString()
        }
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
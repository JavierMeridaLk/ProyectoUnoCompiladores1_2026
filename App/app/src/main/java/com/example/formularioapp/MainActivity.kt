package com.example.formularioapp

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
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
    private lateinit var previewContainer: LinearLayout
    private lateinit var btnReportes: MaterialButton
    private lateinit var btnGuardarForm: MaterialButton
    private lateinit var btnGuardarPKM: MaterialButton
    private lateinit var btnGenerar: MaterialButton
    private lateinit var btnSubir: MaterialButton
    private lateinit var btnColorPicker: MaterialButton
    private lateinit var btnInsertarComponente: MaterialButton
    private val FILE_REQUEST_CODE = 1001
    private val INSERT_COMPONENT_REQUEST_CODE = 1002
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

        // Inicialización de vistas
        editor = findViewById(R.id.editor)
        lineNumbers = findViewById(R.id.lineNumbers)
        previewContainer = findViewById(R.id.previewContainer)
        btnGenerar = findViewById(R.id.btnGenerar)
        btnSubir = findViewById(R.id.btnSubir)
        btnReportes = findViewById(R.id.btnReportes)
        btnGuardarForm = findViewById(R.id.btnGuardarForm)
        btnGuardarPKM = findViewById(R.id.btnGuardarPKM)
        btnColorPicker = findViewById(R.id.btnColorPicker)
        btnInsertarComponente = findViewById(R.id.btnInsertarComponente)
        formRenderer = FormRenderer(this)
        setupEditor()
        // Listeners
        btnGenerar.setOnClickListener { generarFormulario() }
        btnSubir.setOnClickListener { abrirSelectorDeArchivo(FILE_REQUEST_CODE) }
        btnReportes.setOnClickListener { abrirReportes() }
        btnGuardarForm.setOnClickListener { guardarArchivo(".form", CREATE_FILE_FORM) }
        btnGuardarPKM.setOnClickListener { mostrarDialogoGuardarPKM() }
        btnColorPicker.setOnClickListener { abrirColorPicker() }
        btnInsertarComponente.setOnClickListener { abrirSelectorDeArchivo(INSERT_COMPONENT_REQUEST_CODE) }
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

    private fun generarFormulario() {
        val codigo = editor.text.toString()
        if (codigo.isEmpty()) return
        val resultado = analizadorManager.analizarCodigo(codigo)
        erroresGenerados = ArrayList(resultado.errores)
        val ast = resultado.resultado
        // vacias el contenedor
        previewContainer.removeAllViews()
        if (ast is Nodo) {
            formRenderer.render(ast, previewContainer)
        }else {
            Toast.makeText(this, "AST inválido o vacío", Toast.LENGTH_SHORT).show()
        }

        Toast.makeText(
            this,
            if (erroresGenerados.isEmpty()) "Formulario generado"
            else "${erroresGenerados.size} errores encontrados",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun mostrarDialogoGuardarPKM() {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(60, 40, 60, 20)
        }

        val inputAutor = EditText(this).apply { hint = "Nombre del autor (Obligatorio)" }
        val inputDesc = EditText(this).apply { hint = "Descripción (Opcional)" }

        layout.addView(inputAutor)
        layout.addView(inputDesc)

        AlertDialog.Builder(this)
            .setTitle("Guardar Archivo .pkm")
            .setMessage("Ingrese la información del autor para el encabezado")
            .setView(layout)
            .setPositiveButton("Guardar") { _, _ ->
                val autor = inputAutor.text.toString()
                val descripcion = inputDesc.text.toString()

                if (autor.isNotBlank()) {
                    // Obtener estadísticas y generar encabezado
                    val stats = coloreado.obtenerEstadisticas(editor.text.toString())
                    val encabezado = coloreado.generarEncabezado(autor, descripcion, stats)

                    // Insertar al inicio del editor
                    val textoFinal = encabezado + editor.text.toString()
                    editor.setText(textoFinal)

                    // Abrir selector para guardar
                    guardarArchivo(".pkm", CREATE_FILE_PKM)
                } else {
                    Toast.makeText(this, "El nombre del autor es obligatorio", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun abrirSelectorDeArchivo(requestCode: Int) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }
        startActivityForResult(intent, requestCode)
    }

    private fun guardarArchivo(extension: String, requestCode: Int) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            putExtra(Intent.EXTRA_TITLE, "nuevo_archivo$extension")
        }
        startActivityForResult(intent, requestCode)
    }

    private fun abrirReportes() {
        if (erroresGenerados.isEmpty()) {
            Toast.makeText(this, "No hay errores", Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(this, ReportesActivity::class.java)
            intent.putExtra("listaErrores", erroresGenerados)
            startActivity(intent)
        }
    }

    private fun abrirColorPicker() {
        ColorPickerDialog.Builder(this)
            .setTitle("Selecciona un color")
            .setPreferenceName("ColorPicker")
            .setPositiveButton("Seleccionar",
                ColorEnvelopeListener { envelope, _ ->
                    val color = envelope.color
                    val hex = "#%06X".format(0xFFFFFF and color)
                    val r = Color.red(color)
                    val g = Color.green(color)
                    val b = Color.blue(color)

                    val hsv = FloatArray(3)
                    Color.colorToHSV(color, hsv)
                    mostrarOpcionesColor(hex, r, g, b, hsv[0].toInt(), (hsv[1] * 100).toInt(), (hsv[2] * 100).toInt())
                })
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun mostrarOpcionesColor(hex: String, r: Int, g: Int, b: Int, h: Int, s: Int, v: Int) {
        val opciones = arrayOf("HEX: $hex", "RGB: ($r,$g,$b)", "HSV: <$h,$s,$v>")
        AlertDialog.Builder(this)
            .setTitle("Selecciona formato")
            .setItems(opciones) { _, which ->
                val textoInsertar = when (which) {
                    0 -> hex
                    1 -> "($r,$g,$b)"
                    else -> "<$h,$s,$v>"
                }
                insertarEnEditor(textoInsertar)
            }
            .show()
    }

    private fun insertarEnEditor(texto: String) {
        val start = editor.selectionStart
        editor.text.insert(start, texto)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return
        val uri = data?.data ?: return

        when (requestCode) {
            FILE_REQUEST_CODE -> {
                val contenido = contentResolver.openInputStream(uri)?.bufferedReader().use { it?.readText() } ?: ""
                editor.setText(contenido)
            }
            INSERT_COMPONENT_REQUEST_CODE -> {
                val contenido = contentResolver.openInputStream(uri)?.bufferedReader().use { it?.readText() } ?: ""
                editor.text.insert(editor.selectionStart, "\n$contenido\n")
            }
            CREATE_FILE_FORM, CREATE_FILE_PKM -> {
                contentResolver.openOutputStream(uri)?.use { it.write(editor.text.toString().toByteArray()) }
                Toast.makeText(this, "Archivo guardado exitosamente", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun imprimirAST(nodo: Any?, indent: String = ""): String {
        if (nodo == null) return "${indent}null\n"
        return when (nodo) {
            is NodoPrograma -> {
                var res = "${indent}NodoPrograma\n"
                nodo.instrucciones.forEach { res += imprimirAST(it, indent + "  ") }
                res
            }
            is NodoSeccion -> {
                var res = "${indent}NodoSeccion\n"
                res += "${indent}  Atributos:\n"
                nodo.atributos.forEach { (k, v) -> res += "${indent}    $k = $v\n" }
                res += "${indent}  Elementos:\n"
                nodo.elementos.forEach { res += imprimirAST(it, indent + "    ") }
                res
            }
            is NodoTexto -> "${indent}Texto: ${nodo.atributos["content"]}\n"
            is NodoPreguntaAbierta -> "${indent}Pregunta: ${nodo.label}\n"
            is NodoPreguntaSeleccion -> "${indent}Select: ${nodo.label}\n"
            else -> "${indent}${nodo::class.java.simpleName}\n"
        }
    }

    private fun mostrarASTDialog(texto: String) {
        val tv = TextView(this).apply {
            text = texto
            setPadding(30, 30, 30, 30)
        }
        val scroll = ScrollView(this).apply { addView(tv) }
        AlertDialog.Builder(this)
            .setTitle("Árbol AST")
            .setView(scroll)
            .setPositiveButton("Cerrar", null)
            .show()
    }
}
package com.example.formularioapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var editor: EditText
    private lateinit var lineNumbers: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editor = findViewById(R.id.editor)
        lineNumbers = findViewById(R.id.lineNumbers)

        setupEditor()
    }

    private fun setupEditor() {
        editor.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                updateLineNumbers()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        updateLineNumbers()
    }

    private fun updateLineNumbers() {
        val text = editor.text.toString()
        val lines = if (text.isEmpty()) 1 else text.split("\n").size

        val numbers = StringBuilder()
        for (i in 1..lines) {
            numbers.append(i).append("\n")
        }

        lineNumbers.text = numbers.toString()
    }
}
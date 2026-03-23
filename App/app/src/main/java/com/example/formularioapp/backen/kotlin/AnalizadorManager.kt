package com.example.formularioapp.backen.kotlin

import com.example.formularioapp.backen.java.ErroresDeAnalizadores
import com.example.formularioapp.backen.java.ast.Nodo
import com.example.formularioapp.backen.java.lexer.AnalizadorLexicoCF
import com.example.formularioapp.backen.java.lexer.LexerPKM
import com.example.formularioapp.backen.java.parser.parser
import com.example.formularioapp.backen.java.parser.parserPKM
import java.io.StringReader

class AnalizadorManager {

    fun analizarCodigo(codigo: String): ResultadoAnalisis {

        return try {
            val erroresTotales = mutableListOf<ErroresDeAnalizadores>()
            var ast: Nodo? = null

            if (codigo.startsWith("###") || codigo.startsWith("<")) {

                val readerPKM = StringReader(codigo)
                val lexerPKM = LexerPKM(readerPKM)
                val parserPKM = parserPKM(lexerPKM)

                val resultado = parserPKM.parse()

                erroresTotales.addAll(lexerPKM.getErrores())
                erroresTotales.addAll(parserPKM.getErrores())

                ast = resultado.value as? Nodo

            } else {

                val readerCF = StringReader(codigo)
                val lexerCF = AnalizadorLexicoCF(readerCF)
                val parserCF = parser(lexerCF)

                val resultado = parserCF.parse()

                erroresTotales.addAll(lexerCF.getErrores())
                erroresTotales.addAll(parserCF.getErrores())

                ast = resultado.value as? Nodo
            }

            ResultadoAnalisis(
                errores = erroresTotales,
                resultado = ast
            )

        } catch (e: Exception) {

            ResultadoAnalisis(
                errores = listOf(
                    ErroresDeAnalizadores(
                        "EXCEPCION",
                        "FATAL",
                        "Analizador",
                        e.message ?: "Error desconocido",
                        0,
                        0
                    )
                ),
                resultado = null
            )
        }
    }
}
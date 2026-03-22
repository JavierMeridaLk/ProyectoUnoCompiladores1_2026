package com.example.formularioapp.backen.kotlin

import com.example.formularioapp.backen.java.ErroresDeAnalizadores
import com.example.formularioapp.backen.java.lexer.AnalizadorLexicoCF
import com.example.formularioapp.backen.java.lexer.LexerPKM
import com.example.formularioapp.backen.java.parser.parser
import com.example.formularioapp.backen.java.parser.parserPKM
import java.io.StringReader

class AnalizadorManager {

    fun analizarCodigo(codigo: String): ResultadoAnalisis {
        // Declarar todos los analizadores y parsers
        val lexerPKM: LexerPKM
        val parserPKM: parserPKM
        val lexerCF: AnalizadorLexicoCF
        val parserCF: parser

        return try {
            val erroresTotales = mutableListOf<ErroresDeAnalizadores>()

            if (codigo.startsWith("###") || codigo.startsWith("<")) {
                // Código de tipo .pkm
                val readerPKM = StringReader(codigo)
                lexerPKM = LexerPKM(readerPKM)
                parserPKM = parserPKM(lexerPKM)

                try {
                    parserPKM.parse()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                // Agregar errores generados por PKM
                erroresTotales.addAll(lexerPKM.getErrores())
                erroresTotales.addAll(parserPKM.getErrores())

            } else {
                // Código de tipo .form
                val readerCF = StringReader(codigo)
                lexerCF = AnalizadorLexicoCF(readerCF)
                parserCF = parser(lexerCF)

                try {
                    parserCF.parse()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                // Agregar errores generados por CF
                erroresTotales.addAll(lexerCF.getErrores())
                erroresTotales.addAll(parserCF.getErrores())
            }

            // Retornar lista de errores y null como resultado
            ResultadoAnalisis(errores = erroresTotales, resultado = null)

        } catch (e: Exception) {
            // manejo de errores inesperados
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
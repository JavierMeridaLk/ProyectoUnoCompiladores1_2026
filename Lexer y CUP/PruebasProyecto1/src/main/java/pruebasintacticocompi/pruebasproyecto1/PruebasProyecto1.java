package pruebasintacticocompi.pruebasproyecto1;

import java.io.StringReader;

// ===== CF =====
import lexer.AnalizadorLexicoCF;
import parser.parser;

// ===== PKM =====
import lexer.LexerPKM;
import parser.parserPKM;

public class PruebasProyecto1 {

    public static void main(String[] args) {

        // =========================================================
        // 🔵 PRIMER LENGUAJE (CF)
        // =========================================================
        String entradaCF = """
        number x = 10;

        /* ERROR LEXICO */
        @@@

        /* ERROR SINTACTICO */
        IF (x > ) {
            TEXT [ content: "Hola" ]
        }
        """;

        StringReader readerCF = new StringReader(entradaCF);
        AnalizadorLexicoCF lexerCF = new AnalizadorLexicoCF(readerCF);
        parser parserCF = new parser(lexerCF);

        try {
            parserCF.parse();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("=================================");
        System.out.println("=== ERRORES CF ===");

        System.out.println("=== LEXICOS ===");
        for (ErroresDeAnalizadores err : lexerCF.getErrores()) {
            System.out.println(
                "- Lexema: " + err.getLexema() +
                " | Tipo: " + err.getTipo() +
                " | Lenguaje: " + err.getLenguaje() +
                " | Fila: " + err.getLine() +
                " | Col: " + err.getColm()
            );
        }

        System.out.println("\n=== SINTACTICOS ===");
        for (ErroresDeAnalizadores err : parserCF.getErrores()) {
            System.out.println(
                "- Lexema: " + err.getLexema() +
                " | Tipo: " + err.getTipo() +
                " | Lenguaje: " + err.getLenguaje() +
                " | Fila: " + err.getLine() +
                " | Col: " + err.getColm()
            );
        }


        // =========================================================
        // 🟢 SEGUNDO LENGUAJE (PKM)
        // =========================================================
        String entradaPKM = """
        ###
        Author: Javier
        Fecha: 10/10/2025
        Hora: 10:30
        Description: "Formulario de prueba @[:smile:]"
        Total de Secciones: 1
        Total de Preguntas: 2
        Abiertas: 1
        Desplegables: 1
        Selección: 0
        Múltiples: 0
        ###

        <style>
            <color=#FF00FF/>
            <background=(10,10,10)/>
            <font family=MONO/>
            <text size=12/>
        </style>

        <section=100,100,0,0,VERTICAL>
            <content>

                <!-- TEXTO CORRECTO -->
                <text=50,20,"Hola mundo @[:star:1:]"/>

                <!-- ERROR LEXICO -->
                @@@

                <!-- ERROR SINTACTICO -->
                <open=50,20,"Pregunta abierta">

                <!-- ERROR: falta cerrar etiqueta -->
                <drop=50,20,"Pregunta",{"A","B"},0

            </content>
        </section>
        """;

        StringReader readerPKM = new StringReader(entradaPKM);
        LexerPKM lexerPKM = new LexerPKM(readerPKM);
        parserPKM parserPKM = new parserPKM(lexerPKM);

        try {
            parserPKM.parse();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("\n=================================");
        System.out.println("=== ERRORES PKM ===");

        System.out.println("=== LEXICOS ===");
        for (ErroresDeAnalizadores err : lexerPKM.getErrores()) {
            System.out.println(
                "- Lexema: " + err.getLexema() +
                " | Tipo: " + err.getTipo() +
                " | Lenguaje: " + err.getLenguaje() +
                " | Fila: " + err.getLine() +
                " | Col: " + err.getColm()
            );
        }

        System.out.println("\n=== SINTACTICOS ===");
        for (ErroresDeAnalizadores err : parserPKM.getErrores()) {
            System.out.println(
                "- Lexema: " + err.getLexema() +
                " | Tipo: " + err.getTipo() +
                " | Lenguaje: " + err.getLenguaje() +
                " | Fila: " + err.getLine() +
                " | Col: " + err.getColm()
            );
        }
    }
}
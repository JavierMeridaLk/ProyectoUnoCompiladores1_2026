package pruebasintacticocompi.pruebasproyecto1;

import java.io.StringReader;
import java_cup.runtime.Scanner;
import lexer.AnalizadorLexicoCF;
import parser.parser;   // 👈 IMPORTANTE
import parser.sym;

public class PruebasProyecto1 {

    public static void main(String[] args) {

        String entrada = """
            SECTION [
                nombre: "Formulario";
                edad: 25;

                DRAW: {
                    TEXT [titulo: "Bienvenido 😊"],
                    OPEN_QUESTION [q1: "¿Cómo te llamas?"],
                    SELECT_QUESTION [],
                    MULTIPLE_QUESTION []
                }
            ]

            number x = 10;
            string nombre = "Javier";
            
            x = 10 + 20 * 3;

            IF (x > 5) {
                SECTION [
                    mensaje: "Mayor";
                ]
            } ELSE {
                SECTION [
                    mensaje: "Menor";
                ]
            }

            FOR (i = 0; i < 5; i = i + 1) {
                SECTION [
                    iteracion: "ok";
                ]
            }

            // ❌ ERRORES LÉXICOS
            @@@
                         _
                         
            #ZZZZZZ
            "cadena sin cerrar
                         
            
            // ❌ ERRORES SINTÁCTICOS
            SECTION [
                nombre "Falta dos puntos"
                edad: ;
            ]
        """;
        
        StringReader reader = new StringReader(entrada);
        AnalizadorLexicoCF lexer = new AnalizadorLexicoCF(reader);
        try {
            while (lexer.next_token().sym != sym.EOF) {
                // solo consumir tokens
            }
        } catch (Exception e) {
            e.printStackTrace();
}
        

       
            System.out.println("=== ERRORES LEXICOS ===");
            for (ErroresDeAnalizadores err : lexer.getErrores()) {
                System.out.println(
                    "- Lexema: " + err.getLexema() +
                    " | Tipo: " + err.getTipo() +
                    " | Fila: " + err.getLine() +
                    " | Col: " + err.getColm()
                );
            }

   


    }
}
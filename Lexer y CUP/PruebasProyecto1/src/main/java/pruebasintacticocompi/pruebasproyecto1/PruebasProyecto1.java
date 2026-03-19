package pruebasintacticocompi.pruebasproyecto1;

import java.io.StringReader;
import java_cup.runtime.Scanner;
import lexer.AnalizadorLexicoCF;
import parser.parser;   // 👈 IMPORTANTE
import parser.sym;

public class PruebasProyecto1 {

    public static void main(String[] args) {

        String entrada = """
                         
                          @@@
                                 #ZZZZZZ
                         
        /* ================================
           PROGRAMA CORRECTO
        ================================ */
                          @@@
                                                          #ZZZZZZ
        number widthGlobal = 800;
        number heightGlobal = 600;
                          @@@
                                                          #ZZZZZZ
        string titulo = "Formulario @[:star:2:]";
 @@@
                                 #ZZZZZZ
        /* special con comodines */
        special pregunta1 = OPEN_QUESTION [
            width: ?,
            height: 100 + ?,
            label: "¿Cómo te llamas?"
        ];

        /* sección válida */
        SECTION [
            width: widthGlobal,
            height: heightGlobal,
            pointX: 10,
            pointY: 20,
            orientation: VERTICAL,

            elements: {
                TEXT [
                    content: "Bienvenido @[:smile:]",
                    width: 200,
                    height: 50
                ],

                pregunta1,

                DROP_QUESTION [
                    label: "Elige un pokemon",
                    options: { POKEMON },
                    correct: 1
                ],

                SELECT_QUESTION [
                    options: { "Rojo", "Azul", "Verde" },
                    correct: 2
                ],

                MULTIPLE_QUESTION [
                    options: { "A", "B", "C" },
                    correct: {1,2}
                ]
            }
        ]

        /* draw con comodines */
        pregunta1.draw(50, 10);

        /* expresiones válidas */
        number x = 10 + 20 * 3;
        number y = (x ^ 2) % 5;

        /* lógica válida */
        IF (x > 5 && x < 100) {
            TEXT [ content: "Mayor" ]
        } ELSE {
            TEXT [ content: "Menor" ]
        }

        /* for clásico */
        FOR (i = 0; i < 5; i = i + 1) {
            TEXT [ content: "Loop" ]
        }

        /* for rango */
        FOR (j in 1 .. 3) {
            TEXT [ content: "Rango" ]
        }

        /* ================================
           ❌ ERRORES LÉXICOS
        ================================ */

       
        

        /* ================================
           ❌ ERRORES SINTÁCTICOS
        ================================ */

        /* falta dos puntos */
        SECTION [
            width 100,
            height: ;
        ]

        /* mal uso de operadores lógicos (mezcla) */
        IF (10 > 5 && 3 < 2 || 1 == 1) {
            TEXT [ content: "Error lógico" ]
        }

        /* draw mal */
        pregunta1.draw(10);

        /* tabla mal formada */
        TABLE [
            width: 100,
            height: 100,
            pointX: 0,
            pointY: 0,
            elements: {
                [ TEXT [ content: "A" ], TEXT [ content: "B" ] ],
                [ TEXT [ content: "C" ]  // ❌ falta cerrar
            }
        ]
        """;
        
        StringReader reader = new StringReader(entrada);
        AnalizadorLexicoCF lexer = new AnalizadorLexicoCF(reader);

        parser p = new parser(lexer);

        try {
            p.parse(); // 👈 IMPORTANTE: ejecuta el parser
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

        System.out.println("\n=== ERRORES SINTACTICOS ===");
        for (ErroresDeAnalizadores err : p.getErrores()) {
            System.out.println(
                "- Lexema: " + err.getLexema() +
                " | Tipo: " + err.getTipo() +
                " | Fila: " + err.getLine() +
                " | Col: " + err.getColm()
            );
        }


    }
}
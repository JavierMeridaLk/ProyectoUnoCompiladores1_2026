/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package pruebasintacticocompi.pruebasproyecto1;
import java.io.StringReader;
import lexer.AnalizadorLexicoCF;

/**
 *
 * @author xavi
 */
public class PruebasProyecto1 {

    public static void main(String[] args) {
        
        String entrada = """
            SECTION {
                         @[<<<<<<<<<<33333333333333333]
                number edad = 25
                string nombre = "Javier 😊"
                special code = @[:smile:]
            
                TEXT "Bienvenido al sistema @[:)]"
                

                color1 = #FFAACC
                color2 = (255,128,64)
                color3 = <120,50,50>
            

                @[:)]
                @[:((]
                @[:||||]
                @[<3]
                @[<<333]
                @[:star:]
                @[:star:5:]
                @[:star-10:]
                @[:cat:]
            

                resultado = 10 + 20 * 3 / 2 ^ 2 % 5
            

                IF edad >= 18 {
                    TEXT "Mayor de edad"
                } ELSE {
                    TEXT "Menor de edad"
                }
            

                FOR i in 1 .. 5 {
                    TEXT "Iteración"
                }
            
                WHILE edad > 0 {
                    edad = edad - 1
                }
            

            
                @              
                @[:)           
                @[<3abc]       
                @[:star:]abc   
                123.           
                .45            
                #ZZZZZZ        
                (256,300,999)  
                <a,b,c>        
                "cadena sin cerrar
                $ comentario mal cerrado /*
            
                ???           
            }
        """;

        AnalizadorLexicoCF lexer = new AnalizadorLexicoCF(new StringReader(entrada));

        try {
            while (lexer.yylex() != -1) {
                // No hacemos nada aquí porque el lexer ya imprime
            }
            System.out.println("=== FIN DEL ANALISIS ===");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


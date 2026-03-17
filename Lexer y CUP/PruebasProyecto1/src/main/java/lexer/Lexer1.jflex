package lexer;
//import java_cup.runtime.*;
import java.util.List;
import java.util.ArrayList;
import pruebasintacticocompi.pruebasproyecto1.Error;

%%

%public
%unicode
%class AnalizadorLexicoCF
%line
%column
%type int

%{
    private List<Error> listaDeErrores = new ArrayList();

    public List<Error> getErrores() {
        return listaDeErrores;
    }

    private void agregarError(String lexema) {
        listaDeErrores.add(new Error(lexema,"Lexico", yyline + 1, yycolumn + 1));
    }    

%}

//*****Definicion de expresiones regulares*****
DIGITO = [0-9]
LETRA = [a-zA-Z]
IDENTIFICADOR = {LETRA}({LETRA}|{DIGITO}|_)*
ESPACIOS = [ \t\r\n]+
ENTERO = {DIGITO}+
DECIMAL = {DIGITO}+"."{DIGITO}+
STRING = \"([^\"\\]|\\.)*\"

// Comentarios
COMENTARIO_LINEA = "$".*
COMENTARIO_BLOQUE = "/*"([^*]|\*+[^*/])*\*+"/"

// Colores
HEX = "#"([0-9A-Fa-f]{6})
RGB = \({ENTERO},{ENTERO},{ENTERO}\)
HSL = "<"({ENTERO}","{ENTERO}","{ENTERO})">"

// Emojis
SMILE   = @\[:\)+\] | @\[:smile:\]
SAD     = @\[:\(+\] | @\[:sad:\]
SERIOUS = @\[:\|+\] | @\[:serious:\]
HEART   = @\[<+3+\] | @\[:heart:\]
STAR    = @\[:star:\] 
        | @\[:star:{ENTERO}:\] 
        | @\[:star-{ENTERO}:\]
CAT     = @\[:\^\^:\] | @\[:cat:\]

%%

//******** PALABRAS RESERVADAS ********

// Tipos
"number"            { System.out.println("TOKEN: NUMBER_TYPE"); }
"string"            { System.out.println("TOKEN: STRING_TYPE"); }
"special"           { System.out.println("TOKEN: SPECIAL_TYPE"); }

// Estructuras
"SECTION"           { System.out.println("TOKEN: SECTION"); }
"TEXT"              { System.out.println("TOKEN: TEXT"); }

// Preguntas
"OPEN_QUESTION"     { System.out.println("TOKEN: OPEN_QUESTION"); }
"DROP_QUESTION"     { System.out.println("TOKEN: DROP_QUESTION"); }
"SELECT_QUESTION"   { System.out.println("TOKEN: SELECT_QUESTION"); }
"MULTIPLE_QUESTION" { System.out.println("TOKEN: MULTIPLE_QUESTION"); }

// Control
"IF"                { System.out.println("TOKEN: IF"); }
"ELSE"              { System.out.println("TOKEN: ELSE"); }
"WHILE"             { System.out.println("TOKEN: WHILE"); }
"DO"                { System.out.println("TOKEN: DO"); }
"FOR"               { System.out.println("TOKEN: FOR"); }
"in"                { System.out.println("TOKEN: IN"); }

// Orientación
"VERTICAL"          { System.out.println("TOKEN: VERTICAL"); }
"HORIZONTAL"        { System.out.println("TOKEN: HORIZONTAL"); }

// Estilos
"MONO"              { System.out.println("TOKEN: MONO"); }
"SANS_SERIF"        { System.out.println("TOKEN: SANS_SERIF"); }
"CURSIVE"           { System.out.println("TOKEN: CURSIVE"); }

// Bordes
"LINE"              { System.out.println("TOKEN: LINE"); }
"DOTTED"            { System.out.println("TOKEN: DOTTED"); }
"DOUBLE"            { System.out.println("TOKEN: DOUBLE"); }

// Colores base
"RED"               { System.out.println("TOKEN: COLOR_RED"); }
"BLUE"              { System.out.println("TOKEN: COLOR_BLUE"); }
"GREEN"             { System.out.println("TOKEN: COLOR_GREEN"); }
"PURPLE"            { System.out.println("TOKEN: COLOR_PURPLE"); }
"SKY"               { System.out.println("TOKEN: COLOR_SKY"); }
"YELLOW"            { System.out.println("TOKEN: COLOR_YELLOW"); }
"BLACK"             { System.out.println("TOKEN: COLOR_BLACK"); }
"WHITE"             { System.out.println("TOKEN: COLOR_WHITE"); }

//******** OPERADORES ********

// Aritméticos
"+"                 { System.out.println("TOKEN: SUMA"); }
"-"                 { System.out.println("TOKEN: RESTA"); }
"*"                 { System.out.println("TOKEN: MULTIPLICACION"); }
"/"                 { System.out.println("TOKEN: DIVISION"); }
"^"                 { System.out.println("TOKEN: POTENCIA"); }
"%"                 { System.out.println("TOKEN: MODULO"); }

// Comparación
">="                { System.out.println("TOKEN: MAYOR_IGUAL"); }
"<="                { System.out.println("TOKEN: MENOR_IGUAL"); }
">"                 { System.out.println("TOKEN: MAYOR"); }
"<"                 { System.out.println("TOKEN: MENOR"); }
"=="                { System.out.println("TOKEN: IGUAL"); }
"!!"                { System.out.println("TOKEN: DIFERENTE"); }
"="                { System.out.println("TOKEN: ASIGNACION"); }

// Lógicos
"||"                { System.out.println("TOKEN: OR"); }
"&&"                { System.out.println("TOKEN: AND"); }
"~"                 { System.out.println("TOKEN: NOT"); }

//******** SIGNOS ********
"("                 { System.out.println("TOKEN: ("); }
")"                 { System.out.println("TOKEN: )"); }
"{"                 { System.out.println("TOKEN: {"); }
"}"                 { System.out.println("TOKEN: }"); }
"["                 { System.out.println("TOKEN: ["); }
"]"                 { System.out.println("TOKEN: ]"); }
","                 { System.out.println("TOKEN: COMA"); }
":"                 { System.out.println("TOKEN: DOS_PUNTOS"); }
";"                 { System.out.println("TOKEN: PUNTO_COMA"); }
".."                { System.out.println("TOKEN: RANGO"); }

//******** VALORES ********

{HEX}               { System.out.println("TOKEN: COLOR_HEX -> " + yytext()); }
{RGB}               { System.out.println("TOKEN: COLOR_RGB -> " + yytext()); }
{HSL}               { System.out.println("TOKEN: COLOR_HSL -> " + yytext()); }

{STRING}            { System.out.println("TOKEN: STRING -> " + yytext()); }
{DECIMAL}           { System.out.println("TOKEN: DECIMAL -> " + yytext()); }
{ENTERO}            { System.out.println("TOKEN: ENTERO -> " + yytext()); }

{SMILE}             { System.out.println("EMOJI: 😊"); }
{SAD}               { System.out.println("EMOJI: 😢"); }
{SERIOUS}           { System.out.println("EMOJI: 😐"); }
{HEART}             { System.out.println("EMOJI: ❤️"); }
{STAR}              { System.out.println("EMOJI: ⭐"); }
{CAT}               { System.out.println("EMOJI: 🐱"); }

{IDENTIFICADOR}     { System.out.println("TOKEN: IDENTIFICADOR -> " + yytext()); }

//******** IGNORADOS ********

{COMENTARIO_LINEA}  { /* ignorar */ }
{COMENTARIO_BLOQUE} { /* ignorar */ }
{ESPACIOS}          { /* ignorar */ }

//******** ERROR LEXICO ********

.                   {agregarError(yytext()); }

<<EOF>> { return -1; }
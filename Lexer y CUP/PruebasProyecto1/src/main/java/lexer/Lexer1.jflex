package lexer;

import java_cup.runtime.Symbol;
import java.util.List;
import parser.sym;
import java.util.ArrayList;
import pruebasintacticocompi.pruebasproyecto1.ErroresDeAnalizadores;

%%
%public
%unicode
%class AnalizadorLexicoCF
%cup
%line
%column
%state DENTRO_CADENA

%{
    private List<ErroresDeAnalizadores> listaDeErrores = new ArrayList<>();

    public List<ErroresDeAnalizadores> getErrores() {
        return listaDeErrores;
    }

    private void agregarError(String lexema) {
        listaDeErrores.add(new ErroresDeAnalizadores(lexema,"Lexico", yyline + 1, yycolumn + 1));
    }

    private Symbol symbol(int tipo) {
        String nombre = nombreToken(tipo);
        String texto = yytext();

        System.out.println("TOKEN -> " + nombre + " | LEXEMA -> '" + texto + "'");

        return new Symbol(tipo, yyline + 1, yycolumn + 1);
    }

    private Symbol symbol(int tipo, Object valor) {
        String nombre = nombreToken(tipo);
        String texto = yytext();

        System.out.println("TOKEN -> " + nombre + " | LEXEMA -> '" + texto + "' | VALOR -> " + valor);

        return new Symbol(tipo, yyline + 1, yycolumn + 1, valor);
    }

    private String nombreToken(int tipo){
        switch(tipo){
            case sym.NUMBER_TYPE: return "NUMBER_TYPE";
            case sym.STRING_TYPE: return "STRING_TYPE";
            case sym.SPECIAL_TYPE: return "SPECIAL_TYPE";

            case sym.SECTION: return "SECTION";
            case sym.TABLE: return "TABLE";
            case sym.TEXT: return "TEXT";

            case sym.OPEN_QUESTION: return "OPEN_QUESTION";
            case sym.DROP_QUESTION: return "DROP_QUESTION";
            case sym.SELECT_QUESTION: return "SELECT_QUESTION";
            case sym.MULTIPLE_QUESTION: return "MULTIPLE_QUESTION";

            case sym.WIDTH: return "WIDTH";
            case sym.HEIGHT: return "HEIGHT";
            case sym.POINTX: return "POINTX";
            case sym.POINTY: return "POINTY";

            case sym.IF: return "IF";
            case sym.ELSE: return "ELSE";
            case sym.WHILE: return "WHILE";
            case sym.DO: return "DO";
            case sym.FOR: return "FOR";

            case sym.SUMA: return "SUMA";
            case sym.RESTA: return "RESTA";
            case sym.MULTIPLICACION: return "MULTIPLICACION";
            case sym.DIVISION: return "DIVISION";

            case sym.ENTERO: return "ENTERO";
            case sym.DECIMAL: return "DECIMAL";
            case sym.IDENTIFICADOR: return "IDENTIFICADOR";

            case sym.COMILLA: return "COMILLA";
            case sym.TEXTO: return "TEXTO";
            case sym.EMOJI: return "EMOJI";

            case sym.EOF: return "EOF";

            default: return "TOKEN_" + tipo;
        }
    }
%}

//***** EXPRESIONES REGULARES *****
DIGITO = [0-9]
LETRA = [a-zA-Z]
IDENTIFICADOR = {LETRA}({LETRA}|{DIGITO}|_)*
ESPACIOS = [ \t\r\n]+

ENTERO = {DIGITO}+
DECIMAL = {DIGITO}+"."{DIGITO}+

// CADENA
CADENA = \"

// Comentarios
COMENTARIO_LINEA = "$".*
COMENTARIO_BLOQUE = "/*"([^*]|\*+[^*/])*\*+"/"

// Colores
HEX = "#"([0-9A-Fa-f]{6})
RGB = \({ENTERO},{ENTERO},{ENTERO}\)
HSL = "<"({ENTERO}","{ENTERO}","{ENTERO})">"

// Emojis
EMOJI = @\[:\)+\] 
      | @\[:smile:\]
      | @\[:\(+\]
      | @\[:sad:\]
      | @\[:\|+\]
      | @\[:serious:\]
      | @\[<+3+\]
      | @\[:heart:\]
      | @\[:star:\] 
      | @\[:star:{ENTERO}:\] 
      | @\[:star-{ENTERO}:\]
      | @\[:\^\^:\] 
      | @\[:cat:\]

%%


// Estado inciaña

<YYINITIAL> {

//******** PALABRAS RESERVADAS ********
"number"            { return symbol(sym.NUMBER_TYPE); }
"string"            { return symbol(sym.STRING_TYPE); }
"special"           { return symbol(sym.SPECIAL_TYPE); }

"SECTION"           { return symbol(sym.SECTION); }
"TABLE"             { return symbol(sym.TABLE); }
"TEXT"              { return symbol(sym.TEXT); }

"OPEN_QUESTION"     { return symbol(sym.OPEN_QUESTION); }
"DROP_QUESTION"     { return symbol(sym.DROP_QUESTION); }
"SELECT_QUESTION"   { return symbol(sym.SELECT_QUESTION); }
"MULTIPLE_QUESTION" { return symbol(sym.MULTIPLE_QUESTION); }

// atributos
"width"             { return symbol(sym.WIDTH); }
"height"            { return symbol(sym.HEIGHT); }
"pointX"            { return symbol(sym.POINTX); }
"pointY"            { return symbol(sym.POINTY); }
"orientation"       { return symbol(sym.ORIENTATION); }
"elements"          { return symbol(sym.ELEMENTS); }
"styles"            { return symbol(sym.STYLES); }
"label"             { return symbol(sym.LABEL); }
"content"           { return symbol(sym.CONTENT); }
"options"           { return symbol(sym.OPTIONS); }
"correct"           { return symbol(sym.CORRECT); }

// control
"IF"                { return symbol(sym.IF); }
"ELSE"              { return symbol(sym.ELSE); }
"WHILE"             { return symbol(sym.WHILE); }
"DO"                { return symbol(sym.DO); }
"FOR"               { return symbol(sym.FOR); }
"in"                { return symbol(sym.IN); }

// estilos
"VERTICAL"          { return symbol(sym.VERTICAL); }
"HORIZONTAL"        { return symbol(sym.HORIZONTAL); }

"MONO"              { return symbol(sym.MONO); }
"SANS_SERIF"        { return symbol(sym.SANS_SERIF); }
"CURSIVE"           { return symbol(sym.CURSIVE); }

"LINE"              { return symbol(sym.LINE); }
"DOTTED"            { return symbol(sym.DOTTED); }
"DOUBLE"            { return symbol(sym.DOUBLE); }

// colores base
"RED"               { return symbol(sym.COLOR_RED); }
"BLUE"              { return symbol(sym.COLOR_BLUE); }
"GREEN"             { return symbol(sym.COLOR_GREEN); }
"PURPLE"            { return symbol(sym.COLOR_PURPLE); }
"SKY"               { return symbol(sym.COLOR_SKY); }
"YELLOW"            { return symbol(sym.COLOR_YELLOW); }
"BLACK"             { return symbol(sym.COLOR_BLACK); }
"WHITE"             { return symbol(sym.COLOR_WHITE); }

// funciones
"draw"              { return symbol(sym.DRAW); }
"who_is_that_pokemon" { return symbol(sym.POKEMON); }

//******** OPERADORES ********
"+"   { return symbol(sym.SUMA); }
"-"   { return symbol(sym.RESTA); }
"*"   { return symbol(sym.MULTIPLICACION); }
"/"   { return symbol(sym.DIVISION); }
"^"   { return symbol(sym.POTENCIA); }
"%"   { return symbol(sym.MODULO); }

">="  { return symbol(sym.MAYOR_IGUAL); }
"<="  { return symbol(sym.MENOR_IGUAL); }
">"   { return symbol(sym.MAYOR); }
"<"   { return symbol(sym.MENOR); }
"=="  { return symbol(sym.IGUAL); }
"!!"  { return symbol(sym.DIFERENTE); }
"="   { return symbol(sym.ASIGNACION); }

"||"  { return symbol(sym.OR); }
"&&"  { return symbol(sym.AND); }
"~"   { return symbol(sym.NOT); }

//******** SIGNOS ********
"?"   { return symbol(sym.INTERROGACION_ABIERTA); }
"("   { return symbol(sym.PARENTESIS_ABRE); }
")"   { return symbol(sym.PARENTESIS_CIERRA); }
"{"   { return symbol(sym.LLAVE_ABRE); }
"}"   { return symbol(sym.LLAVE_CIERRA); }
"["   { return symbol(sym.CORCHETE_ABRE); }
"]"   { return symbol(sym.CORCHETE_CIERRA); }
","   { return symbol(sym.COMA); }
":"   { return symbol(sym.DOS_PUNTOS); }
";"   { return symbol(sym.PUNTO_COMA); }
".."  { return symbol(sym.RANGO); }
"."   { return symbol(sym.PUNTO); }

//******** CADENAS ********
\" { yybegin(DENTRO_CADENA); return symbol(sym.COMILLA); }

//******** VALORES ********
{HEX}     { return symbol(sym.COLOR_HEX, yytext()); }
{RGB}     { return symbol(sym.COLOR_RGB, yytext()); }
{HSL}     { return symbol(sym.COLOR_HSL, yytext()); }

{DECIMAL} { return symbol(sym.DECIMAL, Double.parseDouble(yytext())); }
{ENTERO}  { return symbol(sym.ENTERO, Integer.parseInt(yytext())); }

//******** IDENTIFICADOR ********
{IDENTIFICADOR} { return symbol(sym.IDENTIFICADOR, yytext()); }

//******** IGNORADOS ********
{ESPACIOS}          { }
{COMENTARIO_LINEA}  { }
{COMENTARIO_BLOQUE} { }

//******** ERROR ********
. { agregarError(yytext()); }
}

// Estado de cameda

<DENTRO_CADENA> {

    {EMOJI} { return symbol(sym.EMOJI, yytext()); }

    [^\"@\n]+ { return symbol(sym.TEXTO, yytext()); }

    "@" { return symbol(sym.TEXTO, yytext()); }

    \" { 
        yybegin(YYINITIAL); 
        return symbol(sym.COMILLA); 
    }

    \n {
        agregarError("Cadena no cerrada");
        yybegin(YYINITIAL);
    }

    <<EOF>> {
        agregarError("Cadena no cerrada");
        return symbol(sym.EOF);
    }

    . { agregarError(yytext()); }
}

//******** EOF ********
<<EOF>> { return symbol(sym.EOF); }
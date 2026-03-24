package com.example.formularioapp.backen.java.lexer;

import java_cup.runtime.Symbol;
import java.util.List;
import parser.sym;
import java.util.ArrayList;
import com.example.formularioapp.backen.java.ErroresDeAnalizadores; 

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
        listaDeErrores.add(new ErroresDeAnalizadores(
            lexema,
            "Léxico",
            "Formulario",
            "Símbolo no reconocido en el lenguaje",
            yyline + 1,
            yycolumn + 1
        ));
    }

    private Symbol symbol(int tipo) {
        return new Symbol(tipo, yyline + 1, yycolumn + 1);
    }

    private Symbol symbol(int tipo, Object valor) {
        return new Symbol(tipo, yyline + 1, yycolumn + 1, valor);
    }
%}

//******** REGEX ********
DIGITO = [0-9]
LETRA = [a-zA-Z]
IDENTIFICADOR = {LETRA}({LETRA}|{DIGITO}|_)*
ESPACIOS = [ \t\r\n]+

ENTERO = {DIGITO}+
DECIMAL = {DIGITO}+"."{DIGITO}+

HEX = "#"([0-9A-Fa-f]{6})
RGB = \({ENTERO},{ENTERO},{ENTERO}\)
HSL = "<"({ENTERO}","{ENTERO}","{ENTERO})">"

COMENTARIO_LINEA = "$".*
COMENTARIO_BLOQUE = "/*"([^*]|\*+[^*/])*\*+"/"

%%

<YYINITIAL> {

//******** ESTRUCTURA ********
"SECTION"           { return symbol(sym.SECTION); }
"TABLE"             { return symbol(sym.TABLE); }
"TEXT"              { return symbol(sym.TEXT); }

//******** PREGUNTAS ********
"OPEN_QUESTION"     { return symbol(sym.OPEN_QUESTION); }
"DROP_QUESTION"     { return symbol(sym.DROP_QUESTION); }
"SELECT_QUESTION"   { return symbol(sym.SELECT_QUESTION); }
"MULTIPLE_QUESTION" { return symbol(sym.MULTIPLE_QUESTION); }

//******** ATRIBUTOS ********
"width"             { return symbol(sym.WIDTH); }
"height"            { return symbol(sym.HEIGHT); }
"pointX"            { return symbol(sym.POINTX); }
"pointY"            { return symbol(sym.POINTY); }
"orientation"       { return symbol(sym.ORIENTATION); }
"elements"          { return symbol(sym.ELEMENTS); }
"label"             { return symbol(sym.LABEL); }
"content"           { return symbol(sym.CONTENT); }
"options"           { return symbol(sym.OPTIONS); }
"correct"           { return symbol(sym.CORRECT); }
"styles"           { return symbol(sym.STYLES); }
//******** POKE API ********
"who_is_that_pokemon" { return symbol(sym.WHO_IS_THAT_POKEMON); }

//******** ORIENTACIÓN ********
"VERTICAL"          { return symbol(sym.VERTICAL); }
"HORIZONTAL"        { return symbol(sym.HORIZONTAL); }

//******** ESTILOS ********
"MONO"              { return symbol(sym.MONO); }
"SANS_SERIF"        { return symbol(sym.SANS_SERIF); }
"CURSIVE"           { return symbol(sym.CURSIVE); }

"LINE"              { return symbol(sym.LINE); }
"DOTTED"            { return symbol(sym.DOTTED); }
"DOUBLE"            { return symbol(sym.DOUBLE); }

//******** COLORES ********
"RED"               { return symbol(sym.COLOR_RED); }
"BLUE"              { return symbol(sym.COLOR_BLUE); }
"GREEN"             { return symbol(sym.COLOR_GREEN); }
"PURPLE"            { return symbol(sym.COLOR_PURPLE); }
"SKY"               { return symbol(sym.COLOR_SKY); }
"YELLOW"            { return symbol(sym.COLOR_YELLOW); }
"BLACK"             { return symbol(sym.COLOR_BLACK); }
"WHITE"             { return symbol(sym.COLOR_WHITE); }

//******** OPERADORES ********
"+"   { return symbol(sym.MAS); }
"-"   { return symbol(sym.MENOS); }
"*"   { return symbol(sym.POR); }
"/"   { return symbol(sym.DIV); }
"^"   { return symbol(sym.POT); }
"%"   { return symbol(sym.MOD); }

">="  { return symbol(sym.MAYORIGUAL); }
"<="  { return symbol(sym.MENORIGUAL); }
">"   { return symbol(sym.MAYOR); }
"<"   { return symbol(sym.MENOR); }
"=="  { return symbol(sym.IGUAL); }
"!!"  { return symbol(sym.DIF); }

"||"  { return symbol(sym.OR); }
"&&"  { return symbol(sym.AND); }
"~"   { return symbol(sym.NOT); }

//******** SIGNOS ********
"("   { return symbol(sym.PARENTESIS_ABRE); }
")"   { return symbol(sym.PARENTESIS_CIERRA); }
"{"   { return symbol(sym.LLAVE_ABRE); }
"}"   { return symbol(sym.LLAVE_CIERRA); }
"["   { return symbol(sym.CORCHETE_ABRE); }
"]"   { return symbol(sym.CORCHETE_CIERRA); }
","   { return symbol(sym.COMA); }
":"   { return symbol(sym.DOS_PUNTOS); }

"number" { return symbol(sym.NUMBER_T); }
"string" { return symbol(sym.STRING_T); }
"="      { return symbol(sym.IGUAL_ASIG); }

//******** CADENA ********
\" { yybegin(DENTRO_CADENA); }

//******** COLORES COMPLEJOS ********
{HEX}     { return symbol(sym.COLOR_HEX, yytext()); }
{RGB}     { return symbol(sym.COLOR_RGB, yytext()); }
{HSL}     { return symbol(sym.COLOR_HSL, yytext()); }

//******** NUMEROS ********
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

//******** CADENAS ********
<DENTRO_CADENA> {

    [^\"@\n]+ { return symbol(sym.TEXTO, yytext()); }

    "@" { return symbol(sym.TEXTO, yytext()); }

    \" { yybegin(YYINITIAL); }

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
package com.example.formularioapp.backen.java.lexer;

import java_cup.runtime.*;
import java.util.List;
import java.util.ArrayList;
import parser.symPKM;
import com.example.formularioapp.backen.java.ErroresDeAnalizadores; 

%%
%public
%unicode
%cupsym symPKM
%class LexerPKM
%cup
%line
%column
%ignorecase  
%state DENTRO_CADENA 
%eofval{
    return new Symbol(symPKM.EOF, yyline + 1, yycolumn + 1, "EOF");
%eofval}

%{
    private List<ErroresDeAnalizadores> errores = new ArrayList<>();

    public List<ErroresDeAnalizadores> getErrores() {
        return errores;
    }

    private Symbol symbol(int tipo) {
        return new Symbol(tipo, yyline + 1, yycolumn + 1);
    }

    private Symbol symbol(int tipo, Object valor) {
        return new Symbol(tipo, yyline + 1, yycolumn + 1, valor);
    }

    private void agregarError(String lexema) {
        errores.add(new ErroresDeAnalizadores(
            lexema,
            "Léxico",
            "PKM",
            "Token no válido",
            yyline + 1,
            yycolumn + 1
        ));
    }

%}

FINALLINEA = \r|\n|\r\n
ESPACIO = {FINALLINEA}|[ \t\f]

NUMERO = "-"? [0-9]+ ("." [0-9]+)?

COLORHEX = "#" [0-9a-fA-F]{6}
COLORRGB = "\(" [ ]* {NUMERO} [ ]* "," [ ]* {NUMERO} [ ]* "," [ ]* {NUMERO} [ ]* "\)"
COLORHSL = "<" [ ]* {NUMERO} [ ]* "," [ ]* {NUMERO} [ ]* "," [ ]* {NUMERO} [ ]* ">"

FECHA_FORMATO = [0-9]{2} "/" [0-9]{2} "/" [0-9]{2,4}
HORA_FORMATO = [0-9]{2} ":" [0-9]{2}

COMENTARIO = "#" [^\n]*
COMENTARIO_HTML = "<!--" [^>]* "-->"

SMILE   = "@[:smile:]"
SAD     = "@[:sad:]"
STAR    = "@[:star:" [0-9]+ ":]"
CAT     = "@[:^^:]"

%%
<YYINITIAL> {

    "###" { return symbol(symPKM.METADATA_SEP); }

    "Author:" { return symbol(symPKM.AUTHOR); }
    "Fecha:" { return symbol(symPKM.FECHA); }
    "Hora:" { return symbol(symPKM.HORA); }
    "Description:" { return symbol(symPKM.DESCRIPTION); }

    "Total de Secciones:" { return symbol(symPKM.TOTAL_SECCIONES); }
    "Total de Preguntas:" { return symbol(symPKM.TOTAL_PREGUNTAS); }
    "Abiertas:" { return symbol(symPKM.ABIERTAS); }
    "Desplegables:" { return symbol(symPKM.DESPLEGABLES); }
    "Selección:" | "Seleccion:" { return symbol(symPKM.SELECCION); }
    "Múltiples:" | "Multiples:" { return symbol(symPKM.MULTIPLES); }

    "style" { return symbol(symPKM.STYLE); }
    "color" { return symbol(symPKM.COLOR); }
    "background" { return symbol(symPKM.BACKGROUND); }
    "font" { return symbol(symPKM.FONT); }
    "family" { return symbol(symPKM.FAMILY); }
    "text" { return symbol(symPKM.TEXT); }
    "size" { return symbol(symPKM.SIZE); }
    "border" { return symbol(symPKM.BORDER); }

    "section" { return symbol(symPKM.SECTION); }
    "content" { return symbol(symPKM.CONTENT); }

    "table" { return symbol(symPKM.TABLE); }
    "line" { return symbol(symPKM.LINE); }
    "element" { return symbol(symPKM.ELEMENT); }

    "open" { return symbol(symPKM.OPEN); }
    "drop" { return symbol(symPKM.DROP); }
    "select" { return symbol(symPKM.SELECT); }
    "multiple" { return symbol(symPKM.MULTIPLE); }

    "MONO" { return symbol(symPKM.MONO); }
    "SANS_SERIF" { return symbol(symPKM.SANS_SERIF); }
    "CURSIVE" { return symbol(symPKM.CURSIVE); }

    "VERTICAL" { return symbol(symPKM.VERTICAL); }
    "HORIZONTAL" { return symbol(symPKM.HORIZONTAL); }

    "<" { return symbol(symPKM.MENOR); }
    ">" { return symbol(symPKM.MAYOR); }
    "</" { return symbol(symPKM.CIERRE_ETIQUETA); }
    "/>" { return symbol(symPKM.AUTO_CIERRE); }

    "=" { return symbol(symPKM.IGUAL); }
    "," { return symbol(symPKM.COMA); }
    "{" { return symbol(symPKM.LLAVE_ABRE); }
    "}" { return symbol(symPKM.LLAVE_CIERRA); }

    "\"" { yybegin(DENTRO_CADENA); return symbol(symPKM.COMILLA); }

    {FECHA_FORMATO} { return symbol(symPKM.VALOR_FECHA, yytext()); }
    {HORA_FORMATO} { return symbol(symPKM.VALOR_HORA, yytext()); }
    {NUMERO} { return symbol(symPKM.NUMERO, Double.parseDouble(yytext())); }

    {COLORHEX} { return symbol(symPKM.COLORHEX, yytext()); }
    {COLORRGB} { return symbol(symPKM.COLORRGB, yytext()); }
    {COLORHSL} { return symbol(symPKM.COLORHSL, yytext()); }

    {COMENTARIO} { }
    {COMENTARIO_HTML} { }

    {ESPACIO} { }

    [a-zA-Z_][a-zA-Z0-9_]* { return symbol(symPKM.IDENTIFICADOR, yytext()); }

    . { agregarError(yytext()); }
}

<DENTRO_CADENA> {

    {SMILE} | {SAD} | {STAR} | {CAT} {
        return symbol(symPKM.EMOJI, yytext());
    }

    [^\"@]+ { return symbol(symPKM.TEXTO, yytext()); }

    "\"" { yybegin(YYINITIAL); return symbol(symPKM.COMILLA); }

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

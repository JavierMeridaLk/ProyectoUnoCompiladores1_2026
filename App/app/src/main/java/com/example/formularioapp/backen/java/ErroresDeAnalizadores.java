package com.example.formularioapp.backen.java;
/**
 *
 * @author xavi
 */

import java.io.Serializable;
public class ErroresDeAnalizadores implements Serializable {
    private String lexema;
    private String tipo;
    private String lenguaje;
    private String descripcion;
    private int line;
    private int colm;

    public ErroresDeAnalizadores(String lexema, String tipo, String lenguaje, String descripcion, int line, int colm) {
        this.lexema = lexema;
        this.tipo = tipo;
        this.lenguaje = lenguaje;
        this.descripcion = descripcion;
        this.line = line;
        this.colm = colm;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getLexema() {
        return lexema;
    }

    public String getTipo() {
        return tipo;
    }

    public String getLenguaje() {
        return lenguaje;
    }

    public int getLine() {
        return line;
    }

    public int getColm() {
        return colm;
    }
}
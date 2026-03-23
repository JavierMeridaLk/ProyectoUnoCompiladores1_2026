package com.example.formularioapp.backen.java.ast;

public class NodoTexto extends NodoComponente {

    public String contenido;

    public NodoTexto(String contenido, int linea, int columna) {
        super(linea, columna);
        this.contenido = contenido;
    }
}
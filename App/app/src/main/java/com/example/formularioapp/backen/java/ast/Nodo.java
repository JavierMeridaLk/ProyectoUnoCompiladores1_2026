package com.example.formularioapp.backen.java.ast;

public abstract class Nodo {
    public int linea;
    public int columna;

    public Nodo(int linea, int columna) {
        this.linea = linea;
        this.columna = columna;
    }
}
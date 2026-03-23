package com.example.formularioapp.backen.java.ast;

public class NodoPreguntaAbierta extends NodoComponente {

    public String label;

    public NodoPreguntaAbierta(String label, int linea, int columna) {
        super(linea, columna);
        this.label = label;
    }
}
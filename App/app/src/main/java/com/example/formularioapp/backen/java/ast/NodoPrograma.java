package com.example.formularioapp.backen.java.ast;

import java.util.List;

public class NodoPrograma extends Nodo {
    public List<Nodo> instrucciones;

    public NodoPrograma(List<Nodo> instrucciones, int linea, int columna) {
        super(linea, columna);
        this.instrucciones = instrucciones;
    }
}
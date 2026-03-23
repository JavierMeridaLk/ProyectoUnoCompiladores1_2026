package com.example.formularioapp.backen.java.ast;

import java.util.List;

public class NodoIf extends Nodo {
    public NodoExpresion condicion;
    public List<Nodo> cuerpoIf;
    public List<Nodo> cuerpoElse;

    public NodoIf(NodoExpresion condicion, List<Nodo> cuerpoIf, List<Nodo> cuerpoElse, int linea, int columna) {
        super(linea, columna);
        this.condicion = condicion;
        this.cuerpoIf = cuerpoIf;
        this.cuerpoElse = cuerpoElse;
    }
}
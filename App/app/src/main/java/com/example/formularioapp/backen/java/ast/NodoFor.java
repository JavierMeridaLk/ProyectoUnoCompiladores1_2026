package com.example.formularioapp.backen.java.ast;

import java.util.List;

public class NodoFor extends Nodo {
    public NodoAsignacion inicio;
    public NodoExpresion condicion;
    public NodoAsignacion incremento;
    public List<Nodo> cuerpo;

    public NodoFor(NodoAsignacion inicio, NodoExpresion condicion, NodoAsignacion incremento, List<Nodo> cuerpo, int linea, int columna) {
        super(linea, columna);
        this.inicio = inicio;
        this.condicion = condicion;
        this.incremento = incremento;
        this.cuerpo = cuerpo;
    }
}
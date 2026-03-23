package com.example.formularioapp.backen.java.ast;

import java.util.List;

public class NodoWhile extends Nodo {
    public NodoExpresion condicion;
    public List<Nodo> cuerpo;

    public NodoWhile(NodoExpresion condicion, List<Nodo> cuerpo, int linea, int columna) {
        super(linea, columna);
        this.condicion = condicion;
        this.cuerpo = cuerpo;
    }
}
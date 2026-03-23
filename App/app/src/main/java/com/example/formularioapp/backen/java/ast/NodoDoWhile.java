package com.example.formularioapp.backen.java.ast;

import java.util.List;

public class NodoDoWhile extends Nodo {
    public List<Nodo> cuerpo;
    public NodoExpresion condicion;

    public NodoDoWhile(List<Nodo> cuerpo, NodoExpresion condicion, int linea, int columna) {
        super(linea, columna);
        this.cuerpo = cuerpo;
        this.condicion = condicion;
    }
}
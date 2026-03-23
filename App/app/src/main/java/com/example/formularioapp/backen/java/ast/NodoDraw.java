package com.example.formularioapp.backen.java.ast;

import java.util.List;

public class NodoDraw extends Nodo {
    public String nombre;
    public List<NodoExpresion> parametros;

    public NodoDraw(String nombre, List<NodoExpresion> parametros, int linea, int columna) {
        super(linea, columna);
        this.nombre = nombre;
        this.parametros = parametros;
    }
}
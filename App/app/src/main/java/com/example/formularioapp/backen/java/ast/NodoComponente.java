package com.example.formularioapp.backen.java.ast;

import java.util.Map;

public abstract class NodoComponente extends Nodo {

    public Map<String, Object> atributos;
    public NodoEstilo estilo;

    public NodoComponente(Map<String, Object> atributos, int linea, int columna) {
        super(linea, columna);
        this.atributos = atributos;
    }
}
package com.example.formularioapp.backen.java.ast;

import java.util.Map;

public class NodoEstilo extends Nodo {
    public Map<String,Object> atributos;

    public NodoEstilo(Map<String,Object> atributos, int linea, int columna) {
        super(linea, columna);
        this.atributos = atributos;
    }
}
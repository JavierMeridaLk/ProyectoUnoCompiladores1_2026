package com.example.formularioapp.backen.java.ast;

import java.util.Map;

public class NodoPreguntaAbierta extends NodoComponente {

    public String label;

    public NodoPreguntaAbierta(Map<String, Object> atributos, int linea, int columna) {
        super(atributos, linea, columna);
        this.label = (String) atributos.get("label");
    }
}
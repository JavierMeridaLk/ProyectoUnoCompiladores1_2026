package com.example.formularioapp.backen.java.ast;

import java.util.Map;

public class NodoTexto extends NodoComponente {

    public String contenido;

    public NodoTexto(Map<String, Object> atributos, int linea, int columna) {
        super(atributos, linea, columna);
        this.contenido = (String) atributos.get("content");
    }
}
package com.example.formularioapp.backen.java.ast;

import java.util.Map;

public class NodoComponente extends Nodo {
    public Map<String, Object> atributos;
    public NodoEstilo estilo;

    public NodoComponente(Map<String,Object> atributos, int linea, int columna) {
        super(linea, columna);
        this.atributos = atributos;
        this.estilo = new NodoEstilo(atributos); // 🔥 CLAVE
    }
}
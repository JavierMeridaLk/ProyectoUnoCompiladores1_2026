package com.example.formularioapp.backen.java.ast;

import java.util.List;
import java.util.Map;

public class NodoSeccion extends NodoComponente {

    public Map<String, Object> atributos;
    public List<NodoComponente> elementos;

    public NodoSeccion(Map<String, Object> atributos, List<NodoComponente> elementos, int linea, int columna) {
        super(linea, columna);
        this.atributos = atributos;
        this.elementos = elementos;
    }
}
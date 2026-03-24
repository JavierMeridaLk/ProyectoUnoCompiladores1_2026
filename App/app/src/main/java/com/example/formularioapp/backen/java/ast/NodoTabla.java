package com.example.formularioapp.backen.java.ast;

import java.util.List;
import java.util.Map;

public class NodoTabla extends NodoComponente {
    public List<List<NodoComponente>> filas;

    public NodoTabla(Map<String,Object> atributos, List<List<NodoComponente>> filas, int linea, int columna) {
        super(atributos, linea, columna);
        this.filas = filas;
    }
}
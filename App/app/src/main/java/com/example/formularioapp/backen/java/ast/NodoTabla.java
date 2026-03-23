package com.example.formularioapp.backen.java.ast;

import java.util.List;

public class NodoTabla extends NodoComponente {
    public List<List<NodoComponente>> filas;

    public NodoTabla(List<List<NodoComponente>> filas, int linea, int columna) {
        super(linea, columna);
        this.filas = filas;
    }
}
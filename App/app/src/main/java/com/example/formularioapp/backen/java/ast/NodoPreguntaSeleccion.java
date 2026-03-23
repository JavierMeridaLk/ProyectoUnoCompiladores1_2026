package com.example.formularioapp.backen.java.ast;

import java.util.List;

public class NodoPreguntaSeleccion extends NodoComponente {

    public String label;
    public List<String> opciones;

    public NodoPreguntaSeleccion(String label, List<String> opciones, int linea, int columna) {
        super(linea, columna);
        this.label = label;
        this.opciones = opciones;
    }
}
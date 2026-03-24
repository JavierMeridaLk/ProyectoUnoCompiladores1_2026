package com.example.formularioapp.backen.java.ast;

import java.util.List;
import java.util.Map;

public class NodoPreguntaSeleccion extends NodoComponente {
    public String tipo;
    public String label;
    public Object opciones;
    public Object correct;

    public NodoPreguntaSeleccion(String tipo, Map<String, Object> atributos, int linea, int columna) {
        super(atributos, linea, columna);
        this.tipo = tipo;
        this.label = (String) atributos.get("label");
        this.opciones = atributos.get("options");
        this.correct = atributos.get("correct");
    }
}
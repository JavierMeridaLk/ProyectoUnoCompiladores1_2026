package com.example.formularioapp.backen.java.ast;

import java.util.List;
import java.util.Map;

public class NodoPreguntaSeleccion extends NodoComponente {

    public String label;
    public List<String> opciones;
    public Boolean correct;

    public NodoPreguntaSeleccion(Map<String, Object> atributos, int linea, int columna) {
        super(atributos, linea, columna);

        this.label = (String) atributos.get("label");
        this.opciones = (List<String>) atributos.get("options");
        this.correct = (Boolean) atributos.get("correct");
    }
}
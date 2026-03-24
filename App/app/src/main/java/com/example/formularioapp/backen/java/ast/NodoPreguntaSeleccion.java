package com.example.formularioapp.backen.java.ast;

import java.util.List;
import java.util.Map;

public class NodoPreguntaSeleccion extends NodoComponente {
    public String tipo;
    public String label;
    public Object opciones; // 🔥 Cambiado de List<String> a Object para soportar la API
    public Object correct;

    public NodoPreguntaSeleccion(String tipo, Map<String, Object> atributos, int linea, int columna) {
        super(atributos, linea, columna);
        this.tipo = tipo;
        this.label = (String) atributos.get("label");
        // 🔥 Extraemos "options" del mapa de atributos
        this.opciones = atributos.get("options");
        this.correct = atributos.get("correct");
    }
}
package com.example.formularioapp.backen.java.ast;

import java.util.HashMap;
import java.util.Map;

public class NodoEstilo {
    public Map<String, Object> atributos;

    public NodoEstilo(Map<String, Object> atributos) {
        this.atributos = atributos != null ? atributos : new HashMap<>();
    }
}
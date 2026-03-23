package com.example.formularioapp.backen.java.ast;

public abstract class NodoComponente extends Nodo {
    public NodoEstilo estilo; // Para estilos de fuente, bordes, colores

    public NodoComponente(int linea, int columna) {
        super(linea, columna);
    }
}
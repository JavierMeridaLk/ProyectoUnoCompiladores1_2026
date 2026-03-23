package com.example.formularioapp.backen.java.ast;

public class NodoOperacion extends NodoExpresion {
    public String operador;
    public NodoExpresion izquierda;
    public NodoExpresion derecha;

    public NodoOperacion(String operador, NodoExpresion izquierda, NodoExpresion derecha, int linea, int columna) {
        super(linea, columna);
        this.operador = operador;
        this.izquierda = izquierda;
        this.derecha = derecha;
    }
}
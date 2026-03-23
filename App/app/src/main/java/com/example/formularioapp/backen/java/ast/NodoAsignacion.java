package com.example.formularioapp.backen.java.ast;

public class NodoAsignacion extends Nodo {
    public NodoIdentificador variable;
    public NodoExpresion valor;

    public NodoAsignacion(NodoIdentificador variable, NodoExpresion valor, int linea, int columna) {
        super(linea, columna);
        this.variable = variable;
        this.valor = valor;
    }
}
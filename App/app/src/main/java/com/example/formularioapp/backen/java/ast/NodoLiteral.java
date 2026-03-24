package com.example.formularioapp.backen.java.ast;

public class NodoLiteral extends NodoExpresion {
    private Object valor;

    public NodoLiteral(Object valor, int linea, int columna) {
        super(linea, columna);
        this.valor = valor;
    }

    public Object getValor() {
        return valor;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }
}
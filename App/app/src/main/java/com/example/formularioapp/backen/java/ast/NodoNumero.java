package com.example.formularioapp.backen.java.ast;

public class NodoNumero extends NodoExpresion {
    public double valor;

    public NodoNumero(double valor, int linea, int columna) {
        super(linea, columna);
        this.valor = valor;
    }
}
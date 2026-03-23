package com.example.formularioapp.backen.java.ast;

public class NodoDeclaracion extends Nodo {
    public String tipo;
    public NodoIdentificador nombre;
    public NodoExpresion valor;

    public NodoDeclaracion(String tipo, NodoIdentificador nombre, NodoExpresion valor, int linea, int columna) {
        super(linea, columna);
        this.tipo = tipo;
        this.nombre = nombre;
        this.valor = valor;
    }
}
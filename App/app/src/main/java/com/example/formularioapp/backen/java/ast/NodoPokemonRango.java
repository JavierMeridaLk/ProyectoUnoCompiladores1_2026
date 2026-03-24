package com.example.formularioapp.backen.java.ast;

public class NodoPokemonRango extends Nodo {

    public int inicio;
    public int fin;

    public NodoPokemonRango(int inicio, int fin) {
        super(0,0); // o tus coordenadas si usas
        this.inicio = inicio;
        this.fin = fin;
    }
}

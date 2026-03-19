/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pruebasintacticocompi.pruebasproyecto1;

/**
 *
 * @author xavi
 */
public class ErroresDeAnalizadores {
    
    private String lexema;
    private String tipo;
    private int line;
    private int colm;

    public ErroresDeAnalizadores(String lexema, String tipo, int line, int colm) {
        this.lexema = lexema;
        this.tipo = tipo;
        this.line = line;
        this.colm = colm;
    }

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getColm() {
        return colm;
    }

    public void setColm(int colm) {
        this.colm = colm;
    }
    
    
}

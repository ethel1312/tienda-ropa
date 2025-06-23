package com.example.tienda_ropa.model;

public class Distrito {
    private int id_distrito;
    private String nombre_distrito;

    public Distrito(int id_distrito, String nombre_distrito) {
        this.id_distrito = id_distrito;
        this.nombre_distrito = nombre_distrito;
    }

    public int getId_distrito() {
        return id_distrito;
    }

    public String getNombre_distrito() {
        return nombre_distrito;
    }

    @Override
    public String toString() {
        return nombre_distrito;
    }
}

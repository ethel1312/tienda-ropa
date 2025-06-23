package com.example.tienda_ropa.ui.home;

public class PrendaEntry {

    public int id;
    public String nombre;
    public float precio;
    public String url;
    public boolean isFavorito;

    public PrendaEntry(int p_id,String p_nombre, float p_precio, String p_url, boolean esFavorito){
        this.id = p_id;
        this.nombre = p_nombre;
        this.precio = p_precio;
        this.url = p_url;
        this.isFavorito = esFavorito;
    }


}


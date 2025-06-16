package com.example.tienda_ropa.ui.home;

public class PrendaEntry {

    public int id;
    public String nombre;
    public float precio;
    public String url;
    //creamos un contrustor, donde pase lo sparametros correspondientes

    public PrendaEntry(int p_id,String p_nombre, float p_precio, String p_url){
        this.id = p_id;
        this.nombre = p_nombre;
        this.precio = p_precio;
        this.url = p_url;
    }


}


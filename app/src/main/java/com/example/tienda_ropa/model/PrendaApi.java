package com.example.tienda_ropa.model;


public class PrendaApi {
    private int id_prenda;
    private String nomPrenda;
    private float precio;
    private String url_imagen;

    private String descripcion;

    public int getId() {
        return id_prenda;
    }

    public String getNomPrenda() {
        return nomPrenda;
    }

    public float getPrecio() {
        return precio;
    }

    public String getUrl_imagen() {
        return url_imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }
}

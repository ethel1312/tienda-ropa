package com.example.tienda_ropa.model;

public class ListaDeseosApi {
    private int id_prenda;
    private String url_imagen;
    private String nomPrenda;
    private String precio;

    public String getUrl_imagen() {
        return url_imagen;
    }

    public int getId_prenda() {
        return id_prenda;
    }

    public String getNomPrenda() {
        return nomPrenda;
    }

    public String getPrecio() {
        return precio;
    }
}

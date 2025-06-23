package com.example.tienda_ropa.model;


import java.util.List;

public class PrendaApi {
    private int id_prenda;
    private String nomPrenda;
    private float precio;
    private String url_imagen;
    private String descripcion;
    private int es_favorito;
    private List<TallaApi> tallas;
    private List<ColorApi> colores;

    public int getId_prenda() {
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

    public boolean isFavorito() {
        return es_favorito == 1;
    }

    public List<TallaApi> getTallas() {
        return tallas;
    }

    public List<ColorApi> getColores() {
        return colores;
    }
}

package com.example.tienda_ropa.pedido;

import com.google.gson.annotations.SerializedName;

public class ItemCompra {
    private String nomPrenda;
    private String talla;
    private double precio;
    private int    cantidad;
    @SerializedName("url_imagen")
    private String urlImagen;

    public String getNomPrenda() {
        return nomPrenda;
    }

    public String getTalla() {
        return talla;
    }

    public double getPrecio() {
        return precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getUrlImagen() {
        return urlImagen;
    }
}

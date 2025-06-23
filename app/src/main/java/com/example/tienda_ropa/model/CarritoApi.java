package com.example.tienda_ropa.model;

public class CarritoApi {
    private int id_prenda;
    private String url_imagen;
    private String nomPrenda;
    private String talla;
    private String precio;
    private int cantidad;
    private String total;
    private int stock;

    public int getId_prenda() {
        return id_prenda;
    }

    public String getUrl_imagen() {
        return url_imagen;
    }

    public String getNomPrenda() {
        return nomPrenda;
    }
    public String getTalla() {
        return talla;
    }
    public String getPrecio() {
        return precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getTotal() {
        return total;
    }
    public int getStock() {
        return stock;
    }
}

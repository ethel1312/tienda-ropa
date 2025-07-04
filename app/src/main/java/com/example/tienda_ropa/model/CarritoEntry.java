package com.example.tienda_ropa.model;

public class CarritoEntry {
    public String url_imagen;
    public String nomPrenda;
    public String precio;
    public int cantidad;
    public int idPrenda;
    public String talla;
    public int stock;

    public int getIdPrenda() {
        return idPrenda;
    }
    public String getTalla() {
        return talla;
    }

    public CarritoEntry(String p_url_imagen, String p_nomPrenda, String p_precio, int p_cantidad,
                        int p_idPrenda, String p_talla, int p_stock){
        this.url_imagen=p_url_imagen;
        this.nomPrenda=p_nomPrenda;
        this.precio=p_precio;
        this.cantidad=p_cantidad;
        this.idPrenda=p_idPrenda;
        this.talla = p_talla;
        this.stock = p_stock;
    }
}

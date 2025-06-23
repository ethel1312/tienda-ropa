package com.example.tienda_ropa.model;

public class AgregarAlCarritoReq {
    private int id_prenda;
    private int id_talla;
    private int cantidad;

    public void setId_prenda(int id_prenda) {
        this.id_prenda = id_prenda;
    }

    public void setId_talla(int id_talla) {
        this.id_talla = id_talla;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}

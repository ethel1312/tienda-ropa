package com.example.tienda_ropa.model;

import com.example.tienda_ropa.pedido.Direccion;
import com.example.tienda_ropa.pedido.ItemCompra;
import com.example.tienda_ropa.pedido.Metodo;
import com.example.tienda_ropa.pedido.Resumen;

import java.util.List;

public class DetalleApi {
    private Resumen resumen;
    private Direccion direccion;   // puede llegar {}
    private Metodo metodo;       // "
    private List<ItemCompra> items;

    public Resumen getResumen() {
        return resumen;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public Metodo getMetodo() {
        return metodo;
    }

    public List<ItemCompra> getItems() {
        return items;
    }
}

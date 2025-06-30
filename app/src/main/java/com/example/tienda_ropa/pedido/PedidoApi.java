package com.example.tienda_ropa.pedido;

import com.google.gson.annotations.SerializedName;

public class PedidoApi {
    private long   id_orden;
    private String codigo_pedido;
    private String fecha_realizado;
    private String articulos;  // viene como "3"
    private String total;

    private String estado;

    public long getId_orden() {
        return id_orden;
    }

    public String getCodigo_pedido() {
        return codigo_pedido;
    }

    public String getFecha_realizado() {
        return fecha_realizado;
    }

    public String getArticulos() {
        return articulos;
    }

    public String getTotal() {
        return total;
    }

    public String getEstado() {
        return estado;
    }
}

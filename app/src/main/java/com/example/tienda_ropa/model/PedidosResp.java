package com.example.tienda_ropa.model;

import com.example.tienda_ropa.pedido.PedidoApi;
import com.example.tienda_ropa.pedido.PedidoEntry;

import java.util.List;

public class PedidosResp {

    private int code;
    private String message;
    private List<PedidoApi> data;   // ← lista de pedidos

    /* ───────────  GETTERS  ─────────── */

    public int getCode()            { return code; }
    public String getMessage()      { return message; }
    public List<PedidoApi> getData(){ return data; }
}
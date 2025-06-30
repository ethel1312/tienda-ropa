package com.example.tienda_ropa.pedido;

public class PedidoEntry {
    public long   id;
    public String codigo;     // puede venir vacío
    public String fecha;
    public int    articulos;
    public float total;      // “S/.203”

    public PedidoEntry(long p_id, String p_codigo, String p_fecha,
                       int p_articulos, String p_total) {
        this.id        = p_id;
        this.codigo    = p_codigo;
        this.fecha     = p_fecha;
        this.articulos = p_articulos;
        this.total     = Float.parseFloat(p_total);
    }
}


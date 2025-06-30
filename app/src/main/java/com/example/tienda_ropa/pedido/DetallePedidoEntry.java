package com.example.tienda_ropa.pedido;

public class DetallePedidoEntry {
    private final String nomPrenda;
    private final String talla;
    private final double precio;
    private final int    cantidad;
    private final String urlImagen;   // opcional, si quieres foto

    public DetallePedidoEntry(String nomPrenda, String talla,
                           double precio, int cantidad, String urlImagen) {
        this.nomPrenda = nomPrenda;
        this.talla     = talla;
        this.precio    = precio;
        this.cantidad  = cantidad;
        this.urlImagen = urlImagen;
    }

    // ─── getters ────────────────────────────────────────────
    public String getNomPrenda() { return nomPrenda; }
    public String getTalla()     { return talla; }
    public double getPrecio()    { return precio; }
    public int    getCantidad()  { return cantidad; }
    public String getUrlImagen() { return urlImagen; }
}

package com.example.tienda_ropa.pedido;

public class Resumen {
    private String  codigo;
    private String  fecha;
    private String  estado;
    private double  subtotal;
    private double  igv;
    private double  envio;
    private double  total;

    public String getCodigo() {
        return codigo;
    }

    public String getFecha() {
        return fecha;
    }

    public String getEstado() {
        return estado;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getIgv() {
        return igv;
    }

    public double getEnvio() {
        return envio;
    }

    public double getTotal() {
        return total;
    }
}

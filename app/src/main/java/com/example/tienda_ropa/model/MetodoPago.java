package com.example.tienda_ropa.model;

public class MetodoPago {

    private int id_metodo_pago;
    private String numero_enmascarado;
    private String fecha_vencimiento;
    private String titular;
    private int es_principal;
    private String tipo_metodo_pago;

    public MetodoPago(int id_metodo_pago,
                      String numero_enmascarado,
                      String fecha_vencimiento,
                      String titular,
                      int es_principal,
                      String tipo_metodo_pago) {
        this.id_metodo_pago       = id_metodo_pago;
        this.numero_enmascarado = numero_enmascarado;
        this.fecha_vencimiento  = fecha_vencimiento;
        this.titular            = titular;
        this.es_principal       = es_principal;
        this.tipo_metodo_pago   = tipo_metodo_pago;
    }

    public int getId_metodo_pago() {
        return id_metodo_pago;
    }

    public String getNumero_enmascarado() {
        return numero_enmascarado;
    }

    public void setNumero_enmascarado(String numero_enmascarado) {
        this.numero_enmascarado = numero_enmascarado;
    }

    public String getFecha_vencimiento() {
        return fecha_vencimiento;
    }

    public void setFecha_vencimiento(String fecha_vencimiento) {
        this.fecha_vencimiento = fecha_vencimiento;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public int getEs_principal() {
        return es_principal;
    }

    public void setEs_principal(int es_principal) {
        this.es_principal = es_principal;
    }

    public String getTipo_metodo_pago() {
        return tipo_metodo_pago;
    }

    public void setTipo_metodo_pago(String tipo_metodo_pago) {
        this.tipo_metodo_pago = tipo_metodo_pago;
    }
}

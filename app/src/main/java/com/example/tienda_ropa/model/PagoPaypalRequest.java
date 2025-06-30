package com.example.tienda_ropa.model;

public class PagoPaypalRequest {
    private int id_usuario;
    private int id_domicilio;
    private int id_metodo_pago;

    public PagoPaypalRequest(int id_usuario, int id_domicilio, int id_metodo_pago) {
        this.id_usuario = id_usuario;
        this.id_domicilio = id_domicilio;
        this.id_metodo_pago = id_metodo_pago;
    }

    // Getters y setters opcionales si los necesitas
}

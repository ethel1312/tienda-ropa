package com.example.tienda_ropa.model;

public class DevolucionRequest {
    private long id_orden;
    private String motivo;

    public DevolucionRequest(long id_orden, String motivo) {
        this.id_orden = id_orden;
        this.motivo = motivo;
    }


}
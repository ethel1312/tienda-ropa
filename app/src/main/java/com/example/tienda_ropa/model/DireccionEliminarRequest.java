package com.example.tienda_ropa.model;
public class DireccionEliminarRequest {
    private int id_usuario;
    private int id_domicilio;

    public DireccionEliminarRequest(int id_usuario, int id_domicilio) {
        this.id_usuario = id_usuario;
        this.id_domicilio = id_domicilio;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_domicilio() {
        return id_domicilio;
    }

    public void setId_domicilio(int id_domicilio) {
        this.id_domicilio = id_domicilio;
    }
}

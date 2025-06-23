package com.example.tienda_ropa.model;

public class DireccionRequest {

    private int id_usuario;
    private String calle;
    private int id_distrito;
    private String info_adicional;
    private int es_principal;

    public DireccionRequest(int id_usuario, String calle, int id_distrito, String info_adicional, int es_principal) {
        this.id_usuario = id_usuario;
        this.calle = calle;
        this.id_distrito = id_distrito;
        this.info_adicional = info_adicional;
        this.es_principal = es_principal;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public int getId_distrito() {
        return id_distrito;
    }

    public void setId_distrito(int id_distrito) {
        this.id_distrito = id_distrito;
    }

    public String getInfo_adicional() {
        return info_adicional;
    }

    public void setInfo_adicional(String info_adicional) {
        this.info_adicional = info_adicional;
    }

    public int getEs_principal() {
        return es_principal;
    }

    public void setEs_principal(int es_principal) {
        this.es_principal = es_principal;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

}

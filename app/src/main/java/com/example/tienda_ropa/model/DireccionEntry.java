package com.example.tienda_ropa.model;

public class DireccionEntry {
    public int id_domicilio;
    public String calle;
    public String info_adicional;

    public DireccionEntry(int id, String calle, String info) {
        this.id_domicilio = id;
        this.calle = calle;
        this.info_adicional = info;
    }
}

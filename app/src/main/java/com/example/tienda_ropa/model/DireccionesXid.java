package com.example.tienda_ropa.model;
import com.google.gson.annotations.SerializedName;

public class DireccionesXid {

    @SerializedName("calle")
    private String calle;

    @SerializedName("id_domicilio")
    private int id_domicilio;

    @SerializedName("info_adicional")
    private String info_adicional;


    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public int getId_domicilio() {
        return id_domicilio;
    }

    public void setId_domicilio(int id_domicilio) {
        this.id_domicilio = id_domicilio;
    }

    public String getInfo_adicional() {
        return info_adicional;
    }

    public void setInfo_adicional(String info_adicional) {
        this.info_adicional = info_adicional;
    }
}

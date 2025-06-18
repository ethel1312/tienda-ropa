package com.example.tienda_ropa.model;

public class ListaDeseosEntry {
    public String url_imagen;
    public String nomPrenda;
    public String precio;
    public int idPrenda;

    public int getIdPrenda() {
        return idPrenda;
    }

    public ListaDeseosEntry(String p_url_imagen, String p_nomPrenda, String p_precio, int p_idPrenda){
        this.url_imagen=p_url_imagen;
        this.nomPrenda=p_nomPrenda;
        this.precio=p_precio;
        this.idPrenda=p_idPrenda;
    }
}

package com.example.tienda_ropa.model;

public class RatingReq {
    private int id_prenda;
    private int estrellas;
    private String comentario;

    public void setId_prenda(int id_prenda) {
        this.id_prenda = id_prenda;
    }

    public void setEstrellas(int estrellas) {
        this.estrellas = estrellas;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}

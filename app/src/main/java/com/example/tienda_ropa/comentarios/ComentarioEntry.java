package com.example.tienda_ropa.comentarios;

public class ComentarioEntry {
    public int    estrellas;
    public String fecha;          // "25/06/2025"
    public String usuario;
    public String comentario;
    public int verificada;    // true = “VERIFICADO”

    public int getEstrellas() {
        return estrellas;
    }

    public String getFecha() {
        return fecha;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getComentario() {
        return comentario;
    }

    public boolean isVerificada() {          // helper
        return verificada == 1;
    }
}

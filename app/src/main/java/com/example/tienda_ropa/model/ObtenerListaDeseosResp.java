package com.example.tienda_ropa.model;

import java.util.List;

public class ObtenerListaDeseosResp {
    private int code;
    private List<ListaDeseosApi> data;
    private String message;

    public int getCode() {
        return code;
    }

    public List<ListaDeseosApi> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}

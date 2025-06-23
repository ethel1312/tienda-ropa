package com.example.tienda_ropa.model;

import java.util.List;

public class ObtenerPrendaResp {
    private int code;
    private List<PrendaApi> data;
    private String message;

    public int getCode() {
        return code;
    }

    public List<PrendaApi> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}

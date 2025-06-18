package com.example.tienda_ropa.model;

import java.util.List;

public class PrendaResponse {
    private int code;
    private String message;
    private List<PrendaApi> data;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<PrendaApi> getData() {
        return data;
    }
}
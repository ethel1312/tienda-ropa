package com.example.tienda_ropa.model;

import java.util.List;

public class ObtenerCarritoResp {
    private int code;
    private List<CarritoApi> data;
    private String message;

    public int getCode() {
        return code;
    }

    public List<CarritoApi> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}

package com.example.tienda_ropa.model;

import java.util.List;

public class ObtenerDirecciones {
    private int code;
    private List<DireccionesXid> data;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DireccionesXid> getData() {
        return data;
    }

    public void setData(List<DireccionesXid> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

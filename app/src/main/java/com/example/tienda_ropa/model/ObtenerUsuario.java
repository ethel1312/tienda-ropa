package com.example.tienda_ropa.model;

import java.util.List;

public class ObtenerUsuario {
    private int code;
    private List<UsuarioXid> data;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<UsuarioXid> getData() {
        return data;
    }

    public void setData(List<UsuarioXid> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

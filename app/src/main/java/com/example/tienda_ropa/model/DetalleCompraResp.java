package com.example.tienda_ropa.model;

public class DetalleCompraResp {
    private int code;
    private String message;
    private DetalleApi data;      // ← tu objeto DetalleApi

    public int getCode()             { return code; }
    public String getMessage()       { return message; }
    public DetalleApi getData()      { return data; }
}

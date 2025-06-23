package com.example.tienda_ropa.model;

public class ApiResponse<T> {
    public int code;
    public String message;
    public T data;
}
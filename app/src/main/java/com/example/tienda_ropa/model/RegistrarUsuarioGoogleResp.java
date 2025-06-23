package com.example.tienda_ropa.model;

public class RegistrarUsuarioGoogleResp {
    private int code;
    private String message;
    private int id_usuario;
    private String token;
    private String username;

    public RegistrarUsuarioGoogleResp() {
    }

    public RegistrarUsuarioGoogleResp(int code, String message, int id_usuario, String token, String username) {
        this.code = code;
        this.message = message;
        this.id_usuario = id_usuario;
        this.token = token;
        this.username = username;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

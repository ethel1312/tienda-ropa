package com.example.tienda_ropa.model;

public class Departamento {
    private int id_departamento;
    private String nombre_departamento;

    public Departamento(int id_departamento, String nombre_departamento) {
        this.id_departamento = id_departamento;
        this.nombre_departamento = nombre_departamento;
    }

    public int getId_departamento() {
        return id_departamento;
    }

    public String getNombre_departamento() {
        return nombre_departamento;
    }

    @Override
    public String toString() {
        return nombre_departamento; // Para que se muestre correctamente en el Spinner
    }
}

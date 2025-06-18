package com.example.tienda_ropa.model;

import java.util.List;

public class Ubigeo {
    public int id_departamento;
    public String nombre_departamento;
    public List<Provincia> provincias;

    public static class Provincia {
        public int id_provincia;
        public String nombre_provincia;
        public List<Distrito> distritos;
    }

    public static class Distrito {
        public int id_distrito;
        public String nombre_distrito;
    }
}

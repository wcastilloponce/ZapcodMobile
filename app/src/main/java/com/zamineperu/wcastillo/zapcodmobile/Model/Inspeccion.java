package com.zamineperu.wcastillo.zapcodmobile.Model;

public class Inspeccion {

    private String id;
    private String descripcion;

    public Inspeccion() {
    }

    public Inspeccion(String id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return this.descripcion;
    }
}

package com.zamineperu.wcastillo.zapcodmobile.Model;

public class Ubicacion {

    private String id;
    private String operacion_id;
    private String descripcion;

    public Ubicacion(){}

    public Ubicacion(String id, String operacion_id, String descripcion) {
        this.id = id;
        this.operacion_id = operacion_id;
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperacion_id() {
        return operacion_id;
    }

    public void setOperacion_id(String operacion_id) {
        this.operacion_id = operacion_id;
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
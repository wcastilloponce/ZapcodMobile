package com.zamineperu.wcastillo.zapcodmobile.Model;

import com.google.gson.annotations.SerializedName;

public class Programacion {

    private String id;
    @SerializedName("operacion_id")
    private String operacionId;
    @SerializedName("año")
    private String anio;

    public Programacion(String id, String operacion_id, String anio) {
        this.id = id;
        this.operacionId = operacion_id;
        this.anio = anio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperacion_id() {
        return operacionId;
    }

    public void setOperacion_id(String operacion_id) {
        this.operacionId = operacion_id;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }
}

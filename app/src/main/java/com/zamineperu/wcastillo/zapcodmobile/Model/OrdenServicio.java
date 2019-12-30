package com.zamineperu.wcastillo.zapcodmobile.Model;

import com.google.gson.annotations.SerializedName;

public class OrdenServicio {

    @SerializedName("id")
    private String id;
    @SerializedName("codigo")
    private String codigo;
    @SerializedName("equipo")
    private String equipo;
    @SerializedName("fecha_inicio")
    private String fecha_inicio;
    @SerializedName("descripcion")
    private String descripcion;


    public OrdenServicio(String id, String codigo, String equipo, String fecha_inicio, String descripcion){
        this.id = id;
        this.codigo = codigo;
        this.equipo = equipo;
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getEquipo() {
        return equipo;
    }

    public void setEquipo(String equipo) {
        this.equipo = equipo;
    }

    public String getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}

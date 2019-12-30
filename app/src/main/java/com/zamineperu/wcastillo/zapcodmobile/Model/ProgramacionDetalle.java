package com.zamineperu.wcastillo.zapcodmobile.Model;

public class ProgramacionDetalle {

    private String id;
    private String mes_id;
    private String responsable_user_id;
    private String inspeccion_id;
    private String fecha_inicio;
    private String fecha_fin;
    private String tipo_inspeccion_id;
    private String fecha_ejecucion;
    private String ubicacion_id;
    private String descripcion_inspeccion;

    public ProgramacionDetalle(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMes_id() {
        return mes_id;
    }

    public void setMes_id(String mes_id) {
        this.mes_id = mes_id;
    }

    public String getResponsable_user_id() {
        return responsable_user_id;
    }

    public void setResponsable_user_id(String responsable_user_id) {
        this.responsable_user_id = responsable_user_id;
    }

    public String getInspeccion_id() {
        return inspeccion_id;
    }

    public void setInspeccion_id(String inspeccion_id) {
        this.inspeccion_id = inspeccion_id;
    }

    public String getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public String getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(String fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public String getTipo_inspeccion_id() {
        return tipo_inspeccion_id;
    }

    public void setTipo_inspeccion_id(String tipo_inspeccion_id) {
        this.tipo_inspeccion_id = tipo_inspeccion_id;
    }

    public String getFecha_ejecucion() {
        return fecha_ejecucion;
    }

    public void setFecha_ejecucion(String fecha_ejecucion) {
        this.fecha_ejecucion = fecha_ejecucion;
    }

    public String getUbicacion_id() {
        return ubicacion_id;
    }

    public void setUbicacion_id(String ubicacion_id) {
        this.ubicacion_id = ubicacion_id;
    }

    public String getDescripcion_inspeccion() {
        return descripcion_inspeccion;
    }

    public void setDescripcion_inspeccion(String descripcion_inspeccion) {
        this.descripcion_inspeccion = descripcion_inspeccion;
    }
}

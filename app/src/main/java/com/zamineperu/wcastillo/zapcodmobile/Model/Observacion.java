package com.zamineperu.wcastillo.zapcodmobile.Model;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

public class Observacion {

    private String id;
    private String programacion_id;
    private String descripcion;
    private String fecha_limite_subsanacion;
    private String responsable_id;
    private String nivel_riesgo_id;
    @SerializedName("img_antes")
    private String imgAntes;
    private String pathImage;
    private String uriPath;

    public Observacion(){}

    public String getProgramacion_id() {
        return programacion_id;
    }

    public void setProgramacion_id(String programacion_id) {
        this.programacion_id = programacion_id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha_limite_subsanacion() {
        return fecha_limite_subsanacion;
    }

    public void setFecha_limite_subsanacion(String fecha_limite_subsanacion) {
        this.fecha_limite_subsanacion = fecha_limite_subsanacion;
    }

    public String getResponsable_id() {
        return responsable_id;
    }

    public void setResponsable_id(String responsable_id) {
        this.responsable_id = responsable_id;
    }

    public String getNivel_riesgo_id() {
        return nivel_riesgo_id;
    }

    public void setNivel_riesgo_id(String nivel_riesgo_id) {
        this.nivel_riesgo_id = nivel_riesgo_id;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public String getImgAntes() {
        return imgAntes;
    }

    public void setImgAntes(String imgAntes) {
        this.imgAntes = imgAntes;
    }

    public String getUriPath() {
        return uriPath;
    }

    public void setUriPath(String uriPath) {
        this.uriPath = uriPath;
    }
}

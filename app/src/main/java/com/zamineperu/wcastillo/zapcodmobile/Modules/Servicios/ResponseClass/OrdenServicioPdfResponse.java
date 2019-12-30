package com.zamineperu.wcastillo.zapcodmobile.Modules.Servicios.ResponseClass;

import com.google.gson.annotations.SerializedName;

public class OrdenServicioPdfResponse {

    @SerializedName("id")
    private String id;
    @SerializedName("codigo")
    private String codigo;
    @SerializedName("numero_cotizacion")
    private String numero_cotizacion;
    @SerializedName("descripcion")
    private String descripcion;
    @SerializedName("fecha")
    private String fecha;
    @SerializedName("turno")
    private String turno;
    @SerializedName("status")
    private String status;
    @SerializedName("equipo")
    private String equipo;
    @SerializedName("ubicacion")
    private String ubicacion;
    @SerializedName("trabajo_realizado")
    private String realizado;
    @SerializedName("hora_inicio")
    private String hora_inicio;
    @SerializedName("hora_fin")
    private String hora_fin;
    @SerializedName("tipo_trabajo")
    private String tipo_trabajo;
    @SerializedName("supervisor_cliente")
    private String supervisor_cliente;
    @SerializedName("supervisor_operacion")
    private String supervisor_operacion;
    @SerializedName("supervisor_seguridad")
    private String supervisor_seguridad;
    @SerializedName("trabajadores")
    private String trabajadores;

    public OrdenServicioPdfResponse(String id, String codigo, String numero_cotizacion, String descripcion){
        this.id = id;
        this.codigo = codigo;
        this.numero_cotizacion = numero_cotizacion;
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

    public String getNumero_cotizacion() {
        return numero_cotizacion;
    }

    public void setNumero_cotizacion(String numero_cotizacion) {
        this.numero_cotizacion = numero_cotizacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEquipo() {
        return equipo;
    }

    public void setEquipo(String equipo) {
        this.equipo = equipo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getRealizado() {
        return realizado;
    }

    public void setRealizado(String realizado) {
        this.realizado = realizado;
    }

    public String getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(String hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public String getHora_fin() {
        return hora_fin;
    }

    public void setHora_fin(String hora_fin) {
        this.hora_fin = hora_fin;
    }

    public String getTipo_trabajo() {
        return tipo_trabajo;
    }

    public void setTipo_trabajo(String tipo_trabajo) {
        this.tipo_trabajo = tipo_trabajo;
    }

    public String getSupervisor_cliente() {
        return supervisor_cliente;
    }

    public void setSupervisor_cliente(String supervisor_cliente) {
        this.supervisor_cliente = supervisor_cliente;
    }

    public String getSupervisor_seguridad() {
        return supervisor_seguridad;
    }

    public void setSupervisor_seguridad(String supervisor_seguridad) {
        this.supervisor_seguridad = supervisor_seguridad;
    }

    public String getSupervisor_operacion() {
        return supervisor_operacion;
    }

    public void setSupervisor_operacion(String supervisor_operacion) {
        this.supervisor_operacion = supervisor_operacion;
    }

    public String getTrabajadores() {
        return trabajadores;
    }

    public void setTrabajadores(String trabajadores) {
        this.trabajadores = trabajadores;
    }
}

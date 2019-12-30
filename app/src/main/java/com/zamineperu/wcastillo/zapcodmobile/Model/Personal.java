package com.zamineperu.wcastillo.zapcodmobile.Model;

public class Personal {

    private String id;
    private String apellido_paterno;
    private String apellido_materno;
    private String nombres;

    public Personal(){}

    public Personal(String id, String apellido_paterno, String apellido_materno, String nombres) {
        this.id = id;
        this.apellido_paterno = apellido_paterno;
        this.apellido_materno = apellido_materno;
        this.nombres = nombres;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApellido_paterno() {
        return apellido_paterno;
    }

    public void setApellido_paterno(String apellido_paterno) {
        this.apellido_paterno = apellido_paterno;
    }

    public String getApellido_materno() {
        return apellido_materno;
    }

    public void setApellido_materno(String apellido_materno) {
        this.apellido_materno = apellido_materno;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String toString() {
        return this.nombres+" "+this.apellido_paterno+" "+this.apellido_materno;
    }
}

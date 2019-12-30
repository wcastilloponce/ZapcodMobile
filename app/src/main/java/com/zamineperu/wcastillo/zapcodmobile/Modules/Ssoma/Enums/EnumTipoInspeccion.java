package com.zamineperu.wcastillo.zapcodmobile.Modules.Ssoma.Enums;

public enum EnumTipoInspeccion {

    PLANIFICADO("287"), NO_PLANIFICADO("288");

    private String id;

    EnumTipoInspeccion(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }
}

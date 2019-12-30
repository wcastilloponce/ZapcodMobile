package com.zamineperu.wcastillo.zapcodmobile.Modules.Ssoma.ResponseClass;

import com.google.gson.annotations.SerializedName;

public class RegistroInspeccionResponse {

    @SerializedName("id")
    private String id;

    public RegistroInspeccionResponse() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

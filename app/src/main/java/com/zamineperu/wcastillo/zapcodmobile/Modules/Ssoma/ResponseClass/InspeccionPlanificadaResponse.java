package com.zamineperu.wcastillo.zapcodmobile.Modules.Ssoma.ResponseClass;

import com.zamineperu.wcastillo.zapcodmobile.Model.Inspeccion;
import com.zamineperu.wcastillo.zapcodmobile.Model.Personal;
import com.zamineperu.wcastillo.zapcodmobile.Model.ProgramacionDetalle;
import com.zamineperu.wcastillo.zapcodmobile.Model.Ubicacion;

public class InspeccionPlanificadaResponse {

    private Inspeccion inspeccion;
    private Ubicacion ubicacion;
    private Personal responsable;
    private ProgramacionDetalle programacionDetalle;

    public Inspeccion getInspeccion() {
        return inspeccion;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public Personal getResponsable() {
        return responsable;
    }

    public ProgramacionDetalle getProgramacionDetalle() {
        return programacionDetalle;
    }

    @Override
    public String toString(){
        return inspeccion.toString()+" - "+programacionDetalle.getFecha_inicio();
    }
}

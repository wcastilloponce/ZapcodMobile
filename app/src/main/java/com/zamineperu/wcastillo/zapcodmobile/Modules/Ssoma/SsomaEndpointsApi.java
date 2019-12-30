package com.zamineperu.wcastillo.zapcodmobile.Modules.Ssoma;

import com.zamineperu.wcastillo.zapcodmobile.Model.Inspeccion;
import com.zamineperu.wcastillo.zapcodmobile.Model.Personal;
import com.zamineperu.wcastillo.zapcodmobile.Model.Programacion;
import com.zamineperu.wcastillo.zapcodmobile.Model.ProgramacionDetalle;
import com.zamineperu.wcastillo.zapcodmobile.Model.Proyecto;
import com.zamineperu.wcastillo.zapcodmobile.Model.Tipo;
import com.zamineperu.wcastillo.zapcodmobile.Model.Ubicacion;
import com.zamineperu.wcastillo.zapcodmobile.Modules.Ssoma.ResponseClass.InspeccionPlanificadaResponse;
import com.zamineperu.wcastillo.zapcodmobile.Modules.Ssoma.ResponseClass.RegistroInspeccionResponse;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface SsomaEndpointsApi {

    String BASE_URL = "https://zamine-temp.com/"; // SERVIDOR DE PRUEBA
    //String BASE_URL = "https://zamineperu-operaciones.com/"; // SERVIDOR DE PRODUCCION
    String URL = BASE_URL+"api/";

    String PATH_GET_LISTA_PROYECTOS_INSPECCIONES = "proyectosInspecciones";
    String PATH_GET_OBTENER_PROGRAMACION = "obtenerProgramacion";
    String PATH_GET_LISTA_TIPOS_INSPECCIONES = "tiposInspecciones";
    String PATH_GET_LISTA_INSPECCIONES_PLANIFICADAS = "inspeccionesPlanificadas";
    String PATH_GET_LISTA_INSPECCIONES_NO_PLANIFICADAS = "inspeccionesNoPlanificadas";
    String PATH_GET_LISTA_PERSONAL_ACTIVO = "personalActivo";
    String PATH_GET_LISTA_UBICACIONES = "ubicaciones";
    String PATH_PATCH_REGISTRAR_INSPECCION_PLANIFICADA = "registrarInspeccionPlanificada";
    String PATH_PATCH_REGISTRAR_INSPECCION_NO_PLANIFICADA = "registrarInspeccionNoPlanificada";
    String PATH_GET_LISTA_NIVELES_RIESGO = "nivelesRiesgo";
    String PATH_PATCH_REGISTRAR_OBSERVACIONES = "registrarObservaciones";
    String PATH_GET_LISTA_INSPECCIONES_FINALIZADAS_USUARIO = "listaInspeccionesFinalizadasPorUsuario";
    String PATH_GET_LISTA_INSPECCIONES_PENDIENTES_USUARIO = "listaInspeccionesPendientesPorUsuario";

    @GET(PATH_GET_LISTA_PROYECTOS_INSPECCIONES)
    Call<ArrayList<Proyecto>> listaProyectosInspecciones();

    @GET(PATH_GET_OBTENER_PROGRAMACION)
    Call<Programacion> obtenerProgramacion(@Query("operacion_id") String operacionId, @Query("anio") String anio);

    @GET(PATH_GET_LISTA_TIPOS_INSPECCIONES)
    Call<ArrayList<Tipo>> tiposInspecciones();

    @GET(PATH_GET_LISTA_INSPECCIONES_PLANIFICADAS)
    Call<ArrayList<InspeccionPlanificadaResponse>> listaInspeccionesPlanificadas(@Query("programacion_id") String id);

    @GET(PATH_GET_LISTA_INSPECCIONES_NO_PLANIFICADAS)
    Call<ArrayList<Inspeccion>> listaInspeccionesNoPlanificadas(@Query("operacion_id") String id);

    @GET(PATH_GET_LISTA_PERSONAL_ACTIVO)
    Call<ArrayList<Personal>> listaPersonalActivo();

    @GET(PATH_GET_LISTA_UBICACIONES)
    Call<ArrayList<Ubicacion>> listaUbicaciones(@Query("operacion_id") String operacion_id);

    @FormUrlEncoded
    @PATCH(PATH_PATCH_REGISTRAR_INSPECCION_PLANIFICADA)
    Call<ProgramacionDetalle> registrarInspeccionPlanificada(@Field("programacion_detalle_id") String programacionDetalleId,
                                                               @Field("fecha_ejecucion") String fecha_ejecucion,
                                                               @Field("descripcion_inspeccion") String descripcion,
                                                               @Field("user_id") String user_id);

    @FormUrlEncoded
    @PATCH(PATH_PATCH_REGISTRAR_INSPECCION_NO_PLANIFICADA)
    Call<ProgramacionDetalle> registrarInspeccionNoPlanificada(@Field("operacion_id") String operacion_id,
                                                  @Field("anio") String anio,
                                                  @Field("inspeccion_id") String inspeccion_id,
                                                  @Field("responsable_user_id") String responsable_user_id,
                                                  @Field("fecha_ejecucion") String fecha_ejecucion,
                                                  @Field("ubicacion_id") String ubicacion_id,
                                                  @Field("descripcion_inspeccion") String descripcion,
                                                  @Field("user_id") String user_id);

    @GET(PATH_GET_LISTA_NIVELES_RIESGO)
    Call<ArrayList<Tipo>> listaNivelesRiesgo();

    @Multipart
    @POST(PATH_PATCH_REGISTRAR_OBSERVACIONES)
    Call<RegistroInspeccionResponse> registrarObservaciones(@Part MultipartBody.Part archivo,
                                                @Part("jsonObservacion") RequestBody jsonObservacion);

    @GET(PATH_GET_LISTA_INSPECCIONES_FINALIZADAS_USUARIO)
    Call<ArrayList<InspeccionPlanificadaResponse>> listaInspeccionesFinalizadasUsuario(@Query("user_id") String userId);

    @GET(PATH_GET_LISTA_INSPECCIONES_PENDIENTES_USUARIO)
    Call<ArrayList<InspeccionPlanificadaResponse>> listaInspeccionesPendientesUsuario(@Query("user_id") String userId);

    @FormUrlEncoded
    @POST("finalizarInspeccion")
    Call<RegistroInspeccionResponse> finalizarInspeccion(@Field("programacion_detalle_id") String programacion_detalle_id);
}
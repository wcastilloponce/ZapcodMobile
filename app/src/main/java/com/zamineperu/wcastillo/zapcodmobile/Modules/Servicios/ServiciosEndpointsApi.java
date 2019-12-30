package com.zamineperu.wcastillo.zapcodmobile.Modules.Servicios;

import com.zamineperu.wcastillo.zapcodmobile.Model.OrdenServicio;
import com.zamineperu.wcastillo.zapcodmobile.Modules.Servicios.ResponseClass.OrdenServicioPdfResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Query;

public interface ServiciosEndpointsApi {

    String BASE_URL = "https://zamine-temp.com/"; // SERVIDOR DE PRUEBA
    //String BASE_URL = "https://zamineperu-operaciones.com/"; // SERVIDOR DE PRODUCCION
    String URL = BASE_URL+"api/";

    String PATH_GET_LISTA_ORDEN_SERVICIO = "ordenesServicio";
    String PATH_GET_ORDEN_SERVICIO = "ordenServicio";
    String PATH_PATCH_FIRMAR_ORDEN_SERVICIO = "firmarOrdenServicio";

    @GET(PATH_GET_LISTA_ORDEN_SERVICIO)
    Call<ArrayList<OrdenServicio>> listaOrdenServicio();

    @GET(PATH_GET_ORDEN_SERVICIO)
    Call<OrdenServicioPdfResponse> getOrdenServicio(@Query("id") String id);

    @FormUrlEncoded
    @PATCH(PATH_PATCH_FIRMAR_ORDEN_SERVICIO)
    Call<OrdenServicioPdfResponse> firmarOrdenServicio(@Field("id") String id,
                                                       @Field("user_id") String user_id,
                                                       @Field("imei") String imei);

}
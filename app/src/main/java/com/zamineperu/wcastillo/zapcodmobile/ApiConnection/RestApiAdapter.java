package com.zamineperu.wcastillo.zapcodmobile.ApiConnection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zamineperu.wcastillo.zapcodmobile.Modules.Servicios.ServiciosEndpointsApi;
import com.zamineperu.wcastillo.zapcodmobile.Modules.Ssoma.SsomaEndpointsApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApiAdapter {

    Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    public EndpointsApi establecerConexionRestApi(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(EndpointsApi.URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(EndpointsApi.class);
    }

    public ServiciosEndpointsApi establecerConexionRestApiServicios(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServiciosEndpointsApi.URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(ServiciosEndpointsApi.class);
    }

    public SsomaEndpointsApi establecerConexionRestApiSsoma(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SsomaEndpointsApi.URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(SsomaEndpointsApi.class);
    }

}
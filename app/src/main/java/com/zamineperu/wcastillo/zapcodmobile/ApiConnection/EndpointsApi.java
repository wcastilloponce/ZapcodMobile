package com.zamineperu.wcastillo.zapcodmobile.ApiConnection;

import com.zamineperu.wcastillo.zapcodmobile.ResponseClass.UserLoginResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EndpointsApi {

    String BASE_URL = "https://zamine-temp.com/"; // SERVIDOR DE PRUEBA
    //String BASE_URL = "https://zamineperu-operaciones.com/"; // SERVIDOR DE PRODUCCION
    String URL = BASE_URL+"api/";

    String PATH_GET_BASIC_LOGIN = "basicLogin";

    @GET(PATH_GET_BASIC_LOGIN)
    Call<UserLoginResponse> basicLogin(@Query("username") String username, @Query("password") String password);
}
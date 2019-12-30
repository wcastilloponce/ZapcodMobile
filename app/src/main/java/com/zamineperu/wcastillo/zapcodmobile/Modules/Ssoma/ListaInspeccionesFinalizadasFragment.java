package com.zamineperu.wcastillo.zapcodmobile.Modules.Ssoma;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.zamineperu.wcastillo.zapcodmobile.Adapters.InspeccionProgramacionDetalleAdapter;
import com.zamineperu.wcastillo.zapcodmobile.ApiConnection.RestApiAdapter;
import com.zamineperu.wcastillo.zapcodmobile.Modules.Ssoma.ResponseClass.InspeccionPlanificadaResponse;
import com.zamineperu.wcastillo.zapcodmobile.R;
import com.zamineperu.wcastillo.zapcodmobile.ResponseClass.UserLoginResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaInspeccionesFinalizadasFragment extends Fragment {

    private View rootview;
    private RecyclerView rvInspectionsList;
    private RestApiAdapter restApiAdapter;
    private SsomaEndpointsApi endpointsApi;
    private String user_id;

    public ListaInspeccionesFinalizadasFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.fragment_lista_inspecciones_finalizadas, container, false);
        inicializarObjetos();
        inicializarVariables();
        listaInspeccionesFinalizadasUsuario();
        return rootview;
    }

    private void inicializarObjetos(){
        restApiAdapter = new RestApiAdapter();
        endpointsApi = restApiAdapter.establecerConexionRestApiSsoma();

        rvInspectionsList =  rootview.findViewById(R.id.ssoma_finished_inspections_list);
        rvInspectionsList.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void inicializarVariables(){
        SharedPreferences preferences = getContext().getSharedPreferences(getResources().getString(R.string.nombre_archivo_preferences),Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String userJson = preferences.getString(getResources().getString(R.string.nombre_variable_user), "");
        final UserLoginResponse user = gson.fromJson(userJson, UserLoginResponse.class);
        user_id = user.getId();
    }

    private void listaInspeccionesFinalizadasUsuario()
    {
        endpointsApi.listaInspeccionesFinalizadasUsuario(user_id).enqueue(new Callback<ArrayList<InspeccionPlanificadaResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<InspeccionPlanificadaResponse>> call, Response<ArrayList<InspeccionPlanificadaResponse>> response) {
                if(response.isSuccessful()) {
                    ArrayList<InspeccionPlanificadaResponse> dataset = response.body();
                    System.out.println("Cantidad: "+dataset.size());
                    InspeccionProgramacionDetalleAdapter adapter = new InspeccionProgramacionDetalleAdapter(dataset);
                    rvInspectionsList.setAdapter(adapter);
                }
                else{
                    System.out.println(getResources().getString(R.string.error_onresponse));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<InspeccionPlanificadaResponse>> call, Throwable t) {
                System.out.println(getResources().getString(R.string.error_onfailure)+t.getMessage());
            }
        });

    }

}
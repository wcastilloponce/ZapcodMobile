package com.zamineperu.wcastillo.zapcodmobile.Modules.Servicios;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.zamineperu.wcastillo.zapcodmobile.Adapters.OrdenServicioAdapter;
import com.zamineperu.wcastillo.zapcodmobile.ApiConnection.RestApiAdapter;
import com.zamineperu.wcastillo.zapcodmobile.Model.OrdenServicio;
import com.zamineperu.wcastillo.zapcodmobile.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdenServicioFragment extends Fragment implements AdapterView.OnItemClickListener {

    private View rootView;
    private RecyclerView recyclerView;
    private OrdenServicioAdapter adapter;
    private RestApiAdapter restApiAdapter;
    private ServiciosEndpointsApi endpointsApi;

    public OrdenServicioFragment() {
        restApiAdapter = new RestApiAdapter();
        endpointsApi = restApiAdapter.establecerConexionRestApiServicios();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_orden_servicio, container, false);

        inicializarObjetos();
        obtenerListaTrabajos();

        return rootView;
    }

    private void inicializarObjetos(){
        recyclerView = rootView.findViewById(R.id.service_os_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new OrdenServicioAdapter(new ArrayList<OrdenServicio>()));
    }

    public void obtenerListaTrabajos() {

        endpointsApi.listaOrdenServicio().enqueue(new Callback<ArrayList<OrdenServicio>>() {
            @Override
            public void onResponse(Call<ArrayList<OrdenServicio>> call, Response<ArrayList<OrdenServicio>> response) {
                if (response.isSuccessful()) {
                    ArrayList<OrdenServicio> dataset = response.body();
                    adapter = new OrdenServicioAdapter(dataset);
                    recyclerView.setAdapter(adapter);
                } else {
                    System.out.println(getResources().getString(R.string.error_onresponse)+response.message());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<OrdenServicio>> call, Throwable t) {
                System.out.println(getResources().getString(R.string.error_onfailure)+t.getMessage());
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
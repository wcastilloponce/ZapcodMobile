package com.zamineperu.wcastillo.zapcodmobile.Modules.Ssoma;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zamineperu.wcastillo.zapcodmobile.Adapters.ObservacionInspeccionAdapter;
import com.zamineperu.wcastillo.zapcodmobile.ApiConnection.RestApiAdapter;
import com.zamineperu.wcastillo.zapcodmobile.Model.Observacion;
import com.zamineperu.wcastillo.zapcodmobile.Model.ProgramacionDetalle;
import com.zamineperu.wcastillo.zapcodmobile.Modules.Ssoma.ResponseClass.RegistroInspeccionResponse;
import com.zamineperu.wcastillo.zapcodmobile.R;

import java.io.File;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroObservacionesFragment extends Fragment implements View.OnClickListener {

    public View rootView;
    private FloatingActionButton fbAddObservation;
    private AppCompatButton btnRegistrarObservaciones;
    private RecyclerView rv_listObservations;
    private ArrayList<String> arrayObservaciones;
    private ProgramacionDetalle programacionDetalle;

    private RestApiAdapter restApiAdapter;
    private SsomaEndpointsApi endpointsApi;

    public RegistroObservacionesFragment() {
        restApiAdapter = new RestApiAdapter();
        endpointsApi = restApiAdapter.establecerConexionRestApiSsoma();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_registro_observaciones, container, false);
        programacionDetalle = recuperarProgramacionDetalle();
        inicializarObjetos();
        inicializarVariables();
        manejarListaObservaciones();

        return rootView;
    }

    private void inicializarObjetos(){
        fbAddObservation = rootView.findViewById(R.id.ss_fb_add_observation);
        rv_listObservations = rootView.findViewById(R.id.ss_list_observations);
        btnRegistrarObservaciones = rootView.findViewById(R.id.ss_btn_registrar_observaciones);

        rv_listObservations.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_listObservations.setAdapter(new ObservacionInspeccionAdapter(new ArrayList<String>()));

        fbAddObservation.setOnClickListener(this);
        btnRegistrarObservaciones.setOnClickListener(this);
    }

    private ProgramacionDetalle recuperarProgramacionDetalle(){
        SharedPreferences preferences = getContext().getSharedPreferences(getResources().getString(R.string.nombre_archivo_preferences), Context.MODE_PRIVATE);
        String json = preferences.getString("programacionDetalle",null);
        return new Gson().fromJson(json, ProgramacionDetalle.class);

    }

    private void inicializarVariables(){
        arrayObservaciones = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.ss_fb_add_observation:
                mostrarFormularioObservacion();
                break;
            case R.id.ss_btn_registrar_observaciones:
                registrarObservaciones();
                break;
        }
    }

    private void mostrarFormularioObservacion(){
        Fragment fragment = new FormularioObservacionFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.ssoma_content_frame, fragment)
                .commit();
    }

    private void manejarListaObservaciones(){
        SharedPreferences preferences = getContext().getSharedPreferences(getResources().getString(R.string.nombre_archivo_preferences),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {}.getType();

        if(preferences.contains("listaObservaciones")){
            String json = preferences.getString("listaObservaciones", "");
            arrayObservaciones = gson.fromJson(json, type);
            ObservacionInspeccionAdapter adapter = new ObservacionInspeccionAdapter(arrayObservaciones);
            rv_listObservations.setAdapter(adapter);
        }
        else{
            String json = gson.toJson(arrayObservaciones, type);
            editor.putString("listaObservaciones",json);
            editor.apply();
        }

    }

    private void registrarObservaciones(){
        if(arrayObservaciones.size()==0 || arrayObservaciones==null){
            endpointsApi.finalizarInspeccion(programacionDetalle.getId()).enqueue(new Callback<RegistroInspeccionResponse>() {
                @Override
                public void onResponse(Call<RegistroInspeccionResponse> call, Response<RegistroInspeccionResponse> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(getContext(), "La inspección se ejecutó correctamente", Toast.LENGTH_SHORT).show();
                        limpiarObservaciones();
                        gotToFinishedInspections();
                    }
                    else{
                        System.out.println(getResources().getString(R.string.error_onresponse) + response.message());
                    }
                }

                @Override
                public void onFailure(Call<RegistroInspeccionResponse> call, Throwable t) {
                    System.out.println(getResources().getString(R.string.error_onfailure) + t.getMessage());
                }
            });
        }
        else {
            for (String jsonObservacion : arrayObservaciones) {
                File file;
                Observacion observacion = new Gson().fromJson(jsonObservacion, Observacion.class);
                file = new File(observacion.getPathImage());

                String filename = file.getName();
                observacion.setImgAntes(filename);

                MultipartBody.Part fileImage = MultipartBody.Part.createFormData("archivo", filename, RequestBody.create(MediaType.parse("image/*"), file));
                RequestBody paramObservation = RequestBody.create(MediaType.parse("text/plain"), jsonObservacion);

                endpointsApi.registrarObservaciones(fileImage, paramObservation).enqueue(new Callback<RegistroInspeccionResponse>() {
                    @Override
                    public void onResponse(Call<RegistroInspeccionResponse> call, Response<RegistroInspeccionResponse> response) {
                        if (response.isSuccessful()) {
                            System.out.println(response.body().getId());
                            Toast.makeText(getContext(), "Las observaciones se registraron correctamente", Toast.LENGTH_SHORT).show();
                            limpiarObservaciones();
                            gotToFinishedInspections();
                        } else {
                            System.out.println(getResources().getString(R.string.error_onresponse) + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<RegistroInspeccionResponse> call, Throwable t) {
                        System.out.println(getResources().getString(R.string.error_onfailure) + t.getMessage());
                    }
                });
            }
        }
    }

    private void limpiarObservaciones(){
        SharedPreferences preferences = getContext().getSharedPreferences(getResources().getString(R.string.nombre_archivo_preferences),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("listaObservaciones").commit();
    }

    private void gotToFinishedInspections()
    {
        Fragment fragment = new ListaInspeccionesFinalizadasFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.ssoma_content_frame, fragment)
                .addToBackStack(null)
                .commit();
    }

}
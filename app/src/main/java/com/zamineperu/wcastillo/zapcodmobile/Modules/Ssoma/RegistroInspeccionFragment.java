package com.zamineperu.wcastillo.zapcodmobile.Modules.Ssoma;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zamineperu.wcastillo.zapcodmobile.ApiConnection.RestApiAdapter;
import com.zamineperu.wcastillo.zapcodmobile.Model.Inspeccion;
import com.zamineperu.wcastillo.zapcodmobile.Model.Personal;
import com.zamineperu.wcastillo.zapcodmobile.Model.Programacion;
import com.zamineperu.wcastillo.zapcodmobile.Model.ProgramacionDetalle;
import com.zamineperu.wcastillo.zapcodmobile.Model.Proyecto;
import com.zamineperu.wcastillo.zapcodmobile.Model.Tipo;
import com.zamineperu.wcastillo.zapcodmobile.Model.Ubicacion;
import com.zamineperu.wcastillo.zapcodmobile.Modules.Ssoma.Enums.EnumTipoInspeccion;
import com.zamineperu.wcastillo.zapcodmobile.Modules.Ssoma.ResponseClass.InspeccionPlanificadaResponse;
import com.zamineperu.wcastillo.zapcodmobile.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroInspeccionFragment extends Fragment implements View.OnClickListener {

    public View rootview;
    public String anio, user_id, programacionId;
    public Proyecto proyecto;
    public Tipo tipoInspeccion;
    public Inspeccion inspeccion;
    public Personal responsable;
    public Ubicacion ubicacion;
    public ProgramacionDetalle programacionDetalle;
    public AutoCompleteTextView dropdownProyectos, dropdownAnios, dropdownTipos, dropdownInspecciones,
            dropdownPersonal, dropdownUbicaciones;
    public TextInputEditText txt_fecha_ejecucion, txt_descripcion;
    public TextView tv_error_registro;
    private AppCompatButton btn_registrar;

    private RestApiAdapter restApiAdapter;
    private SsomaEndpointsApi endpointsApi;

    public RegistroInspeccionFragment() {
        restApiAdapter = new RestApiAdapter();
        endpointsApi = restApiAdapter.establecerConexionRestApiSsoma();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_registro_inspeccion, container, false);

        if(savedInstanceState!=null){
            Gson gson = new Gson();
            if(savedInstanceState.containsKey("proyecto")){
                String json = savedInstanceState.getString("proyecto");
                proyecto = gson.fromJson(json,Proyecto.class);
            }
            if(savedInstanceState.containsKey("anio")){
                anio = savedInstanceState.getString("anio");
            }
            if(savedInstanceState.containsKey("tipoInspeccion")){
                String json = savedInstanceState.getString("tipoInspeccion");
                tipoInspeccion = gson.fromJson(json,Tipo.class);
            }
            if(savedInstanceState.containsKey("inspeccion")){
                String json = savedInstanceState.getString("inspeccion");
                inspeccion = gson.fromJson(json,Inspeccion.class);
            }
            if(savedInstanceState.containsKey("responsable")){
                String json = savedInstanceState.getString("responsable");
                responsable = gson.fromJson(json,Personal.class);
            }
            if(savedInstanceState.containsKey("ubicacion")){
                String json = savedInstanceState.getString("ubicacion");
                ubicacion = gson.fromJson(json,Ubicacion.class);
            }
        }

        Bundle bundle = getArguments();
        user_id = bundle.getString("user_id");
        
        inicializarObjetos();
        cargarFormulario();
        return rootview;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Gson gson = new Gson();

        if(proyecto != null){ outState.putString("proyecto", gson.toJson(proyecto));}
        if(anio != null){ outState.putString("anio", anio);}
        if(tipoInspeccion != null){ outState.putString("tipoInspeccion", gson.toJson(tipoInspeccion));}
        if(inspeccion != null){ outState.putString("inspeccion", gson.toJson(inspeccion));}
        if(responsable != null){ outState.putString("responsable", gson.toJson(responsable));}
        if(ubicacion != null){ outState.putString("ubicacion", gson.toJson(ubicacion));}
    }

    private void inicializarObjetos()
    {
        dropdownProyectos = rootview.findViewById(R.id.dropdown_lista_proyectos);
        dropdownProyectos.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dropdownProyectos.showDropDown();
                return false;
            }
        });
        dropdownAnios = rootview.findViewById(R.id.dropdown_lista_anios);
        dropdownAnios.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dropdownAnios.showDropDown();
                return false;
            }
        });
        dropdownTipos = rootview.findViewById(R.id.dropdown_lista_tipos_inspeccion);
        dropdownTipos.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dropdownTipos.showDropDown();
                return false;
            }
        });
        dropdownInspecciones = rootview.findViewById(R.id.dropdown_lista_inspecciones);
        dropdownInspecciones.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dropdownInspecciones.showDropDown();
                return false;
            }
        });
        dropdownPersonal = rootview.findViewById(R.id.dropdown_lista_responsables);
        dropdownPersonal.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dropdownPersonal.showDropDown();
                return false;
            }
        });
        dropdownUbicaciones = rootview.findViewById(R.id.dropdown_lista_ubicaciones);
        dropdownUbicaciones.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dropdownUbicaciones.showDropDown();
                return false;
            }
        });

        txt_fecha_ejecucion = rootview.findViewById(R.id.etxt_fecha_ejecucion);
        txt_descripcion = rootview.findViewById(R.id.etxt_descripcion);
        btn_registrar = rootview.findViewById(R.id.ss_btn_registrar_inspeccion);
        tv_error_registro = rootview.findViewById(R.id.ss_etxt_error_registro_inspeccion);

        btn_registrar.setOnClickListener(this);
    }

    public void cargarFormulario()
    {
        cargarFechaInspeccion();
        obtenerListaProyectos();
        obtenerListaAnios();
        obtenerListaTiposInspeccion();
    }

    public void obtenerListaProyectos() {

        endpointsApi.listaProyectosInspecciones().enqueue(new Callback<ArrayList<Proyecto>>() {
            @Override
            public void onResponse(Call<ArrayList<Proyecto>> call, Response<ArrayList<Proyecto>> response) {
                if (response.isSuccessful()) {
                    ArrayList<Proyecto> listaProyectos = response.body();
                    ArrayAdapter<Proyecto> adapterProyectos = new ArrayAdapter<>(getContext(),R.layout.dropdown_menu_popup_item,listaProyectos);
                    dropdownProyectos.setAdapter(adapterProyectos);

                    dropdownProyectos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            proyecto = (Proyecto) parent.getAdapter().getItem(position);
                            obtenerListaUbicaciones(proyecto.getId());
                        }
                    });
                } else {
                    System.out.println(getResources().getString(R.string.error_onresponse));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Proyecto>> call, Throwable t) {
                System.out.println(getResources().getString(R.string.error_onfailure)+t.getMessage());
            }
        });
    }

    private void obtenerListaAnios() {
        String[] lista_anios = new String[] {"2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026"};
        ArrayAdapter<String> adapterAnios = new ArrayAdapter<>(getContext(),R.layout.dropdown_menu_popup_item,lista_anios);
        dropdownAnios.setAdapter(adapterAnios);

        dropdownAnios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                anio = (String) parent.getAdapter().getItem(position);
                obtenerProgramacion(proyecto.getId(), anio);
            }
        });
    }

    public void obtenerProgramacion(String operacionId, String anio)
    {
        endpointsApi.obtenerProgramacion(operacionId, anio).enqueue(new Callback<Programacion>() {
            @Override
            public void onResponse(Call<Programacion> call, Response<Programacion> response) {
                if (response.isSuccessful()) {
                    Programacion programacion = response.body();
                    programacionId = programacion.getId();
                } else {
                    System.out.println(getResources().getString(R.string.error_onresponse));
                }
            }

            @Override
            public void onFailure(Call<Programacion> call, Throwable t) {
                System.out.println(getResources().getString(R.string.error_onfailure)+t.getMessage());
            }
        });
    }

    public void obtenerListaTiposInspeccion() {

        endpointsApi.tiposInspecciones().enqueue(new Callback<ArrayList<Tipo>>() {
            @Override
            public void onResponse(Call<ArrayList<Tipo>> call, Response<ArrayList<Tipo>> response) {
                if (response.isSuccessful()) {
                    ArrayList<Tipo> listaTipos = response.body();
                    ArrayAdapter<Tipo> adapterTipos = new ArrayAdapter<>(getContext(),R.layout.dropdown_menu_popup_item,listaTipos);
                    dropdownTipos.setAdapter(adapterTipos);

                    dropdownTipos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            tipoInspeccion = (Tipo)parent.getAdapter().getItem(position);
                            if(tipoInspeccion.getId().equals(EnumTipoInspeccion.PLANIFICADO.getId())){
                                obtenerListaInspeccionesPlanificadas();
                                dropdownPersonal.setEnabled(true);
                                dropdownUbicaciones.setEnabled(true);
                            }
                            if(tipoInspeccion.getId().equals(EnumTipoInspeccion.NO_PLANIFICADO.getId())){
                                obtenerListaInspeccionesNoPlanificadas();
                                obtenerPersonalActivo();
                            }
                            dropdownInspecciones.setText("");
                            dropdownPersonal.setText("");
                            dropdownUbicaciones.setText("");
                            txt_descripcion.setText("");
                        }
                    });
                } else {
                    System.out.println(getResources().getString(R.string.error_onresponse));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Tipo>> call, Throwable t) {
                System.out.println(getResources().getString(R.string.error_onfailure)+t.getMessage());
            }
        });
    }

    public void obtenerListaInspeccionesPlanificadas() {
        endpointsApi.listaInspeccionesPlanificadas(programacionId).enqueue(new Callback<ArrayList<InspeccionPlanificadaResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<InspeccionPlanificadaResponse>> call, Response<ArrayList<InspeccionPlanificadaResponse>> response) {
                if (response.isSuccessful()) {
                    ArrayList<InspeccionPlanificadaResponse> listaInspecciones = response.body();
                    ArrayAdapter<InspeccionPlanificadaResponse> adapterInspecciones = new ArrayAdapter<>(getContext(),R.layout.dropdown_menu_popup_item,listaInspecciones);
                    dropdownInspecciones.setAdapter(adapterInspecciones);

                    dropdownInspecciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            InspeccionPlanificadaResponse inspeccionPlanificada = (InspeccionPlanificadaResponse) parent.getAdapter().getItem(position);
                            inspeccion = inspeccionPlanificada.getInspeccion();
                            ubicacion = inspeccionPlanificada.getUbicacion();
                            responsable = inspeccionPlanificada.getResponsable();
                            programacionDetalle = inspeccionPlanificada.getProgramacionDetalle();

                            dropdownPersonal.setText(responsable.toString());
                            dropdownPersonal.setEnabled(false);
                            dropdownUbicaciones.setText(ubicacion.toString());
                            dropdownUbicaciones.setEnabled(false);
                        }
                    });
                } else {
                    System.out.println(getResources().getString(R.string.error_onresponse));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<InspeccionPlanificadaResponse>> call, Throwable t) {
                System.out.println(getResources().getString(R.string.error_onfailure)+t.getMessage());
            }
        });
    }

    public void obtenerListaInspeccionesNoPlanificadas() {

        endpointsApi.listaInspeccionesNoPlanificadas(proyecto.getId()).enqueue(new Callback<ArrayList<Inspeccion>>() {
            @Override
            public void onResponse(Call<ArrayList<Inspeccion>> call, Response<ArrayList<Inspeccion>> response) {
                if (response.isSuccessful()) {
                    ArrayList<Inspeccion> listaInspecciones = response.body();
                    ArrayAdapter<Inspeccion> adapterInspecciones = new ArrayAdapter<>(getContext(),R.layout.dropdown_menu_popup_item,listaInspecciones);
                    dropdownInspecciones.setAdapter(adapterInspecciones);

                    dropdownInspecciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            inspeccion = (Inspeccion) parent.getAdapter().getItem(position);
                        }
                    });
                } else {
                    System.out.println(getResources().getString(R.string.error_onresponse));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Inspeccion>> call, Throwable t) {
                System.out.println(getResources().getString(R.string.error_onfailure)+t.getMessage());
            }
        });
    }

    public void obtenerPersonalActivo() {

        endpointsApi.listaPersonalActivo().enqueue(new Callback<ArrayList<Personal>>() {
            @Override
            public void onResponse(Call<ArrayList<Personal>> call, Response<ArrayList<Personal>> response) {
                if (response.isSuccessful()) {
                    ArrayList<Personal> listaPersonal = response.body();
                    ArrayAdapter<Personal> adapterPersonal = new ArrayAdapter<>(getContext(),R.layout.dropdown_menu_popup_item,listaPersonal);
                    dropdownPersonal.setAdapter(adapterPersonal);

                    dropdownPersonal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            responsable = (Personal) parent.getAdapter().getItem(position);
                        }
                    });
                } else {
                    System.out.println(getResources().getString(R.string.error_onresponse));
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Personal>> call, Throwable t) {
                System.out.println(getResources().getString(R.string.error_onfailure)+t.getMessage());
            }
        });
    }

    public void obtenerListaUbicaciones(String operacion_id) {
        endpointsApi.listaUbicaciones(operacion_id).enqueue(new Callback<ArrayList<Ubicacion>>() {
            @Override
            public void onResponse(Call<ArrayList<Ubicacion>> call, Response<ArrayList<Ubicacion>> response) {
                if (response.isSuccessful()) {
                    ArrayList<Ubicacion> listaUbicacion = response.body();
                    ArrayAdapter<Ubicacion> adapterUbicacion = new ArrayAdapter<>(getContext(),R.layout.dropdown_menu_popup_item,listaUbicacion);
                    dropdownUbicaciones.setAdapter(adapterUbicacion);

                    dropdownUbicaciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ubicacion = (Ubicacion) parent.getAdapter().getItem(position);
                        }
                    });
                } else {
                    System.out.println(getResources().getString(R.string.error_onresponse));
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Ubicacion>> call, Throwable t) {
                System.out.println(getResources().getString(R.string.error_onfailure)+t.getMessage());
            }
        });
    }

    public void registrarInspeccion() {

        if(proyecto==null){
            tv_error_registro.setText("Debe elegir un proyecto.");
            return;
        }
        if(anio==null){
            tv_error_registro.setText("Debe elegir un año.");
            return;
        }
        if(tipoInspeccion==null){
            tv_error_registro.setText("Debe elegir un tipo de inspección.");
            return;
        }
        if(inspeccion==null){
            tv_error_registro.setText("Debe elegir un tipo de inspección.");
            return;
        }
        if(ubicacion==null){
            tv_error_registro.setText("Debe elegir una ubicación.");
            return;
        }
        if(txt_descripcion.getText().toString().isEmpty()){
            tv_error_registro.setText("Debe ingresar una descripción.");
            return;
        }

        String fecha_ejecucion = txt_fecha_ejecucion.getText().toString();
        String descripcion = txt_descripcion.getText().toString();
        String tipo_inspeccion_id = tipoInspeccion.getId().trim();



        if(tipo_inspeccion_id.equals(EnumTipoInspeccion.PLANIFICADO.getId())){
            endpointsApi.registrarInspeccionPlanificada(programacionDetalle.getId(), fecha_ejecucion, descripcion,
                    user_id).enqueue(new Callback<ProgramacionDetalle>() {
                @Override
                public void onResponse(Call<ProgramacionDetalle> call, Response<ProgramacionDetalle> response) {
                    if (response.isSuccessful()) {
                        programacionDetalle = response.body();
                        saveProgramacionDetalleMemory(programacionDetalle);
                        Toast.makeText(getContext(), "La inspección se registró correctamente", Toast.LENGTH_SHORT).show();
                        mostrarListaObservaciones();
                    } else {
                        System.out.println(getResources().getString(R.string.error_onresponse) + response.message());
                    }
                }

                @Override
                public void onFailure(Call<ProgramacionDetalle> call, Throwable t) {
                    System.out.println(getResources().getString(R.string.error_onfailure) + t.getMessage());
                }
            });

        }
        else if(tipo_inspeccion_id .equals(EnumTipoInspeccion.NO_PLANIFICADO.getId())){

            String operacion_id = proyecto.getId();
            String inspeccion_id = inspeccion.getId();
            String responsable_id = responsable.getId();
            String ubicacion_id = ubicacion.getId();

            endpointsApi.registrarInspeccionNoPlanificada(operacion_id, anio, inspeccion_id, responsable_id,
                    fecha_ejecucion, ubicacion_id, descripcion, user_id).enqueue(new Callback<ProgramacionDetalle>() {
                @Override
                public void onResponse(Call<ProgramacionDetalle> call, Response<ProgramacionDetalle> response) {
                    if (response.isSuccessful()) {
                        programacionDetalle = response.body();
                        saveProgramacionDetalleMemory(programacionDetalle);
                        Toast.makeText(getContext(), "La inspección se registró correctamente", Toast.LENGTH_SHORT).show();
                        mostrarListaObservaciones();
                    } else {
                        System.out.println(getResources().getString(R.string.error_onresponse) + response.message());
                    }
                }

                @Override
                public void onFailure(Call<ProgramacionDetalle> call, Throwable t) {
                    System.out.println(getResources().getString(R.string.error_onfailure) + t.getMessage());
                }
            });

        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.ss_btn_registrar_inspeccion:
                registrarInspeccion();
                break;
        }
    }

    private void cargarFechaInspeccion()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String fecha_inspeccion = dateFormat.format(date);
        txt_fecha_ejecucion.setText(fecha_inspeccion);
    }

    private void mostrarListaObservaciones(){
        Fragment fragment = new RegistroObservacionesFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.ssoma_content_frame, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void saveProgramacionDetalleMemory(ProgramacionDetalle programacion){
        SharedPreferences preferences = getContext().getSharedPreferences(getResources().getString(R.string.nombre_archivo_preferences),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(programacion);
        editor.putString("programacionDetalle",json);
        editor.apply();
    }

}
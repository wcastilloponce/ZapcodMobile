package com.zamineperu.wcastillo.zapcodmobile.Modules.Servicios;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.zamineperu.wcastillo.zapcodmobile.ApiConnection.RestApiAdapter;
import com.zamineperu.wcastillo.zapcodmobile.R;
import com.zamineperu.wcastillo.zapcodmobile.Modules.Servicios.ResponseClass.OrdenServicioPdfResponse;
import com.zamineperu.wcastillo.zapcodmobile.ResponseClass.UserLoginResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdenServicioDetailFragment extends Fragment implements View.OnClickListener {

    private String orden_servicio_id, user_id, imei;
    private Button btn_generate_pdf, btn_firma_cliente;
    private OrdenServicioPdfResponse ordenServicioPdfResponse;
    private TextView tv_fecha,tv_turno,tv_status,tv_otmina,tv_equipo,tv_ubicacion,tv_trabajo_realizado,
            tv_inicio,tv_fin,tv_tipo_trabajo,tv_descripcion,tv_sup_operacion,tv_sup_seguridad,tv_sup_cliente,
            tv_horas_totales;
    private ImageView img_firma_cliente;
    private View view;
    private TableLayout tableTrabajadores;
    private Bitmap bm_firma_cliente;
    private SharedPreferences preferences;
    private boolean signed;

    private RestApiAdapter restApiAdapter;
    private ServiciosEndpointsApi endpointsApi;

    protected String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };

    private static final int MULTIPLE_PERMISSIONS = 100;

    public OrdenServicioDetailFragment()
    {
        restApiAdapter = new RestApiAdapter();
        endpointsApi = restApiAdapter.establecerConexionRestApiServicios();
        signed = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_orden_servicio_detail, container, false);
        inicializarObjetos(view);

        if(savedInstanceState!=null){
            bm_firma_cliente = savedInstanceState.getParcelable(getResources().getString(R.string.sv_orden_servicio_detalle_variable_firma));
            signed = savedInstanceState.getBoolean(getResources().getString(R.string.sv_orden_servicio_detalle_variable_signed));
            img_firma_cliente.setImageBitmap(bm_firma_cliente);
        }

        Bundle bundle = getArguments();
        if(bundle != null){
            orden_servicio_id = bundle.getString(getResources().getString(R.string.sv_orden_servicio_detalle_variable_os_id));
            obtenerTrabajo();
        }

        preferences = getContext().getSharedPreferences(getResources().getString(R.string.nombre_archivo_preferences),Context.MODE_PRIVATE);
        //obteniendo usuario logueado
        Gson gson = new Gson();
        String userJson = preferences.getString(getResources().getString(R.string.nombre_variable_user), "");
        final UserLoginResponse user = gson.fromJson(userJson, UserLoginResponse.class);
        user_id = user.getId();
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(getResources().getString(R.string.sv_orden_servicio_detalle_variable_firma),bm_firma_cliente);
        outState.putBoolean(getResources().getString(R.string.sv_orden_servicio_detalle_variable_signed),signed);
    }

    private void inicializarObjetos(View view){
        tv_fecha = view.findViewById(R.id.service_os_pdf_fecha);
        tv_turno = view.findViewById(R.id.service_os_pdf_turno);
        tv_status = view.findViewById(R.id.service_os_pdf_status);
        tv_otmina = view.findViewById(R.id.service_os_pdf_otmina);
        tv_equipo = view.findViewById(R.id.service_os_pdf_equipo);
        tv_ubicacion = view.findViewById(R.id.service_os_pdf_ubicacion);
        tv_trabajo_realizado = view.findViewById(R.id.service_os_pdf_trabajo_realizado);
        tv_inicio = view.findViewById(R.id.service_os_pdf_inicio);
        tv_fin = view.findViewById(R.id.service_os_pdf_fin);
        tv_tipo_trabajo = view.findViewById(R.id.service_os_pdf_tipo_trabajo);
        tv_descripcion = view.findViewById(R.id.service_os_pdf_descripcion);
        tv_sup_operacion = view.findViewById(R.id.service_os_pdf_supervisor_operacion);
        tv_sup_seguridad = view.findViewById(R.id.service_os_pdf_supervisor_seguridad);
        tv_sup_cliente = view.findViewById(R.id.service_os_pdf_supervisor_cliente);
        tv_horas_totales = view.findViewById(R.id.service_os_pdf_total_horas_trabajadores);
        tableTrabajadores = view.findViewById(R.id.service_od_pdf_lista_trabajadores);

        img_firma_cliente = view.findViewById(R.id.service_os_pdf_firma_cliente);
        btn_generate_pdf =  view.findViewById(R.id.service_os_btn_generate_pdf);
        btn_generate_pdf.setOnClickListener(this);
        btn_firma_cliente =  view.findViewById(R.id.service_os_btn_firma_cliente);
        btn_firma_cliente.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.service_os_btn_generate_pdf:
                autorizarGeneracionPDF();
                break;
            case R.id.service_os_btn_firma_cliente:
                Intent intent = new Intent(getContext(),FirmaDigitalActivity.class);
                startActivityForResult(intent,0);break;
        }
    }

    public void generarPDF(){
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(800,1500,1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        View content = view;

        view.findViewById(R.id.service_os_btn_generate_pdf).setVisibility(View.GONE);
        view.findViewById(R.id.service_os_btn_firma_cliente).setVisibility(View.GONE);

        int measureWidth = View.MeasureSpec.makeMeasureSpec(page.getCanvas().getWidth(), View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(page.getCanvas().getHeight(), View.MeasureSpec.EXACTLY);

        content.measure(measureWidth, measuredHeight);
        content.layout(0, 0, page.getCanvas().getWidth(), page.getCanvas().getHeight());

        content.draw(page.getCanvas());

        document.finishPage(page);

        String document_name = " FORMATO OS - "+tv_equipo.getText().toString()+" "+tv_fecha.getText().toString();
        //String targetPdf = "/sdcard/FORMATO OS - "+document_name+".pdf";
        String targetPdf = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+document_name+".pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(getContext(), getResources().getString(R.string.sv_orden_servicio_detalle_msj_pdf_ok)+targetPdf, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), getResources().getString(R.string.sv_orden_servicio_detalle_msj_pdf_error) + e.toString(),
                    Toast.LENGTH_LONG).show();
        }
        view.findViewById(R.id.service_os_btn_generate_pdf).setVisibility(View.VISIBLE);
        view.findViewById(R.id.service_os_btn_firma_cliente).setVisibility(View.VISIBLE
        );
        // close the document
        document.close();
        if(signed){
            System.out.println(user_id);
            firmarOrdenServicio();
        }
        else{
            System.out.println(getResources().getString(R.string.sv_orden_servicio_detalle_msj_pdf_sin_firma));
        }
    }

    public void obtenerTrabajo() {

        endpointsApi.getOrdenServicio(orden_servicio_id).enqueue(new Callback<OrdenServicioPdfResponse>() {
            @Override
            public void onResponse(Call<OrdenServicioPdfResponse> call, Response<OrdenServicioPdfResponse> response) {
                if (response.isSuccessful()) {
                    ordenServicioPdfResponse = response.body();
                    asignarOrdenServicioObjetos(ordenServicioPdfResponse);
                } else {
                    System.out.println(getResources().getString(R.string.error_onresponse)+response.message());
                }
            }

            @Override
            public void onFailure(Call<OrdenServicioPdfResponse> call, Throwable t) {
                System.out.println(getResources().getString(R.string.error_onfailure)+t.getMessage());
            }
        });
    }

    public void firmarOrdenServicio(){

        endpointsApi.firmarOrdenServicio(orden_servicio_id, user_id, imei).enqueue(new Callback<OrdenServicioPdfResponse>() {
            @Override
            public void onResponse(Call<OrdenServicioPdfResponse> call, Response<OrdenServicioPdfResponse> response) {
                if (response.isSuccessful()) {
                    OrdenServicioPdfResponse ordenServicioPdfResponse = response.body();
                } else {
                    System.out.println(getResources().getString(R.string.error_onresponse)+response.message());
                }
            }

            @Override
            public void onFailure(Call<OrdenServicioPdfResponse> call, Throwable t) {
                System.out.println(getResources().getString(R.string.error_onresponse)+t.getMessage());
            }
        });
    }

    private void asignarOrdenServicioObjetos(OrdenServicioPdfResponse ordenServicioPdfResponse){
        Gson gson = new Gson();

        tv_fecha.setText(ordenServicioPdfResponse.getFecha());
        tv_turno.setText(ordenServicioPdfResponse.getTurno());
        tv_status.setText(ordenServicioPdfResponse.getStatus());
        tv_otmina.setText(ordenServicioPdfResponse.getNumero_cotizacion());
        tv_equipo.setText(ordenServicioPdfResponse.getEquipo());
        tv_ubicacion.setText(ordenServicioPdfResponse.getUbicacion());
        tv_trabajo_realizado.setText(ordenServicioPdfResponse.getRealizado());
        tv_inicio.setText(ordenServicioPdfResponse.getHora_inicio());
        tv_fin.setText(ordenServicioPdfResponse.getHora_fin());
        tv_tipo_trabajo.setText(ordenServicioPdfResponse.getTipo_trabajo());
        tv_descripcion.setText(ordenServicioPdfResponse.getDescripcion());
        tv_sup_operacion.setText(ordenServicioPdfResponse.getSupervisor_operacion());
        tv_sup_seguridad.setText(ordenServicioPdfResponse.getSupervisor_seguridad());
        tv_sup_cliente.setText(ordenServicioPdfResponse.getSupervisor_cliente());

        double suma_horas = 0;
        JsonObject[] lista_trabajadores = gson.fromJson(ordenServicioPdfResponse.getTrabajadores(),JsonObject[].class);
        for (JsonObject trabajador: lista_trabajadores)
        {
            String nombre = trabajador.get("nombre").getAsString();
            String hora_inicio = trabajador.get("hora_inicio").toString().replaceAll("\"","");
            String hora_fin = trabajador.get("hora_fin").toString().replaceAll("\"","");
            String horas = trabajador.get("horas").toString();

            suma_horas += Double.parseDouble(horas);

            if(hora_inicio.equals("null")) hora_inicio = "";
            if(hora_fin.equals("null")) hora_fin = "";
            if(horas.equals("null")) horas = "";

            TextView tv_nombre = new TextView(getContext());
            TextView tv_inicio = new TextView(getContext());
            TextView tv_fin = new TextView(getContext());
            TextView tv_horas = new TextView(getContext());

            tv_nombre.setText(nombre);
            tv_nombre.setTextSize(10);
            tv_inicio.setText(hora_inicio);
            tv_inicio.setTextSize(10);
            tv_fin.setText(hora_fin);
            tv_fin.setTextSize(10);
            tv_horas.setText(horas);
            tv_horas.setTextSize(10);
            tv_horas.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);

            TableRow row = new TableRow(getContext());
            row.addView(tv_nombre);
            row.addView(tv_inicio);
            row.addView(tv_fin);
            row.addView(tv_horas);
            row.setBackgroundResource(R.drawable.border_table);
            tableTrabajadores.addView(row);
        }
        tv_horas_totales.setText(String.valueOf(suma_horas));
    }

    private void autorizarGeneracionPDF(){
        int permissionExternalStorage = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionPhoneState = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.READ_PHONE_STATE);

        if (permissionExternalStorage != PackageManager.PERMISSION_GRANTED ||
                permissionPhoneState != PackageManager.PERMISSION_GRANTED) {

            boolean shouldShowExternalStorage = ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE);
            boolean shouldShowPhoneState = ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.READ_PHONE_STATE);

            if (shouldShowExternalStorage || shouldShowPhoneState) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                        .setTitle(getResources().getString(R.string.sv_orden_servicio_detalle_permiso_sdcard_titulo))
                        .setMessage(getResources().getString(R.string.sv_orden_servicio_detalle_permiso_sdcard_texto))
                        .setPositiveButton(getResources().getString(R.string.sv_orden_servicio_detalle_permiso_sdcard_aceptar), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                solicitarPermisoMemoriaExterna();
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.sv_orden_servicio_permiso_sdcard_cancelar), null);
                builder.show();

            } else {
                solicitarPermisoMemoriaExterna();
            }
        }
        else{
            TelephonyManager mTelephony = (TelephonyManager) getContext().getSystemService(getContext().TELEPHONY_SERVICE);
            imei = mTelephony.getImei();
            generarPDF();
        }
    }

    private void solicitarPermisoMemoriaExterna() {
        ActivityCompat.requestPermissions(getActivity(),permissions,MULTIPLE_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    generarPDF();
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.sv_orden_servicio_detalle_msj_pdf_denegado), Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode == 1) {
            bm_firma_cliente = BitmapFactory.decodeByteArray(
                    data.getByteArrayExtra("byteArray"), 0,
                    data.getByteArrayExtra("byteArray").length);
            img_firma_cliente.setImageBitmap(bm_firma_cliente);
            signed = true;
        }
    }

}
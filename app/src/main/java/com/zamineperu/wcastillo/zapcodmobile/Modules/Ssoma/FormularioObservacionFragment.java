package com.zamineperu.wcastillo.zapcodmobile.Modules.Ssoma;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zamineperu.wcastillo.zapcodmobile.ApiConnection.RestApiAdapter;
import com.zamineperu.wcastillo.zapcodmobile.Model.Observacion;
import com.zamineperu.wcastillo.zapcodmobile.Model.Personal;
import com.zamineperu.wcastillo.zapcodmobile.Model.ProgramacionDetalle;
import com.zamineperu.wcastillo.zapcodmobile.Model.Tipo;
import com.zamineperu.wcastillo.zapcodmobile.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormularioObservacionFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private TextInputEditText txtDescripcion, txtFechaLimite;
    private AutoCompleteTextView dropdownResponsables, dropdownNivelRiesgo;
    private TextView tv_error_registro;
    private ImageView imagen;
    private Button btnRegistrarObservacion;
    private Personal responsable;
    private Tipo nivelReiesgo;

    private RestApiAdapter restApiAdapter;
    private SsomaEndpointsApi endpointsApi;

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_TAKE_GALLERY = 11;
    private static final int REQUEST_PERMISO_CAMARA_ = 2;
    String pathPhoto;
    String uriPhoto;


    public FormularioObservacionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_formulario_observacion, container, false);
        inicializarObjetos();
        inicializarVariables();

        obtenerPersonalActivo();
        obtenerNivelesRiesgo();
        setHasOptionsMenu(true);

        return rootView;
    }

    private void inicializarObjetos(){
        txtDescripcion = rootView.findViewById(R.id.ss_formulario_observacion_descripcion);
        txtFechaLimite = rootView.findViewById(R.id.ss_formulario_observacion_fecha_limite);
        dropdownResponsables = rootView.findViewById(R.id.ss_formulario_observacion_dropdown_responsables);
        dropdownResponsables.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dropdownResponsables.showDropDown();
                return false;
            }
        });
        dropdownNivelRiesgo = rootView.findViewById(R.id.ss_formulario_observacion_dropdown_niveles_riesgo);
        dropdownNivelRiesgo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dropdownNivelRiesgo.showDropDown();
                return false;
            }
        });
        btnRegistrarObservacion = rootView.findViewById(R.id.ss_btn_registrar_observacion);
        imagen = rootView.findViewById(R.id.ss_img_observatyion);
        tv_error_registro = rootView.findViewById(R.id.ss_etxt_error_registro_observacion);
        txtFechaLimite.setOnClickListener(this);
        btnRegistrarObservacion.setOnClickListener(this);
    }

    private void inicializarVariables(){
        restApiAdapter = new RestApiAdapter();
        endpointsApi = restApiAdapter.establecerConexionRestApiSsoma();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.ss_formulario_observaciones, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.ssoma_observation_image:
                abrirAppCamara();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.ss_formulario_observacion_fecha_limite:
                showDateDialog();
                break;

            case R.id.ss_btn_registrar_observacion:
                registrarObservacion();
                break;
        }
    }

    private void showDateDialog()
    {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        DatePickerDialog dialog = new DatePickerDialog(getContext(), dateOnDateSetListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private DatePickerDialog.OnDateSetListener dateOnDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            selectedMonth++;
            String dia = selectedDay<10 ? "0"+selectedDay : ""+selectedDay;
            String mes = selectedMonth<10 ? "0"+selectedMonth : ""+selectedMonth;
            txtFechaLimite.setText(new StringBuilder().append(selectedYear).append("-").append(mes).append("-").append(dia));
        }
    };

    public void obtenerPersonalActivo() {

        endpointsApi.listaPersonalActivo().enqueue(new Callback<ArrayList<Personal>>() {
            @Override
            public void onResponse(Call<ArrayList<Personal>> call, Response<ArrayList<Personal>> response) {
                if (response.isSuccessful()) {
                    ArrayList<Personal> listaPersonal = response.body();
                    ArrayAdapter<Personal> adapterPersonal = new ArrayAdapter<>(getContext(),R.layout.dropdown_menu_popup_item,listaPersonal);
                    dropdownResponsables.setAdapter(adapterPersonal);

                    dropdownResponsables.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

    public void obtenerNivelesRiesgo() {

        endpointsApi.listaNivelesRiesgo().enqueue(new Callback<ArrayList<Tipo>>() {
            @Override
            public void onResponse(Call<ArrayList<Tipo>> call, Response<ArrayList<Tipo>> response) {
                if (response.isSuccessful()) {
                    ArrayList<Tipo> listaTipos = response.body();
                    ArrayAdapter<Tipo> adapterTipos = new ArrayAdapter<>(getContext(),R.layout.dropdown_menu_popup_item,listaTipos);
                    dropdownNivelRiesgo.setAdapter(adapterTipos);

                    dropdownNivelRiesgo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            nivelReiesgo = (Tipo)parent.getAdapter().getItem(position);
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

    private void registrarObservacion(){
        if(txtDescripcion.getText().toString().isEmpty()){
            tv_error_registro.setText("Debe ingresar una descripción.");
            return;
        }
        if(txtFechaLimite.getText().toString().isEmpty()){
            tv_error_registro.setText("Debe elegir una fecha límite de subsanación.");
            return;
        }
        if(responsable==null){
            tv_error_registro.setText("Debe elegir un responsable.");
            return;
        }
        if(nivelReiesgo==null){
            tv_error_registro.setText("Debe elegir un nivel de riesgo.");
            return;
        }
        if(pathPhoto==null){
            tv_error_registro.setText("Debe elegir una iamgen.");
            return;
        }

        Gson gson = new Gson();
        SharedPreferences preferences = getContext().getSharedPreferences(getResources().getString(R.string.nombre_archivo_preferences),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String jsonProgramacionDetalle = preferences.getString("programacionDetalle", "");
        ProgramacionDetalle programacion = gson.fromJson(jsonProgramacionDetalle, ProgramacionDetalle.class);

        Observacion observacion = new Observacion();
        observacion.setDescripcion(txtDescripcion.getText().toString());
        observacion.setFecha_limite_subsanacion(txtFechaLimite.getText().toString());
        observacion.setResponsable_id(responsable.getId());
        observacion.setNivel_riesgo_id(nivelReiesgo.getId());
        observacion.setProgramacion_id(programacion.getId());
        observacion.setPathImage(pathPhoto);
//        observacion.setUriPath(uriPhoto);
        String jsonObject = gson.toJson(observacion);

        String json = preferences.getString("listaObservaciones", "");
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        ArrayList<String> arrayObservaciones = gson.fromJson(json, type);

        arrayObservaciones.add(jsonObject);
        String newJson = gson.toJson(arrayObservaciones, type);
        editor.putString("listaObservaciones",newJson);
        editor.apply();

        Fragment fragment = new RegistroObservacionesFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.ssoma_content_frame, fragment)
                .commit();
    }

    private void abrirAppCamara() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                        .setTitle("Abrir Camara")
                        .setMessage("Es necesario aceptar permiso")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                solicitarPermisoCamaraGaleria();
                            }
                        })
                        .setNegativeButton("Cancelar", null);
                builder.show();
            } else {
                solicitarPermisoCamaraGaleria();
            }
        } else {
            takeImage();
        }
    }

    private void takeImage(){
        final List<Intent> intents = new ArrayList<>();
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intents.add(cameraIntent);
        File photoFile = null;
        if(cameraIntent.resolveActivity(getContext().getPackageManager()) != null){
            try{
                photoFile = createImageFile();
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
            if(photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        getResources().getString(R.string.file_provider),
                        photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            }
        }

//        final Intent galleryIntent = new Intent();
//        galleryIntent.setType("image/*");
//        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
//
//        // Chooser of filesystem options.
//        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Elegir origen de imagen");
//
//        // Add the camera options.
//        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents.toArray(new Parcelable[intents.size()]));

        startActivityForResult(cameraIntent,REQUEST_TAKE_PHOTO);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHss").format(new Date());
        String imageFileName = "JPEG_"+timeStamp+"_";
        File storeDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storeDir);
        pathPhoto = image.getAbsolutePath();
        return image;
    }

    private void solicitarPermisoCamaraGaleria() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_PERMISO_CAMARA_);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            Uri uri;
            if(data != null){//GALLERY
                uri = data.getData();
                File myFile = new File(uri.getPath());
                pathPhoto = myFile.getAbsolutePath();
            }
            else{//CAMERA
                uri = null;
            }
            showImage(uri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISO_CAMARA_: {
                if (grantResults.length > 0){
                    boolean abrirCamara = false;
                    for(int i=0; i<grantResults.length; i++){
                        if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                            abrirCamara = true;
                        }
                    }
                    if(abrirCamara) abrirAppCamara();
                }
                break;
            }
        }
    }

    private void showImage(Uri uri)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        int targetW = imagen.getMeasuredWidth();
        int targetH = imagen.getMeasuredHeight();
        Bitmap bitmap;

        BitmapFactory.Options bmpOptions = new BitmapFactory.Options();
        bmpOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathPhoto, bmpOptions);

        int photoW = bmpOptions.outWidth;
        int photoH = bmpOptions.outHeight;

        if(uri == null){//Camara
            int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
            bmpOptions.inJustDecodeBounds = false;
            bmpOptions.inSampleSize = scaleFactor;

            bitmap = BitmapFactory.decodeFile(pathPhoto, bmpOptions);
        }
        else{//Galeria
            try{
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
            }
            catch(Exception e){
                bitmap = null;
                e.printStackTrace();
            }
        }
        Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                matrix, true);
        imagen.setImageBitmap(rotated);
    }

}
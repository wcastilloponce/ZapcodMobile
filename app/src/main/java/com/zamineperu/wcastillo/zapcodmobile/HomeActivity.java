package com.zamineperu.wcastillo.zapcodmobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.zamineperu.wcastillo.zapcodmobile.Modules.Servicios.ServiceDashboardActivity;
import com.zamineperu.wcastillo.zapcodmobile.Modules.Ssoma.SsomaDashboardActivity;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, BasicActivityFunctions {

    private ImageButton btnSsoma, btnServicios;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        showToolbar(getResources().getString(R.string.home_titulo_toolbar),false);
        inicializarObjetos();
    }

    @Override
    public void onBackPressed () {
        moveTaskToBack (true);
    }

    @Override
    public void onClick(View v)
    {
        Intent intent;
        switch (v.getId())
        {
            case R.id.img_btn_ssoma:
                intent = new Intent(getApplicationContext(),SsomaDashboardActivity.class);
                startActivity(intent);
                finish();break;
            case R.id.img_btn_service:
                intent = new Intent(getApplicationContext(),ServiceDashboardActivity.class);
                startActivity(intent);
                finish();break;

                default:
                    intent = new Intent();
        }
    }

    private void showToolbar(String titulo, boolean upButton){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(titulo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.home_logout:
                logout();break;
        }
        return true;
    }

    @Override
    public void inicializarObjetos()
    {
        btnSsoma = findViewById(R.id.img_btn_ssoma);
        btnSsoma.setOnClickListener(this);

        btnServicios = findViewById(R.id.img_btn_service);
        btnServicios.setOnClickListener(this);
    }

    @Override
    public void inicializarVariables()
    {

    }

    private void logout(){
        SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.nombre_archivo_preferences),MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(getResources().getString(R.string.nombre_variable_user));
        editor.commit();

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

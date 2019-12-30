package com.zamineperu.wcastillo.zapcodmobile.Modules.Ssoma;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zamineperu.wcastillo.zapcodmobile.HomeActivity;
import com.zamineperu.wcastillo.zapcodmobile.LoginActivity;
import com.zamineperu.wcastillo.zapcodmobile.R;
import com.zamineperu.wcastillo.zapcodmobile.ResponseClass.UserLoginResponse;

public class SsomaDashboardActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navView;
    SharedPreferences preferences;
    String userJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ssoma_dashboard);
        showToolbar(getResources().getString(R.string.ss_dashboard_titulo_toolbar), true);

        drawerLayout = findViewById(R.id.ssoma_drawer_layout);
        navView = findViewById(R.id.ssoma_navview);
        preferences = getSharedPreferences(getResources().getString(R.string.nombre_archivo_preferences), Context.MODE_PRIVATE);

        //obteniendo usuario logueado
        Gson gson = new Gson();
        if (savedInstanceState != null) {
            userJson = savedInstanceState.getString(getResources().getString(R.string.nombre_variable_user));
        } else {
            userJson = preferences.getString(getResources().getString(R.string.nombre_variable_user), "");
        }
        final UserLoginResponse user = gson.fromJson(userJson, UserLoginResponse.class);

        //seteando el nombre de usuario en el menú
        View headerView = navView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.navview_tv_user);
        String nombre_apellidos = user.getNombres() + " " + user.getApellido_paterno() + " " + user.getApellido_materno();
        navUsername.setText(nombre_apellidos);

        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        boolean fragmentTransaction = false;
                        Fragment fragment = null;

                        switch (menuItem.getItemId()) {
                            case R.id.ss_menu_registro_inspecciones:
                                fragment = new RegistroInspeccionFragment();
                                fragmentTransaction = true;
                                break;
                            case R.id.ss_menu_inspecciones_finalizadas:
                                fragment = new ListaInspeccionesFinalizadasFragment();
                                fragmentTransaction = true;
                                break;
                            case R.id.ss_menu_inspecciones_pendientes:
                                fragment = new ListaInspeccionesPendientesFragment();
                                fragmentTransaction = true;
                                break;
                            case R.id.ssoma_salir:
                                logout();
                                return true;
                        }

                        Bundle bundle = new Bundle();
                        bundle.putString("user_id", user.getId());

                        if (fragmentTransaction) {
                            fragment.setArguments(bundle);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.ssoma_content_frame, fragment)
                                    .commit();

                            menuItem.setChecked(true);
                        }

                        drawerLayout.closeDrawers();

                        return true;
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0){
            getSupportFragmentManager().popBackStack();
        }
        else{
            moveTaskToBack(true);
        }

    }

    private void showToolbar(String titulo, boolean upButton) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(titulo);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_menu_white_18dp);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(getResources().getString(R.string.nombre_variable_user), userJson);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ss_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }
                if (!drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
                return true;
            case R.id.ssoma_home:
                home();
                return true;
            case R.id.ssoma_logout:
                logout();
                return true;
            default: return false;
        }
    }

    private void home(){
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void logout() {
        SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.nombre_archivo_preferences), MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(getResources().getString(R.string.nombre_variable_user));
        editor.remove("listaObservaciones");
        editor.commit();

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
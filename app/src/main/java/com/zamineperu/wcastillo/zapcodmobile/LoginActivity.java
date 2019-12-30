package com.zamineperu.wcastillo.zapcodmobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zamineperu.wcastillo.zapcodmobile.ApiConnection.EndpointsApi;
import com.zamineperu.wcastillo.zapcodmobile.ApiConnection.RestApiAdapter;
import com.zamineperu.wcastillo.zapcodmobile.ResponseClass.UserLoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, BasicActivityFunctions {

    private EditText etxtUsername, etxtPassword;
    private TextView etxtErrorMessage;
    private Button btnLogin;
    private UserLoginResponse user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inicializarVariables();
        inicializarObjetos();

    }

    public void inicializarObjetos()
    {
        etxtUsername = findViewById(R.id.etxt_user);
        etxtPassword = findViewById(R.id.etxt_password);
        etxtErrorMessage = findViewById(R.id.etxt_mensaje_error);
        btnLogin = findViewById(R.id.btn_ingresar);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void inicializarVariables() {
        user = null;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_ingresar:
                cleanMemory();
                acceder();break;
        }

    }

    private void acceder()
    {
        String username = etxtUsername.getText().toString().trim();
        String password = etxtPassword.getText().toString().trim();

        if(username.equals("") || password.equals("")){
            etxtErrorMessage.setText(getResources().getString(R.string.login_error_vacios));
        }
        else {
            validarAcceso(username, password);
        }
    }

    private void validarAcceso(String username, String password)
    {
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApi();

        endpointsApi.basicLogin(username,password).enqueue(new Callback<UserLoginResponse>() {
            @Override
            public void onResponse(Call<UserLoginResponse> call, Response<UserLoginResponse> response) {
                if(response.isSuccessful()){
                    user = response.body();
                    grabarUsuario(user);
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    if(response.code()==401){
                        etxtErrorMessage.setText(getResources().getString(R.string.login_error_incorrectos));
                    }
                    System.out.println(getResources().getString(R.string.error_onresponse)+response.code()+" - "+response.message());
                }
            }

            @Override
            public void onFailure(Call<UserLoginResponse> call, Throwable t) {
                System.out.println(getResources().getString(R.string.error_onfailure)+t.getMessage());
            }
        });

    }

    private void grabarUsuario(UserLoginResponse user){
        SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.nombre_archivo_preferences),MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(getResources().getString(R.string.nombre_variable_user),json);
        editor.apply();
    }

    private void cleanMemory(){
        SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.nombre_archivo_preferences), MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(getResources().getString(R.string.nombre_variable_user));
        editor.remove("listaObservaciones");
        editor.commit();
    }
}

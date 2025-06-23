package com.example.tienda_ropa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tienda_ropa.Interface.PyAnyApi;
import com.example.tienda_ropa.model.AuthReq;
import com.example.tienda_ropa.model.AuthResp;
import com.example.tienda_ropa.ui.home.HomeFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IniciarActivity extends AppCompatActivity {

    // Controles MDC
    TextInputLayout usernameTextInput;
    TextInputEditText usernameEditText;
    TextInputLayout passwordTextInput;
    TextInputEditText passwordEditText;
    TextView tvOlvidoContrasena;
    TextView tvLinkTerminos;
    MaterialButton mBtnIniciarSesion;
    ImageButton btnBack;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_iniciar);

        sharedPref = this.getSharedPreferences("user_session", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        btnBack = findViewById(R.id.btnBack);
        usernameTextInput = findViewById(R.id.username_text_input);
        usernameEditText = findViewById(R.id.username_edit_text);
        passwordTextInput = findViewById(R.id.password_text_input);
        passwordEditText = findViewById(R.id.password_edit_text);
        tvOlvidoContrasena = findViewById(R.id.tvOlvidoContrasena);
        tvLinkTerminos = findViewById(R.id.tvLinkTerminos);
        mBtnIniciarSesion = findViewById(R.id.btnLogin);

        tvOlvidoContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IniciarActivity.this, OlvidarActivity.class);
                startActivity(intent);
            }
        });

        tvLinkTerminos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IniciarActivity.this, TerminosActivity.class);
                startActivity(intent);
            }
        });

        mBtnIniciarSesion.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                Snackbar.make(mBtnIniciarSesion, "Ingrese usuario y contraseña", Snackbar.LENGTH_SHORT).show();
                return;
            }

            obtenerToken(username, password);
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void obtenerToken(String p_username, String p_password){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PyAnyApi pyAnyApi = retrofit.create(PyAnyApi.class);
        AuthReq authReq = new AuthReq();
        authReq.setUsername(p_username);
        authReq.setPassword(p_password);
        Call<AuthResp> call = pyAnyApi.obtenerToken(authReq);
        call.enqueue(new Callback<AuthResp>() {
            @Override
            public void onResponse(Call<AuthResp> call, Response<AuthResp> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 401) {
                        Snackbar.make(mBtnIniciarSesion, "Usuario o contraseña incorrectos", Snackbar.LENGTH_LONG).show();
                    } else {
                        Snackbar.make(mBtnIniciarSesion, "Error: Código " + response.code(), Snackbar.LENGTH_LONG).show();
                    }
                    return;
                }

                Log.d("LOGIN_RESPONSE_DEBUG", new Gson().toJson(response.body()));

                AuthResp objAuthResp = response.body();
                String token = objAuthResp.getAccess_token().toString();

                Log.d("TOKEN_DEBUG", token);

                try {
                    String[] parts = token.split("\\."); // header.payload.signature
                    Log.d("TOKEN_PARTS", "Partes del token: " + parts.length);
                    String payloadJson = new String(android.util.Base64.decode(parts[1], android.util.Base64.URL_SAFE), "UTF-8");
                    Log.d("TOKEN_PAYLOAD", payloadJson);

                    // Parsear JSON
                    org.json.JSONObject payload = new org.json.JSONObject(payloadJson);
                    int idUsuario = payload.getInt("identity"); // Aquí obtienes el ID del usuario

                    // Guardar en SharedPreferences
                    editor.putString("token", token);
                    editor.putInt("id_usuario", idUsuario);
                    editor.putString("username", usernameEditText.getText().toString());
                    editor.apply();

                    Log.d("USER_ID", String.valueOf(idUsuario));

                    Intent intent = new Intent(IniciarActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } catch (Exception e){
                    e.printStackTrace();
                    Snackbar.make(mBtnIniciarSesion, "Error al decodificar el token", Snackbar.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<AuthResp> call, Throwable throwable) {
                Snackbar.make(mBtnIniciarSesion, "Error de red: " + throwable.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

}
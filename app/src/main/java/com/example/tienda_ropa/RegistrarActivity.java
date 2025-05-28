package com.example.tienda_ropa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tienda_ropa.Interface.PyAnyApi;
import com.example.tienda_ropa.model.AuthReq;
import com.example.tienda_ropa.model.AuthResp;
import com.example.tienda_ropa.model.GeneralResp;
import com.example.tienda_ropa.model.RegistrarUsuarioReq;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistrarActivity extends AppCompatActivity {

    ImageButton btnBack;
    MaterialButton mBtnRegistrar;

    TextInputEditText mEtUsuario;
    TextInputEditText mEtPassword;
    TextInputEditText mEtEmail;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar);

        sharedPref = this.getSharedPreferences("user_session", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        btnBack = findViewById(R.id.btnBack);
        mBtnRegistrar = findViewById(R.id.btnRegistrar);
        mEtUsuario = findViewById(R.id.etUsuario);
        mEtEmail = findViewById(R.id.etEmail);
        mEtPassword = findViewById(R.id.etPassword);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBtnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistrarUsuarioReq registrarUsuarioReq = new RegistrarUsuarioReq();
                registrarUsuarioReq.setUsername(mEtUsuario.getText().toString());
                registrarUsuarioReq.setEmail(mEtEmail.getText().toString());
                registrarUsuarioReq.setPassword(mEtPassword.getText().toString());
                registrarUsuario(registrarUsuarioReq);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void registrarUsuario(RegistrarUsuarioReq registrarUsuarioReq){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PyAnyApi pyAnyApi = retrofit.create(PyAnyApi.class);

        Call<GeneralResp> call = pyAnyApi.registrarUsuario("JWT " +
                sharedPref.getString("token", ""), registrarUsuarioReq);

        call.enqueue(new Callback<GeneralResp>() {
            @Override
            public void onResponse(Call<GeneralResp> call, Response<GeneralResp> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(RegistrarActivity.this, "Error: Código " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(RegistrarActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                // Redirigir al login
                Intent intent = new Intent(RegistrarActivity.this, IniciarActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<GeneralResp> call, Throwable throwable) {

            }
        });
    }
}
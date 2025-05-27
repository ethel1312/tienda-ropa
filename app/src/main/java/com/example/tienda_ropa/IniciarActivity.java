package com.example.tienda_ropa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tienda_ropa.Interface.PyAnyApi;
import com.example.tienda_ropa.model.AuthReq;
import com.example.tienda_ropa.model.AuthResp;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IniciarActivity extends AppCompatActivity {

    TextView mJsonText;
    EditText mEtxtUsername;
    EditText mEtxtPassword;
    Button mBtnIniciarSesion;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_iniciar);

        sharedPref = this.getSharedPreferences("user_session", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        mJsonText = findViewById(R.id.jsonText);
        mEtxtUsername = findViewById(R.id.etUsuario);
        mEtxtPassword = findViewById(R.id.etPassword);
        mBtnIniciarSesion = findViewById(R.id.btnLogin);

        mBtnIniciarSesion.setOnClickListener(v -> {
            obtenerToken(mEtxtUsername.getText().toString(),mEtxtPassword.getText().toString());
            Intent intent = new Intent(this, RegistrarActivity.class);
            startActivity(intent);
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
                if(!response.isSuccessful()){
                    mJsonText.setText("Codigo: " + response.code());
                }
                AuthResp objAuthResp = response.body();
                String token = objAuthResp.getAccess_token().toString();
                Log.d("TOKEN_DEBUG", token);
                editor.putString("token", token);
                editor.apply();
            }

            @Override
            public void onFailure(Call<AuthResp> call, Throwable throwable) {
                mJsonText.setText(throwable.getMessage());
            }
        });
    }

}
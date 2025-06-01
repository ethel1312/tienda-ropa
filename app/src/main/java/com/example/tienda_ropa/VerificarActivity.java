package com.example.tienda_ropa;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tienda_ropa.Interface.PyAnyApi;
import com.example.tienda_ropa.model.GeneralResp;
import com.example.tienda_ropa.model.VerificarCodReq;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VerificarActivity extends AppCompatActivity {

    ImageButton btnBack;
    EditText mEtCod1;
    EditText mEtCod2;
    EditText mEtCod3;
    EditText mEtCod4;
    MaterialButton mBtnVerificar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verificar);

        btnBack = findViewById(R.id.btnBack);
        mEtCod1 = findViewById(R.id.codigo1);
        mEtCod2 = findViewById(R.id.codigo2);
        mEtCod3 = findViewById(R.id.codigo3);
        mEtCod4 = findViewById(R.id.codigo4);
        mBtnVerificar = findViewById(R.id.btnVerificar);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mEtCod1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    mEtCod2.requestFocus();
                }
            }
        });
        mEtCod2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    mEtCod3.requestFocus();
                }
            }
        });
        mEtCod3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    mEtCod4.requestFocus();
                }
            }
        });

        mBtnVerificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputCode = mEtCod1.getText().toString().trim()
                        + mEtCod2.getText().toString().trim()
                        + mEtCod3.getText().toString().trim()
                        + mEtCod4.getText().toString().trim();

                if(inputCode.length() != 4){
                    Toast.makeText(VerificarActivity.this,"Ingrese los 4 dígitos del código",Toast.LENGTH_SHORT).show();
                    return;
                }

                VerificarCodReq verificarCodReq = new VerificarCodReq();
                verificarCodReq.setEmail(getIntent().getStringExtra("email"));
                verificarCodReq.setCode(Integer.parseInt(inputCode));
                checkCode(verificarCodReq);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void checkCode(VerificarCodReq verificarCodReq) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PyAnyApi pyAnyApi = retrofit.create(PyAnyApi.class);

        Call<GeneralResp> call = pyAnyApi.verificarCodigo(verificarCodReq);

        call.enqueue(new Callback<GeneralResp>() {
            @Override
            public void onResponse(Call<GeneralResp> call, Response<GeneralResp> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(VerificarActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                GeneralResp resp = response.body();
                if (resp != null) {
                    Toast.makeText(VerificarActivity.this, resp.getMessage(), Toast.LENGTH_SHORT).show();

                    String email = getIntent().getStringExtra("email");

                    Intent intent = new Intent(VerificarActivity.this, ModificarContraActivity.class);
                    intent.putExtra("email", email);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                } else {
                    Toast.makeText(VerificarActivity.this, "Código incorrecto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GeneralResp> call, Throwable t) {
                Toast.makeText(VerificarActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
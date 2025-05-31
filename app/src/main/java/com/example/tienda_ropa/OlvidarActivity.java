package com.example.tienda_ropa;

import android.content.Intent;
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
import com.example.tienda_ropa.model.EmailReq;
import com.example.tienda_ropa.model.GeneralResp;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OlvidarActivity extends AppCompatActivity {

    ImageButton btnBack;
    MaterialButton btnConfirmar;
    TextInputEditText emailTXT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_olvidar);

        btnBack = findViewById(R.id.btnBack);
        btnConfirmar = findViewById(R.id.btnConfirmar);
        emailTXT = findViewById(R.id.email_edit_text);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnConfirmar.setOnClickListener(v -> {
            String email = emailTXT.getText().toString().trim();
            if(email.isEmpty()) {
                Toast.makeText(OlvidarActivity.this, "Por favor ingresa un correo", Toast.LENGTH_SHORT).show();
                return;
            }
            EmailReq emailReq = new EmailReq();
            emailReq.setEmail(email);
            sendVerifyEmail(emailReq);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void sendVerifyEmail(EmailReq emailreq) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/") // Cambia al base URL de tu API
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PyAnyApi pyAnyApi = retrofit.create(PyAnyApi.class);

        Call<GeneralResp> call = pyAnyApi.enviarCodigo(emailreq);

        call.enqueue(new Callback<GeneralResp>() {
            @Override
            public void onResponse(Call<GeneralResp> call, Response<GeneralResp> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(OlvidarActivity.this, "Error: Código " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                GeneralResp resp = response.body();
                if(resp != null){
                    Toast.makeText(OlvidarActivity.this, resp.getMessage(), Toast.LENGTH_LONG).show();

                    // Obtener el email del campo de texto
                    String email = emailTXT.getText().toString();

                    // Redirigir al login
                    Intent intent = new Intent(OlvidarActivity.this, VerificarActivity.class);
                    intent.putExtra("email", email); // <- Aquí pasas el email
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                } else {
                    Toast.makeText(OlvidarActivity.this, "Respuesta inesperada del servidor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GeneralResp> call, Throwable t) {
                Toast.makeText(OlvidarActivity.this, "Error en la conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

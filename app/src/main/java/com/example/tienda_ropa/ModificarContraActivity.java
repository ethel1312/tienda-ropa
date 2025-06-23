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
import com.example.tienda_ropa.model.GeneralResp;
import com.example.tienda_ropa.model.ModificarContraReq;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ModificarContraActivity extends AppCompatActivity {
    TextInputLayout mTiNewPassword;
    TextInputLayout mTiConfirmPassword;
    TextInputEditText mEtNewPassword;
    TextInputEditText mEtConfirmPassword;
    ImageButton btnBack;
    MaterialButton mBtnCambiarContra;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_modificar_contra);

        sharedPref = this.getSharedPreferences("user_session", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        btnBack = findViewById(R.id.btnBack);
        mTiNewPassword = findViewById(R.id.password_text_input);
        mEtNewPassword = findViewById(R.id.password_edit_text);
        mTiConfirmPassword = findViewById(R.id.password_confirm_text_input);
        mEtConfirmPassword = findViewById(R.id.password_confirm_edit_text);
        mBtnCambiarContra = findViewById(R.id.btnCambiarContra);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBtnCambiarContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = mEtNewPassword.getText().toString().trim();
                String confirmPassword = mEtConfirmPassword.getText().toString().trim();

                // Limpiar errores previos
                mTiNewPassword.setError(null);
                mTiConfirmPassword.setError(null);

                // Validaciones
                if (password.isEmpty()) {
                    mTiNewPassword.setError("La contraseña no puede estar vacía");
                    return;
                }

                if (confirmPassword.isEmpty()) {
                    mTiConfirmPassword.setError("Debe confirmar la contraseña");
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    mTiConfirmPassword.setError("Las contraseñas no coinciden");
                    return;
                }

                ModificarContraReq modificarContraReq = new ModificarContraReq();
                modificarContraReq.setEmail(getIntent().getStringExtra("email"));
                modificarContraReq.setNew_password(password);
                actualizar_password(modificarContraReq);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void actualizar_password(ModificarContraReq modificarContraReq){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PyAnyApi pyAnyApi = retrofit.create(PyAnyApi.class);

        Call<GeneralResp> call = pyAnyApi.actualizar_password(modificarContraReq);

        call.enqueue(new Callback<GeneralResp>() {
            @Override
            public void onResponse(Call<GeneralResp> call, Response<GeneralResp> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(ModificarContraActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                GeneralResp resp = response.body();
                if (resp != null) {
                    Toast.makeText(ModificarContraActivity.this, resp.getMessage(), Toast.LENGTH_SHORT).show();

                    // Redirigir al login
                    Intent intent = new Intent(ModificarContraActivity.this, IniciarActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                } else {
                    Toast.makeText(ModificarContraActivity.this, "Contraseña inválida", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GeneralResp> call, Throwable throwable) {
                Toast.makeText(ModificarContraActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
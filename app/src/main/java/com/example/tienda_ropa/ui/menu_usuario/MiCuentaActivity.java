package com.example.tienda_ropa.ui.menu_usuario;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tienda_ropa.Interface.PyAnyApi;
import com.example.tienda_ropa.R;
import com.example.tienda_ropa.model.ObtenerUsuario;
import com.example.tienda_ropa.model.ParamsUsuario;
import com.example.tienda_ropa.model.UsuarioXid;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MiCuentaActivity extends AppCompatActivity {

    TextView tvNombreCompleto, tvEmail, tvDocumento, tvTelefono;

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta);

        tvNombreCompleto = findViewById(R.id.tvNombreCompleto);
        tvEmail = findViewById(R.id.tvEmail);
        tvDocumento = findViewById(R.id.tvDocumento);
        tvTelefono = findViewById(R.id.tvTelefono);

        // Corrección: obtener SharedPreferences correctamente en Activity
        sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        int idUsuario = sharedPref.getInt("id_usuario", -1);

        if (idUsuario != -1) {
            obtenerDatosUsuario(idUsuario);
        } else {
            Toast.makeText(this, "ID de usuario no encontrado", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void obtenerDatosUsuario(int idUsuario) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PyAnyApi apiService = retrofit.create(PyAnyApi.class);

        ParamsUsuario params = new ParamsUsuario(idUsuario);
        Call<ObtenerUsuario> call = apiService.obtenerUsuario(params);

        call.enqueue(new Callback<ObtenerUsuario>() {
            @Override
            public void onResponse(Call<ObtenerUsuario> call, Response<ObtenerUsuario> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().getData().isEmpty()) {
                    UsuarioXid usuario = response.body().getData().get(0);

                    tvNombreCompleto.setText(usuario.getNombreCompleto());
                    tvEmail.setText(usuario.getEmail());
                    tvDocumento.setText(usuario.getTipoDoc() + " - " + usuario.getNumDoc());
                    tvTelefono.setText(usuario.getTelefono());
                } else {
                    Toast.makeText(MiCuentaActivity.this, "No se encontraron datos del usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ObtenerUsuario> call, Throwable t) {
                Toast.makeText(MiCuentaActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

package com.example.tienda_ropa.ui.menu_usuario;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tienda_ropa.Interface.PyAnyApi;
import com.example.tienda_ropa.R;
import com.example.tienda_ropa.model.DireccionesXid;
import com.example.tienda_ropa.model.ObtenerDirecciones;
import com.example.tienda_ropa.model.ParamsUsuario;
import com.example.tienda_ropa.ui.menu_usuario.adapter.DireccionAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MiDireccionActivity extends AppCompatActivity {

    private RecyclerView recyclerDirecciones;
    private DireccionAdapter direccionAdapter;
    private List<DireccionesXid> listaDirecciones;

    private SharedPreferences sharedPref;
    private int idUsuario;

    String token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direcciones);

        recyclerDirecciones = findViewById(R.id.recyclerDireccionesa);
        Button btnAgregar = findViewById(R.id.btnAgregarDireccion);
        recyclerDirecciones.setLayoutManager(new LinearLayoutManager(this));

        sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        idUsuario = sharedPref.getInt("id_usuario", -1);
        token = sharedPref.getString("token", "");

        btnAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(MiDireccionActivity.this, AgregarDireccionActivity.class);
            startActivity(intent);
        });

        if (idUsuario == -1) {
            Toast.makeText(this, "ID de usuario no encontrado", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (idUsuario != -1) {
            obtenerDireccionesUsuario(idUsuario);
        }
    }

    private void obtenerDireccionesUsuario(int idUsuario) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PyAnyApi pyAnyApi = retrofit.create(PyAnyApi.class);
        ParamsUsuario params = new ParamsUsuario(idUsuario);

        Call<ObtenerDirecciones> call = pyAnyApi.obtenerDireccion("JWT " + token,params);

        call.enqueue(new Callback<ObtenerDirecciones>() {
            @Override
            public void onResponse(Call<ObtenerDirecciones> call, Response<ObtenerDirecciones> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().getData().isEmpty()) {
                    listaDirecciones = response.body().getData();

                    direccionAdapter = new DireccionAdapter(MiDireccionActivity.this, listaDirecciones);
                    recyclerDirecciones.setAdapter(direccionAdapter);
                } else {
                    Toast.makeText(MiDireccionActivity.this, "No se encontraron direcciones", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ObtenerDirecciones> call, Throwable t) {
                Toast.makeText(MiDireccionActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

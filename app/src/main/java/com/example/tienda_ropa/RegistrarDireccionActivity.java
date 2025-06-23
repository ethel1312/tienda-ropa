package com.example.tienda_ropa;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tienda_ropa.Interface.PyAnyApi;
import com.example.tienda_ropa.model.ApiResponse;
import com.example.tienda_ropa.model.DireccionRequest;
import com.example.tienda_ropa.model.Ubigeo;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistrarDireccionActivity extends AppCompatActivity {

    private EditText etCalle, etReferencia;
    private AutoCompleteTextView cbxDepartamento, cbxProvincia, cbxDistrito;
    private SwitchMaterial switchGuardarPrincipal;
    private MaterialButton btnGuardar;

    private SharedPreferences sharedPref;
    private String token;

    private Map<String, Ubigeo> mapDepartamentos = new HashMap<>();
    private Map<String, Ubigeo.Provincia> mapProvincias = new HashMap<>();
    private Map<String, Ubigeo.Distrito> mapDistritos = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_direccion);

        getSupportActionBar().hide();
        sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        token = "Bearer " + sharedPref.getString("token", "");

        etCalle = findViewById(R.id.etCalle);
        etReferencia = findViewById(R.id.etReferencia);
        cbxDepartamento = findViewById(R.id.cbxDepartamento);
        cbxProvincia = findViewById(R.id.cbxProvincia);
        cbxDistrito = findViewById(R.id.cbxDistrito);
        switchGuardarPrincipal = findViewById(R.id.switchGuardarPrincipal);
        btnGuardar = findViewById(R.id.btnGuardarDireccion);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        cargarUbigeoDesdeApi();

        btnGuardar.setOnClickListener(v -> registrarDireccion());
    }

    private void cargarUbigeoDesdeApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PyAnyApi api = retrofit.create(PyAnyApi.class);
        Map<String, String> body = new HashMap<>();
        body.put("modo", "obtener_ubigeo");

        api.obtenerUbigeo(body, token).enqueue(new Callback<ApiResponse<List<Ubigeo>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Ubigeo>>> call, Response<ApiResponse<List<Ubigeo>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().code == 1) {
                    configurarSpinners(response.body().data);
                } else {
                    Toast.makeText(getApplicationContext(), "Error cargando ubigeo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Ubigeo>>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Fallo de red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configurarSpinners(List<Ubigeo> lista) {
        List<String> nombresDep = new ArrayList<>();
        for (Ubigeo dep : lista) {
            nombresDep.add(dep.nombre_departamento);
            mapDepartamentos.put(dep.nombre_departamento, dep);
        }
        cbxDepartamento.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, nombresDep));

        cbxDepartamento.setOnItemClickListener((parent, view, position, id) -> {
            Ubigeo dep = mapDepartamentos.get(parent.getItemAtPosition(position).toString());
            List<String> provs = new ArrayList<>();
            mapProvincias.clear();
            for (Ubigeo.Provincia p : dep.provincias) {
                provs.add(p.nombre_provincia);
                mapProvincias.put(p.nombre_provincia, p);
            }
            cbxProvincia.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, provs));
        });

        cbxProvincia.setOnItemClickListener((parent, view, position, id) -> {
            Ubigeo.Provincia prov = mapProvincias.get(parent.getItemAtPosition(position).toString());
            List<String> dists = new ArrayList<>();
            mapDistritos.clear();
            for (Ubigeo.Distrito d : prov.distritos) {
                dists.add(d.nombre_distrito);
                mapDistritos.put(d.nombre_distrito, d);
            }
            cbxDistrito.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, dists));
        });
    }

    private void registrarDireccion() {
        String calle = etCalle.getText().toString().trim();
        String referencia = etReferencia.getText().toString().trim();
        String distrito = cbxDistrito.getText().toString();
        boolean esPrincipal = switchGuardarPrincipal.isChecked();

        if (calle.isEmpty() || referencia.isEmpty() || distrito.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int idDistrito = mapDistritos.get(distrito).id_distrito;

        DireccionRequest body = new DireccionRequest();
        body.calle = calle;
        body.info_adicional = referencia;
        body.id_distrito = idDistrito;
        body.es_principal = esPrincipal ? 1 : 0;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PyAnyApi api = retrofit.create(PyAnyApi.class);
        api.agregarDireccion(token, body).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().code == 1) {
                    Toast.makeText(getApplicationContext(), "Dirección registrada correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Error al registrar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Fallo al conectar con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

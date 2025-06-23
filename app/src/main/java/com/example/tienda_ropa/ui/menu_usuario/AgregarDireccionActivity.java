package com.example.tienda_ropa.ui.menu_usuario;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tienda_ropa.Interface.PyAnyApi;
import com.example.tienda_ropa.R;
import com.example.tienda_ropa.model.Departamento;
import com.example.tienda_ropa.model.DireccionRequest;
import com.example.tienda_ropa.model.Distrito;
import com.example.tienda_ropa.model.GeneralResp;
import com.example.tienda_ropa.model.ParamsDepartamento;
import com.example.tienda_ropa.model.ParamsProvincia;
import com.example.tienda_ropa.model.Provincia;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AgregarDireccionActivity extends AppCompatActivity {

    private TextInputEditText etCalle, etReferencia;
    private AutoCompleteTextView spinnerDepartamento, spinnerProvincia, spinnerDistrito;
    private MaterialButton btnGuardarDireccion;

    private PyAnyApi apiService;
    private List<Departamento> listaDepartamentos;
    private List<Provincia> listaProvincias;
    private List<Distrito> listaDistritos;

    private ArrayAdapter<String> adapterDepartamentos, adapterProvincias, adapterDistritos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_direccion);

        etCalle = findViewById(R.id.etCalle);
        etReferencia = findViewById(R.id.etReferencia);
        spinnerDepartamento = findViewById(R.id.spinnerDepartamento);
        spinnerProvincia = findViewById(R.id.spinnerProvincia);
        spinnerDistrito = findViewById(R.id.spinnerDistrito);
        btnGuardarDireccion = findViewById(R.id.btnGuardarDireccion);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(PyAnyApi.class);

        cargarDepartamentos();

        spinnerDepartamento.setOnItemClickListener((parent, view, pos, id) -> {
            if (pos > 0) {
                int idDep = listaDepartamentos.get(pos - 1).getId_departamento();
                cargarProvincias(idDep);
            } else {
                limpiarAutoComplete(spinnerProvincia);
                limpiarAutoComplete(spinnerDistrito);
            }
        });

        spinnerProvincia.setOnItemClickListener((parent, view, pos, id) -> {
            if (pos > 0) {
                int idProv = listaProvincias.get(pos - 1).getId_provincia();
                cargarDistritos(idProv);
            } else {
                limpiarAutoComplete(spinnerDistrito);
            }
        });

        btnGuardarDireccion.setOnClickListener(v -> guardarDireccion());
    }

    private void cargarDepartamentos() {
        apiService.obtenerDepartamentos().enqueue(new Callback<List<Departamento>>() {
            @Override
            public void onResponse(Call<List<Departamento>> call, Response<List<Departamento>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaDepartamentos = response.body();
                    List<String> nombres = new ArrayList<>();
                    nombres.add("Seleccione");
                    for (Departamento d : listaDepartamentos) {
                        nombres.add(d.getNombre_departamento());
                    }

                    adapterDepartamentos = new ArrayAdapter<>(
                            AgregarDireccionActivity.this,
                            android.R.layout.simple_dropdown_item_1line,
                            nombres);
                    spinnerDepartamento.setAdapter(adapterDepartamentos);
                    spinnerDepartamento.setText("Seleccione", false);
                }
            }

            @Override
            public void onFailure(Call<List<Departamento>> call, Throwable t) {
                Toast.makeText(AgregarDireccionActivity.this, "Error al cargar departamentos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarProvincias(int idDepartamento) {
        apiService.obtenerProvincias(new ParamsDepartamento(idDepartamento))
                .enqueue(new Callback<List<Provincia>>() {
                    @Override
                    public void onResponse(Call<List<Provincia>> call, Response<List<Provincia>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            listaProvincias = response.body();
                            List<String> nombres = new ArrayList<>();
                            nombres.add("Seleccione");
                            for (Provincia p : listaProvincias) {
                                nombres.add(p.getNombre_provincia());
                            }

                            adapterProvincias = new ArrayAdapter<>(
                                    AgregarDireccionActivity.this,
                                    android.R.layout.simple_dropdown_item_1line,
                                    nombres);
                            spinnerProvincia.setAdapter(adapterProvincias);
                            spinnerProvincia.setText("Seleccione", false);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Provincia>> call, Throwable t) {
                        Toast.makeText(AgregarDireccionActivity.this, "Error al cargar provincias", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void cargarDistritos(int idProvincia) {
        apiService.obtenerDistritos(new ParamsProvincia(idProvincia))
                .enqueue(new Callback<List<Distrito>>() {
                    @Override
                    public void onResponse(Call<List<Distrito>> call, Response<List<Distrito>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            listaDistritos = response.body();
                            List<String> nombres = new ArrayList<>();
                            nombres.add("Seleccione");
                            for (Distrito d : listaDistritos) {
                                nombres.add(d.getNombre_distrito());
                            }

                            adapterDistritos = new ArrayAdapter<>(
                                    AgregarDireccionActivity.this,
                                    android.R.layout.simple_dropdown_item_1line,
                                    nombres);
                            spinnerDistrito.setAdapter(adapterDistritos);
                            spinnerDistrito.setText("Seleccione", false);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Distrito>> call, Throwable t) {
                        Toast.makeText(AgregarDireccionActivity.this, "Error al cargar distritos", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void limpiarAutoComplete(AutoCompleteTextView actv) {
        List<String> vacio = new ArrayList<>();
        vacio.add("Seleccione");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, vacio);
        actv.setAdapter(adapter);
        actv.setText("Seleccione", false);
    }

    private void guardarDireccion() {
        String calle = etCalle.getText().toString().trim();
        String referencia = etReferencia.getText().toString().trim();
        String departamento = spinnerDepartamento.getText().toString();
        String provincia = spinnerProvincia.getText().toString();
        String distrito = spinnerDistrito.getText().toString();

        if (calle.isEmpty() || referencia.isEmpty() ||
                departamento.equals("Seleccione") ||
                provincia.equals("Seleccione") ||
                distrito.equals("Seleccione")) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            Log.e("GUARDAR_DIR", "Datos incompletos");
            return;
        }

        int indexDistrito = adapterDistritos.getPosition(distrito) - 1;

        int idDistrito = listaDistritos.get(indexDistrito).getId_distrito();

        SharedPreferences prefs = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        int idUsuario = prefs.getInt("id_usuario", -1);
        DireccionRequest req = new DireccionRequest(idUsuario, calle, idDistrito, referencia, 1);

        apiService.agregarDireccion(req).enqueue(new Callback<GeneralResp>() {
            @Override
            public void onResponse(Call<GeneralResp> call, Response<GeneralResp> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(AgregarDireccionActivity.this, "Dirección guardada", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AgregarDireccionActivity.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                    Log.e("GUARDAR_DIR", "Error: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<GeneralResp> call, Throwable t) {
                Toast.makeText(AgregarDireccionActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                Log.e("GUARDAR_DIR", t.getMessage(), t);
            }
        });
    }
}

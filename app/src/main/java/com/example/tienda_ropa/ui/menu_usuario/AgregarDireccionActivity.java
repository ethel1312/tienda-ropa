package com.example.tienda_ropa.ui.menu_usuario;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
    private Spinner spinnerDepartamento, spinnerProvincia, spinnerDistrito;
    private Button btnGuardarDireccion;

    private PyAnyApi apiService;

    private List<Departamento> listaDepartamentos;
    private List<Provincia> listaProvincias;
    private List<Distrito> listaDistritos;

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

        spinnerDepartamento.setOnItemSelectedListener(new SimpleOnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                if (position > 0) {
                    int idDepartamento = listaDepartamentos.get(position - 1).getId_departamento();
                    cargarProvincias(idDepartamento);
                } else {
                    limpiarSpinner(spinnerProvincia);
                    limpiarSpinner(spinnerDistrito);
                }
            }
        });

        spinnerProvincia.setOnItemSelectedListener(new SimpleOnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                if (position > 0) {
                    int idProvincia = listaProvincias.get(position - 1).getId_provincia();
                    cargarDistritos(idProvincia);
                } else {
                    limpiarSpinner(spinnerDistrito);
                }
            }
        });

        btnGuardarDireccion.setOnClickListener(v -> guardarDireccion());
    }

    private void cargarDepartamentos() {
        Call<List<Departamento>> call = apiService.obtenerDepartamentos();

        call.enqueue(new Callback<List<Departamento>>() {
            @Override
            public void onResponse(Call<List<Departamento>> call, Response<List<Departamento>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaDepartamentos = response.body();
                    List<String> nombres = new ArrayList<>();
                    nombres.add("Seleccione");

                    for (Departamento dep : listaDepartamentos) {
                        nombres.add(dep.getNombre_departamento());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(AgregarDireccionActivity.this, android.R.layout.simple_spinner_item, nombres);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerDepartamento.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Departamento>> call, Throwable t) {
                Toast.makeText(AgregarDireccionActivity.this, "Error al cargar departamentos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarProvincias(int idDepartamento) {
        ParamsDepartamento params = new ParamsDepartamento(idDepartamento);
        Call<List<Provincia>> call = apiService.obtenerProvincias(params);

        call.enqueue(new Callback<List<Provincia>>() {
            @Override
            public void onResponse(Call<List<Provincia>> call, Response<List<Provincia>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaProvincias = response.body();
                    List<String> nombres = new ArrayList<>();
                    nombres.add("Seleccione");

                    for (Provincia prov : listaProvincias) {
                        nombres.add(prov.getNombre_provincia());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(AgregarDireccionActivity.this, android.R.layout.simple_spinner_item, nombres);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerProvincia.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Provincia>> call, Throwable t) {
                Toast.makeText(AgregarDireccionActivity.this, "Error al cargar provincias", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarDistritos(int idProvincia) {
        ParamsProvincia params = new ParamsProvincia(idProvincia);
        Call<List<Distrito>> call = apiService.obtenerDistritos(params);

        call.enqueue(new Callback<List<Distrito>>() {
            @Override
            public void onResponse(Call<List<Distrito>> call, Response<List<Distrito>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaDistritos = response.body();
                    List<String> nombres = new ArrayList<>();
                    nombres.add("Seleccione");

                    for (Distrito dis : listaDistritos) {
                        nombres.add(dis.getNombre_distrito());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(AgregarDireccionActivity.this, android.R.layout.simple_spinner_item, nombres);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerDistrito.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Distrito>> call, Throwable t) {
                Toast.makeText(AgregarDireccionActivity.this, "Error al cargar distritos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void limpiarSpinner(Spinner spinner) {
        List<String> vacio = new ArrayList<>();
        vacio.add("Seleccione");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vacio);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    /*private void guardarDireccion() {
        String calle = etCalle.getText().toString().trim();
        String referencia = etReferencia.getText().toString().trim();
        String departamento = spinnerDepartamento.getSelectedItem().toString();
        String provincia = spinnerProvincia.getSelectedItem().toString();
        String distrito = spinnerDistrito.getSelectedItem().toString();

        if (calle.isEmpty() || referencia.isEmpty() || departamento.equals("Seleccione") ||
                provincia.equals("Seleccione") || distrito.equals("Seleccione")) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Aquí puedes hacer tu llamado API para guardar la dirección
        Toast.makeText(this, "Dirección guardada correctamente", Toast.LENGTH_SHORT).show();
        finish();
    }*/

    // Listener reutilizable para evitar repetir código
    private abstract class SimpleOnItemSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {
        public abstract void onItemSelected(int position);

        @Override
        public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
            onItemSelected(position);
        }

        @Override
        public void onNothingSelected(android.widget.AdapterView<?> parent) { }
    }

    private void guardarDireccion() {
        String calle = etCalle.getText().toString().trim();
        String referencia = etReferencia.getText().toString().trim();
        int esPrincipal = 1;

        if (spinnerDepartamento.getSelectedItemPosition() == 0 ||
                spinnerProvincia.getSelectedItemPosition() == 0 ||
                spinnerDistrito.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            Log.e("GUARDAR_DIRECCION", "Campos incompletos");
            return;
        }

        int idDistrito = listaDistritos.get(spinnerDistrito.getSelectedItemPosition() - 1).getId_distrito();

        SharedPreferences sharedPref;
        sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        int id_usuario = sharedPref.getInt("id_usuario", -1);

        DireccionRequest direccionRequest = new DireccionRequest(id_usuario, calle, idDistrito, referencia, esPrincipal);
        Log.d("GUARDAR_DIRECCION", "Datos a enviar: " + direccionRequest.toString());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PyAnyApi apiService = retrofit.create(PyAnyApi.class);

        Call<GeneralResp> call = apiService.agregarDireccion(direccionRequest);

        call.enqueue(new Callback<GeneralResp>() {
            @Override
            public void onResponse(Call<GeneralResp> call, Response<GeneralResp> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("GUARDAR_DIRECCION", "Respuesta exitosa: " + response.body().getMessage());
                    Toast.makeText(AgregarDireccionActivity.this, "Dirección guardada correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Log.e("GUARDAR_DIRECCION", "Error en la respuesta: " + response.code() + " - " + response.message());
                    Toast.makeText(AgregarDireccionActivity.this, "Error al guardar dirección", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GeneralResp> call, Throwable t) {
                Log.e("GUARDAR_DIRECCION", "Error de conexión: " + t.getMessage());
                Toast.makeText(AgregarDireccionActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}

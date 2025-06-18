
package com.example.tienda_ropa;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tienda_ropa.R;
import com.example.tienda_ropa.Interface.PyAnyApi;
import com.example.tienda_ropa.model.GeneralResp;
import com.example.tienda_ropa.model.MetodoPago;
import com.example.tienda_ropa.model.MetodoPagoAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityHistorialTarjetas extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MetodoPagoAdapter adapter;
    private List<MetodoPago> listaMetodosPago;
    private SharedPreferences sharedPref;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_historial_tarjetas);

        // --- Toolbar ---
        toolbar = findViewById(R.id.toolbarMetodoPago);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // --- Edge-to-Edge padding ---
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main),
                (v, insets) -> {
                    Insets sys = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(sys.left, sys.top, sys.right, sys.bottom);
                    return insets;
                }
        );

        // --- RecyclerView + Adapter vacío ---
        recyclerView       = findViewById(R.id.rvMetodosPago);
        listaMetodosPago   = new ArrayList<>();
        adapter = new MetodoPagoAdapter(
                listaMetodosPago,
                this,
                this::eliminarMetodoPago  // callback de borrado
        );
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // --- JWT desde SharedPreferences ---
        sharedPref = getSharedPreferences("APP_PREF", MODE_PRIVATE);
        String token = sharedPref.getString("token", null);
        if (token == null) {
            Toast.makeText(this, "Debes iniciar sesión primero", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // --- Retrofit & GET métodos de pago ---
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PyAnyApi api = retrofit.create(PyAnyApi.class);
        String jwtHeader = "JWT " + token;
        api.api_obtener_metodos_pago_usuario(jwtHeader)
                .enqueue(new Callback<GeneralResp>() {
                    @Override
                    public void onResponse(Call<GeneralResp> call,
                                           Response<GeneralResp> resp) {
                        if (!resp.isSuccessful() || resp.body() == null) {
                            Toast.makeText(ActivityHistorialTarjetas.this,
                                    "Error al cargar métodos", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        @SuppressWarnings("unchecked")
                        List<MetodoPago> datos =
                                (List<MetodoPago>) resp.body().getData();

                        listaMetodosPago.clear();
                        listaMetodosPago.addAll(datos);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<GeneralResp> call, Throwable t) {
                        Toast.makeText(ActivityHistorialTarjetas.this,
                                "Fallo de red: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Llama a tu API DELETE y, al confirmar, quita el ítem de la lista.
     */
    private void eliminarMetodoPago(int idMetodoPago) {
        // TODO: sustituye con tu interfaz y ruta DELETE real
        new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PyAnyApi.class)
                .api_eliminar_metodo_pago(
                        "JWT " + sharedPref.getString("token", ""),
                        idMetodoPago
                )
                .enqueue(new Callback<GeneralResp>() {
                    @Override
                    public void onResponse(Call<GeneralResp> call,
                                           Response<GeneralResp> resp) {
                        if (resp.isSuccessful() && resp.body() != null
                                && resp.body().getCode() == 1) {
                            // remueve del RecyclerView
                            for (int i = 0; i < listaMetodosPago.size(); i++) {
                                if (listaMetodosPago.get(i).getId_metodo_pago() == idMetodoPago) {
                                    listaMetodosPago.remove(i);
                                    adapter.notifyItemRemoved(i);
                                    break;
                                }
                            }
                        } else {
                            Toast.makeText(ActivityHistorialTarjetas.this,
                                    "No se pudo eliminar", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<GeneralResp> call, Throwable t) {
                        Toast.makeText(ActivityHistorialTarjetas.this,
                                "Error de red: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

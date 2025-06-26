package com.example.tienda_ropa.ui.menu_usuario;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tienda_ropa.Interface.PyAnyApi;
import com.example.tienda_ropa.ObtenerCarrito.CarritoItemDecoration;
import com.example.tienda_ropa.R;
import com.example.tienda_ropa.model.PedidosResp;
import com.example.tienda_ropa.pedido.PedidoAdapter;
import com.example.tienda_ropa.pedido.PedidoApi;
import com.example.tienda_ropa.pedido.PedidoEntry;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HistorialPedidosActivity extends AppCompatActivity {

    /* ───────────── Views ───────────── */
    private RecyclerView recyclerPedidos;
    private ImageButton btnVolver;

    /* ───────────── Datos ───────────── */
    private PedidoAdapter pedidoAdapter;
    private List<PedidoEntry> listaPedidos;

    /* ───────────── Session ─────────── */
    private SharedPreferences sharedPref;
    private int idUsuario;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* 2.- Credenciales del usuario */
        sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        idUsuario  = sharedPref.getInt("id_usuario", -1);
        token      = sharedPref.getString("token", "");

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_historial_pedidos);

        /* 1.- View binding (manual) */
        recyclerPedidos = findViewById(R.id.recyclerPedidos);
        recyclerPedidos.setHasFixedSize(true);
        btnVolver       = findViewById(R.id.btnVolver);

        recyclerPedidos.setLayoutManager(new LinearLayoutManager(this));

        /* 3.- Botón volver */
        btnVolver.setOnClickListener(v -> finish());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        obtenerHistorialPedidos();
    }

    private void obtenerHistorialPedidos() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PyAnyApi api = retrofit.create(PyAnyApi.class);

        Call<PedidosResp> call = api.historialPedidos("JWT " + token);
        call.enqueue(new Callback<PedidosResp>() {
            @Override
            public void onResponse(Call<PedidosResp> call, Response<PedidosResp> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(HistorialPedidosActivity.this,
                            "Error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                PedidosResp resp = response.body();
                if (resp.getCode() != 1 || resp.getData() == null) {
                    Toast.makeText(HistorialPedidosActivity.this,
                            "Sin historial de pedidos", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (resp.getCode() == 1) {

                    // 1️⃣  Lista recibida desde la API (Modelo “API”)
                    List<PedidoApi> listaPedidosApi = resp.getData();

                    // 3️⃣  Convierte a la lista para la capa de UI (PedidoEntry)
                    listaPedidos = new ArrayList<>();
                    for (PedidoApi p : listaPedidosApi) {
                        PedidoEntry entry = new PedidoEntry(
                                p.getId_orden(),               // long
                                p.getCodigo_pedido(),          // String  (puede venir vacío "")
                                p.getFecha_realizado(),        // String  "dd/MM/yyyy"
                                Integer.parseInt(p.getArticulos()), // int
                                p.getTotal()                   // String  "150.00"
                        );
                        listaPedidos.add(entry);
                    }

                    // 4️⃣  Carga el RecyclerView con su Adapter
                    PedidoAdapter adapter = new PedidoAdapter(listaPedidos);
                    recyclerPedidos.setAdapter(adapter);

                    /* 3️⃣ Espaciado opcional */
                    int large = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
                    int small = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);
                    if (recyclerPedidos.getItemDecorationCount() == 0) {
                        recyclerPedidos.addItemDecoration(new CarritoItemDecoration(large, small));
                    }

                } else {
                    Log.e("API_ERROR", "Error en el código devuelto: " + resp.getCode());
                }

            }

            @Override
            public void onFailure(Call<PedidosResp> call, Throwable throwable) {

            }
        });
    }
}
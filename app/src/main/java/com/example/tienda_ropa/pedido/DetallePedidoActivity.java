package com.example.tienda_ropa.pedido;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tienda_ropa.Interface.PyAnyApi;
import com.example.tienda_ropa.R;
import com.example.tienda_ropa.model.DetalleApi;
import com.example.tienda_ropa.model.DetalleCompraResp;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetallePedidoActivity extends AppCompatActivity {

    // ─── Views ──────────────────────────────────────────────
    private TextView txtCodigo, txtFecha, txtEstado,
            txtSubtotal, txtEnvio, txtIgv, txtTotal,
            txtDireccion, txtMetodo;
    private RecyclerView recyclerItems;
    private DetalleApi detalle;

    // ─── Datos de sesión ────────────────────────────────────
    private String token;
    private long idOrden;

    ImageButton btnVolver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalle_pedido);

        // Obtenemos token y id de orden
        SharedPreferences sp = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        token   = sp.getString("token", "");
        idOrden = getIntent().getLongExtra("id_orden", -1);
        Log.d("DetallePedido", "ID recibido: " + idOrden);
        if (idOrden == -1) {
            Toast.makeText(this, "Pedido inválido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Vinculamos las vistas
        txtCodigo   = findViewById(R.id.txtCodigo);
        txtFecha    = findViewById(R.id.txtFecha);
        txtEstado   = findViewById(R.id.txtEstado);
        txtSubtotal = findViewById(R.id.txtSubtotal);
        txtEnvio    = findViewById(R.id.txtEnvio);
        txtIgv      = findViewById(R.id.txtIgv);
        txtTotal    = findViewById(R.id.txtTotalPagar);
        txtDireccion= findViewById(R.id.txtDireccion);
        txtMetodo   = findViewById(R.id.txtMetodoPago);
        recyclerItems = findViewById(R.id.recyclerItems);
        recyclerItems.setLayoutManager(new LinearLayoutManager(this));

        btnVolver = findViewById(R.id.btnVolverDetalle);

        btnVolver.setOnClickListener(v -> finish());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainDetalle), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Llamamos a la API
        obtenerDetallePedido(idOrden);
    }

    private void obtenerDetallePedido(long idOrden) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PyAnyApi api = retrofit.create(PyAnyApi.class);

        Call<DetalleCompraResp> call =
                api.obtenerDetalleCompra("JWT " + token, idOrden);
        call.enqueue(new Callback<DetalleCompraResp>() {
            @Override
            public void onResponse(Call<DetalleCompraResp> call, Response<DetalleCompraResp> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(DetallePedidoActivity.this,"Error "+response.code(),Toast.LENGTH_SHORT).show();
                    return;
                }
                detalle = response.body().getData();
                pintarPantalla(detalle);
            }

            @Override
            public void onFailure(Call<DetalleCompraResp> call, Throwable throwable) {
                Toast.makeText(DetallePedidoActivity.this,
                        "Fallo conexión: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void pintarPantalla(DetalleApi d) {

        // ─ Resumen ─
        txtCodigo.setText("Pedido: " + d.getResumen().getCodigo());
        txtFecha .setText("Fecha: "  + d.getResumen().getFecha());
        txtEstado.setText("Estado: " + d.getResumen().getEstado());

        Locale es = new Locale("es","PE");
        txtSubtotal.setText(String.format(es,"Subtotal: S/ %.2f", d.getResumen().getSubtotal()));
        txtEnvio   .setText(String.format(es,"Envío: S/ %.2f", d.getResumen().getEnvio()));
        txtIgv     .setText(String.format(es,"IGV: S/ %.2f", d.getResumen().getIgv()));
        txtTotal   .setText(String.format(es,"TOTAL: S/ %.2f", d.getResumen().getTotal()));

        // ─ Dirección ─
        if (d.getDireccion() != null) {
            String dir = (d.getDireccion().getLinea1() != null ? d.getDireccion().getLinea1()+"\n":"") +
                    (d.getDireccion().getReferencia()!=null? d.getDireccion().getReferencia()+"\n":"") +
                    d.getDireccion().getDistrito()+" - "+d.getDireccion().getProvincia()+"\n" +
                    d.getDireccion().getDepartamento();
            txtDireccion.setText(dir);
        }


        if (d.getMetodo()!=null)
            txtMetodo.setText(d.getMetodo().getDescripcion());

        // ─ Ítems ─
        List<DetallePedidoEntry> lista = new ArrayList<>();
        for (ItemCompra it : d.getItems()) {
            lista.add(new DetallePedidoEntry(
                    it.getNomPrenda(),        // String nomPrenda
                    it.getTalla(),            // String talla
                    it.getPrecio(),           // double precio  (ya es double)
                    it.getCantidad(),         // int cantidad
                    it.getUrlImagen()));
        }
        recyclerItems.setAdapter(new DetallePedidoAdapter(DetallePedidoActivity.this, lista)); // usa tu adapter
    }

}
package com.example.tienda_ropa.ui.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.toolbox.NetworkImageView;
import com.example.tienda_ropa.Interface.PyAnyApi;
import com.example.tienda_ropa.R;
import com.example.tienda_ropa.model.ObtenerPrendaResp;
import com.example.tienda_ropa.model.PrendaApi;
import com.example.tienda_ropa.model.PrendaDetalleResp;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetallesPrenda extends AppCompatActivity {

    private TextView nombrePrenda, precioPrenda, descripcionPrenda;
    private NetworkImageView imagenPrenda;
    private SharedPreferences sharedPreferences;
    private int prendaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalles_prenda);

        nombrePrenda = findViewById(R.id.nombrePrenda);
        precioPrenda = findViewById(R.id.precioPrenda);
        descripcionPrenda = findViewById(R.id.descripcionPrenda);
        imagenPrenda = findViewById(R.id.imagenPrenda);

        // Obtener el aidi
        prendaId = getIntent().getIntExtra("prenda_id", -1);

        if(prendaId==-1){
            Toast.makeText(this,"Prenda inválida",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        obtenerDetallesPrenda(prendaId);
    }


    private void obtenerDetallesPrenda(int prendaId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")  // Cambiar por tu URL de la API
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PyAnyApi apiService = retrofit.create(PyAnyApi.class);
        sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        Call<PrendaDetalleResp> call = apiService.obtenerDetallesPrenda("JWT " + token, prendaId);
        call.enqueue(new Callback<PrendaDetalleResp>() {
            @Override
            public void onResponse(Call<PrendaDetalleResp> call, Response<PrendaDetalleResp> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PrendaApi prenda = response.body().getData();
                    nombrePrenda.setText(prenda.getNomPrenda());
                    precioPrenda.setText(String.valueOf(prenda.getPrecio()));
                    descripcionPrenda.setText(prenda.getDescripcion());

                    ImageRequester.getInstance().setImageFromUrl(imagenPrenda, prenda.getUrl_imagen());
                } else {
                    Toast.makeText(DetallesPrenda.this, "Error al obtener los detalles de la prenda", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PrendaDetalleResp> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(DetallesPrenda.this, "Error en la conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
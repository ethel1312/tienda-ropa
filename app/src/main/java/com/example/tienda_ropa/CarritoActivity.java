package com.example.tienda_ropa;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tienda_ropa.Interface.OnCantidadChangeListener;
import com.example.tienda_ropa.Interface.PyAnyApi;
import com.example.tienda_ropa.ObtenerCarrito.CarritoCardRecyclerViewAdapter;
import com.example.tienda_ropa.ObtenerCarrito.CarritoItemDecoration;
import com.example.tienda_ropa.ObtenerCarrito.ProductoCarritoReq;
import com.example.tienda_ropa.model.CarritoApi;
import com.example.tienda_ropa.model.CarritoEntry;
import com.example.tienda_ropa.model.GeneralResp;
import com.example.tienda_ropa.model.ObtenerCarritoResp;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CarritoActivity extends AppCompatActivity implements OnCantidadChangeListener {

    SharedPreferences sharedPref;
    ImageButton mBotonVolver;
    MaterialButton mBtnVerificar;
    MaterialTextView txtSubtotal;
    MaterialTextView txtEnvio;
    MaterialTextView txtTotal;
    RecyclerView recyclerView;
    String token;
    int idUsuario;
    private List<CarritoEntry> carritoList;
    private int conteo=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = this.getSharedPreferences("user_session", Context.MODE_PRIVATE);
        token= sharedPref.getString("token","");
        idUsuario = sharedPref.getInt("id_usuario", 0);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_carrito);

        mBotonVolver= findViewById(R.id.btnVolver);
        mBtnVerificar= findViewById(R.id.btnVerificar);

        txtSubtotal = findViewById(R.id.txtSubtotal);
        txtEnvio = findViewById(R.id.txtEnvio);
        txtTotal = findViewById(R.id.txtTotal);

        //para mostrar los productos
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        //recyclerView.setLayoutManager(new GridLayoutManager(this.getBaseContext(), 2, GridLayoutManager.VERTICAL, false));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        obtenerCarrito();


        mBotonVolver.setOnClickListener(v -> {
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void obtenerCarrito(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PyAnyApi dambPyAnyApi = retrofit.create(PyAnyApi.class);
        Log.d("TOKEN_DEBUG", sharedPref.getString("token",""));
        Call<ObtenerCarritoResp> call=dambPyAnyApi.obtenerCarrito("JWT " + token);

        call.enqueue(new Callback<ObtenerCarritoResp>() {
            @Override
            public void onResponse(Call<ObtenerCarritoResp> call, Response<ObtenerCarritoResp> response) {
                if(!response.isSuccessful() || response.body() == null){
                    Log.e("API_ERROR", "Código: " + response.code() + " - Cuerpo vacío");
                    return;
                }
                ObtenerCarritoResp resp = response.body();
                if (resp.getCode() == 1) {
                    List<CarritoApi> listaCarrito = resp.getData();


                    actualizarInformacionPedido(listaCarrito);
                    carritoList= new ArrayList<>();
                    for(CarritoApi elemento: listaCarrito){
                        CarritoEntry obj= new CarritoEntry(
                                elemento.getUrl_imagen(),
                                elemento.getNomPrenda(),
                                elemento.getPrecio(),
                                elemento.getCantidad(),
                                elemento.getId_prenda(),
                                elemento.getTalla(),
                                elemento.getStock());
                        carritoList.add(obj);
                    }

                    CarritoCardRecyclerViewAdapter adapter= new CarritoCardRecyclerViewAdapter(carritoList,CarritoActivity.this);
                    recyclerView.setAdapter(adapter);
                    if(conteo==0){
                        int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
                        int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);
                        recyclerView.addItemDecoration(new CarritoItemDecoration(largePadding, smallPadding));
                    }
                    conteo++;
                } else {
                    Log.e("API_ERROR", "Error en el código devuelto: " + resp.getCode());
                }
            }
            @Override
            public void onFailure(Call<ObtenerCarritoResp> call, Throwable throwable) {

            }
        });

    }

    public void onIncrementarCantidad(int idPrenda) {
        // Llamamos a la API para aumentar la cantidad del producto
        aumentarCantPro(idPrenda);

    }

    @Override
    public void onDisminuirCantidad(int idPrenda) {
        disminuirCantPro(idPrenda);
    }

    @Override
    public void onEliminarProducto(int idPrenda) {
        eliminarPro(idPrenda);
    }

    public void aumentarCantPro(int idPrenda){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PyAnyApi dambPyAnyApi = retrofit.create(PyAnyApi.class);
        ProductoCarritoReq obj= new ProductoCarritoReq();
        obj.setId_usuario(idUsuario);
        obj.setId_prenda(idPrenda);
        Call<GeneralResp> call=dambPyAnyApi.incrementarCantProduc(token,obj);
        call.enqueue(new Callback<GeneralResp>() {
            @Override
            public void onResponse(Call<GeneralResp> call, Response<GeneralResp> response) {
                if(!response.isSuccessful()){
                    return;
                }
                obtenerCarrito();
            }
            @Override
            public void onFailure(Call<GeneralResp> call, Throwable throwable) {

            }
        });
    }
    public void disminuirCantPro(int idPrenda){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PyAnyApi dambPyAnyApi = retrofit.create(PyAnyApi.class);
        ProductoCarritoReq obj= new ProductoCarritoReq();
        obj.setId_usuario(idUsuario);
        obj.setId_prenda(idPrenda);
        Call<GeneralResp> call=dambPyAnyApi.disminuirCantProduc(token,obj);
        call.enqueue(new Callback<GeneralResp>() {
            @Override
            public void onResponse(Call<GeneralResp> call, Response<GeneralResp> response) {
                if(!response.isSuccessful()){
                    return;
                }
                obtenerCarrito();
            }
            @Override
            public void onFailure(Call<GeneralResp> call, Throwable throwable) {

            }
        });
    }
    public void eliminarPro(int idPrenda){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PyAnyApi dambPyAnyApi = retrofit.create(PyAnyApi.class);
        ProductoCarritoReq obj= new ProductoCarritoReq();
        obj.setId_usuario(idUsuario);
        obj.setId_prenda(idPrenda);
        Call<GeneralResp> call=dambPyAnyApi.eliminarProduc(token,obj);
        call.enqueue(new Callback<GeneralResp>() {
            @Override
            public void onResponse(Call<GeneralResp> call, Response<GeneralResp> response) {
                if(!response.isSuccessful()){
                    return;
                }
                obtenerCarrito();
                GeneralResp resp = response.body();
                Toast miToast= Toast.makeText(getApplicationContext(),resp.getMessage().toString(),Toast.LENGTH_SHORT);
                miToast.show();
            }
            @Override
            public void onFailure(Call<GeneralResp> call, Throwable throwable) {

            }
        });
    }

    private void actualizarInformacionPedido(List<CarritoApi> listaCarrito) {
        double subtotal = 0.0;
        double costoEnvio = 10.0; // Puedes cambiar este valor según convenga
        double total;

        for (CarritoApi item : listaCarrito) {
            try {
                // Sumamos directamente el total ya calculado (precio * cantidad)
                double totalItem = Double.parseDouble(item.getTotal());
                subtotal += totalItem;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        total = subtotal + costoEnvio;

        txtSubtotal.setText(String.format("S/. %.2f", subtotal));
        txtEnvio.setText(String.format("S/. %.2f", costoEnvio));
        txtTotal.setText(String.format("S/. %.2f", total));
    }


}
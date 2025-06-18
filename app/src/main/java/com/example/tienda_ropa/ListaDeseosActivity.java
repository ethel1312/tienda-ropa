package com.example.tienda_ropa;

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

import com.example.tienda_ropa.Interface.OnCantidadChangeListener;
import com.example.tienda_ropa.Interface.OnListaDeseosListener;
import com.example.tienda_ropa.Interface.PyAnyApi;
import com.example.tienda_ropa.ListaDeseos.ListaDeseosCardRecyclerViewAdapter;
import com.example.tienda_ropa.ObtenerCarrito.CarritoCardRecyclerViewAdapter;
import com.example.tienda_ropa.ObtenerCarrito.CarritoItemDecoration;
import com.example.tienda_ropa.ObtenerCarrito.ProductoCarritoReq;
import com.example.tienda_ropa.model.CarritoApi;
import com.example.tienda_ropa.model.CarritoEntry;
import com.example.tienda_ropa.model.GeneralResp;
import com.example.tienda_ropa.model.ListaDeseosApi;
import com.example.tienda_ropa.model.ListaDeseosEntry;
import com.example.tienda_ropa.model.ObtenerCarritoResp;
import com.example.tienda_ropa.model.ObtenerListaDeseosResp;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListaDeseosActivity extends AppCompatActivity implements OnListaDeseosListener {

    SharedPreferences sharedPref;
    ImageButton mBotonVolver;
    RecyclerView recyclerView;
    String token;
    private List<ListaDeseosEntry> listaDeseosList;
    private int conteo=0;
    MaterialTextView txtNumArticulos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = this.getSharedPreferences("user_session", Context.MODE_PRIVATE);
        token= sharedPref.getString("token","");
        //token="JWT "+ "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3NTAwODc5NjIsImlhdCI6MTc1MDA4NzY2MiwibmJmIjoxNzUwMDg3NjYyLCJpZGVudGl0eSI6MX0.0vR2lkrlFdg3Sdasr1oadjjFHGV0khrRfw_s1ov-P2Y";

        getSupportActionBar().hide();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lista_deseos);

        mBotonVolver= findViewById(R.id.btnVolverLD);
        txtNumArticulos=findViewById(R.id.txtArticulos);

        //para mostrar los productos
        recyclerView = findViewById(R.id.recycler_viewLD);
        recyclerView.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        //recyclerView.setLayoutManager(new GridLayoutManager(this.getBaseContext(), 2, GridLayoutManager.VERTICAL, false));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        obtenerListaDeseos();


        mBotonVolver.setOnClickListener(v -> {
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void obtenerListaDeseos(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PyAnyApi dambPyAnyApi = retrofit.create(PyAnyApi.class);
        Log.d("TOKEN_DEBUG", sharedPref.getString("token",""));
        //Call<ObtenerListaDeseosResp> call=dambPyAnyApi.obtenerCarrito(1,"JWT "+sharedPref.getString("token",""));
        Call<ObtenerListaDeseosResp> call=dambPyAnyApi.obtenerListaDeseos(1,token);
        call.enqueue(new Callback<ObtenerListaDeseosResp>() {
            @Override
            public void onResponse(Call<ObtenerListaDeseosResp> call, Response<ObtenerListaDeseosResp> response) {
                if(!response.isSuccessful() || response.body() == null){
                    Log.e("API_ERROR", "Código: " + response.code() + " - Cuerpo vacío");
                    return;
                }
                ObtenerListaDeseosResp resp = response.body();
                if (resp.getCode() == 1) {
                    List<ListaDeseosApi> listaLD= resp.getData();
                    numArticulos(listaLD);

                    listaDeseosList= new ArrayList<>();
                    for(ListaDeseosApi elemento: listaLD){
                        ListaDeseosEntry obj= new ListaDeseosEntry(elemento.getUrl_imagen(), elemento.getNomPrenda() , elemento.getPrecio(),elemento.getId_prenda());
                        listaDeseosList.add(obj);
                    }

                    ListaDeseosCardRecyclerViewAdapter adapter= new ListaDeseosCardRecyclerViewAdapter(listaDeseosList,ListaDeseosActivity.this);
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
            public void onFailure(Call<ObtenerListaDeseosResp> call, Throwable throwable) {

            }
        });
    }

    public void numArticulos(List<ListaDeseosApi> lista){
        int sum=0;
        for (ListaDeseosApi item : lista) {
            try {
                sum++;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        txtNumArticulos.setText(String.valueOf(sum)+" artículos");
    }

    @Override
    public void onAgregarCarrito(int idPrenda) {
        agrgarProCarrito(idPrenda);
    }

    @Override
    public void onEliminarProducto(int idPrenda) {
        eliminarProLD(idPrenda);
    }

    public void agrgarProCarrito(int idPrenda){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PyAnyApi dambPyAnyApi = retrofit.create(PyAnyApi.class);
        ProductoCarritoReq obj= new ProductoCarritoReq();
        obj.setId_usuario(1);
        obj.setId_prenda(idPrenda);
        Call<GeneralResp> call=dambPyAnyApi.moverAcarrito(token,obj);
        call.enqueue(new Callback<GeneralResp>() {
            @Override
            public void onResponse(Call<GeneralResp> call, Response<GeneralResp> response) {
                if(!response.isSuccessful()){
                    return;
                }
                obtenerListaDeseos();
                GeneralResp resp = response.body();
                Toast miToast= Toast.makeText(getApplicationContext(),resp.getMessage().toString(),Toast.LENGTH_SHORT);
                miToast.show();
            }
            @Override
            public void onFailure(Call<GeneralResp> call, Throwable throwable) {

            }
        });
    }
    public void eliminarProLD(int idPrenda){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PyAnyApi dambPyAnyApi = retrofit.create(PyAnyApi.class);
        ProductoCarritoReq obj= new ProductoCarritoReq();
        obj.setId_usuario(1);
        obj.setId_prenda(idPrenda);
        Call<GeneralResp> call=dambPyAnyApi.eliminarDeListaDeseos(token,obj);
        call.enqueue(new Callback<GeneralResp>() {
            @Override
            public void onResponse(Call<GeneralResp> call, Response<GeneralResp> response) {
                if(!response.isSuccessful()){
                    return;
                }
                obtenerListaDeseos();
                GeneralResp resp = response.body();
                Toast miToast= Toast.makeText(getApplicationContext(),resp.getMessage().toString(),Toast.LENGTH_SHORT);
                miToast.show();
            }
            @Override
            public void onFailure(Call<GeneralResp> call, Throwable throwable) {

            }
        });
    }
}
package com.example.tienda_ropa.ui.deseos;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.tienda_ropa.Interface.OnListaDeseosListener;
import com.example.tienda_ropa.Interface.PyAnyApi;
import com.example.tienda_ropa.ListaDeseos.ListaDeseosCardRecyclerViewAdapter;
import com.example.tienda_ropa.ObtenerCarrito.CarritoItemDecoration;
import com.example.tienda_ropa.ObtenerCarrito.ProductoCarritoReq;
import com.example.tienda_ropa.R;
import com.example.tienda_ropa.databinding.FragmentListaDeseosBinding;
import com.example.tienda_ropa.model.GeneralResp;
import com.example.tienda_ropa.model.ListaDeseosApi;
import com.example.tienda_ropa.model.ListaDeseosEntry;
import com.example.tienda_ropa.model.ObtenerListaDeseosResp;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback; // ← AGREGADO
import retrofit2.Response; // ← AGREGADO
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListaDeseosFragment extends Fragment implements OnListaDeseosListener {

    private FragmentListaDeseosBinding binding;
    ImageButton mBotonVolver;
    SharedPreferences sharedPref;
    RecyclerView recyclerView;
    String token;
    int idUsuario;
    private List<ListaDeseosEntry> listaDeseosList;
    private int conteo=0;
    MaterialTextView txtNumArticulos;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListaDeseosBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Obtener token e id_usuario
        sharedPref = requireContext().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        token = sharedPref.getString("token", "");
        idUsuario = sharedPref.getInt("id_usuario", 0);

//        mBotonVolver= binding.btnVolverLD;
        txtNumArticulos=binding.txtArticulos;


        // Configurar RecyclerView
        recyclerView = binding.recyclerViewLD;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        obtenerListaDeseos();

//        mBotonVolver.setOnClickListener(v -> {
//            NavHostFragment.findNavController(ListaDeseosFragment.this).navigateUp();
//        });

        return view;
    }

    private void obtenerListaDeseos() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PyAnyApi dambPyAnyApi = retrofit.create(PyAnyApi.class);

        Call<ObtenerListaDeseosResp> call = dambPyAnyApi.obtenerListaDeseos("JWT " + token);
        call.enqueue(new Callback<ObtenerListaDeseosResp>() {
            @Override
            public void onResponse(Call<ObtenerListaDeseosResp> call, Response<ObtenerListaDeseosResp> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Log.e("API_ERROR", "Error en la respuesta de la API");
                    return;
                }

                ObtenerListaDeseosResp resp = response.body();
                if (resp.getCode() == 1) {
                    List<ListaDeseosApi> listaLD = resp.getData();
                    numArticulos(listaLD);
                    listaDeseosList = new ArrayList<>();

                    for (ListaDeseosApi elemento : listaLD) {
                        ListaDeseosEntry obj = new ListaDeseosEntry(
                                elemento.getUrl_imagen(),
                                elemento.getNomPrenda(),
                                elemento.getPrecio(),
                                elemento.getId_prenda(),
                                elemento.getStock()
                        );
                        listaDeseosList.add(obj);
                    }

                    ListaDeseosCardRecyclerViewAdapter adapter =
                            new ListaDeseosCardRecyclerViewAdapter(listaDeseosList, ListaDeseosFragment.this);
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
                Log.e("API_ERROR", "Fallo de red: " + throwable.getMessage());
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
        obj.setId_usuario(idUsuario);
        obj.setId_prenda(idPrenda);
        Call<GeneralResp> call = dambPyAnyApi.moverAcarrito("JWT " + token, obj);
        call.enqueue(new Callback<GeneralResp>() {
            @Override
            public void onResponse(Call<GeneralResp> call, Response<GeneralResp> response) {
                if(!response.isSuccessful()){
                    return;
                }
                obtenerListaDeseos();
                GeneralResp resp = response.body();
                Toast miToast= Toast.makeText(requireContext(),resp.getMessage().toString(),Toast.LENGTH_SHORT);
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
        obj.setId_usuario(idUsuario);
        obj.setId_prenda(idPrenda);

        Call<GeneralResp> call=dambPyAnyApi.eliminarDeListaDeseos("JWT " + token, obj);
        call.enqueue(new Callback<GeneralResp>() {
            @Override
            public void onResponse(Call<GeneralResp> call, Response<GeneralResp> response) {
                if(!response.isSuccessful()){
                    return;
                }
                obtenerListaDeseos();
                GeneralResp resp = response.body();
                Toast miToast= Toast.makeText(requireContext(),resp.getMessage().toString(),Toast.LENGTH_SHORT);
                miToast.show();
            }
            @Override
            public void onFailure(Call<GeneralResp> call, Throwable throwable) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        AppCompatActivity act = (AppCompatActivity) getActivity();
        if (act != null && act.getSupportActionBar() != null) {
            act.getSupportActionBar().setTitle("Lista de deseos");
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

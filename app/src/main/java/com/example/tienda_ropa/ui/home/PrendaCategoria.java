package com.example.tienda_ropa.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tienda_ropa.ObtenerCarrito.ProductoCarritoReq;
import com.example.tienda_ropa.R;
import com.example.tienda_ropa.Interface.PyAnyApi;
import com.example.tienda_ropa.model.AgregarListaDeseosReq;
import com.example.tienda_ropa.model.GeneralResp;
import com.example.tienda_ropa.model.ObtenerPrendaResp;
import com.example.tienda_ropa.model.ParamsCategoria;
import com.example.tienda_ropa.model.PrendaApi;
import com.example.tienda_ropa.model.PrendaResponse;
import com.example.tienda_ropa.ui.home.PrendaEntry;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PrendaCategoria extends Fragment  implements FavoritoHandler{

    private RecyclerView recyclerView;
    private TextView indicatorLayout;
    private SharedPreferences sharedPref;
    String token;
    int idUsuario;
    private PrendaCardRecyclerViewAdapterH adapter;
    private List<PrendaEntry> prendaList = new ArrayList<>();
    private String categoria; // "hombre" o "mujer"

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_prenda_por_categoria, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPref = requireContext().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        token = sharedPref.getString("token", "");
        idUsuario = sharedPref.getInt("id_usuario", 0);

        recyclerView = view.findViewById(R.id.recycler_view);
        indicatorLayout = view.findViewById(R.id.indicatorLayout);

        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        // Obtener categoría desde argumentos
        if (getArguments() != null) {
            categoria = getArguments().getString("categoria", "mujer");
        } else {
            categoria = "mujer";
        }

        indicatorLayout.setText("Categoría: " + categoria.toUpperCase());
        Bundle args = new Bundle();
        int id_categoria = args.getInt("id_categoria");
        //obtenerPrendaPorCategoria(id_categoria);
    }

    /*private void obtenerPrendaPorCategoria(int id_categoria) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PyAnyApi api = retrofit.create(PyAnyApi.class);

        ParamsCategoria params = new ParamsCategoria(id_categoria);

        Call<PrendaResponse> call = api.prendasPorCategoria("JWT " + token, params);

        call.enqueue(new Callback<PrendaResponse>() {
            @Override
            public void onResponse(Call<PrendaResponse> call, Response<PrendaResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<PrendaApi> listaPrendas = response.body().getData();
                    prendaList.clear();
                    for (PrendaApi elemento : listaPrendas) {
                        PrendaEntry prendaEntry = new PrendaEntry(
                                elemento.getId_prenda(),
                                elemento.getNomPrenda(),
                                elemento.getPrecio(),
                                elemento.getUrl_imagen(),
                                elemento.isFavorito()
                        );
                        prendaList.add(prendaEntry);
                    }
                    adapter = new PrendaCardRecyclerViewAdapterH(prendaList, getContext(), PrendaCategoria.this);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "No se pudieron obtener prendas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PrendaResponse> call, Throwable throwable) {

            }


        });
    }*/

    @Override
    public void agregarAFavoritos(AgregarListaDeseosReq req) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PyAnyApi api = retrofit.create(PyAnyApi.class);

        Call<GeneralResp> call = api.agregarListaDeseos("JWT " + token, req);
        call.enqueue(new Callback<GeneralResp>() {
            @Override
            public void onResponse(Call<GeneralResp> call, Response<GeneralResp> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Error al agregar a favoritos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GeneralResp> call, Throwable t) {
                Toast.makeText(getContext(), "Fallo de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void eliminarDeFavoritos(int idPrenda) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PyAnyApi api = retrofit.create(PyAnyApi.class);

        ProductoCarritoReq req = new ProductoCarritoReq();
        req.setId_usuario(idUsuario);
        req.setId_prenda(idPrenda);

        Call<GeneralResp> call = api.eliminarDeListaDeseos("JWT " + token, req);
        call.enqueue(new Callback<GeneralResp>() {
            @Override
            public void onResponse(Call<GeneralResp> call, Response<GeneralResp> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Eliminado de favoritos", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Error al eliminar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GeneralResp> call, Throwable t) {
                Toast.makeText(getContext(), "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

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

import com.example.tienda_ropa.R;
import com.example.tienda_ropa.Interface.PyAnyApi;
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

public class PrendaCategoria extends Fragment {

    private RecyclerView recyclerView;
    private TextView indicatorLayout;
    private SharedPreferences sharedPreferences;
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

        obtenerPrendaPorCategoria(categoria);
    }

    private void obtenerPrendaPorCategoria(String categoria) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PyAnyApi api = retrofit.create(PyAnyApi.class);

        sharedPreferences = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        ParamsCategoria params = new ParamsCategoria(1);

        Call<PrendaResponse> call = api.prendasPorCategoria("JWT " + token, params);

        call.enqueue(new Callback<PrendaResponse>() {
            @Override
            public void onResponse(Call<PrendaResponse> call, Response<PrendaResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<PrendaApi> listaPrendas = response.body().getData();
                    prendaList.clear();
                    for (PrendaApi elemento : listaPrendas) {
                        PrendaEntry prendaEntry = new PrendaEntry(
                                elemento.getId(),
                                elemento.getNomPrenda(),
                                elemento.getPrecio(),
                                elemento.getUrl_imagen()
                        );
                        prendaList.add(prendaEntry);
                    }
                    adapter = new PrendaCardRecyclerViewAdapterH(prendaList, getContext());
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "No se pudieron obtener prendas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PrendaResponse> call, Throwable throwable) {

            }


        });
    }
}

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tienda_ropa.Interface.PyAnyApi;
import com.example.tienda_ropa.databinding.FragmentHomeBinding;
import com.example.tienda_ropa.R;
import com.example.tienda_ropa.model.ObtenerPrendaResp;
import com.example.tienda_ropa.model.PrendaApi;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import me.relex.circleindicator.CircleIndicator3;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Callback;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private ViewPager2 viewPager;
    private View indicator1, indicator2;
    private List<String> imageList;
    private CircleIndicator3 circleIndicator;

    private RecyclerView recyclerView;
    private SharedPreferences sharedPreferences;
    private List<PrendaEntry> prendaList;
    private PrendaCardRecyclerViewAdapterH adapter;



    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        // Inicializar el ViewPager2
        viewPager = root.findViewById(R.id.viewPager);
        indicator1 = root.findViewById(R.id.indicator1);
        indicator2 = root.findViewById(R.id.indicator2);

        imageList = new ArrayList<>();
        imageList.add(String.valueOf(R.drawable.carrusel1));  // Usar los recursos locales
        imageList.add(String.valueOf(R.drawable.carrusel2));

        // Configurar el adaptador para ViewPager2
        Carrusel adapter = new Carrusel(imageList);
        viewPager.setAdapter(adapter);


        recyclerView = root.findViewById(R.id.recycler_view);

        // Cartas
        recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 2, GridLayoutManager.VERTICAL, false));
        prendaList = new ArrayList<>();


        obtenerPrenda();


        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                // para que inicien en gris
                indicator1.setBackgroundResource(R.drawable.carrusel_puntos2);
                indicator2.setBackgroundResource(R.drawable.carrusel_puntos2);

                // para que ca,nbien de color
                if (position == 0) {
                    indicator1.setBackgroundResource(R.drawable.carrusel_puntos);
                } else if (position == 1) {
                    indicator2.setBackgroundResource(R.drawable.carrusel_puntos);
                }
            }
        });


        return root;

    }

    private void obtenerPrenda() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PyAnyApi PyAnyApi = retrofit.create(PyAnyApi.class);
        sharedPreferences = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        Call<ObtenerPrendaResp> call = PyAnyApi.obtenerPrendas("JWT " + token);
        call.enqueue(new Callback<ObtenerPrendaResp>() {
            @Override
            public void onResponse(Call<ObtenerPrendaResp> call, Response<ObtenerPrendaResp> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // prendas para las cartitassss
                    List<PrendaApi> listaPrendas = response.body().getData();
                            for (PrendaApi elemento : listaPrendas) {
                                PrendaEntry prendaEntry = new PrendaEntry(elemento.getId(),elemento.getNomPrenda(), elemento.getPrecio(), elemento.getUrl_imagen());
                                prendaList.add(prendaEntry);
                            }
                            adapter = new PrendaCardRecyclerViewAdapterH(prendaList, getContext());
                            recyclerView.setAdapter(adapter);
                        } else {
                            Toast.makeText(getContext(), "Error en la respuesta de la API", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ObtenerPrendaResp> call, Throwable t) {
                        Toast.makeText(getContext(), "Error en la conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }



    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = requireActivity()
                .getSharedPreferences("user_session", Context.MODE_PRIVATE);
        String username = prefs.getString("username", "Usuario");
        String saludo = "Hola, " + username;
        AppCompatActivity act = (AppCompatActivity) getActivity();
        if (act != null && act.getSupportActionBar() != null) {
            act.getSupportActionBar().setTitle(saludo);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
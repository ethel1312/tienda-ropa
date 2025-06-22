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
import com.example.tienda_ropa.ObtenerCarrito.ProductoCarritoReq;
import com.example.tienda_ropa.databinding.FragmentHomeBinding;
import com.example.tienda_ropa.R;
import com.example.tienda_ropa.model.AgregarListaDeseosReq;
import com.example.tienda_ropa.model.GeneralResp;
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

public class HomeFragment extends Fragment implements FavoritoHandler {

    private ViewPager2 viewPager;
    private View indicator1, indicator2;
    private List<String> imageList;
    private CircleIndicator3 circleIndicator;

    private RecyclerView recyclerView;
    private SharedPreferences sharedPref;
    String token;
    int idUsuario;
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

        sharedPref = requireContext().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        token = sharedPref.getString("token", "");
        idUsuario = sharedPref.getInt("id_usuario", 0);

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

        Call<ObtenerPrendaResp> call = PyAnyApi.obtenerPrendas("JWT " + token);
        call.enqueue(new Callback<ObtenerPrendaResp>() {
            @Override
            public void onResponse(Call<ObtenerPrendaResp> call, Response<ObtenerPrendaResp> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // prendas para las cartitassss
                    List<PrendaApi> listaPrendas = response.body().getData();
                            for (PrendaApi elemento : listaPrendas) {
                                PrendaEntry prendaEntry = new PrendaEntry(elemento.getId_prenda(),elemento.getNomPrenda(), elemento.getPrecio(), elemento.getUrl_imagen(), elemento.isFavorito());
                                prendaList.add(prendaEntry);
                            }
                    adapter = new PrendaCardRecyclerViewAdapterH(prendaList, getContext(), HomeFragment.this);

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

    public void agregarAFavoritos(AgregarListaDeseosReq agregarListaDeseosReq) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PyAnyApi pyAnyApi = retrofit.create(PyAnyApi.class);

        Call<GeneralResp> call = pyAnyApi.agregarListaDeseos("JWT " + token, agregarListaDeseosReq);

        call.enqueue(new Callback<GeneralResp>() {
            @Override
            public void onResponse(Call<GeneralResp> call, Response<GeneralResp> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Error: Código " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GeneralResp> call, Throwable t) {
                Toast.makeText(getContext(), "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
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
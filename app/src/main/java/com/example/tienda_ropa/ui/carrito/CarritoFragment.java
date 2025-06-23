package com.example.tienda_ropa.ui.carrito;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tienda_ropa.Interface.OnCantidadChangeListener;
import com.example.tienda_ropa.Interface.PyAnyApi;
import com.example.tienda_ropa.ObtenerCarrito.CarritoCardRecyclerViewAdapter;
import com.example.tienda_ropa.ObtenerCarrito.CarritoItemDecoration;
import com.example.tienda_ropa.ObtenerCarrito.ProductoCarritoReq;
import com.example.tienda_ropa.R;
import com.example.tienda_ropa.databinding.FragmentCarritoBinding;
import com.example.tienda_ropa.model.CarritoApi;
import com.example.tienda_ropa.model.CarritoEntry;
import com.example.tienda_ropa.model.GeneralResp;
import com.example.tienda_ropa.model.ObtenerCarritoResp;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CarritoFragment extends Fragment implements OnCantidadChangeListener {

    private FragmentCarritoBinding binding;

    SharedPreferences sharedPref;
    MaterialButton mBtnVerificar;
    MaterialTextView txtSubtotal;
    MaterialTextView txtIGV;
    MaterialTextView txtEnvio;
    MaterialTextView txtTotal;
    RecyclerView recyclerView;
    String token;
    int idUsuario;
    private List<CarritoEntry> carritoList;
    private int conteo = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCarritoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sharedPref = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        token = sharedPref.getString("token", "");
        idUsuario = sharedPref.getInt("id_usuario", 0);

        mBtnVerificar = binding.btnVerificar;

        txtSubtotal = binding.txtSubtotal;
        txtIGV = binding.txtIGV;
        txtEnvio = binding.txtEnvio;
        txtTotal = binding.txtTotal;

        recyclerView = binding.recyclerView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        obtenerCarrito();

        return root;
    }

    private void obtenerCarrito() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PyAnyApi dambPyAnyApi = retrofit.create(PyAnyApi.class);

        Call<ObtenerCarritoResp> call = dambPyAnyApi.obtenerCarrito("JWT " + token);

        call.enqueue(new Callback<ObtenerCarritoResp>() {
            @Override
            public void onResponse(Call<ObtenerCarritoResp> call, Response<ObtenerCarritoResp> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Log.e("API_ERROR", "Código: " + response.code() + " - Cuerpo vacío");
                    return;
                }
                ObtenerCarritoResp resp = response.body();
                if (resp.getCode() == 1) {
                    List<CarritoApi> listaCarrito = resp.getData();
                    actualizarInformacionPedido(listaCarrito);

                    carritoList = new ArrayList<>();
                    for (CarritoApi elemento : listaCarrito) {
                        CarritoEntry obj = new CarritoEntry(
                                elemento.getUrl_imagen(),
                                elemento.getNomPrenda(),
                                elemento.getPrecio(),
                                elemento.getCantidad(),
                                elemento.getId_prenda(),
                                elemento.getTalla(),
                                elemento.getStock()
                        );
                        carritoList.add(obj);
                    }

                    CarritoCardRecyclerViewAdapter adapter =
                            new CarritoCardRecyclerViewAdapter(carritoList, CarritoFragment.this);
                    recyclerView.setAdapter(adapter);

                    if (conteo == 0) {
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
                Log.e("API_FAILURE", "Error de red", throwable);
            }
        });
    }

    @Override
    public void onIncrementarCantidad(int idPrenda) {
        // Llamamos a la API para aumentar la cantidad del producto
        aumentarCantPro(idPrenda);

    }

    @Override
    public void onDisminuirCantidad(int idPrenda) {
        disminuirCantPro(idPrenda);
    }

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
        Call<GeneralResp> call=dambPyAnyApi.incrementarCantProduc("JWT " + token,obj);
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
        Call<GeneralResp> call=dambPyAnyApi.disminuirCantProduc("JWT " + token,obj);
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
        Call<GeneralResp> call=dambPyAnyApi.eliminarProduc("JWT " + token,obj);
        call.enqueue(new Callback<GeneralResp>() {
            @Override
            public void onResponse(Call<GeneralResp> call, Response<GeneralResp> response) {
                if(!response.isSuccessful()){
                    return;
                }
                obtenerCarrito();
                GeneralResp resp = response.body();
                Toast miToast= Toast.makeText(requireContext(),resp.getMessage().toString(),Toast.LENGTH_SHORT);
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


        for (CarritoApi item : listaCarrito) {
            try {
                // Sumamos directamente el total ya calculado (precio * cantidad)
                double totalItem = Double.parseDouble(item.getTotal());
                subtotal += totalItem;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        double totalSinIGV = subtotal + costoEnvio;
        double igv = subtotal * 0.18;
        double totalConIGV = totalSinIGV + igv;

        txtSubtotal.setText(String.format("S/. %.2f", subtotal));
        txtEnvio.setText(String.format("S/. %.2f", costoEnvio));
        txtIGV.setText(String.format("S/. %.2f", igv));
        txtTotal.setText(String.format("S/. %.2f", totalConIGV));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

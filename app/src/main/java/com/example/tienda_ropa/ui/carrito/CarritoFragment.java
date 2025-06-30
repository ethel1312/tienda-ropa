package com.example.tienda_ropa.ui.carrito;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tienda_ropa.Interface.OnCantidadChangeListener;
import com.example.tienda_ropa.Interface.PyAnyApi;
import com.example.tienda_ropa.ObtenerCarrito.CarritoCardRecyclerViewAdapter;
import com.example.tienda_ropa.ObtenerCarrito.CarritoItemDecoration;
import com.example.tienda_ropa.ObtenerCarrito.ProductoCarritoReq;
import com.example.tienda_ropa.PagoActivity;
import com.example.tienda_ropa.R;
import com.example.tienda_ropa.databinding.FragmentCarritoBinding;
import com.example.tienda_ropa.model.CarritoApi;
import com.example.tienda_ropa.model.CarritoEntry;
import com.example.tienda_ropa.model.DireccionEliminarRequest;
import com.example.tienda_ropa.model.DireccionesXid;
import com.example.tienda_ropa.model.GeneralResp;
import com.example.tienda_ropa.model.ObtenerCarritoResp;
import com.example.tienda_ropa.model.ObtenerDirecciones;
import com.example.tienda_ropa.model.ParamsUsuario;
import com.example.tienda_ropa.ui.menu_usuario.adapter.DireccionAdapter;
import com.example.tienda_ropa.ui.menu_usuario.adapter.DireccionesAdapterPago;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CarritoFragment extends Fragment implements OnCantidadChangeListener {

    private FragmentCarritoBinding binding;
    SharedPreferences sharedPref;
    MaterialButton mBtnVerificar;
    MaterialTextView txtSubtotal, txtIGV, txtEnvio, txtTotal;
    RecyclerView recyclerView;
    String token;
    int idUsuario;
    private List<CarritoEntry> carritoList;
    private int conteo = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

        mBtnVerificar.setOnClickListener(view -> obtenerYMostrarDirecciones(view.getContext()));
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
        aumentarCantPro(idPrenda);
    }

    @Override
    public void onDisminuirCantidad(int idPrenda) {
        disminuirCantPro(idPrenda);
    }

    public void onEliminarProducto(int idPrenda) {
        eliminarPro(idPrenda);
    }

    public void aumentarCantPro(int idPrenda) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PyAnyApi api = retrofit.create(PyAnyApi.class);
        ProductoCarritoReq obj = new ProductoCarritoReq();
        obj.setId_usuario(idUsuario);
        obj.setId_prenda(idPrenda);
        Call<GeneralResp> call = api.incrementarCantProduc("JWT " + token, obj);
        call.enqueue(new Callback<GeneralResp>() {
            @Override
            public void onResponse(Call<GeneralResp> call, Response<GeneralResp> response) {
                if (response.isSuccessful()) obtenerCarrito();
            }
            @Override
            public void onFailure(Call<GeneralResp> call, Throwable throwable) {}
        });
    }

    public void disminuirCantPro(int idPrenda) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PyAnyApi api = retrofit.create(PyAnyApi.class);
        ProductoCarritoReq obj = new ProductoCarritoReq();
        obj.setId_usuario(idUsuario);
        obj.setId_prenda(idPrenda);
        Call<GeneralResp> call = api.disminuirCantProduc("JWT " + token, obj);
        call.enqueue(new Callback<GeneralResp>() {
            @Override
            public void onResponse(Call<GeneralResp> call, Response<GeneralResp> response) {
                if (response.isSuccessful()) obtenerCarrito();
            }
            @Override
            public void onFailure(Call<GeneralResp> call, Throwable throwable) {}
        });
    }

    public void eliminarPro(int idPrenda) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PyAnyApi api = retrofit.create(PyAnyApi.class);
        ProductoCarritoReq obj = new ProductoCarritoReq();
        obj.setId_usuario(idUsuario);
        obj.setId_prenda(idPrenda);
        Call<GeneralResp> call = api.eliminarProduc("JWT " + token, obj);
        call.enqueue(new Callback<GeneralResp>() {
            @Override
            public void onResponse(Call<GeneralResp> call, Response<GeneralResp> response) {
                if (response.isSuccessful()) {
                    obtenerCarrito();
                    Toast.makeText(requireContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<GeneralResp> call, Throwable throwable) {}
        });
    }

    private void actualizarInformacionPedido(List<CarritoApi> listaCarrito) {
        double subtotal = 0.0;
        double costoEnvio = 10.0;

        for (CarritoApi item : listaCarrito) {
            try {
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

    private void obtenerYMostrarDirecciones(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE);
        int idUsuario = prefs.getInt("id_usuario", -1);

        if (idUsuario == -1) {
            Toast.makeText(context, "ID de usuario no encontrado", Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PyAnyApi pyAnyApi = retrofit.create(PyAnyApi.class);
        ParamsUsuario params = new ParamsUsuario(idUsuario);

        Call<ObtenerDirecciones> call = pyAnyApi.obtenerDireccion("JWT " + token, params);

        call.enqueue(new Callback<ObtenerDirecciones>() {
            @Override
            public void onResponse(Call<ObtenerDirecciones> call, Response<ObtenerDirecciones> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().getData().isEmpty()) {
                    mostrarDialogoDirecciones(context, response.body().getData());
                } else {
                    Toast.makeText(context, "No se encontraron direcciones", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ObtenerDirecciones> call, Throwable t) {
                Toast.makeText(context, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void mostrarDialogoDirecciones(Context context, List<DireccionesXid> direcciones) {
        android.app.Dialog dialog = new android.app.Dialog(context);
        dialog.setContentView(R.layout.dialog_lista_direcciones);

        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerDireccionesModal);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        DireccionesAdapterPago adapter = new DireccionesAdapterPago(context, direcciones, direccion -> {
            Intent intent = new Intent(context, PagoActivity.class);
            intent.putExtra("id_domicilio", direccion.getId_domicilio());
            context.startActivity(intent);
            dialog.dismiss();
        });

        recyclerView.setAdapter(adapter);

        // Swipe para eliminar
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                DireccionesXid direccion = direcciones.get(position);

                // Eliminar de la lista visualmente
                direcciones.remove(position);
                adapter.notifyItemRemoved(position);

                // Eliminar desde API
                eliminarDireccionDesdeAPI(direccion.getId_domicilio(), context);
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY,
                        actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_red_dark))
                        .addActionIcon(R.drawable.ic_delete)
                        .create()
                        .decorate();


                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

        // Botón cancelar
        View btnCancelar = dialog.findViewById(R.id.btnCancelarDirecciones);
        if (btnCancelar != null) {
            btnCancelar.setOnClickListener(v -> dialog.dismiss());
        }

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void eliminarDireccionDesdeAPI(int idDomicilio, Context context) {
        DireccionEliminarRequest request = new DireccionEliminarRequest(idUsuario, idDomicilio);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PyAnyApi api = retrofit.create(PyAnyApi.class);

        Call<GeneralResp> call = api.eliminarDireccion("JWT " + token, request);
        call.enqueue(new Callback<GeneralResp>() {
            @Override
            public void onResponse(Call<GeneralResp> call, Response<GeneralResp> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 1) {
                    Toast.makeText(context, "Dirección eliminada correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error al eliminar dirección", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GeneralResp> call, Throwable t) {
                Toast.makeText(context, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

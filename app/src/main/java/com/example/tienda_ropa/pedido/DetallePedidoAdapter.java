package com.example.tienda_ropa.pedido;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tienda_ropa.ObtenerCarrito.ImageRequester;
import com.example.tienda_ropa.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class DetallePedidoAdapter extends RecyclerView.Adapter<DetallePedidoViewHolder> {

    private final List<DetallePedidoEntry> lista;
    private final Context ctx;
    private ImageRequester imageRequester;

    public DetallePedidoAdapter(Context ctx, List<DetallePedidoEntry> lista) {
        this.ctx   = ctx;
        this.lista = lista;
        imageRequester=ImageRequester.getInstance();
    }

    @NonNull
    @Override
    public DetallePedidoViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_detalle_compra, parent, false);
        return new DetallePedidoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(
            @NonNull DetallePedidoViewHolder h, int position) {

        DetallePedidoEntry it = lista.get(position);

        h.txtNombre .setText(it.getNomPrenda());
        h.txtTalla  .setText("Talla: " + it.getTalla());
        h.txtPrecio .setText(String.format(Locale.US, "S/ %.2f", it.getPrecio()));
        h.txtCantidad.setText("x" + it.getCantidad());

        if (it.getUrlImagen() != null && !it.getUrlImagen().isEmpty()) {
            imageRequester.setImageFromUrl(h.imgItem, it.getUrlImagen());
        } else {
            h.imgItem.setImageResource(R.drawable.placeholder); // opcional
        }
    }

    @Override
    public int getItemCount() { return lista.size(); }

}

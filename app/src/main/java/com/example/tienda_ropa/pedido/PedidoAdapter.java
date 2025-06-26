package com.example.tienda_ropa.pedido;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tienda_ropa.R;

import java.util.List;

/** Adaptador para la lista de pedidos */
public class PedidoAdapter
        extends RecyclerView.Adapter<PedidoViewHolder> {

    private final List<PedidoEntry> lista;

    public PedidoAdapter(List<PedidoEntry> lista) {
        this.lista     = lista;
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_historial_pedidos, parent, false);
        return new PedidoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(
            @NonNull PedidoViewHolder h, int position) {

        PedidoEntry p = lista.get(position);

        h.txtFechaRealizado.setText(
                String.format("Pedido realizado el: %s", p.fecha));

        h.txtCodigoPedido.setText(
                String.format("Pedido: %s", p.codigo));

        h.txtArticulos.setText(
                String.format("Artículos: %d", p.articulos));

        h.txtTotal.setText(
                String.format("Total: S/.%.2f", p.total));
    }

    @Override
    public int getItemCount() { return lista.size(); }
}

package com.example.tienda_ropa.pedido;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tienda_ropa.R;

/** ViewHolder para cada tarjeta de pedido en el RecyclerView */
public class PedidoViewHolder extends RecyclerView.ViewHolder {

    public TextView    txtFechaRealizado;   // “Pedido realizado el…”
    public TextView    txtCodigoPedido;     // Código del pedido
    public TextView    txtArticulos;        // Nº de artículos
    public TextView    txtTotal;            // Total S/.
    public ImageButton btnDetalle;          // Flecha “>” para ver detalle

    public TextView     txtEstado;

    public PedidoViewHolder(@NonNull View itemView) {
        super(itemView);

        txtFechaRealizado = itemView.findViewById(R.id.txtFechaRealizado);
        txtCodigoPedido   = itemView.findViewById(R.id.txtCodigoPedido);
        txtArticulos      = itemView.findViewById(R.id.txtArticulos);
        txtTotal          = itemView.findViewById(R.id.txtTotal);
        btnDetalle        = itemView.findViewById(R.id.btnDetalle);
        txtEstado         = itemView.findViewById(R.id.txtEstado);
    }
}

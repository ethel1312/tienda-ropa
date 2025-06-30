package com.example.tienda_ropa.pedido;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;

import com.example.tienda_ropa.ObtenerCarrito.ImageRequester;
import com.example.tienda_ropa.R;

public class DetallePedidoViewHolder extends RecyclerView.ViewHolder{
    NetworkImageView imgItem;
    TextView txtNombre, txtTalla, txtPrecio, txtCantidad;

    public DetallePedidoViewHolder(@NonNull View itemView) {
        super(itemView);
        imgItem    = itemView.findViewById(R.id.imgItem);
        txtNombre  = itemView.findViewById(R.id.txtNombreItem);
        txtTalla   = itemView.findViewById(R.id.txtTallaItem);
        txtPrecio  = itemView.findViewById(R.id.txtPrecioItem);
        txtCantidad= itemView.findViewById(R.id.txtCantidadItem);
    }
}

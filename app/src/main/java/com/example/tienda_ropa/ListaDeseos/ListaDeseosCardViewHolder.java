package com.example.tienda_ropa.ListaDeseos;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.tienda_ropa.R;
import com.google.android.material.card.MaterialCardView;

public class ListaDeseosCardViewHolder extends RecyclerView.ViewHolder{
    public NetworkImageView productoImage;
    public TextView productoNombre;
    public TextView productoPrecio;

    public int idPrenda;
    public Button btnAgregarCarrito;
    public ImageButton btnEliminar;
    public TextView txtAgotado;
    public MaterialCardView cardItem;

    public ListaDeseosCardViewHolder(@NonNull View itemView) {
        super(itemView);
        productoImage= itemView.findViewById(R.id.imgProductoLD);
        productoNombre= itemView.findViewById(R.id.txtNombreLD);
        productoPrecio= itemView.findViewById(R.id.txtPrecioLD);
        btnAgregarCarrito=itemView.findViewById(R.id.btnAgregarLD);
        btnEliminar=itemView.findViewById(R.id.btnEliminarLD);
        txtAgotado = itemView.findViewById(R.id.txtAgotado);
        cardItem = itemView.findViewById(R.id.cardItem);
    }
}

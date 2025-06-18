package com.example.tienda_ropa.ObtenerCarrito;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.tienda_ropa.R;

public class CarritoCardViewHolder extends RecyclerView.ViewHolder{

    public NetworkImageView productoImage;
    public TextView productoNombre;
    public TextView productoPrecio;
    public TextView productoCantidad;
    public int idPrenda;
    public ImageButton btnSumar;
    public ImageButton btnRestar;
    public ImageButton btnEliminar;


    public CarritoCardViewHolder(@NonNull View itemView) {
        super(itemView);
        productoImage= itemView.findViewById(R.id.imgProducto);
        productoNombre= itemView.findViewById(R.id.txtNombre);
        productoPrecio= itemView.findViewById(R.id.txtPrecio);
        productoCantidad= itemView.findViewById(R.id.txtCantidad);
        btnSumar=itemView.findViewById(R.id.btnSumar);
        btnRestar=itemView.findViewById(R.id.btnResta);
        btnEliminar=itemView.findViewById(R.id.btnEliminar);
    }

}

package com.example.tienda_ropa.ui.home;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.tienda_ropa.R;

public class PrendaCardViewHolder extends RecyclerView.ViewHolder {
    public NetworkImageView prendaImage;
    public TextView prendaNombre;
    public TextView prendaPrecio;
    public ImageButton mBtnFavoritos;

    public PrendaCardViewHolder(@NonNull View itemView) {
        super(itemView);
        prendaImage = itemView.findViewById(R.id.prenda_image);
        prendaNombre = itemView.findViewById(R.id.prenda_nombre);
        prendaPrecio = itemView.findViewById(R.id.prenda_precio);
        mBtnFavoritos = itemView.findViewById(R.id.btnFavorito);
    }
}

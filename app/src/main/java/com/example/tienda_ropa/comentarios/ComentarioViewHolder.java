package com.example.tienda_ropa.comentarios;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import com.example.tienda_ropa.R;
public class ComentarioViewHolder extends RecyclerView.ViewHolder {
    public MaterialRatingBar rating;
    public TextView txtFecha, txtUsuario, txtComentario, badge;

    public ComentarioViewHolder(@NonNull View itemView) {
        super(itemView);
        rating        = itemView.findViewById(R.id.ratingItem);
        txtFecha      = itemView.findViewById(R.id.txtFecha);
        txtUsuario    = itemView.findViewById(R.id.txtUsuario);
        txtComentario = itemView.findViewById(R.id.txtComentario);
        badge         = itemView.findViewById(R.id.badgeVerificado);
    }
}

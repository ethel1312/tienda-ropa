package com.example.tienda_ropa.comentarios;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tienda_ropa.R;

import com.example.tienda_ropa.comentarios.ComentarioViewHolder;

import java.util.List;

public class ComentarioAdapter
        extends RecyclerView.Adapter<ComentarioViewHolder> {

    private final List<ComentarioEntry> lista;

    public ComentarioAdapter(List<ComentarioEntry> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ComentarioViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comentario, parent, false);
        return new ComentarioViewHolder(v);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ComentarioViewHolder h, int position) {

        ComentarioEntry c = lista.get(position);

        h.rating.setRating(c.estrellas);
        h.txtFecha.setText(c.fecha);
        h.txtUsuario.setText(c.usuario);
        h.txtComentario.setText(c.comentario);
        h.badge.setVisibility( c.isVerificada() ? View.VISIBLE : View.GONE );
    }

    @Override public int getItemCount() { return lista.size(); }
}
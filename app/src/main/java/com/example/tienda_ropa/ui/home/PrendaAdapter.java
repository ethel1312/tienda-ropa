package com.example.tienda_ropa.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.tienda_ropa.R;
import com.example.tienda_ropa.model.PrendaApi;

import java.util.List;

public class PrendaAdapter extends RecyclerView.Adapter<PrendaAdapter.PrendaViewHolder> {
    private final List<PrendaApi> lista;
    private final Context context;

    public PrendaAdapter(Context context, List<PrendaApi> lista) {
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public PrendaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.prendas_prenda_card_inicio, parent, false);
        return new PrendaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrendaViewHolder holder, int position) {
        PrendaApi prenda = lista.get(position);
        holder.nombre.setText(prenda.getNomPrenda());
        holder.precio.setText("S/ " + prenda.getPrecio());

        // Aquí usamos tu clase personalizada para cargar la imagen
        ImageRequester.getInstance(context).setImageFromUrl(holder.imagen, prenda.getUrl_imagen());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class PrendaViewHolder extends RecyclerView.ViewHolder {
        NetworkImageView imagen;
        TextView nombre, precio;

        public PrendaViewHolder(@NonNull View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.prenda_image);
            nombre = itemView.findViewById(R.id.prenda_nombre);
            precio = itemView.findViewById(R.id.prenda_precio);
        }
    }
}

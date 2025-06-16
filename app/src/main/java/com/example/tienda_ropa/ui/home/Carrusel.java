package com.example.tienda_ropa.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tienda_ropa.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Carrusel extends RecyclerView.Adapter<Carrusel.ViewHolder> {
    private List<String> imagenesLista;

    public Carrusel(List<String> imagenesLista) {
        this.imagenesLista = imagenesLista;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.imagenes_carrusel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Usa una librería como Picasso para cargar la imagen
        holder.imageView.setImageResource(Integer.parseInt(imagenesLista.get(position)));
    }

    @Override
    public int getItemCount() {
        return imagenesLista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
        }
    }
}

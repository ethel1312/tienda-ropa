package com.example.tienda_ropa.ui.home;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.tienda_ropa.R;

import java.util.List;

public class PrendaCardRecyclerViewAdapterH extends RecyclerView.Adapter<PrendaCardViewHolder> {
    private List<PrendaEntry> prendaList;
    private ImageRequester imageRequester;


    public PrendaCardRecyclerViewAdapterH(List<PrendaEntry> p_prendaList, Context context){
        this.prendaList = p_prendaList;
            imageRequester = ImageRequester.getInstance(context);
    }


    @NonNull
    @Override
    public PrendaCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // return null;
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.prendas_prenda_card_inicio, parent, false);
        return  new PrendaCardViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull PrendaCardViewHolder holder, int position) {
        if (prendaList != null && position < prendaList.size()){
            PrendaEntry prenda = prendaList.get(position);
            holder.prendaNombre.setText(prenda.nombre);
            holder.prendaPrecio.setText(String.valueOf(prenda.precio));
            imageRequester.setImageFromUrl(holder.prendaImage, prenda.url);

            holder.itemView.setOnClickListener(v -> {
                // Obtén el ID de la prenda al hacer clic
                int prendaId = prenda.id;  // O usa el ID de la prenda directamente
                // Crea el Intent para abrir el PrendaDetailActivity
                Toast.makeText(v.getContext(),
                                "ID prenda: " + prendaId,
                                Toast.LENGTH_SHORT)
                        .show();
                Intent intent = new Intent(v.getContext(), DetallesPrenda.class);
                intent.putExtra("prenda_id", prendaId);  // Envía el ID de la prenda seleccionada
                v.getContext().startActivity(intent);  // Inicia la actividad de detalles
            });


        }

    }

    @Override
    public int getItemCount() {
        return prendaList.size();
    }

}

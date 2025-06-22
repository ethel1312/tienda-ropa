package com.example.tienda_ropa.ui.home;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.tienda_ropa.R;
import com.example.tienda_ropa.model.AgregarListaDeseosReq;

import java.util.List;

public class PrendaCardRecyclerViewAdapterH extends RecyclerView.Adapter<PrendaCardViewHolder> {
    private List<PrendaEntry> prendaList;
    private ImageRequester imageRequester;
    private FavoritoHandler favoritoHandler;

    public PrendaCardRecyclerViewAdapterH(List<PrendaEntry> p_prendaList, Context context, FavoritoHandler handler){
        this.prendaList = p_prendaList;
            imageRequester = ImageRequester.getInstance(context);
        this.favoritoHandler = handler;
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

            //MOSTRAR ICONO SEGÚN ESTADO
            if (prenda.isFavorito) {
                holder.mBtnFavoritos.setImageResource(R.drawable.ic_favorito); // corazón lleno
            } else {
                holder.mBtnFavoritos.setImageResource(R.drawable.ic_favorito_borde); // corazón contorno
            }

            //CAMBIAR ESTADO AL HACER CLIC
            holder.mBtnFavoritos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    prenda.isFavorito = !prenda.isFavorito;
                    if(prenda.isFavorito){
                        holder.mBtnFavoritos.setImageResource(R.drawable.ic_favorito);
                        Toast.makeText(v.getContext(), "Agregado a favoritos", Toast.LENGTH_SHORT).show();

                        AgregarListaDeseosReq req = new AgregarListaDeseosReq();
                        req.setId_prenda(String.valueOf(prenda.id));
                        favoritoHandler.agregarAFavoritos(req);
                    } else {
                        holder.mBtnFavoritos.setImageResource(R.drawable.ic_favorito_borde);
                        Toast.makeText(v.getContext(), "Eliminado de favoritos", Toast.LENGTH_SHORT).show();
                        favoritoHandler.eliminarDeFavoritos(prenda.id);
                    }
                }
            });

            //Evento clic en la tarjeta
            holder.itemView.setOnClickListener(v -> {
                // Obtén el ID de la prenda al hacer clic
                int prendaId = prenda.id;  // O usa el ID de la prenda directamente

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

package com.example.tienda_ropa.ListaDeseos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tienda_ropa.Interface.OnCantidadChangeListener;

import com.example.tienda_ropa.Interface.OnListaDeseosListener;
import com.example.tienda_ropa.ListaDeseos.ListaDeseosCardViewHolder;
import com.example.tienda_ropa.ObtenerCarrito.CarritoCardViewHolder;
import com.example.tienda_ropa.ObtenerCarrito.ImageRequester;
import com.example.tienda_ropa.R;

import com.example.tienda_ropa.model.ListaDeseosEntry;

import java.util.List;

public class ListaDeseosCardRecyclerViewAdapter extends RecyclerView.Adapter<ListaDeseosCardViewHolder>{
    private List<ListaDeseosEntry> listaDeseosList;
    private ImageRequester imageRequester;
    private OnListaDeseosListener listener;

    public ListaDeseosCardRecyclerViewAdapter(List<ListaDeseosEntry> p_discoList, OnListaDeseosListener listener){
        this.listaDeseosList=p_discoList;
        this.listener = listener;  // Guardamos la referencia de la interfaz
        imageRequester=ImageRequester.getInstance();
    }

    @NonNull
    @Override
    public ListaDeseosCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //return null;
        View layoutView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto_lista_deseos, parent,false);
        return new ListaDeseosCardViewHolder(layoutView);
    }


    @Override
    public void onBindViewHolder(@NonNull ListaDeseosCardViewHolder holder, int position) {
        //Todo: Pendiente
        if(listaDeseosList !=null && position < listaDeseosList.size()){
            ListaDeseosEntry producto =listaDeseosList.get(position);
            holder.productoNombre.setText(producto.nomPrenda);
            holder.productoPrecio.setText("S/. "+producto.precio);
            imageRequester.setImageFromUrl(holder.productoImage,producto.url_imagen);
            holder.idPrenda = producto.idPrenda;

            //CREAR FUNCIONES PARA BOTONES CON APIS
           holder.btnAgregarCarrito.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAgregarCarrito(holder.idPrenda);  // Notificar a la actividad
                }
            });
            holder.btnEliminar.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEliminarProducto(holder.idPrenda);  // Notificar a la actividad
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listaDeseosList.size();
    }

    // Método para limpiar la lista de productos (vaciar el RecyclerView)
    public void clear() {
        listaDeseosList.clear();
        notifyDataSetChanged();  // Notificamos que el RecyclerView ha sido limpiado
    }
}

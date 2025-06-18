package com.example.tienda_ropa.ObtenerCarrito;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tienda_ropa.CarritoActivity;
import com.example.tienda_ropa.Interface.OnCantidadChangeListener;
import com.example.tienda_ropa.R;
import com.example.tienda_ropa.model.CarritoEntry;

import java.util.List;

public class CarritoCardRecyclerViewAdapter extends RecyclerView.Adapter<CarritoCardViewHolder>{
    private List<CarritoEntry> carritoList;
    private ImageRequester imageRequester;

    private OnCantidadChangeListener listener;

    public CarritoCardRecyclerViewAdapter(List<CarritoEntry> p_discoList, OnCantidadChangeListener listener){
        this.carritoList=p_discoList;
        this.listener = listener;  // Guardamos la referencia de la interfaz
        imageRequester=ImageRequester.getInstance();
    }

    @NonNull
    @Override
    public CarritoCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //return null;
        View layoutView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto_carrito, parent,false);
        return new CarritoCardViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull CarritoCardViewHolder holder, int position) {
        //Todo: Pendiente
        if(carritoList !=null && position < carritoList.size()){
            CarritoEntry producto =carritoList.get(position);
            holder.productoNombre.setText(producto.nomPrenda);
            holder.productoPrecio.setText("S/. "+producto.precio);
            holder.productoCantidad.setText(String.valueOf(producto.cantidad));
            imageRequester.setImageFromUrl(holder.productoImage,producto.url_imagen);
            holder.idPrenda = producto.idPrenda;

            //CREAR FUNCIONES PARA BOTONES CON APIS
            holder.btnSumar.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onIncrementarCantidad(holder.idPrenda);  // Notificar a la actividad
                }
            });
            holder.btnRestar.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDisminuirCantidad(holder.idPrenda);  // Notificar a la actividad
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
        return carritoList.size();
    }

    // Método para limpiar la lista de productos (vaciar el RecyclerView)
    public void clear() {
        carritoList.clear();
        notifyDataSetChanged();  // Notificamos que el RecyclerView ha sido limpiado
    }
}

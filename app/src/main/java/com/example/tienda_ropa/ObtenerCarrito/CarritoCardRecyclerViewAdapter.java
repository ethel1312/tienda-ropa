package com.example.tienda_ropa.ObtenerCarrito;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tienda_ropa.CarritoActivity;
import com.example.tienda_ropa.Interface.OnCantidadChangeListener;
import com.example.tienda_ropa.R;
import com.example.tienda_ropa.model.CarritoEntry;
import com.example.tienda_ropa.ui.home.DetallesPrenda;

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
            holder.productoTalla.setText("Talla: " + producto.talla);
            holder.productoPrecio.setText("S/. "+producto.precio);
            holder.productoCantidad.setText(String.valueOf(producto.cantidad));
            imageRequester.setImageFromUrl(holder.productoImage,producto.url_imagen);
            holder.idPrenda = producto.idPrenda;

            if (producto.stock == 0) {
                holder.txtAgotado.setVisibility(View.VISIBLE);
                holder.cardItem.setAlpha(0.4f);
                holder.cardItem.setEnabled(false);
            } else {
                holder.txtAgotado.setVisibility(View.GONE);
                holder.cardItem.setAlpha(1f);
                holder.cardItem.setEnabled(true);
            }

            //CREAR FUNCIONES PARA BOTONES CON APIS
            // Cambiar el estado del botón según el stock
            boolean puedeIncrementar = producto.cantidad < producto.stock;
            holder.btnSumar.setEnabled(puedeIncrementar);
            holder.btnSumar.setAlpha(puedeIncrementar ? 1.0f : 0.4f);

            holder.btnSumar.setOnClickListener(v -> {
                if (listener != null) {
                    if (producto.cantidad < producto.stock) {
                        listener.onIncrementarCantidad(holder.idPrenda);
                    } else {
                        Toast.makeText(holder.itemView.getContext(),
                                "Stock máximo alcanzado para talla " + producto.talla,
                                Toast.LENGTH_SHORT).show();
                    }
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

            //Evento clic en la tarjeta
            holder.itemView.setOnClickListener(v -> {
                // Obtén el ID de la prenda al hacer clic
                int prendaId = producto.idPrenda;  // O usa el ID de la prenda directamente

                Intent intent = new Intent(v.getContext(), DetallesPrenda.class);
                intent.putExtra("prenda_id", prendaId);  // Envía el ID de la prenda seleccionada
                v.getContext().startActivity(intent);  // Inicia la actividad de detalles
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

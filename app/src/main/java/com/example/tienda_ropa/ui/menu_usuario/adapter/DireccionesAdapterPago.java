package com.example.tienda_ropa.ui.menu_usuario.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tienda_ropa.R;
import com.example.tienda_ropa.model.DireccionesXid;

import java.util.List;

public class DireccionesAdapterPago extends RecyclerView.Adapter<DireccionesAdapterPago.DireccionViewHolder> {

    private Context context;
    private List<DireccionesXid> listaDirecciones;
    private OnDireccionClickListener listener;

    public interface OnDireccionClickListener {
        void onDireccionSeleccionada(DireccionesXid direccion);
    }

    public DireccionesAdapterPago(Context context, List<DireccionesXid> listaDirecciones, OnDireccionClickListener listener) {
        this.context = context;
        this.listaDirecciones = listaDirecciones;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DireccionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.direcciones_direccion_card, parent, false);

        // ✅ Forzar que el ítem ocupe todo el ancho del RecyclerView
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        view.setLayoutParams(layoutParams);

        return new DireccionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DireccionViewHolder holder, int position) {
        DireccionesXid direccion = listaDirecciones.get(position);
        holder.tvAddressName.setText(direccion.getCalle());
        holder.tvAddressDetails.setText(direccion.getInfo_adicional());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDireccionSeleccionada(direccion);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaDirecciones.size();
    }

    public static class DireccionViewHolder extends RecyclerView.ViewHolder {
        TextView tvAddressName, tvAddressDetails;

        public DireccionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddressName = itemView.findViewById(R.id.tvAddressName);
            tvAddressDetails = itemView.findViewById(R.id.tvAddressDetails);
        }
    }
}

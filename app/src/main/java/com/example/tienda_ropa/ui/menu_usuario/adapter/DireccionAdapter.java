package com.example.tienda_ropa.ui.menu_usuario.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tienda_ropa.R;
import com.example.tienda_ropa.model.DireccionesXid;

import java.util.List;

public class DireccionAdapter extends RecyclerView.Adapter<DireccionAdapter.DireccionViewHolder> {

    private Context context;
    private List<DireccionesXid> listaDirecciones;
    private OnDireccionClickListener listener;

    public interface OnDireccionClickListener {
        void onDireccionSeleccionada(DireccionesXid direccion);
    }


    public DireccionAdapter(Context context, List<DireccionesXid> listaDirecciones) {
        this.context = context;
        this.listaDirecciones = listaDirecciones;
    }


    @NonNull
    @Override
    public DireccionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.direcciones_direccion_card, parent, false);
        return new DireccionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DireccionViewHolder holder, int position) {
        DireccionesXid direccion = listaDirecciones.get(position);

        holder.tvAddressName.setText(direccion.getCalle());
        holder.tvAddressDetails.setText(direccion.getInfo_adicional());

        //holder.btnEditAddress.setOnClickListener(v -> listener.onEditarClick(direccion));
    }

    @Override
    public int getItemCount() {
        return listaDirecciones.size();
    }

    public static class DireccionViewHolder extends RecyclerView.ViewHolder {

        TextView tvAddressName, tvAddressDetails;
        ImageButton btnEditAddress;

        public DireccionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddressName = itemView.findViewById(R.id.tvAddressName);
            tvAddressDetails = itemView.findViewById(R.id.tvAddressDetails);
            //btnEditAddress = itemView.findViewById(R.id.btnEditAddress);
        }
    }
}

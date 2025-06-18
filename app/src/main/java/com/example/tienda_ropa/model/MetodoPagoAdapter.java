// com/example/tienda_ropa/model/MetodoPagoAdapter.java
package com.example.tienda_ropa.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tienda_ropa.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MetodoPagoAdapter extends RecyclerView.Adapter<MetodoPagoAdapter.MetodoPagoViewHolder> {

    private List<MetodoPago> listaMetodosPago;
    private Context context;
    private OnDeleteClickListener deleteListener;

    /** Callback para clicks en “eliminar” */
    public interface OnDeleteClickListener {
        void onDelete(int idMetodoPago);
    }


    public MetodoPagoAdapter(List<MetodoPago> listaMetodosPago,
                             Context context,
                             OnDeleteClickListener deleteListener) {
        this.context        = context;
        this.deleteListener = deleteListener;

        // Ordena para que es_principal==1 quede primero
        Collections.sort(listaMetodosPago, new Comparator<MetodoPago>() {
            @Override
            public int compare(MetodoPago a, MetodoPago b) {
                return Integer.compare(b.getEs_principal(), a.getEs_principal());
            }
        });

        this.listaMetodosPago = listaMetodosPago;
    }

    @Override
    public MetodoPagoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.fragment_tarjetas_pago, parent, false);
        return new MetodoPagoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MetodoPagoViewHolder holder, int position) {
        MetodoPago m = listaMetodosPago.get(position);

        holder.titular.setText(m.getTitular());
        holder.numeroTarjeta.setText(m.getNumero_enmascarado());
        holder.fechaVencimiento.setText(m.getFecha_vencimiento());

        holder.btnEliminar.setOnClickListener(v ->
                deleteListener.onDelete(m.getId_metodo_pago())
        );
    }

    @Override
    public int getItemCount() {
        return listaMetodosPago.size();
    }

    /** ViewHolder que contiene cada tarjeta */
    public static class MetodoPagoViewHolder extends RecyclerView.ViewHolder {
        TextView titular, numeroTarjeta, fechaVencimiento;
        ImageButton btnEliminar;

        public MetodoPagoViewHolder(View itemView) {
            super(itemView);
            titular          = itemView.findViewById(R.id.tvTitular);
            numeroTarjeta    = itemView.findViewById(R.id.tvNumeroTarjeta);
            fechaVencimiento = itemView.findViewById(R.id.tvFechaVencimiento);
            btnEliminar      = itemView.findViewById(R.id.btnEliminarTarjetaDeHistorial);
        }
    }
}

package com.zamineperu.wcastillo.zapcodmobile.Adapters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zamineperu.wcastillo.zapcodmobile.Model.OrdenServicio;
import com.zamineperu.wcastillo.zapcodmobile.Modules.Servicios.OrdenServicioDetailFragment;
import com.zamineperu.wcastillo.zapcodmobile.R;

import java.util.ArrayList;

public class OrdenServicioAdapter extends RecyclerView.Adapter<OrdenServicioAdapter.ViewHolder> {

    private ArrayList<OrdenServicio> list;

    public OrdenServicioAdapter(ArrayList<OrdenServicio> list){
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CardView cardView = (CardView) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_service_os,viewGroup,false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final CardView cardView = viewHolder.cardView;
        final OrdenServicio ordenServicio = list.get(i);

        viewHolder.tvId.setText(ordenServicio.getId());
        viewHolder.tvCodigo.setText(ordenServicio.getCodigo());
        viewHolder.tvEquipo.setText(ordenServicio.getEquipo());
        viewHolder.tvFechaInicio.setText(ordenServicio.getFecha_inicio());
        viewHolder.tvDescripcion.setText(ordenServicio.getDescripcion());

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tvId = v.findViewById(R.id.cv_service_os_id);
                String id = tvId.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("orden_servicio_id",id);

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                OrdenServicioDetailFragment fragment = new OrdenServicioDetailFragment();
                fragment.setArguments(bundle);

                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame,fragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvId;
        public TextView tvCodigo;
        public TextView tvEquipo;
        public TextView tvFechaInicio;
        public TextView tvDescripcion;
        CardView cardView;

        public ViewHolder(final CardView itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.cv_service_os_id);
            tvCodigo = itemView.findViewById(R.id.cv_service_os_codigo);
            tvEquipo = itemView.findViewById(R.id.cv_service_os_cotizacion_cliente);
            tvFechaInicio = itemView.findViewById(R.id.cv_service_os_fecha_inicio);
            tvDescripcion = itemView.findViewById(R.id.cv_service_os_descripcion);
            cardView = itemView;
        }
    }
}
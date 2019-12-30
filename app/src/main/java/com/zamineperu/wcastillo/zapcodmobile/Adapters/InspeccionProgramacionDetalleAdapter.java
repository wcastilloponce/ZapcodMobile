package com.zamineperu.wcastillo.zapcodmobile.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zamineperu.wcastillo.zapcodmobile.Modules.Ssoma.ResponseClass.InspeccionPlanificadaResponse;
import com.zamineperu.wcastillo.zapcodmobile.R;

import java.util.ArrayList;

public class InspeccionProgramacionDetalleAdapter extends RecyclerView.Adapter<InspeccionProgramacionDetalleAdapter.ViewHolder>{

    private ArrayList<InspeccionPlanificadaResponse> list;

    public InspeccionProgramacionDetalleAdapter(ArrayList<InspeccionPlanificadaResponse> list){
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CardView cardView = (CardView) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_inspection_programation_detail,viewGroup,false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final CardView cardView = viewHolder.cardView;
        final InspeccionPlanificadaResponse response = list.get(i);

//        viewHolder.tvId.setText(ordenServicio.getId());
        viewHolder.tvInspection.setText(response.getInspeccion().getDescripcion());
        viewHolder.tvLocation.setText(response.getUbicacion().getDescripcion());
        viewHolder.tvStartDate.setText(response.getProgramacionDetalle().getFecha_inicio());
        viewHolder.tvResponsable.setText(response.getResponsable().toString());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

//        public TextView tvId;
        public TextView tvInspection;
        public TextView tvLocation;
        public TextView tvStartDate;
        public TextView tvResponsable;
        CardView cardView;

        public ViewHolder(final CardView itemView) {
            super(itemView);
//            tvId = itemView.findViewById(R.id.cv_service_os_id);
            tvInspection = itemView.findViewById(R.id.cv_inspection);
            tvLocation = itemView.findViewById(R.id.cv_inspection_location);
            tvStartDate = itemView.findViewById(R.id.cv_inspection_start);
            tvResponsable = itemView.findViewById(R.id.cv_inspection_responsable);
            cardView = itemView;
        }
    }

}

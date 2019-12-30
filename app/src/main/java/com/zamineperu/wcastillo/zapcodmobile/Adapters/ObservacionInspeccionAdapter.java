package com.zamineperu.wcastillo.zapcodmobile.Adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zamineperu.wcastillo.zapcodmobile.Model.Observacion;
import com.zamineperu.wcastillo.zapcodmobile.R;

import java.util.ArrayList;

public class ObservacionInspeccionAdapter extends RecyclerView.Adapter<ObservacionInspeccionAdapter.ViewHolder> {

    private ArrayList<String> list;

    public ObservacionInspeccionAdapter(ArrayList<String> list){
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CardView cardView = (CardView) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_observacion_inspeccion,viewGroup,false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final String jsonObservacion = list.get(i);
        final Observacion observacion = new Gson().fromJson(jsonObservacion, Observacion.class);

        viewHolder.tvDescripcion.setText(observacion.getDescripcion());
        viewHolder.tvFechaLimite.setText(observacion.getFecha_limite_subsanacion());

        final ViewTreeObserver observer = viewHolder.ivImagenObservacion.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                viewHolder.ivImagenObservacion.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                setImage(viewHolder, observacion.getPathImage(), observacion.getUriPath());

            }
        });
    }

    private void setImage(ViewHolder viewHolder, String pathPhoto, String uri)
    {
        Bitmap bitmap;
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        int targetW = viewHolder.ivImagenObservacion.getMeasuredWidth();
        int targetH = viewHolder.ivImagenObservacion.getMeasuredHeight();

        BitmapFactory.Options bmpOptions = new BitmapFactory.Options();
        bmpOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathPhoto, bmpOptions);

        int photoW = bmpOptions.outWidth;
        int photoH = bmpOptions.outHeight;

        if(uri == null) {//Camara
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
            bmpOptions.inJustDecodeBounds = false;
            bmpOptions.inSampleSize = scaleFactor;
            bitmap = BitmapFactory.decodeFile(pathPhoto, bmpOptions);
        }
        else{//Galeria
            try{
                bitmap = MediaStore.Images.Media.getBitmap(viewHolder.tvFechaLimite.getContext().getContentResolver(), Uri.parse(uri));
            }
            catch(Exception e){
                bitmap = null;
                e.printStackTrace();
            }
        }


//        Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
//                matrix, true);
        viewHolder.ivImagenObservacion.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvDescripcion;
        public TextView tvFechaLimite;
        public ImageView ivImagenObservacion;
        CardView cardView;

        public ViewHolder(final CardView itemView) {
            super(itemView);
            tvDescripcion = itemView.findViewById(R.id.cv_observacion_descripcion);
            tvFechaLimite = itemView.findViewById(R.id.cv_observacion_fecha_limite);
            ivImagenObservacion = itemView.findViewById(R.id.cv_observacion_image);
            cardView = itemView;
        }
    }
}
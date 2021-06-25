package com.example.mpmanage.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mpmanage.Model.CaSi;
import com.example.mpmanage.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SongSingerAdapter extends RecyclerView.Adapter<SongSingerAdapter.ViewHolder> {
    Context context;
    ArrayList<CaSi> arrayList;

    public SongSingerAdapter(Context context, ArrayList<CaSi> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_baihat_casi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CaSi caSi = arrayList.get(position);
        holder.tvTenCaSi.setText(caSi.getTenCaSi());
        Picasso.with(context).load(caSi.getHinhCaSi().toString()).into(holder.imgCaSi);
        holder.btnDelete.setOnClickListener(v->{

        });
    }

    @Override
    public int getItemCount() {
        if(arrayList != null)
            return arrayList.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView imgCaSi;
        TextView tvTenCaSi;
        ImageView btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCaSi = itemView.findViewById(R.id.img_song_singer);
            tvTenCaSi = itemView.findViewById(R.id.tv_song_singer);
            btnDelete = itemView.findViewById(R.id.btn_delete_song_singer);
        }
    }
}

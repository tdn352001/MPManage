package com.example.mpmanage.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mpmanage.Activity.UpdateSongActivity;
import com.example.mpmanage.Model.BaiHat;
import com.example.mpmanage.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SongSingerAdapter extends RecyclerView.Adapter<SongSingerAdapter.ViewHolder> {

    Context context;
    ArrayList<BaiHat> arrayList;


    public SongSingerAdapter(Context context, ArrayList<BaiHat> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_song_singer, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BaiHat baiHat = arrayList.get(position);
        Picasso.with(context).load(baiHat.getHinhBaiHat()).error(R.drawable.song).into(holder.Avatar);
        holder.txtBaiHat.setText(baiHat.getTenBaiHat());
        holder.txtCaSi.setText(baiHat.getTenAllCaSi());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdateSongActivity.class);
            intent.putExtra("casifragment", 1);
            intent.putExtra("baihat", baiHat);
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView Avatar;
        TextView txtCaSi, txtBaiHat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Avatar = itemView.findViewById(R.id.img_baihat);
            txtCaSi = itemView.findViewById(R.id.txt_tencasi);
            txtBaiHat = itemView.findViewById(R.id.txt_tenbaihat);
        }
    }


}
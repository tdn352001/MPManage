package com.example.mpmanage.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mpmanage.Activity.UpdateAlbumActivity;
import com.example.mpmanage.Model.Album;
import com.example.mpmanage.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AlbumSingerAdaper extends RecyclerView.Adapter<AlbumSingerAdaper.ViewHolder> {

    Context context;
    ArrayList<Album> arrayList;

    public AlbumSingerAdaper(Context context, ArrayList<Album> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        android.view.View view = inflater.inflate(R.layout.dong_album_casi, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Album album = arrayList.get(position);
        Picasso.with(context).load(album.getHinhAlbum()).error(R.drawable.song).into(holder.Avatar);
        holder.txtBaiHat.setText(album.getTenAlbum());
        holder.txtCaSi.setText(album.getTenCaSi());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdateAlbumActivity.class);
            intent.putExtra("album", album);
            intent.putExtra("casifragment", 1);
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
        ImageView btnDelete;
        TextView txtCaSi, txtBaiHat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Avatar = itemView.findViewById(R.id.img_album);
            btnDelete = itemView.findViewById(R.id.img_delete_album);
            txtCaSi = itemView.findViewById(R.id.txt_tencasi);
            txtBaiHat = itemView.findViewById(R.id.txt_tenalbum);
        }
    }




}

package com.example.mpmanage.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mpmanage.Activity.UpdateAlbumActivity;
import com.example.mpmanage.Fragment.MainFragment.AlbumFragment;
import com.example.mpmanage.Model.Album;
import com.example.mpmanage.R;
import com.example.mpmanage.Service.APIService;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> implements Filterable {

    Context context;
    ArrayList<Album> arrayList;
    ArrayList<Album> mArrayList;
    int itemchange;

    public AlbumAdapter(Context context, ArrayList<Album> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        mArrayList = arrayList;
        itemchange = -1;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        android.view.View view = inflater.inflate(R.layout.dong_album, parent, false);
        return new ViewHolder(view);
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Album album = arrayList.get(position);
        if (itemchange == position)
            Glide.with(context).load(album.getHinhAlbum()).error(R.drawable.song).into(holder.Avatar);
        else {
            Glide.with(context).load(album.getHinhAlbum()).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).error(R.drawable.song).into(holder.Avatar);
            itemchange = -1;
        }
        holder.txtBaiHat.setText(album.getTenAlbum());
        holder.txtCaSi.setText(album.getTenCaSi());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdateAlbumActivity.class);
            intent.putExtra("album", album);
            context.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(v -> {
            MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(context);
            dialog.setBackground(context.getResources().getDrawable(R.drawable.custom_diaglog_background));
            dialog.setTitle("Thoát");
            dialog.setIcon(R.drawable.ic_exit);
            dialog.setMessage("Bạn có chắc muốn xóa mục này?");
            dialog.setNegativeButton("Đồng Ý", (dialog1, which) -> {
                Call<String> callback = APIService.getService().DeleteAlbum(album.getIdAlbum());
                callback.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        AlbumFragment.DeleteAlbum(album);
                        Toast.makeText(context, "Cập Nhật Thành Công", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(context, "Cập Nhật Thất Bại", Toast.LENGTH_SHORT).show();
                    }
                });

            });

            dialog.setPositiveButton("Hủy", (dialog12, which) -> dialog12.dismiss());
            dialog.show();
        });
    }


    public void setItemchange(int itemchange) {
        this.itemchange = itemchange;
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


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = (String) constraint;
                if (query.equals("")) {
                    {
                        arrayList = mArrayList;
                    }
                } else {
                    List<Album> albums = new ArrayList<>();
                    for (Album baiHat : mArrayList) {
                        if (baiHat.getTenAlbum().toLowerCase().contains(query.toLowerCase())) {
                            albums.add(baiHat);
                        }
                    }
                    arrayList = (ArrayList<Album>) albums;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = arrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                arrayList = (ArrayList<Album>) results.values;
                notifyDataSetChanged();
            }
        };
    }

}

package com.example.mpmanage.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mpmanage.Activity.UpdatePlaylistActivity;
import com.example.mpmanage.Fragment.MainFragment.PlaylistFragment;
import com.example.mpmanage.Model.Playlist;
import com.example.mpmanage.R;
import com.example.mpmanage.Service.APIService;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> implements Filterable {
    Context context;
    ArrayList<Playlist> arrayList;
    ArrayList<Playlist> mArrayList;
    int itemchange;

    public PlaylistAdapter(Context context, ArrayList<Playlist> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        mArrayList = arrayList;
        itemchange = -1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_playlist, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Playlist playlist = arrayList.get(position);
        if(itemchange == position){
            Glide.with(context).load(playlist.getHinhAnh())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(holder.imageView);
            itemchange = -1;
        }
        Glide.with(context).load(playlist.getHinhAnh()).into(holder.imageView);
        holder.tvTitle.setText(playlist.getTen());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdatePlaylistActivity.class);
            intent.putExtra("playlist", playlist);
            context.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(v -> {
            MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(context);
            dialog.setBackground(context.getResources().getDrawable(R.drawable.custom_diaglog_background));
            dialog.setTitle("Thoát");
            dialog.setIcon(R.drawable.ic_delete);
            dialog.setMessage("Bạn Có Chắc muốn xóa Mục này?");
            dialog.setNegativeButton("Đồng Ý", (dialog1, which) -> {
                Call<String> call = APIService.getService().DeletePlaylist(playlist.getIdPlaylist());
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        PlaylistFragment.DeletePlaylist(playlist);
                        Toast.makeText(context, "Cập Nhật THành Công", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

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
        if (arrayList != null)
            return arrayList.size();
        return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView imageView;
        TextView tvTitle;
        ImageView btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_playlist);
            tvTitle = itemView.findViewById(R.id.tv_playlist);
            btnDelete = itemView.findViewById(R.id.btn_delete_playlist);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = (String) constraint;
                if (query.equals("")) {
                    arrayList = mArrayList;
                } else {
                    List<Playlist> playlists = new ArrayList<>();
                    for (Playlist playlist : mArrayList) {
                        if (playlist.getTen().toLowerCase().contains(query.toLowerCase()))
                            playlists.add(playlist);
                    }

                    arrayList = (ArrayList<Playlist>) playlists;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = arrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                arrayList = (ArrayList<Playlist>) results.values;
                notifyDataSetChanged();
            }
        };
    }


}

package com.example.mpmanage.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mpmanage.Activity.UpdatePlaylistActivity;
import com.example.mpmanage.Model.BaiHat;
import com.example.mpmanage.Model.Playlist;
import com.example.mpmanage.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> implements Filterable {
    Context context;
    ArrayList<Playlist> arrayList;
    ArrayList<Playlist> mArrayList;

    public PlaylistAdapter(Context context, ArrayList<Playlist> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Playlist playlist = arrayList.get(position);
        Glide.with(context).load(playlist.getHinhAnh().toString()).into(holder.imageView);
        holder.tvTitle.setText(playlist.getTen());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdatePlaylistActivity.class);
            intent.putExtra("playlist", playlist);
            context.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(context);
                dialog.setBackground(context.getResources().getDrawable(R.drawable.custom_diaglog_background));
                dialog.setTitle("Thoát");
                dialog.setIcon(R.drawable.ic_delete);
                dialog.setMessage("Bạn Có Chắc muốn xóa Mục này?");
                dialog.setNegativeButton("Đồng Ý", (dialog1, which) -> {


                });

                dialog.setPositiveButton("Hủy", (dialog12, which) -> dialog12.dismiss());
                dialog.show();
            }
        });
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
            imageView = itemView.findViewById(R.id.img_category);
            tvTitle = itemView.findViewById(R.id.tv_category);
            btnDelete = itemView.findViewById(R.id.btn_delete_category);
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

package com.example.mpmanage.Adapter;


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
import com.example.mpmanage.Activity.UpdateAlbumActivity;
import com.example.mpmanage.Model.BaiHat;
import com.example.mpmanage.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class SongAlbumAdapter extends RecyclerView.Adapter<SongAlbumAdapter.ViewHolder> implements Filterable {
    UpdateAlbumActivity context;
    ArrayList<BaiHat> arrayList;
    ArrayList<BaiHat> mArrayList;
    boolean isDialog;

    public SongAlbumAdapter(UpdateAlbumActivity context, ArrayList<BaiHat> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        mArrayList = arrayList;
        isDialog = false;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_add_song_playlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BaiHat baiHat = arrayList.get(position);
        holder.tvTenCaSi.setText(baiHat.getTenAllCaSi());
        holder.tvTenBaiHat.setText(baiHat.getTenBaiHat());
        Glide.with(context).load(baiHat.getHinhBaiHat()).into(holder.imgBaihat);

        if(context.CheckAddBefore(baiHat)){
            holder.imageView.setImageResource(R.drawable.ic_clear);
        }else
            holder.imageView.setImageResource(R.drawable.ic_add);

        if(isDialog){
            holder.itemView.setOnClickListener(v -> {
                if(context.CheckAddBefore(baiHat)){
                    context.DeleteSong(baiHat);
                    holder.imageView.setImageResource(R.drawable.ic_add);
                    Toast.makeText(context, "Đã Xóa" + baiHat.getTenBaiHat(), Toast.LENGTH_SHORT).show();
                }else{
                    context.AddSong(baiHat);
                    holder.imageView.setImageResource(R.drawable.ic_clear);
                    Toast.makeText(context, "Đã Thêm " + baiHat.getTenBaiHat(), Toast.LENGTH_SHORT).show();
                }
            });

        }else{
            holder.imageView.setOnClickListener(v -> {
                context.DeleteSong(baiHat);
            });
        }

    }

    @Override
    public int getItemCount() {
        if (arrayList != null)
            return arrayList.size();
        return 0;
    }

    public boolean isDialog() {
        return isDialog;
    }

    public void setDialog(boolean dialog) {
        isDialog = dialog;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView imgBaihat;
        TextView tvTenCaSi;
        TextView tvTenBaiHat;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBaihat = itemView.findViewById(R.id.img_baihat_playlist);
            tvTenBaiHat = itemView.findViewById(R.id.txt_tenbaihat_playlist);
            tvTenCaSi = itemView.findViewById(R.id.txt_tencasi_playlist);
            imageView = itemView.findViewById(R.id.img_delete_baihat_playlist);

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
                    List<BaiHat> caSis = new ArrayList<>();
                    for (BaiHat caSi : mArrayList) {
                        if (caSi.getTenBaiHat().toString().toLowerCase().contains(query.toLowerCase())) {
                            caSis.add(caSi);
                        }
                    }
                    arrayList = (ArrayList<BaiHat>) caSis;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = arrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                arrayList = (ArrayList<BaiHat>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}

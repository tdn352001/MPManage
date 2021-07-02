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
import com.example.mpmanage.Activity.UpdateSongActivity;
import com.example.mpmanage.Model.CaSi;
import com.example.mpmanage.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class UpdateSongSingerAdapter extends RecyclerView.Adapter<UpdateSongSingerAdapter.ViewHolder> implements Filterable {
    UpdateSongActivity context;
    ArrayList<CaSi> arrayList;
    ArrayList<CaSi> mArrayList;
    boolean isDialog;

    public UpdateSongSingerAdapter(UpdateSongActivity context, ArrayList<CaSi> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        mArrayList = arrayList;
        isDialog = false;
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
        Glide.with(context).load(caSi.getHinhCaSi()).into(holder.imgCaSi);
        if (!isDialog) {
            holder.btnDelete.setOnClickListener(v -> {
                if (arrayList.size() == 1) {
                    Toast.makeText(context, "Phải Có Ít Nhất Một Ca Sĩ", Toast.LENGTH_SHORT).show();
                } else {
                    for(int i = 0; i < context.caSiArrayList.size(); i++){
                        if(context.caSiArrayList.get(i).getIdCaSi().equals(caSi.getIdCaSi())){
                            context.caSiArrayList.remove(i);
                            context.updateSongSingerAdapter.notifyItemRemoved(i);
                            break;
                        }
                    }

                }
            });
        } else {
            if (!context.caSiArrayList.contains(caSi))
                holder.btnDelete.setImageResource(R.drawable.ic_add);

            holder.itemView.setOnClickListener(v -> {
                if (context.caSiArrayList.contains(caSi)) {
                    Toast.makeText(context, "Đã Thêm Trước Đó", Toast.LENGTH_SHORT).show();
                } else {
                    context.caSiArrayList.add(caSi);
                    context.updateSongSingerAdapter.notifyItemInserted(context.caSiArrayList.size());
                    holder.btnDelete.setImageResource(R.drawable.ic_clear);
                    Toast.makeText(context, "Đã Thêm", Toast.LENGTH_SHORT).show();
                }
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
                    List<CaSi> caSis = new ArrayList<>();
                    for (CaSi caSi : mArrayList) {
                        if (caSi.getTenCaSi().toString().toLowerCase().contains(query.toLowerCase())) {
                            caSis.add(caSi);
                        }
                    }
                    arrayList = (ArrayList<CaSi>) caSis;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = arrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                arrayList = (ArrayList<CaSi>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}

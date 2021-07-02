package com.example.mpmanage.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mpmanage.Activity.UpdateAlbumActivity;
import com.example.mpmanage.Model.CaSi;
import com.example.mpmanage.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class SingerAlbumAdapter extends RecyclerView.Adapter<SingerAlbumAdapter.ViewHolder> implements Filterable {
    UpdateAlbumActivity context;
    ArrayList<CaSi> arrayList;
    ArrayList<CaSi> mArrayList;
    boolean isDialog;

    public SingerAlbumAdapter(UpdateAlbumActivity context, ArrayList<CaSi> arrayList) {
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
        Glide.with(context).load(caSi.getHinhCaSi()).error(R.drawable.song).into(holder.imgCaSi);

        holder.itemView.setOnClickListener(v -> context.ChangeSinger(caSi));
    }

    @Override
    public int getItemCount() {
        if (arrayList != null)
            return arrayList.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView imgCaSi;
        TextView tvTenCaSi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCaSi = itemView.findViewById(R.id.img_song_singer);
            tvTenCaSi = itemView.findViewById(R.id.tv_song_singer);
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
                        if (caSi.getTenCaSi().toLowerCase().contains(query.toLowerCase())) {
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

package com.example.mpmanage.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mpmanage.Model.BaiHat;
import com.example.mpmanage.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> implements Filterable {

    Context context;
    ArrayList<BaiHat> arrayList;
    ArrayList<BaiHat> mArrayList;

    public SongAdapter(Context context, ArrayList<BaiHat> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        mArrayList = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongAdapter.ViewHolder holder, int position) {
        BaiHat baiHat = arrayList.get(position);
        Picasso.with(context).load(baiHat.getHinhBaiHat()).error(R.drawable.song).into(holder.Avatar);
        holder.txtBaiHat.setText(baiHat.getTenBaiHat());
        holder.txtCaSi.setText(baiHat.getTenAllCaSi());
    }

    @Override
    public int getItemCount() {
        if (arrayList != null)
            return arrayList.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeLayout;
        RoundedImageView Avatar;
        ImageView btnDelete;
        TextView txtCaSi, txtBaiHat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.dong_baihat);
            Avatar = itemView.findViewById(R.id.img_baihat);
            btnDelete = itemView.findViewById(R.id.img_delete_baihat);
            txtCaSi = itemView.findViewById(R.id.txt_tencasi);
            txtBaiHat = itemView.findViewById(R.id.txt_tenbaihat);
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
                    List<BaiHat> baiHats = new ArrayList<>();
                    for (BaiHat baiHat : mArrayList) {
                        if (baiHat.getTenBaiHat().toString().toLowerCase().contains(query.toLowerCase())) {
                            baiHats.add(baiHat);
                        }
                    }
                    arrayList = (ArrayList<BaiHat>) baiHats;
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
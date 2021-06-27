package com.example.mpmanage.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mpmanage.Activity.BannerActivity.UpdateBannerActivity;
import com.example.mpmanage.Activity.UpdateCategoryActivity;
import com.example.mpmanage.Model.BaiHat;
import com.example.mpmanage.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AddSongCategoryAdapter extends RecyclerView.Adapter<AddSongCategoryAdapter.ViewHolder> implements Filterable {
    UpdateCategoryActivity context;
    ArrayList<BaiHat> arrayList;
    ArrayList<BaiHat> mArrayList;
    boolean isDialog;
    boolean isUpdate;

    public AddSongCategoryAdapter(UpdateCategoryActivity context, ArrayList<BaiHat> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        mArrayList = arrayList;
        isDialog = false;
        isUpdate = false;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_add_song_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BaiHat baiHat = arrayList.get(position);
        holder.tvTenCaSi.setText(baiHat.getTenAllCaSi());
        holder.tvTenBaiHat.setText(baiHat.getTenBaiHat());
        Picasso.with(context).load(baiHat.getHinhBaiHat().toString()).into(holder.imgBaihat);


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


    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView imgBaihat;
        TextView tvTenCaSi;
        TextView tvTenBaiHat;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBaihat = itemView.findViewById(R.id.img_baihat_category);
            tvTenBaiHat = itemView.findViewById(R.id.txt_tenbaihat_category);
            tvTenCaSi = itemView.findViewById(R.id.txt_tencasi_category);
            imageView = itemView.findViewById(R.id.img_delete_baihat_category);
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

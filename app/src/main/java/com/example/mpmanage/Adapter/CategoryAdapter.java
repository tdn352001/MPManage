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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mpmanage.Activity.SongActivity.AddSongActivity;
import com.example.mpmanage.Activity.UpdateCategoryActivity;
import com.example.mpmanage.Model.ChuDeTheLoai;
import com.example.mpmanage.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{
    Context context;
    ArrayList<ChuDeTheLoai> arrayList;
    String category;
    public CategoryAdapter(Context context, ArrayList<ChuDeTheLoai> arrayList, String category) {
        this.context = context;
        this.arrayList = arrayList;
        this.category = category;
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
       ChuDeTheLoai cdtl = arrayList.get(position);
       Picasso.with(context).load(cdtl.getHinh()).into(holder.imageView);
       holder.tvTitle.setText(cdtl.getTen());

       holder.itemView.setOnClickListener(v -> {
           Intent intent = new Intent(context, UpdateCategoryActivity.class);
           intent.putExtra("category", cdtl);
           intent.putExtra("loai", category);
           context.startActivity(intent);
       });

       holder.btnDelete.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

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

//    @Override
//    public Filter getFilter() {
//        return new Filter() {
//            @Override
//            protected FilterResults performFiltering(CharSequence constraint) {
//                String query = (String) constraint;
//                if (query.equals("")) {
//                    {
//                        arrayList = mArrayList;
//                    }
//                } else {
//                    List<ChuDeTheLoai> caSis = new ArrayList<>();
//                    for (ChuDeTheLoai caSi : mArrayList) {
//                        if (caSi.getTenCaSi().toString().toLowerCase().contains(query.toLowerCase())) {
//                            caSis.add(caSi);
//                        }
//                    }
//                    arrayList = (ArrayList<ChuDeTheLoai>) caSis;
//                }
//
//                FilterResults filterResults = new FilterResults();
//                filterResults.values = arrayList;
//                return filterResults;
//            }
//
//            @Override
//            protected void publishResults(CharSequence constraint, FilterResults results) {
//                arrayList = (ArrayList<ChuDeTheLoai>) results.values;
//                notifyDataSetChanged();
//            }
//        };
//    }
}

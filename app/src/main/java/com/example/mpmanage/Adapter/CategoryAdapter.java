package com.example.mpmanage.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mpmanage.Activity.UpdateCategoryActivity;
import com.example.mpmanage.Fragment.MainFragment.CategoryFragment;
import com.example.mpmanage.Model.ChuDeTheLoai;
import com.example.mpmanage.R;
import com.example.mpmanage.Service.APIService;
import com.example.mpmanage.Service.DataService;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    Context context;
    ArrayList<ChuDeTheLoai> arrayList;
    String category;
    int itemchange;

    public CategoryAdapter(Context context, ArrayList<ChuDeTheLoai> arrayList, String category) {
        this.context = context;
        this.arrayList = arrayList;
        this.category = category;
        itemchange = -1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_category, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChuDeTheLoai cdtl = arrayList.get(position);
        if (itemchange != position)
            Glide.with(context).load(cdtl.getHinh()).into(holder.imageView);
        else {
            Glide.with(context).load(cdtl.getHinh())
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true).into(holder.imageView);
            itemchange = -1;
        }
        holder.tvTitle.setText(cdtl.getTen());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdateCategoryActivity.class);
            intent.putExtra("category", cdtl);
            intent.putExtra("loai", category);
            context.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(v -> {
            Log.e("BBB", cdtl.getId() + cdtl.getTen());
            MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(context);
            dialog.setBackground(context.getResources().getDrawable(R.drawable.custom_diaglog_background));
            dialog.setTitle("Tho??t");
            dialog.setIcon(R.drawable.ic_delete);
            dialog.setMessage("B???n C?? Ch???c mu???n x??a M???c n??y?");
            dialog.setNegativeButton("?????ng ??", (dialog1, which) -> {
                DataService dataService = APIService.getService();
                Call<String> callback;
                if (category.equals("chude")) {
                    callback = dataService.DeleteChuDe(cdtl.getId());
                } else
                    callback = dataService.DeleteTheLoai(cdtl.getId());

                callback.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Toast.makeText(context, "C???p Nh???t Th??nh C??ng", Toast.LENGTH_SHORT).show();
                        CategoryFragment.DeleteCategory(category, cdtl);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });

            });

            dialog.setPositiveButton("H???y", (dialog12, which) -> dialog12.dismiss());
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
            imageView = itemView.findViewById(R.id.img_category);
            tvTitle = itemView.findViewById(R.id.tv_category);
            btnDelete = itemView.findViewById(R.id.btn_delete_category);
        }
    }

}

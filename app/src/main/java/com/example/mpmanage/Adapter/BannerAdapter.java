package com.example.mpmanage.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mpmanage.Activity.UpdateBannerActivity;
import com.example.mpmanage.Fragment.MainFragment.BannerFragment;
import com.example.mpmanage.Model.QuangCao;
import com.example.mpmanage.R;
import com.example.mpmanage.Service.APIService;
import com.example.mpmanage.Service.DataService;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.ViewHolder> {

    Context context;
    ArrayList<QuangCao> arrayList;
    int itemchange;

    public BannerAdapter(Context context, ArrayList<QuangCao> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        itemchange = -1;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_baihat_quangcao, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuangCao banner = arrayList.get(position);
        if (itemchange != position)
            Glide.with(context).load(banner.getHinhAnh()).error(R.drawable.song).into(holder.Avatar);
        else {
            Glide.with(context).load(banner.getHinhAnh())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true).error(R.drawable.song).into(holder.Avatar);
            itemchange = -1;
        }
        holder.txtBaiHat.setText(banner.getTenBaiHat());
        holder.txtNoiDung.setText(banner.getNoiDung());

        holder.btnDelete.setOnClickListener(v -> {
            MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(context);
            dialog.setBackground(context.getResources().getDrawable(R.drawable.custom_diaglog_background));
            dialog.setTitle("Thoát");
            dialog.setIcon(R.drawable.ic_delete);
            dialog.setMessage("Bạn Có Chắc muốn xóa bài hát này?");
            dialog.setNegativeButton("Đồng Ý", (dialog1, which) -> {
                if (arrayList.size() < 4) {
                    DataService dataService = APIService.getService();
                    Call<String> callback = dataService.DeleteBanner(banner.getIdQuangCao());
                    callback.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            BannerFragment.DeleteBanner(banner);
                            Toast.makeText(context, "Đã Cập Nhật", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(context, "Đã Cập Nhật", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(context, "Số Quảng Cáo Tối Thiểu là 3", Toast.LENGTH_SHORT).show();
                }

            });

            dialog.setPositiveButton("Hủy", (dialog12, which) -> dialog12.dismiss());
            dialog.show();
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdateBannerActivity.class);
            Log.e("BBB", arrayList.get(position).getHinhAnh());
            intent.putExtra("banner", arrayList.get(position));
            context.startActivity(intent);
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
        ImageView Avatar;
        ImageView btnDelete;
        TextView txtNoiDung, txtBaiHat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Avatar = itemView.findViewById(R.id.img_baihat_banner);
            btnDelete = itemView.findViewById(R.id.img_delete_baihat_banner);
            txtNoiDung = itemView.findViewById(R.id.txt_tencasi_banner);
            txtBaiHat = itemView.findViewById(R.id.txt_tenbaihat_banner);
        }
    }


}

package com.example.mpmanage.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mpmanage.Activity.UpdateCaSiActivity;
import com.example.mpmanage.Activity.UpdateSingerActivity;
import com.example.mpmanage.Fragment.MainFragment.SingerFragment;
import com.example.mpmanage.Model.Album;
import com.example.mpmanage.Model.BaiHat;
import com.example.mpmanage.Model.CaSi;
import com.example.mpmanage.R;
import com.example.mpmanage.Service.APIService;
import com.example.mpmanage.Service.DataService;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingerAdapter extends RecyclerView.Adapter<SingerAdapter.ViewHolder> implements Filterable {
    SingerFragment context;
    ArrayList<CaSi> arrayList;
    ArrayList<CaSi> mArrayList;
    int itemchange;

    public SingerAdapter(SingerFragment context, ArrayList<CaSi> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        mArrayList = arrayList;
        itemchange = -1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context.getContext());
        View view = inflater.inflate(R.layout.dong_singer, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CaSi caSi = arrayList.get(position);
        if (itemchange == position) {
            Glide.with(context).load(caSi.getHinhCaSi())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).into(holder.imageView);
            itemchange = -1;
        } else {
            Glide.with(context).load(caSi.getHinhCaSi()).into(holder.imageView);
        }

        holder.tvTitle.setText(caSi.getTenCaSi());
        holder.btnOptions.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context.getContext(), holder.btnOptions);
            setupPopupMenu(position, popupMenu);
            popupMenu.show();
        });

        holder.itemView.setOnClickListener(v -> ViewSongAlbumSinger(position));
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void setupPopupMenu(int position, PopupMenu popupMenu) {
        popupMenu.getMenuInflater().inflate(R.menu.menu_option_singer, popupMenu.getMenu());
        popupMenu.setGravity(Gravity.RIGHT);
        popupMenu.setForceShowIcon(true);

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.info_singer:
                    UpdateInfoSinger(position);
                    break;
                case R.id.pulish_singer:
                    ViewSongAlbumSinger(position);
                    break;
                case R.id.delete_singer:
                    CheckHaveBaiHat(position);
                    break;
            }

            return true;
        });
    }


    public void setItemchange(int itemchange) {
        this.itemchange = itemchange;
    }

    private void UpdateInfoSinger(int position) {
        Intent intent = new Intent(context.getContext(), UpdateCaSiActivity.class);
        intent.putExtra("casi", arrayList.get(position));
        context.startActivity(intent);
    }

    private void ViewSongAlbumSinger(int position) {
        Intent intent = new Intent(context.getContext(), UpdateSingerActivity.class);
        intent.putExtra("casi", arrayList.get(position));
        context.startActivity(intent);
    }

    private void CheckHaveBaiHat(int position) {
        DataService dataService = APIService.getService();
        Call<List<BaiHat>> callback = dataService.GetBaiHatCaSi(arrayList.get(position).getIdCaSi());
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                ArrayList<BaiHat> baiHatArrayList = (ArrayList<BaiHat>) response.body();
                if (baiHatArrayList != null) {
                    if (baiHatArrayList.size() > 0) {
                        Toast.makeText(context.getContext(), "Không Thể Xóa Ca Sĩ", Toast.LENGTH_SHORT).show();
                        Toast.makeText(context.getContext(), "Bạn Phải Xóa Hết Bài Hát Và Album Của Ca Sĩ Trước", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    CheckHaveAlbum(position);
                }

            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {

            }
        });
    }

    private void CheckHaveAlbum(int postion) {
        DataService dataService = APIService.getService();
        Call<List<Album>> callback = dataService.GetAlbumCaSi(arrayList.get(postion).getIdCaSi());
        callback.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                ArrayList<Album> albums = (ArrayList<Album>) response.body();
                if (albums != null) {
                    if (albums.size() > 0) {
                        Toast.makeText(context.getContext(), "Không Thể Xóa Ca Sĩ", Toast.LENGTH_SHORT).show();
                        Toast.makeText(context.getContext(), "Bạn Phải Xóa Hết Bài Hát Và Album Của Ca Sĩ Trước", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                OpenDialogAskDelete(arrayList.get(postion));
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void OpenDialogAskDelete(CaSi caSi) {
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(context.getContext());
        dialog.setBackground(context.getResources().getDrawable(R.drawable.custom_diaglog_background));
        dialog.setTitle("Thoát");
        dialog.setIcon(R.drawable.ic_delete);
        dialog.setMessage("Bạn Có Chắc muốn xóa Mục này?");
        dialog.setNegativeButton("Đồng Ý", (dialog1, which) -> {
            Call<String> call = APIService.getService().DeleteSinger(caSi.getIdCaSi());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    context.DeleteCaSi(caSi);
                    Toast.makeText(context.getContext(), "Cập Nhật THành Công", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(context.getContext(), "Cập Nhật Thất Bại", Toast.LENGTH_SHORT).show();
                    Log.e("BBBBB", t.getMessage());
                }
            });

        });

        dialog.setPositiveButton("Hủy", (dialog12, which) -> dialog12.dismiss());
        dialog.show();
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
        ImageView btnOptions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_playlist);
            tvTitle = itemView.findViewById(R.id.tv_playlist);
            btnOptions = itemView.findViewById(R.id.btn_delete_playlist);
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
                    List<CaSi> playlists = new ArrayList<>();
                    for (CaSi caSi : mArrayList) {
                        if (caSi.getTenCaSi().toLowerCase().contains(query.toLowerCase()))
                            playlists.add(caSi);
                    }

                    arrayList = (ArrayList<CaSi>) playlists;
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
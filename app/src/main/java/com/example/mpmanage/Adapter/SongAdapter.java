package com.example.mpmanage.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mpmanage.Activity.InfoSongActivity;
import com.example.mpmanage.Activity.UpdateSongActivity;
import com.example.mpmanage.Fragment.MainFragment.SongFragment;
import com.example.mpmanage.Model.BaiHat;
import com.example.mpmanage.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> implements Filterable {

    Context context;
    ArrayList<BaiHat> arrayList;
    ArrayList<BaiHat> mArrayList;
    int View;
    String IdBaiHatChange;

    public SongAdapter(Context context, ArrayList<BaiHat> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        mArrayList = arrayList;
        View = 40;
        IdBaiHatChange = "-1";
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_song, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull SongAdapter.ViewHolder holder, int position) {
        BaiHat baiHat = arrayList.get(position);
        Picasso.with(context).load(baiHat.getHinhBaiHat()).error(R.drawable.song).into(holder.Avatar);
        holder.txtBaiHat.setText(baiHat.getTenBaiHat());
        holder.txtCaSi.setText(baiHat.getTenAllCaSi());

        holder.relativeLayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdateSongActivity.class);
            intent.putExtra("baihat", baiHat);
            IdBaiHatChange = baiHat.getIdBaiHat();
            context.startActivity(intent);
        });

        holder.btnOptions.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.btnOptions);
            OpenOptionsMenu(position, popupMenu);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void OpenOptionsMenu(int position, PopupMenu popupMenu) {
        popupMenu.getMenuInflater().inflate(R.menu.menu_options_bai_hat, popupMenu.getMenu());
        popupMenu.setGravity(Gravity.RIGHT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popupMenu.setForceShowIcon(true);
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                BaiHat baiHat = arrayList.get(position);
                Log.e("BBB", baiHat.getTenBaiHat());
                if (item.getItemId() == R.id.info_song_item) {
                    Intent intent = new Intent(context, InfoSongActivity.class);
                    intent.putExtra("baihat", baiHat);
                    context.startActivity(intent);
                } else {
                    MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(context);
                    dialog.setBackground(context.getResources().getDrawable(R.drawable.custom_diaglog_background));
                    dialog.setTitle("Xóa?");
                    dialog.setIcon(R.drawable.ic_delete);
                    dialog.setMessage("Bạn Có Chắc muốn xóa bài hát này?");
                    dialog.setNegativeButton("Đồng Ý", (dialog1, which) -> {
                        SongFragment.DeleteSong(baiHat);
                        Toast.makeText(context, "Đã Cập Nhật", Toast.LENGTH_SHORT).show();
                    });

                    dialog.setPositiveButton("Hủy", (dialog12, which) -> dialog12.dismiss());
                    dialog.show();
                }
                return true;
            }
        });

        popupMenu.show();
    }




    @Override
    public int getItemCount() {
        if (arrayList != null) {
            if (View <= arrayList.size() && View > 0)
                return View;
            else
                return arrayList.size();
        }
        return 0;
    }

    public void ViewMore(boolean viewmore) {
        if (viewmore) {
            View += 40;
            if (View > arrayList.size())
                View = arrayList.size();
        } else {
            if (View == arrayList.size())
                View -= arrayList.size() % 40;
            else
                View -= 40;
            if (View <= 0)
                View = 40;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeLayout;
        RoundedImageView Avatar;
        ImageView btnOptions;
        TextView txtCaSi, txtBaiHat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.dong_baihat);
            Avatar = itemView.findViewById(R.id.img_baihat);
            btnOptions = itemView.findViewById(R.id.img_delete_baihat);
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
                    {
                        arrayList = mArrayList;
                        View = 40;
                    }
                } else {
                    List<BaiHat> baiHats = new ArrayList<>();
                    for (BaiHat baiHat : mArrayList) {
                        if (baiHat.getTenBaiHat().toString().toLowerCase().contains(query.toLowerCase())) {
                            baiHats.add(baiHat);
                        }
                    }
                    View = 0;
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

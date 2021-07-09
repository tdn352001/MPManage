package com.example.mpmanage.Fragment.MainFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mpmanage.Activity.UpdateBannerActivity;
import com.example.mpmanage.Activity.MainActivity;
import com.example.mpmanage.Adapter.BannerAdapter;
import com.example.mpmanage.Model.QuangCao;
import com.example.mpmanage.R;

import java.util.ArrayList;

public class BannerFragment extends Fragment {

    View view;
    static ArrayList<QuangCao> arrayList;
    static BannerAdapter adapter;

    RecyclerView recyclerView;
    RelativeLayout layoutNoinfo;
    private int i = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_banner, container, false);
        AnhXa();
        SetDataBanner();
        setHasOptionsMenu(true);
        return view;
    }

    private void AnhXa() {
        recyclerView = view.findViewById(R.id.rv_banner);
        layoutNoinfo = view.findViewById(R.id.txt_noinfo_banner);
    }

    private void SetDataBanner() {
        view.setVisibility(View.INVISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 120);
                if (MainActivity.quangCaoArrayList != null) {
                    view.setVisibility(View.VISIBLE);
                    arrayList = MainActivity.quangCaoArrayList;
                    adapter = new BannerAdapter(getActivity(), arrayList);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                    handler.removeCallbacks(this);
                }

                i++;
                if(i >= 20){
                    view.setVisibility(View.VISIBLE);
                }
            }
        }, 120);
    }


    public static void UpdateBaner(QuangCao banner) {
        int i = 0;
        for (i = 0; i < arrayList.size(); i++)
            if (arrayList.get(i).getIdQuangCao().equals(banner.getIdQuangCao())) {
                break;
            }
        if (i <= arrayList.size() - 1){
            arrayList.remove(i);
            arrayList.add(i, banner);
            adapter.setItemchange(i);
            adapter.notifyDataSetChanged();
        }
    }

    public static void AddBanner(QuangCao banner){
        arrayList.add(banner);
        adapter.notifyDataSetChanged();
    }

    public static void DeleteBanner(QuangCao banner){
        int Pos = arrayList.indexOf(banner);
        if(Pos != -1){
            arrayList.remove(Pos);
            adapter.notifyItemRemoved(Pos);
            adapter.notifyItemRangeChanged(Pos, arrayList.size());
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_banner, menu);

        MenuItem addItem = menu.findItem(R.id.item_add_banner);
        addItem.setTitle(R.string.add_song);
        addItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getActivity(), UpdateBannerActivity.class);
                startActivity(intent);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

}
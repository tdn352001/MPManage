package com.example.mpmanage.Fragment.MainFragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mpmanage.Activity.MainActivity;
import com.example.mpmanage.Adapter.SongAdapter;
import com.example.mpmanage.Model.BaiHat;
import com.example.mpmanage.R;

import java.util.ArrayList;

public class SongFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    SongAdapter songAdapter;
    ArrayList<BaiHat> baiHatArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_song, container, false);
        AnhXa();
        GetDataBaiHat();
        return view;
    }

    private void AnhXa() {
        recyclerView = view.findViewById(R.id.rv_song);
    }

    private void GetDataBaiHat() {
        if(baiHatArrayList != null)
            return;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 200);
                if(MainActivity.baiHatArrayList != null){
                    baiHatArrayList = MainActivity.baiHatArrayList;
                    SetRecycleView();
                    handler.removeCallbacks(this);
                }
            }
        }, 200);
    }

    private void SetRecycleView() {
        songAdapter = new SongAdapter(getContext(), baiHatArrayList);
        recyclerView.setAdapter(songAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
    }
}
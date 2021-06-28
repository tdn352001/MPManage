package com.example.mpmanage.Fragment.MainFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.mpmanage.Activity.MainActivity;
import com.example.mpmanage.Adapter.PlaylistAdapter;
import com.example.mpmanage.Model.Playlist;
import com.example.mpmanage.R;

import java.util.ArrayList;

public class PlaylistFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    ArrayList<Playlist> arrayList;
    PlaylistAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_playlist, container, false);
        AnhXa();
        GetDataPlaylist();
        return view;
    }

    private void GetDataPlaylist() {
        if(arrayList != null){
            SetRv();
            return;
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 120);
                if(MainActivity.playlistArrayList != null){
                    arrayList = MainActivity.playlistArrayList;
                    SetRv();
                    handler.removeCallbacks(this);
                }
            }
        }, 120);
    }

    private void SetRv() {
        adapter = new PlaylistAdapter(getContext(), arrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
    }

    private void AnhXa() {
        recyclerView = view.findViewById(R.id.rv_playlist);
    }
}
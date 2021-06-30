package com.example.mpmanage.Fragment.MainFragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mpmanage.Activity.MainActivity;
import com.example.mpmanage.Activity.UpdatePlaylistActivity;
import com.example.mpmanage.Adapter.PlaylistAdapter;
import com.example.mpmanage.Model.Playlist;
import com.example.mpmanage.R;

import java.util.ArrayList;

public class PlaylistFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    TextView title;
    static ArrayList<Playlist> arrayList;
    @SuppressLint("StaticFieldLeak")
    static PlaylistAdapter adapter;
    SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_playlist, container, false);
        AnhXa();
        GetDataPlaylist();
        setHasOptionsMenu(true);
        return view;
    }

    private void GetDataPlaylist() {
        if (arrayList != null) {
            SetRv();
            return;
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 120);
                if (MainActivity.playlistArrayList != null) {
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
        title = view.findViewById(R.id.txt_playlist);
        relativeLayout = view.findViewById(R.id.txt_noinfo_add_online);
        recyclerView = view.findViewById(R.id.rv_playlist);
    }

    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search_view);
        searchItem.setTitle("Tìm Kiếm Playlislt");
        searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Tìm Kiếm Playlist");
        searchView.setIconifiedByDefault(true);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @SuppressLint("SetTextI18n")
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) {
                    title.setText(R.string.List_playlists);

                } else {
                    title.setText("Tìm Kiếm Từ Khóa: " + newText);
                }

                adapter.getFilter().filter(newText);


                //Kiểm tra kết quả
                final int[] i = {0};
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handler.postDelayed(this, 100);
                        if (i[0] >= 3)
                            handler.removeCallbacks(this);

                        if (adapter.getItemCount() == 0) {
                            relativeLayout.setVisibility(View.VISIBLE);
                        } else
                            relativeLayout.setVisibility(View.GONE);
                        i[0]++;
                    }
                }, 200);


                return true;
            }
        });

        MenuItem AddItem = menu.findItem(R.id.add_item);
        AddItem.setTitle("Thêm Playlist");
        AddItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getContext(), UpdatePlaylistActivity.class);
                startActivity(intent);
                return true;
            }
        });


        super.onCreateOptionsMenu(menu, inflater);
    }

    public static void AddPlaylist(Playlist playlist) {
        arrayList.add(playlist);
        adapter.notifyDataSetChanged();
    }

    public static void UpdatePlaylist(Playlist playlist) {
        int i = 0;
        for (i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getIdPlaylist().equals(playlist.getIdPlaylist()))
                break;
        }

        if (i >= arrayList.size())
            return;
       arrayList.get(i).setTen(playlist.getTen());
       arrayList.get(i).setHinhAnh(playlist.getHinhAnh());
       adapter.notifyItemChanged(i);
    }

    public static void DeletePlaylist(Playlist playlist){
        int index = arrayList.indexOf(playlist);
        if(index != -1)
        {
            arrayList.remove(index);
            adapter.notifyItemRemoved(index);
            adapter.notifyItemRangeChanged(index, arrayList.size());
        }
    }
}
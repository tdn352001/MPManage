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
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mpmanage.Activity.MainActivity;
import com.example.mpmanage.Activity.UpdateAlbumActivity;
import com.example.mpmanage.Adapter.AlbumAdapter;
import com.example.mpmanage.Model.Album;
import com.example.mpmanage.R;

import java.util.ArrayList;

import static android.view.View.GONE;

public class AlbumFragment extends Fragment {

    View view;
    TextView title;
    RecyclerView recyclerView;
    static SearchView searchView;
    RelativeLayout relativeLayout;
    public static ArrayList<Album> arrayList;
    static AlbumAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_album, container, false);
        AnhXa();
        GetDataAlbum();
        setHasOptionsMenu(true);
        return view;
    }

    private void GetDataAlbum() {
        view.setVisibility(View.INVISIBLE);
        if (arrayList != null) {
            view.setVisibility(View.VISIBLE);
            SetRv();
            return;
        }
        final int[] i = {0};
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 120);
                if (MainActivity.albumArrayList != null) {
                    view.setVisibility(View.VISIBLE);
                    arrayList = MainActivity.albumArrayList;
                    SetRv();
                    handler.removeCallbacks(this);
                }
                i[0] += 10;
                if (i[0] >= 2000) {
                    view.setVisibility(View.VISIBLE);
                }

            }
        }, 120);
    }

    private void SetRv() {
        adapter = new AlbumAdapter(this, arrayList);
        LayoutAnimationController animlayout = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_anim_left_to_right);
        recyclerView.setLayoutAnimation(animlayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    private void AnhXa() {
        title = view.findViewById(R.id.txt_danhsachbaihat);
        recyclerView = view.findViewById(R.id.rv_album);
        relativeLayout = view.findViewById(R.id.txt_noinfo_add_online);
    }

    public static void UpdateAlbum(Album album) {
        int i = 0;
        for (i = 0; i < arrayList.size(); i++) {
            if (album.getIdAlbum().equals(arrayList.get(i).getIdAlbum())) {
                arrayList.get(i).setIdCaSi(album.getIdCaSi());
                arrayList.get(i).setTenCaSi(album.getTenCaSi());
                arrayList.get(i).setHinhAlbum(album.getHinhAlbum());
                arrayList.get(i).setTenAlbum(album.getTenAlbum());
                if (adapter != null) {
                    adapter.setItemchange(i);
                    adapter.notifyItemChanged(i);
                    if (!searchView.getQuery().equals("")) {
                        String query = searchView.getQuery().toString();
                        searchView.setQuery("", true);
                        searchView.setQuery(query, true);
                    }
                }
                return;
            }
        }
    }

    public static void AddAlbum(Album album) {
        if (!arrayList.contains(album)) {
            arrayList.add(album);
            adapter.notifyDataSetChanged();
            if (!searchView.getQuery().equals("")) {
                String query = searchView.getQuery().toString();
                searchView.setQuery("", true);
                searchView.setQuery(query, true);
            }
        }
    }

    public void DeleteAlbum(Album album) {
        int i = arrayList.indexOf(album);
        if (i != -1) {
            arrayList.remove(i);
            adapter.notifyItemRemoved(i);
            adapter.notifyItemRangeChanged(i, arrayList.size());
            if (!searchView.getQuery().equals("")) {
                String query = searchView.getQuery().toString();
                searchView.setQuery("", true);
                searchView.setQuery(query, true);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_album, menu);
        MenuItem searchItem = menu.findItem(R.id.search_view);
        searchItem.setTitle("Tìm Kiếm Album");
        searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Tìm Kiếm Album");
        searchView.setIconifiedByDefault(true);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @SuppressLint("SetTextI18n")
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) {
                    title.setText(R.string.list_album);
                    relativeLayout.setVisibility(GONE);
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

        MenuItem addItem = menu.findItem(R.id.add_item);
        addItem.setTitle(R.string.add_album);
        addItem.setOnMenuItemClickListener(item -> {
            Intent intent = new Intent(getActivity(), UpdateAlbumActivity.class);
            startActivity(intent);
            return true;
        });

        super.onCreateOptionsMenu(menu, inflater);
    }
}
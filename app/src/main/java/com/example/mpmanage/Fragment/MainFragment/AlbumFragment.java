package com.example.mpmanage.Fragment.MainFragment;

import android.annotation.SuppressLint;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mpmanage.Activity.AddSongActivity;
import com.example.mpmanage.Activity.MainActivity;
import com.example.mpmanage.Activity.UpdateAlbumActivity;
import com.example.mpmanage.Adapter.AlbumAdapter;
import com.example.mpmanage.Model.Album;
import com.example.mpmanage.Model.CaSi;
import com.example.mpmanage.R;
import com.example.mpmanage.Service.APIService;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class AlbumFragment extends Fragment {

    View view;
    TextView title;
    RecyclerView recyclerView;
    SearchView searchView;
    RelativeLayout relativeLayout;
    static ArrayList<Album> arrayList;
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
        if (arrayList != null) {
            SetRv();
            return;
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 120);
                if (MainActivity.albumArrayList != null) {
                    arrayList = MainActivity.albumArrayList;
                    SetRv();
                    handler.removeCallbacks(this);
                }

            }
        }, 120);
    }

    private void SetRv() {
        adapter = new AlbumAdapter(getContext(), arrayList);
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
        Log.e("BBB", "Update Album");
        for (i = 0; i < arrayList.size(); i++) {
            if (album.getIdAlbum().equals(arrayList.get(i).getIdAlbum())) {
                Log.e("BBB", album.getTenCaSi());
                arrayList.get(i).setIdCaSi(album.getIdCaSi());
                arrayList.get(i).setTenCaSi(album.getTenCaSi());
                arrayList.get(i).setHinhAlbum(album.getHinhAlbum());
                arrayList.get(i).setTenAlbum(album.getTenAlbum());
                adapter.notifyItemChanged(i);
                return;
            }
        }
    }

    public static void AddAlbum(Album album) {
        if (!arrayList.contains(album)) {
            arrayList.add(album);
            adapter.notifyDataSetChanged();
        }
    }

    public static void DeleteAlbum(Album album) {
        int i = arrayList.indexOf(album);
        if (i != -1) {
            arrayList.remove(i);
            adapter.notifyItemRemoved(i);
            adapter.notifyItemRangeChanged(i, arrayList.size());
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
                    title.setText(R.string.list_song);
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
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
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mpmanage.Activity.AddSongActivity;
import com.example.mpmanage.Activity.MainActivity;
import com.example.mpmanage.Adapter.SongAdapter;
import com.example.mpmanage.Model.BaiHat;
import com.example.mpmanage.R;
import com.example.mpmanage.Service.APIService;
import com.example.mpmanage.Service.DataService;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class SongFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    static SongAdapter songAdapter;
    public static ArrayList<BaiHat> baiHatArrayList;
    MaterialButton btnViewMore, btnViewLess;
    SearchView searchView;
    TextView title;
    RelativeLayout SearchNoDataResult;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_song, container, false);
        AnhXa();
        setHasOptionsMenu(true);
        GetDataBaiHat();
        EventClick();
        return view;
    }

    private void AnhXa() {
        title = view.findViewById(R.id.txt_danhsachbaihat);
        recyclerView = view.findViewById(R.id.rv_song);
        btnViewLess = view.findViewById(R.id.btn_viewless_baihat);
        btnViewMore = view.findViewById(R.id.btn_viewmore_baihat);
        SearchNoDataResult = view.findViewById(R.id.txt_noinfo_add_online);
    }

    private void GetDataBaiHat() {
        view.setVisibility(View.INVISIBLE);
        if (baiHatArrayList != null) {
            SetRecycleView();
            view.setVisibility(View.VISIBLE);
            return;
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 200);
                if (MainActivity.baiHatArrayList != null) {
                    view.setVisibility(View.VISIBLE);
                    baiHatArrayList = MainActivity.baiHatArrayList;
                    SetRecycleView();
                    handler.removeCallbacks(this);
                }
            }
        }, 200);
    }

    private void SetRecycleView() {
        songAdapter = new SongAdapter(getContext(), baiHatArrayList);
        LayoutAnimationController animlayout = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_anim_left_to_right);
        recyclerView.setLayoutAnimation(animlayout);
        recyclerView.setAdapter(songAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
    }

    private void EventClick() {
        btnViewMore.setOnClickListener(v -> {
            if (songAdapter != null) {
                songAdapter.ViewMore(true);
                songAdapter.notifyDataSetChanged();
                btnViewLess.setVisibility(View.VISIBLE);
                if (songAdapter.getItemCount() == baiHatArrayList.size())
                    btnViewMore.setVisibility(GONE);
            }
        });

        btnViewLess.setOnClickListener(v -> {
            songAdapter.ViewMore(false);
            btnViewMore.setVisibility(View.VISIBLE);
            songAdapter.notifyDataSetChanged();
            if (songAdapter.getItemCount() <= 0)
                btnViewLess.setVisibility(GONE);
        });
    }

    public static void UpdateSong(BaiHat baiHat) {
        int Position = GetPositionById(baiHat.getIdBaiHat());
        if (Position == -1) {
            return;
        }
        baiHatArrayList.get(Position).setTenBaiHat(baiHat.getTenBaiHat());
        baiHatArrayList.get(Position).setHinhBaiHat(baiHat.getHinhBaiHat());
        baiHatArrayList.get(Position).setLinkBaiHat(baiHat.getLinkBaiHat());
        baiHatArrayList.get(Position).setIdCaSi(baiHat.getIdCaSi());
        baiHatArrayList.get(Position).setCaSi(baiHat.getCaSi());
        if (songAdapter != null) {
            songAdapter.setItemchange(Position);
            songAdapter.notifyItemChanged(Position);
        }

    }

    public static void AddSong(BaiHat baihat) {
        baiHatArrayList.add(0, baihat);
        songAdapter.notifyDataSetChanged();
    }

    public static void DeleteSong(BaiHat baiHat) {
        int Pos = baiHatArrayList.indexOf(baiHat);
        baiHatArrayList.remove(Pos);
        songAdapter.notifyItemRemoved(Pos);
        songAdapter.notifyItemRangeChanged(Pos, baiHatArrayList.size());
        DataService dataService = APIService.getService();
        Call<String> callback = dataService.DeleteSong(baiHat.getIdBaiHat());
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    public static int GetPositionById(String IdBaiHat) {
        if (baiHatArrayList != null) {
            for (int i = 0; i < baiHatArrayList.size(); i++) {
                if (baiHatArrayList.get(i).getIdBaiHat().equals(IdBaiHat))
                    return i;
            }
        }
        return -1;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search_view);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Tìm Kiếm Bài Hát");
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
                    btnViewLess.setVisibility(GONE);
                    btnViewMore.setVisibility(View.VISIBLE);
                    SearchNoDataResult.setVisibility(GONE);
                } else {
                    title.setText("Tìm Kiếm Từ Khóa: " + newText);
                    btnViewLess.setVisibility(GONE);
                    btnViewMore.setVisibility(GONE);
                }
                songAdapter.getFilter().filter(newText);


                //Kiểm tra kết quả
                final int[] i = {0};
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handler.postDelayed(this, 100);
                        if (i[0] >= 3)
                            handler.removeCallbacks(this);

                        if (songAdapter.getItemCount() == 0) {
                            SearchNoDataResult.setVisibility(View.VISIBLE);
                        } else
                            SearchNoDataResult.setVisibility(View.GONE);
                        i[0]++;
                    }
                }, 200);


                return true;
            }
        });

        MenuItem addItem = menu.findItem(R.id.add_item);
        addItem.setTitle(R.string.add_song);
        addItem.setOnMenuItemClickListener(item -> {
            Intent intent = new Intent(getActivity(), AddSongActivity.class);
            startActivity(intent);
            return true;
        });

        super.onCreateOptionsMenu(menu, inflater);
    }


}
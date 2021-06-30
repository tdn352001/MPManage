package com.example.mpmanage.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mpmanage.Adapter.AlbumSingerAdaper;
import com.example.mpmanage.Adapter.SongSingerAdapter;
import com.example.mpmanage.Model.Album;
import com.example.mpmanage.Model.BaiHat;
import com.example.mpmanage.Model.CaSi;
import com.example.mpmanage.R;
import com.example.mpmanage.Service.APIService;
import com.example.mpmanage.Service.DataService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateSingerActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView imageView;
    RecyclerView recyclerViewBaiHat, recyclerViewAlBum;
    ProgressBar progressBar;
    TextView txtAlbum, txtBaiHat;
    RelativeLayout relativeLayout;
    static ArrayList<BaiHat> baiHatArrayList;
    static SongSingerAdapter songAdapter;
    static ArrayList<Album> albumArrayList;
    static AlbumSingerAdaper albumAdapter;
    static CaSi caSi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_singer);
        overridePendingTransition(R.anim.from_right, R.anim.to_left);

        AnhXa();
        GetIntent();
        setupToolBar();
    }


    private void AnhXa() {
        toolbar = findViewById(R.id.toolbar_detailsinger);
        progressBar = findViewById(R.id.progress_load_detailsinger);
        imageView = findViewById(R.id.img_detailsinger);
        recyclerViewAlBum = findViewById(R.id.rv_detailsinger_album);
        recyclerViewBaiHat = findViewById(R.id.rv_detailsinger_baihat);
        txtAlbum = findViewById(R.id.txt_detailsinger_album);
        txtBaiHat = findViewById(R.id.txt_detailsinger_baihat);
        relativeLayout = findViewById(R.id.txt_noinfo_add_online);
    }

    private void GetIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra("casi")) {
            caSi = (CaSi) intent.getSerializableExtra("casi");
            getBaiHatCaSi(caSi.getIdCaSi());
            getAlbumCaSi(caSi.getIdCaSi());
            setInfoCaSi();
        }
    }


    private void getBaiHatCaSi(String IdCaSi) {
        DataService dataService = APIService.getService();
        Call<List<BaiHat>> callback = dataService.GetBaiHatCaSi(IdCaSi);
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                baiHatArrayList = (ArrayList<BaiHat>) response.body();
                if (baiHatArrayList == null) {
                    baiHatArrayList = new ArrayList<>();
                }
                setDataBaiHat();
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {

            }
        });
    }


    private void setDataBaiHat() {
        songAdapter = new SongSingerAdapter(UpdateSingerActivity.this, baiHatArrayList);
        recyclerViewBaiHat.setAdapter(songAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UpdateSingerActivity.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewBaiHat.setLayoutManager(linearLayoutManager);
        if (baiHatArrayList.size() == 0) {
            txtBaiHat.setVisibility(View.GONE);
            CheckNoData();
        }
    }

    private void getAlbumCaSi(String IdCaSi) {
        DataService dataService = APIService.getService();
        Call<List<Album>> callback = dataService.GetAlbumCaSi(IdCaSi);
        callback.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                albumArrayList = (ArrayList<Album>) response.body();
                if (albumArrayList == null) {
                    albumArrayList = new ArrayList<>();
                }
                setDataAlbum();
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {
            }
        });
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void setDataAlbum() {

            for (int i = 0; i < albumArrayList.size(); i++) {
                albumArrayList.get(i).setIdCaSi(caSi.getIdCaSi());
                albumArrayList.get(i).setTenCaSi(caSi.getTenCaSi());
            }
            albumAdapter = new AlbumSingerAdaper(this, albumArrayList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UpdateSingerActivity.this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerViewAlBum.setLayoutManager(linearLayoutManager);
            recyclerViewAlBum.setAdapter(albumAdapter);
        if (albumArrayList.size() ==  0) {
            txtAlbum.setVisibility(View.GONE);
            CheckNoData();
        }
    }

    private void setInfoCaSi() {
        Picasso.with(this).load(caSi.getHinhCaSi()).into(imageView);
        toolbar.setTitle(caSi.getTenCaSi());
    }

    private void setupToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void CheckNoData(){
        if(txtBaiHat.getVisibility() == View.GONE && txtAlbum.getVisibility() == View.GONE ){
            relativeLayout.setVisibility(View.VISIBLE);
        }
    }
    public static void UpdateAlbum(Album album) {
        int i = 0;
        for (i = 0; i < albumArrayList.size(); i++) {
            if (album.getIdAlbum().equals(albumArrayList.get(i).getIdAlbum())) {
                break;
            }
        }
        if (i >= albumArrayList.size())
            return;

        if (!caSi.getIdCaSi().equals(album.getIdCaSi())) {
            albumArrayList.remove(i);
            albumAdapter.notifyItemRemoved(i);
            albumAdapter.notifyItemRangeChanged(i, albumArrayList.size());
            return;
        }

        albumArrayList.get(i).setIdCaSi(album.getIdCaSi());
        albumArrayList.get(i).setTenCaSi(album.getTenCaSi());
        albumArrayList.get(i).setHinhAlbum(album.getHinhAlbum());
        albumArrayList.get(i).setTenAlbum(album.getTenAlbum());
        if (albumAdapter != null)
            albumAdapter.notifyItemChanged(i);

    }

    public static void UpdateSong(BaiHat baiHat) {
        int i = 0;
        for (i = 0; i < baiHat.getIdCaSi().size(); i++) {
            if (baiHat.getIdCaSi().get(i).equals(caSi.getIdCaSi())) {
                break;
            }
        }
        int j = 0;
        for (j = 0; j < baiHatArrayList.size(); j++)
            if (baiHat.getIdBaiHat().equals(baiHatArrayList.get(j).getIdBaiHat())) {
                break;
            }

        if (j < baiHatArrayList.size()) {
            if (i >= baiHat.getIdCaSi().size()) {
                baiHatArrayList.remove(j);
                songAdapter.notifyItemRemoved(j);
                songAdapter.notifyItemRangeChanged(j, baiHatArrayList.size());
                return;
            }

            baiHatArrayList.get(j).setTenBaiHat(baiHat.getTenBaiHat());
            baiHatArrayList.get(j).setHinhBaiHat(baiHat.getHinhBaiHat());
            baiHatArrayList.get(j).setLinkBaiHat(baiHat.getLinkBaiHat());
            baiHatArrayList.get(j).setIdCaSi(baiHat.getIdCaSi());
            baiHatArrayList.get(j).setCaSi(baiHat.getCaSi());
            if (songAdapter != null)
                songAdapter.notifyItemChanged(j);
        }

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.from_left, R.anim.to_right);
    }
}
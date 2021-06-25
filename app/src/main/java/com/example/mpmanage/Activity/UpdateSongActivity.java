package com.example.mpmanage.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mpmanage.Adapter.SongSingerAdapter;
import com.example.mpmanage.Model.BaiHat;
import com.example.mpmanage.Model.CaSi;
import com.example.mpmanage.R;
import com.google.android.material.button.MaterialButton;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.mpmanage.Activity.MainActivity.PERMISSION_READ;

public class UpdateSongActivity extends AppCompatActivity {


    BaiHat baiHat;
    ArrayList<CaSi> caSiArrayList;
    SongSingerAdapter songSingerAdapter;

    Toolbar toolbar;
    EditText edtTenBaiHat, edtHinhBaiHat, edtFileBaiHat;
    MaterialButton btnGetHinh, btnGetFile, btnFinish, btnCancel;
    RoundedImageView imgBaiHat;
    RecyclerView rvCaSi;
    RadioButton rdLinkHinh, rdFileHinh, rdLinkNhac, rdFileNhac;

    String RealPathHinh, RealPathNhac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_song);
        AnhXa();
        SetToolbar();
        GetBaiHat();
        checkPermission();
    }

    private void AnhXa() {
        toolbar = findViewById(R.id.update_song_toolbar);
        edtTenBaiHat = findViewById(R.id.edt_ten_baihat);
        edtHinhBaiHat = findViewById(R.id.edt_link_hinh_baihat);
        edtFileBaiHat = findViewById(R.id.edt_link_file_baihat);
        btnGetFile = findViewById(R.id.btn_get_file_bai_hat);
        btnGetHinh = findViewById(R.id.btn_get_hinh_bai_hat);
        btnFinish = findViewById(R.id.btn_finish_update_bai_hat);
        btnCancel = findViewById(R.id.btn_cancel_update_bai_hat);
        imgBaiHat = findViewById(R.id.img_hinh_bai_hat);
        rvCaSi = findViewById(R.id.rv_casi_baihat);
        rdLinkHinh = findViewById(R.id.rd_link_hinh);
        rdFileHinh = findViewById(R.id.rd_file_hinh);
        rdLinkNhac = findViewById(R.id.rd_link_nhac);
        rdFileNhac = findViewById(R.id.rd_file_nhac);
    }

    private void SetToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cập Nhật Bài Hát");
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    public boolean checkPermission() {
        int READ_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(UpdateSongActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if ((READ_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(UpdateSongActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ);
            return false;
        }
        return true;
    }

    private void GetBaiHat() {
        Intent intent = getIntent();
        if (intent.hasExtra("baihat")) {
            baiHat = intent.getParcelableExtra("baihat");
            if (baiHat == null) {
                Log.e("BBB", "Không lấy được bài hát");
                return;
            }
            edtTenBaiHat.setText(baiHat.getTenBaiHat());
            edtHinhBaiHat.setText(baiHat.getLinkBaiHat());
            edtFileBaiHat.setText(baiHat.getLinkBaiHat());
            Picasso.with(this).load(baiHat.getHinhBaiHat()).error(R.drawable.ic_image).into(imgBaiHat);

            caSiArrayList = new ArrayList<>();
            songSingerAdapter = new SongSingerAdapter(this, caSiArrayList);
            rvCaSi.setAdapter(songSingerAdapter);
            rvCaSi.setLayoutManager(new LinearLayoutManager(UpdateSongActivity.this, RecyclerView.VERTICAL, false));
            for (int i = 0; i < baiHat.getIdCaSi().size(); i++) {
                String idsingerSong = baiHat.getIdCaSi().get(i);
                for (int j = 0; j < MainActivity.caSiArrayList.size(); j++) {
                    String idSinger = MainActivity.caSiArrayList.get(j).getIdCaSi();
                    Log.e("BBB", idSinger);
                    if (idsingerSong.equals(idSinger)) {
                        caSiArrayList.add(MainActivity.caSiArrayList.get(j));
                        songSingerAdapter.notifyItemInserted(caSiArrayList.size() - 1);
                    }
                }
            }
        }
    }


}
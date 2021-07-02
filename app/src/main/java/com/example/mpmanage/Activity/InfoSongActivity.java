package com.example.mpmanage.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import com.example.mpmanage.Model.BaiHat;
import com.example.mpmanage.R;
import com.google.android.material.button.MaterialButton;

public class InfoSongActivity extends AppCompatActivity {
     MaterialButton tvId, tvTen, tvCaSi, tvHinh, tvNhac, tvLuotThich;
     Toolbar toolbar;
     BaiHat baiHat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_song);
        overridePendingTransition(R.anim.from_right, R.anim.to_left);

        AnhXa();
        SetToolBar();
        SetThongTinBaiHat();
    }



    private void AnhXa() {
        toolbar = findViewById(R.id.toolbar_info_song);
        tvId = findViewById(R.id.txt_info_id_bai_hat);
        tvTen = findViewById(R.id.txt_info_ten_bai_hat);
        tvCaSi = findViewById(R.id.txt_info_casi_bai_hat);
        tvHinh = findViewById(R.id.txt_info_hinh_bai_hat);
        tvNhac = findViewById(R.id.txt_info_file_bai_hat);
        tvLuotThich = findViewById(R.id.txt_info_lươt_thich_bai_hat);
    }

    private void SetToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chi Tiết Bài Hát");
        toolbar.setNavigationOnClickListener(v -> {
           finish();
        });
    }

    @SuppressLint("SetTextI18n")
    private void SetThongTinBaiHat() {
        Intent intent = getIntent();
        if(intent.hasExtra("baihat")){
            baiHat = intent.getParcelableExtra("baihat");
            if(baiHat == null)
                return;

            tvId.setText("Id Bài Hát: " + baiHat.getIdBaiHat());
            tvTen.setText("Tên Bài Hát: " + baiHat.getTenBaiHat());
            tvCaSi.setText("Ca Sĩ: " + baiHat.getTenAllCaSi());
            tvHinh.setText("Link Hình: " +  baiHat.getHinhBaiHat());
            tvNhac.setText("Link Nhạc: " + baiHat.getLinkBaiHat());
            tvLuotThich.setText("Lượt Thích: " + baiHat.getLuotThich());
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.from_left, R.anim.to_right);
    }
}
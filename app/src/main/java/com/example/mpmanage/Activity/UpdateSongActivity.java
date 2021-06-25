package com.example.mpmanage.Activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.mpmanage.Adapter.SongSingerAdapter;
import com.example.mpmanage.Model.BaiHat;
import com.example.mpmanage.Model.CaSi;
import com.example.mpmanage.Model.RealPathUtil;
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
    MaterialButton btnGetFileHinh, btnGetFileNhac, btnFinish, btnCancel, btnAddCaSi;
    RoundedImageView imgBaiHat;
    RecyclerView rvCaSi;
    RadioGroup rdHinh, rdNhac;

    String RealPathHinh, RealPathNhac;
    Uri uriHinh, uriNhac;
    boolean LoadImageSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_song);
        AnhXa();
        SetToolbar();
        GetBaiHat();
        checkPermission();
        EventListener();
    }

    private void AnhXa() {
        toolbar = findViewById(R.id.update_song_toolbar);
        edtTenBaiHat = findViewById(R.id.edt_ten_baihat);
        edtHinhBaiHat = findViewById(R.id.edt_link_hinh_baihat);
        edtFileBaiHat = findViewById(R.id.edt_link_file_baihat);
        btnGetFileNhac = findViewById(R.id.btn_get_file_bai_hat);
        btnGetFileHinh = findViewById(R.id.btn_get_hinh_bai_hat);
        btnFinish = findViewById(R.id.btn_finish_update_bai_hat);
        btnCancel = findViewById(R.id.btn_cancel_update_bai_hat);
        btnAddCaSi = findViewById(R.id.btn_add_casi);
        imgBaiHat = findViewById(R.id.img_hinh_bai_hat);
        rvCaSi = findViewById(R.id.rv_casi_baihat);
        rdHinh = findViewById(R.id.rd_hinh);
        rdNhac = findViewById(R.id.rd_nhac);
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
            edtHinhBaiHat.setText(baiHat.getHinhBaiHat());
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
                    if (idsingerSong.equals(idSinger)) {
                        caSiArrayList.add(MainActivity.caSiArrayList.get(j));
                        songSingerAdapter.notifyItemInserted(caSiArrayList.size() - 1);
                    }
                }
            }
        }
    }


    private void EventListener() {

        // Mục Radio Hình Ảnh
        rdHinh.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rd_link_hinh) {
                edtHinhBaiHat.setVisibility(View.VISIBLE);
                btnGetFileHinh.setVisibility(View.GONE);
                Picasso.with(this).load(edtHinhBaiHat.getText().toString()).error(R.drawable.ic_image).into(imgBaiHat);
            } else {
                edtHinhBaiHat.setVisibility(View.GONE);
                btnGetFileHinh.setVisibility(View.VISIBLE);
                if (RealPathHinh == null) {
                    imgBaiHat.setImageResource(R.drawable.ic_image);
                    return;
                }
                Picasso.with(this).load(uriHinh).error(R.drawable.ic_image).into(imgBaiHat);
            }
        });


        // Mục Radio Nhạc
        rdNhac.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rd_link_nhac) {
                edtFileBaiHat.setVisibility(View.VISIBLE);
                btnGetFileNhac.setVisibility(View.GONE);
            } else {
                edtFileBaiHat.setVisibility(View.GONE);
                btnGetFileNhac.setVisibility(View.VISIBLE);
            }
        });

        // Chọn File Hình
        btnGetFileHinh.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            FileAnhResult.launch(Intent.createChooser(intent, " Chọn Hình Ảnh"));
        });

        // Chọn File Nhạc
        btnGetFileNhac.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("audio/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            FileAnhResult.launch(Intent.createChooser(intent, " Chọn File Nhạc"));
        });


        edtHinhBaiHat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Glide.with(getApplicationContext()).load(edtHinhBaiHat.getText().toString()).error(R.drawable.ic_image).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        LoadImageSuccess = false;
                        Log.e("BBB", "setImageFalse");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        LoadImageSuccess = true;
                        Log.e("BBB", "setImageSuccess");
                        return false;
                    }
                }).into(imgBaiHat);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnAddCaSi.setOnClickListener(v -> {
           OpenDiaLogAddCaSi();
        });

    }

    private void OpenDiaLogAddCaSi() {
        Dialog dialog = new Dialog(UpdateSongActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_casi_baihat);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();

        if (window == null)
            return;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);
        dialog.setCancelable(true);
        dialog.show();
    }

    private final ActivityResultLauncher<Intent> FileAnhResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    uriHinh = data.getData();
                    RealPathHinh = RealPathUtil.getRealPath(this, uriHinh);
                    if (RealPathHinh == null) {
                        Toast.makeText(this, "Không Thể Lấy File", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Picasso.with(this).load(uriHinh).into(imgBaiHat);
                }
            });

    private final ActivityResultLauncher<Intent> FileNhacResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    uriHinh = data.getData();
                    RealPathNhac = RealPathUtil.getRealPath(this, uriHinh);
                    if (RealPathNhac == null) {
                        Toast.makeText(this, "Không Thể Lấy File", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Picasso.with(this).load(uriNhac).into(imgBaiHat);
                }
            });


}
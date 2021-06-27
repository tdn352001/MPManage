package com.example.mpmanage.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.mpmanage.Adapter.AddSongCategoryAdapter;
import com.example.mpmanage.Model.BaiHat;
import com.example.mpmanage.Model.ChuDeTheLoai;
import com.example.mpmanage.R;
import com.example.mpmanage.Service.APIService;
import com.example.mpmanage.Service.DataService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateCategoryActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView Title;
    TextInputEditText edtTitle, edtLink;
    RadioGroup rdHinh;
    MaterialButton btnGetFile, btnFinish, btnCancel, btnAddSong;
    RecyclerView recyclerView;
    RoundedImageView imgCategory;

    ChuDeTheLoai category;
    ChuDeTheLoai tempcategory;
    ArrayList<BaiHat> arrayList;
    ArrayList<BaiHat> temparrayList;
    AddSongCategoryAdapter adapter;
    String loai;
    boolean LoadImageSuccess;
    private String RealPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_category);
        AnhXa();
        GetDataCategory();
    }


    private void GetDataCategory() {
        Intent intent = getIntent();
        category = new ChuDeTheLoai();
        tempcategory = new ChuDeTheLoai();
        arrayList = new ArrayList<>();
        temparrayList = new ArrayList<>();
        SetRv();
        if (intent.hasExtra("loai"))
            loai = intent.getStringExtra("loai");

        if (intent.hasExtra("category")) {
            category = (ChuDeTheLoai) intent.getSerializableExtra("category");
            tempcategory = category;
            edtTitle.setText(category.getTen());
            edtLink.setText(category.getHinh());
            SetImage();
            if (loai.equals("chude"))
                GetDataBaiHatChuDe();
            else
                GetDataBaiHatTheLoai();
        }
        SetToolBar();
    }

    private void SetRv(){
        adapter = new AddSongCategoryAdapter(UpdateCategoryActivity.this, arrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(UpdateCategoryActivity.this, RecyclerView.VERTICAL, false));
    }

    @SuppressLint("SetTextI18n")
    private void SetToolBar() {
        String title;
        if (category.getId() != null)
            title = "Cập Nhật ";
        else
            title = "Thêm ";

        if (loai.equals("chude")) {
            title = title + "Chủ Đề";
            Title.setText("Tên Chủ Đề");
        } else {
            title = title + "Thể Loại";
            Title.setText("Tên Thể Loại");
        }

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);

        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }

    private void AnhXa() {
        toolbar = findViewById(R.id.update_categoty_toolbar);
        Title = findViewById(R.id.tv_ten_category);
        edtLink = findViewById(R.id.edt_link_hinh_poster);
        edtTitle = findViewById(R.id.edt_ten_category);
        rdHinh = findViewById(R.id.rd_hinh_category);
        btnGetFile = findViewById(R.id.btn_get_hinh_category);
        btnAddSong = findViewById(R.id.btn_add_song_category);
        btnFinish = findViewById(R.id.btn_finish_update_category);
        btnCancel = findViewById(R.id.btn_cancel_update_category);
        recyclerView = findViewById(R.id.rv_baihat_category);
        imgCategory = findViewById(R.id.img_hinh_category);

    }

    private void SetImage() {
        Glide.with(getApplicationContext()).load(edtLink.getText().toString().trim())
                .error(R.drawable.ic_image)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        LoadImageSuccess = false;
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        LoadImageSuccess = true;
                        return false;
                    }
                }).into(imgCategory);
    }

    private void GetDataBaiHatChuDe() {
        DataService dataService = APIService.getService();
        Call<List<BaiHat>> callback = dataService.GetBaiHatChuDe(category.getId());
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                arrayList = (ArrayList<BaiHat>) response.body();
                if (arrayList == null)
                    Log.e("BBB", "null");
                SetRv();
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {

            }
        });
    }

    private void GetDataBaiHatTheLoai() {
        DataService dataService = APIService.getService();
        Call<List<BaiHat>> callback = dataService.GetBaiHatTheLoai(category.getId());
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                arrayList = (ArrayList<BaiHat>) response.body();
                if (arrayList == null)
                    arrayList = new ArrayList<>();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {

            }
        });
    }

    private void UpLoadFile(String FileName) {
        if (RealPath == null)
            return;
        Log.e("BBB", "UpLoadFile");
        File file = new File(RealPath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", FileName, requestBody);
        Call<String> callback = APIService.getFile().UploadFile(body);
        callback.enqueue(new Callback<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String Linkroot = "https://filenhacmp3.000webhostapp.com/file/";
                String LinkHinh = Linkroot + FileName;

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

}
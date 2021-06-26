package com.example.mpmanage.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
import com.example.mpmanage.Adapter.UpdateSongSingerAdapter;
import com.example.mpmanage.Fragment.MainFragment.SongFragment;
import com.example.mpmanage.Model.BaiHat;
import com.example.mpmanage.Model.CaSi;
import com.example.mpmanage.Model.RealPathUtil;
import com.example.mpmanage.R;
import com.example.mpmanage.Service.APIService;
import com.example.mpmanage.Service.DataService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mpmanage.Activity.MainActivity.PERMISSION_READ;

public class UpdateSongActivity extends AppCompatActivity {


    BaiHat baiHat;
    public ArrayList<CaSi> caSiArrayList;
    private ArrayList<CaSi> tempArrayList;
    public UpdateSongSingerAdapter updateSongSingerAdapter;
    ProgressDialog progressDialog;

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
        toolbar.setNavigationOnClickListener(v -> {
            if(!CheckSingerChange())
                if(!CheckSongChange())
                {
                    finish();
                    return;
                }
            OpenDialogFinish();
        });
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
            SetImageView(baiHat.getHinhBaiHat());
            caSiArrayList = new ArrayList<>();
            tempArrayList = new ArrayList<>();
            updateSongSingerAdapter = new UpdateSongSingerAdapter(this, caSiArrayList);
            rvCaSi.setAdapter(updateSongSingerAdapter);
            rvCaSi.setLayoutManager(new LinearLayoutManager(UpdateSongActivity.this, RecyclerView.VERTICAL, false));
            for (int i = 0; i < baiHat.getIdCaSi().size(); i++) {
                String idsingerSong = baiHat.getIdCaSi().get(i);
                for (int j = 0; j < MainActivity.caSiArrayList.size(); j++) {
                    String idSinger = MainActivity.caSiArrayList.get(j).getIdCaSi();
                    if (idsingerSong.equals(idSinger)) {
                        caSiArrayList.add(MainActivity.caSiArrayList.get(j));
                        tempArrayList.add(MainActivity.caSiArrayList.get(j));
                        updateSongSingerAdapter.notifyItemInserted(caSiArrayList.size() - 1);
                    }
                }
            }
        }
    }


    private void EventListener() {

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
            {
                Intent intent = new Intent();
                intent.setType("audio/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                FileNhacResult.launch(Intent.createChooser(intent, " Chọn File Nhạc"));
            }
        });


        edtHinhBaiHat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SetImageView(edtHinhBaiHat.getText().toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnAddCaSi.setOnClickListener(v -> {
            OpenDiaLogAddCaSi();
        });

        btnFinish.setOnClickListener(v -> {
            if (CheckValidate()) {
                progressDialog = ProgressDialog.show(UpdateSongActivity.this, "Đang Cập Nhật", " Vui Lòng Chờ");
                if (CheckSongChange()) {

                    if (rdHinh.getCheckedRadioButtonId() == R.id.rd_file_hinh) {
                        String TenHinh =baiHat.getIdBaiHat() + "BaiHat" + edtTenBaiHat.getText().toString().replaceAll(" ", "") + ".jpg";
                        UpLoadFile(RealPathHinh, TenHinh);
                        return;
                    }

                    if (rdNhac.getCheckedRadioButtonId() == R.id.rd_file_nhac) {
                        String TenFile = baiHat.getIdBaiHat() +"file" + edtTenBaiHat.getText().toString().replaceAll(" ", "")  + ".mp3";
                        UpLoadFile(RealPathNhac, TenFile);
                        return;
                    }

                    UpdateSong();
                    return;
                }

                if (CheckSingerChange()) {
                    UpdateSinger();
                    return;
                }

                progressDialog.dismiss();
                finish();
            }


        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!CheckSingerChange())
                    if(!CheckSongChange())
                    {
                        finish();
                        return;
                    }
                OpenDialogFinish();
            }
        });
    }

    private void SetImageView(String url) {
        Glide.with(getApplicationContext()).load(url).error(R.drawable.ic_image).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                LoadImageSuccess = false;
                Log.e("BBB", "setImageFalse");
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                LoadImageSuccess = true;
                return false;
            }
        }).into(imgBaiHat);
    }

    private boolean CheckValidate() {
        if (edtTenBaiHat.getText().toString().trim().equals("")) {
            edtTenBaiHat.setError("Tên Bài Hát Không Được Trống");
            Toast.makeText(this, "Tên Bài Hát Không Được Trống", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (rdHinh.getCheckedRadioButtonId() == R.id.rd_link_hinh) {
            if (!LoadImageSuccess) {
                edtHinhBaiHat.setError("Link Bài Hát Không Hợp Lệ");
                Toast.makeText(this, "Link Bài Hát Không Hợp Lệ", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (rdHinh.getCheckedRadioButtonId() == R.id.rd_file_hinh) {
            if (RealPathHinh == null) {
                Toast.makeText(this, "File Hình Không Hợp Lệ", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (rdNhac.getCheckedRadioButtonId() == R.id.rd_link_nhac) {
            String linkNhac = edtFileBaiHat.getText().toString().trim();
            if (!Patterns.WEB_URL.matcher(linkNhac).matches()) {
                edtFileBaiHat.setError("Link Nhạc Không Hợp Lệ");
                Toast.makeText(this, "Link Nhạc Không Hợp Lệ", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (!((linkNhac.endsWith(".mp3") || linkNhac.endsWith(".m4a")))) {
                edtFileBaiHat.setError("Link Nhạc Không Hợp Lệ");
                Toast.makeText(this, "Link Nhạc Không Hợp Lệ", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (rdNhac.getCheckedRadioButtonId() == R.id.rd_file_nhac) {
            if (RealPathNhac == null) {
                Toast.makeText(this, "File Nhạc Không Hợp Lệ", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if(caSiArrayList.size() == 0){
            Toast.makeText(this, "Phải có ít nhất 1 ca sĩ", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean CheckSongChange() {
        String TenBaiHat = edtTenBaiHat.getText().toString().trim();

        if (!baiHat.getTenBaiHat().equals(TenBaiHat))
            return true;

        if (rdHinh.getCheckedRadioButtonId() == R.id.rd_file_hinh)
            return true;

        String HinhBaiHat = edtHinhBaiHat.getText().toString().trim();
        if (!baiHat.getHinhBaiHat().equals(HinhBaiHat))
            return true;

        if (rdNhac.getCheckedRadioButtonId() == R.id.rd_file_nhac)
            return true;

        String LinkBaiHat = edtFileBaiHat.getText().toString().trim();
        if (!baiHat.getLinkBaiHat().equals(LinkBaiHat))
            return true;

        return false;
    }

    private boolean CheckSingerChange() {
        if (caSiArrayList.equals(tempArrayList))
            return false;

        return true;
    }

    private void UpdateSong() {
        DataService dataService = APIService.getService();
        String TenBaiHat = edtTenBaiHat.getText().toString().trim();
        String HinhBaiHat = edtHinhBaiHat.getText().toString().trim();
        String LinkBaiHat = edtFileBaiHat.getText().toString().trim();
        Call<String> callback = dataService.UpdateSong(baiHat.getIdBaiHat(), TenBaiHat, HinhBaiHat, LinkBaiHat);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                if (result == null) {
                    finish();
                    return;
                }
                if (result.equals("F")) {
                    finish();
                    return;
                }

                if (CheckSingerChange())
                    UpdateSinger();
                else {
                    UpdateArrayList();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void UpdateSinger() {
        Log.e("BBB", "Update Singer");
        DataService dataService = APIService.getService();
        Call<String> callback = dataService.DeleteSongSinger(baiHat.getIdBaiHat());
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                for (int i = 0; i < caSiArrayList.size(); i++) {
                    Call<String> callback2 = dataService.UpdateSongSinger(baiHat.getIdBaiHat(), caSiArrayList.get(i).getIdCaSi());
                    int finalI = i;
                    callback2.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (finalI >= caSiArrayList.size() - 1) {
                                UpdateArrayList();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(UpdateSongActivity.this, "Vui Lòng", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }

    private void UpdateArrayList() {
        baiHat.setTenBaiHat(edtTenBaiHat.getText().toString());
        baiHat.setHinhBaiHat(edtHinhBaiHat.getText().toString());
        baiHat.setLinkBaiHat(edtFileBaiHat.getText().toString());
        ArrayList<String> idCasi = new ArrayList<>();
        ArrayList<String> tenCasi = new ArrayList<>();
        for (int i = 0; i < caSiArrayList.size(); i++) {
            idCasi.add(caSiArrayList.get(i).getIdCaSi());
            tenCasi.add(caSiArrayList.get(i).getTenCaSi());
        }
        baiHat.setIdCaSi(idCasi);
        baiHat.setCaSi(tenCasi);
        SongFragment.UpdateSong(baiHat);
        Toast.makeText(this, "Cập Nhật Thành Công", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
        finish();
    }

    private void UpLoadFile(String RealPath, String FileName) {
        Log.e("BBB", "upload file: " + RealPath);
        if (RealPath == null)
            return;
        File file = new File(RealPath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", FileName, requestBody);
        Call<String> callback = APIService.getFile().UploadFile(body);
        callback.enqueue(new Callback<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (RealPath == RealPathHinh) {
                    String LinkHinh = "https://filenhacmp3.000webhostapp.com/file/";
                    edtHinhBaiHat.setText(LinkHinh + FileName);
                    if (rdNhac.getCheckedRadioButtonId() == R.id.rd_file_nhac) {
                        String TenFile = baiHat.getIdBaiHat()  + "file" + edtTenBaiHat.getText().toString().replaceAll(" ", "") + ".mp3";
                        UpLoadFile(RealPathNhac, TenFile);
                    } else {
                        UpdateSong();
                    }
                } else {
                    String LinkNhac = "https://filenhacmp3.000webhostapp.com/file/";
                    edtFileBaiHat.setText(LinkNhac + FileName);
                    UpdateSong();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
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

        // Các Thành Phần Trong Dialog
        TextView tvFinish;
        SearchView searchView;
        RecyclerView recyclerView;
        RelativeLayout layoutNoinfo;

        // Dữ Liệu
        ArrayList<CaSi> arrayList = MainActivity.caSiArrayList;
        UpdateSongSingerAdapter adapter = new UpdateSongSingerAdapter(UpdateSongActivity.this, arrayList);

        // Ánh Xạ

        tvFinish = dialog.findViewById(R.id.tv_finish_dialog);
        searchView = dialog.findViewById(R.id.search_view_add_ca_si);
        recyclerView = dialog.findViewById(R.id.rv_add_casi_dialog);
        layoutNoinfo = dialog.findViewById(R.id.txt_noinfo_casi);

        // Set Dữ Liệu
        adapter.setDialog(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));

        tvFinish.setOnClickListener(v -> {
            dialog.dismiss();
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                Handler handler = new Handler();
                final int[] i = {0};
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(i[0] >=4)
                            handler.removeCallbacks(this);

                        if(adapter.getItemCount() == 0)
                            layoutNoinfo.setVisibility(View.VISIBLE);
                        else
                            layoutNoinfo.setVisibility(View.GONE);

                        i[0]++;
                    }
                }, 120);

                return true;
            }
        });


        dialog.show();
    }

    private void OpenDialogFinish(){
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(this);
        dialog.setBackground(getResources().getDrawable(R.drawable.custom_diaglog_background));
        dialog.setTitle("Thoát");
        dialog.setIcon(R.drawable.ic_exit);
        dialog.setMessage("Bạn Có Chắc muốn Thoát Mà Không Lưu Kết Quả?");
        dialog.setNegativeButton("Đồng Ý", (dialog1, which) -> {
            finish();
        });

        dialog.setPositiveButton("Hủy", (dialog12, which) -> dialog12.dismiss());
        dialog.show();
    }

    private final ActivityResultLauncher<Intent> FileAnhResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    assert data != null;
                    uriHinh = data.getData();
                    RealPathHinh = RealPathUtil.getRealPath(this, uriHinh);
                    if (RealPathHinh == null) {
                        Toast.makeText(this, "Không Thể Lấy File", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(this, "Lấy File Thành Công", Toast.LENGTH_SHORT).show();
                    Picasso.with(this).load(uriHinh).into(imgBaiHat);
                }
            });

    private final ActivityResultLauncher<Intent> FileNhacResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    assert data != null;
                    uriNhac = data.getData();
                    RealPathNhac = RealPathUtil.getRealPath(this, uriNhac);
                    if (RealPathNhac == null) {
                        Toast.makeText(this, "Không Thể Lấy File", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Toast.makeText(this, "Lấy File Thành Công", Toast.LENGTH_SHORT).show();
                }
            });


    @Override
    public void onBackPressed() {
        if(!CheckSingerChange())
            if(!CheckSingerChange())
            {
                finish();
                return;
            }
        OpenDialogFinish();
    }
}
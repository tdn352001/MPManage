package com.example.mpmanage.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.mpmanage.Adapter.BannerSongAdapter;
import com.example.mpmanage.Fragment.MainFragment.BannerFragment;
import com.example.mpmanage.Fragment.MainFragment.SongFragment;
import com.example.mpmanage.Model.BaiHat;
import com.example.mpmanage.Model.QuangCao;
import com.example.mpmanage.Model.RealPathUtil;
import com.example.mpmanage.R;
import com.example.mpmanage.Service.APIService;
import com.example.mpmanage.Service.DataService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateBannerActivity extends AppCompatActivity {

    Toolbar toolbar;
    RadioGroup radioGroup;
    TextInputEditText edtPoster, edtContent;
    MaterialButton btnFinish, btnCancel, btnChange, btnPoster;
    RoundedImageView imgPoster;
    RecyclerView recyclerView;

    QuangCao banner;
    QuangCao tempbanner;
    public ArrayList<BaiHat> baiHats;
    public BannerSongAdapter adapter;
    String RealPath;
    boolean LoadImageSuccess;
    Uri uriHinh;
    public Dialog dialog;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_banner);
        overridePendingTransition(R.anim.from_right, R.anim.to_left);
        AnhXa();
        SetUpData();
        EventListener();
    }


    private void AnhXa() {
        toolbar = findViewById(R.id.update_banner_toolbar);
        radioGroup = findViewById(R.id.rd_hinh_poster);
        edtContent = findViewById(R.id.edt_content_banner);
        edtPoster = findViewById(R.id.edt_link_hinh_poster);
        btnFinish = findViewById(R.id.btn_finish_update_quang_cao);
        btnCancel = findViewById(R.id.btn_cancel_update_quang_cao);
        btnChange = findViewById(R.id.btn_add_song_banner);
        btnPoster = findViewById(R.id.btn_get_hinh_poster);
        imgPoster = findViewById(R.id.img_hinh_poster);
        recyclerView = findViewById(R.id.rv_baihat_banner);
    }

    private void SetImage() {
        Glide.with(getApplicationContext()).load(edtPoster.getText().toString().trim())
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
                }).into(imgPoster);
    }

    @SuppressLint("SetTextI18n")
    private void SetUpData() {
        Intent intent = getIntent();
        baiHats = new ArrayList<>();
        banner = new QuangCao();
        tempbanner = new QuangCao();
        adapter = new BannerSongAdapter(UpdateBannerActivity.this, baiHats);


        if (intent.hasExtra("banner")) {
            banner = (QuangCao) intent.getSerializableExtra("banner");
            tempbanner = banner;
            adapter.setUpdate(true);
            int Pos;
            BaiHat baiHat = null;
            if (SongFragment.baiHatArrayList != null) {
                Pos = SongFragment.GetPositionById(banner.getIdBaiHat());
                if (Pos != -1)
                    baiHat = SongFragment.baiHatArrayList.get(Pos);
            } else {
                Pos = MainActivity.GetPostionBaiHatById(banner.getIdBaiHat());
                if (Pos != -1)
                    baiHat = MainActivity.baiHatArrayList.get(Pos);
            }
            if (baiHat != null) {
                baiHats.add(baiHat);
                btnChange.setText("Đổi Bài Hát");
            }
            edtPoster.setText(banner.getHinhAnh());
            edtContent.setText(banner.getNoiDung());
            SetImage();
            SetToolBar("Cập Nhật Quảng Cáo");
        } else {
            adapter.setUpdate(false);
            SetToolBar("Thêm Quảng Cáo");
            btnChange.setText("Chọn Bài Hát");
        }

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));

    }

    private void SetToolBar(String title) {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);

        toolbar.setNavigationOnClickListener(v -> {
            if (banner.getIdQuangCao() == null) {
                OpenDialogFinish();
                return;
            }
            if (CheckChange()) {
                OpenDialogFinish();
            } else
                finish();
        });
    }

    private void EventListener() {
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rd_link_hinh_poster) {
                SetImage();
                edtPoster.setVisibility(View.VISIBLE);
                btnPoster.setVisibility(View.GONE);
            } else {
                edtPoster.setVisibility(View.GONE);
                btnPoster.setVisibility(View.VISIBLE);
                if (RealPath == null) {
                    imgPoster.setImageResource(R.drawable.ic_image);
                    return;
                }
                Glide.with(getApplicationContext()).load(uriHinh).into(imgPoster);

            }
        });

        btnPoster.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            FileAnhResult.launch(Intent.createChooser(intent, " Chọn Hình Ảnh"));
        });

        btnChange.setOnClickListener(v -> OpenDialogChangeBannerSong());

        edtPoster.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SetImage();
            }
        });

        btnFinish.setOnClickListener(v -> {
            if (CheckVaidate()) {
                progressDialog = ProgressDialog.show(UpdateBannerActivity.this, "Đang Cập Nhật", "Vui Lòng Chờ");
                if (banner.getIdBaiHat() != null) {
                    if (CheckChange()) {
                        // Có Thay đổi
                        if (radioGroup.getCheckedRadioButtonId() == R.id.rd_link_hinh_poster) {
                            UpdateBanner();
                        } else {
                            String FileName = banner.getIdBaiHat() + "poster" + banner.getTenBaiHat() + ".jpg";
                            UpLoadFile(FileName);
                        }
                    } else {
                        //Không Thay đổi
                        progressDialog.dismiss();
                        finish();
                    }
                } else {
                    if (radioGroup.getCheckedRadioButtonId() == R.id.rd_link_hinh_poster)
                        AddBanner();
                    else {
                        String FileName = banner.getIdBaiHat() + "poster" + banner.getTenBaiHat() + System.currentTimeMillis() + ".jpg";
                        UpLoadFile(FileName);
                    }
                }
            }
        });

        btnCancel.setOnClickListener(v -> {
            if (banner.getIdQuangCao() == null) {
                OpenDialogFinish();
                return;
            }
            if (CheckChange()) {
                OpenDialogFinish();
            } else
                finish();
        });
    }

    private void AddBanner() {
        DataService dataService = APIService.getService();
        Call<String> callback = dataService.AddBanner(edtPoster.getText().toString(), edtContent.getText().toString(), baiHats.get(0).getIdBaiHat());
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String id = response.body();
                if (id != null) {
                    if (!id.equals("F")) {
                        banner.setIdQuangCao(id);
                        banner.setNoiDung(edtContent.getText().toString().trim());
                        banner.setHinhAnh(edtPoster.getText().toString().trim());
                        banner.setIdBaiHat(baiHats.get(0).getIdBaiHat());
                        banner.setHinhBaiHat(baiHats.get(0).getHinhBaiHat());
                        banner.setTenBaiHat(baiHats.get(0).getTenBaiHat());
                        BannerFragment.AddBanner(banner);
                        Toast.makeText(UpdateBannerActivity.this, "Cập Nhật Thành Công", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        finish();
                        return;
                    }
                }
                Toast.makeText(UpdateBannerActivity.this, "Cập Nhật Thất Bại", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                finish();

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(UpdateBannerActivity.this, "Cập Nhật Thất Bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void UpdateBanner() {
        banner.setNoiDung(edtContent.getText().toString().trim());
        banner.setHinhAnh(edtPoster.getText().toString().trim());
        banner.setIdBaiHat(baiHats.get(0).getIdBaiHat());
        banner.setHinhBaiHat(baiHats.get(0).getHinhBaiHat());
        banner.setTenBaiHat(baiHats.get(0).getTenBaiHat());
        DataService dataService = APIService.getService();
        Call<String> callback = dataService.UpdateBanner(banner.getIdQuangCao(), banner.getHinhAnh(), banner.getNoiDung(), banner.getIdBaiHat());
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                BannerFragment.UpdateBaner(banner);
                progressDialog.dismiss();
                finish();
                Toast.makeText(UpdateBannerActivity.this, "Cập Nhật Thành Công", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
                finish();
                Toast.makeText(UpdateBannerActivity.this, "Cập Nhật Thất Bại", Toast.LENGTH_SHORT).show();
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
                String Linkroot = "http://192.168.1.3/PlayerMusicProject/Server/Client/image/";
                String LinkHinh = Linkroot + FileName;
                edtPoster.setText(LinkHinh);
                if (banner.getIdQuangCao() == null) {
                    AddBanner();
                } else {
                    UpdateBanner();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }


    public void OpenDialogChangeBannerSong() {
        dialog = null;
        dialog = new Dialog(UpdateBannerActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_change_baihat_quangcao);
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

        ArrayList<BaiHat> arrayList = MainActivity.baiHatArrayList;
        BannerSongAdapter bannerSongAdapter = new BannerSongAdapter(UpdateBannerActivity.this, arrayList);
        bannerSongAdapter.setDialog(true);

        tvFinish = dialog.findViewById(R.id.tv_finish_dialog);
        searchView = dialog.findViewById(R.id.search_view_change_song);
        recyclerView = dialog.findViewById(R.id.rv_change_song_dialog);
        layoutNoinfo = dialog.findViewById(R.id.txt_noinfo_baihat);

        tvFinish.setOnClickListener(v -> {
            dialog.dismiss();
        });

        recyclerView.setAdapter(bannerSongAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                bannerSongAdapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                bannerSongAdapter.getFilter().filter(newText);
                Handler handler = new Handler();
                final int[] i = {0};
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (i[0] >= 4)
                            handler.removeCallbacks(this);

                        if (bannerSongAdapter.getItemCount() == 0)
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

    private void OpenDialogFinish() {
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

    public boolean CheckVaidate() {
        if (edtContent.getText().length() < 10) {
            Toast.makeText(this, "Nội Dung tối thiểu có 10 kí tự", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (radioGroup.getCheckedRadioButtonId() == R.id.rd_file_hinh_poster) {
            if (RealPath == null) {
                Toast.makeText(this, "Chưa Chọn File Poster", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            if (!LoadImageSuccess) {
                Toast.makeText(this, "Link Poster Không Hợp Lệ", Toast.LENGTH_SHORT).show();
                return false;
            }

        }
        if (baiHats == null) {
            Toast.makeText(this, "Chưa Chọn Bài Hát", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (baiHats.size() == 0) {
            Toast.makeText(this, "Chưa Chọn Bài Hát", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (banner.getIdQuangCao() == null) {
            if (baiHats.get(0).getIdBaiHat() != null) {
                Log.e("BBBB", "Cheeck exit, " + baiHats.get(0).getTenBaiHat());
                for (int i = 0; i < MainActivity.quangCaoArrayList.size(); i++)
                    if (MainActivity.quangCaoArrayList.get(i).getIdBaiHat().equals(baiHats.get(0).getIdBaiHat())) {
                        Toast.makeText(this, "Bài Hát Đã Được Quảng Cáo", Toast.LENGTH_SHORT).show();
                        return false;
                    }
            }
        }

        return true;
    }

    public boolean CheckChange() {
        if (radioGroup.getCheckedRadioButtonId() == R.id.rd_file_hinh_poster)
            return true;
        else {
            if (!edtPoster.getText().toString().trim().equals(tempbanner.getHinhAnh()))
                return true;
        }
        if (!edtContent.getText().toString().trim().equals(tempbanner.getNoiDung()))
            return true;
        if (!baiHats.get(0).getIdBaiHat().equals(tempbanner.getIdBaiHat()))
            return true;

        return false;
    }

    private final ActivityResultLauncher<Intent> FileAnhResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    assert data != null;
                    uriHinh = data.getData();
                    RealPath = RealPathUtil.getRealPath(this, uriHinh);
                    if (RealPath == null) {
                        Toast.makeText(this, "Không Thể Lấy File", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(this, "Lấy File Thành Công", Toast.LENGTH_SHORT).show();
                    Glide.with(this).load(uriHinh).into(imgPoster);
                }
            });


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.from_left, R.anim.to_right);

    }
}
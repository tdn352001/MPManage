package com.example.mpmanage.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.mpmanage.Fragment.MainFragment.SingerFragment;
import com.example.mpmanage.Model.CaSi;
import com.example.mpmanage.Model.RealPathUtil;
import com.example.mpmanage.R;
import com.example.mpmanage.Service.APIService;
import com.example.mpmanage.Service.DataService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateCaSiActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextInputEditText edtTitle, edtLink;
    RadioGroup rdHinh;
    MaterialButton btnGetFile, btnFinish, btnCancel;
    CircleImageView imgCaSi;
    ProgressDialog progressDialog;
    private CaSi caSi;
    private boolean LoadImageSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_ca_si);
        overridePendingTransition(R.anim.from_right, R.anim.to_left);
        AnhXa();
        GetDataCaSi();
        EventListener();
    }

    private void AnhXa() {
        toolbar = findViewById(R.id.update_casi_toolbar);
        edtLink = findViewById(R.id.edt_link_hinh_casi);
        edtTitle = findViewById(R.id.edt_ten_casi);
        rdHinh = findViewById(R.id.rd_hinh_casi);
        btnGetFile = findViewById(R.id.btn_get_hinh_casi);
        btnFinish = findViewById(R.id.btn_finish_update_casi);
        btnCancel = findViewById(R.id.btn_cancel_update_casi);
        imgCaSi = findViewById(R.id.img_hinh_casi);
    }

    private void GetDataCaSi() {
        Intent intent = getIntent();
        caSi = new CaSi();
        if (intent.hasExtra("casi")) {
            caSi = (CaSi) intent.getSerializableExtra("casi");
            edtTitle.setText(caSi.getTenCaSi());
            edtLink.setText(caSi.getHinhCaSi());
            SetImage();
        }
        SetUpToolBar();
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
                }).into(imgCaSi);
    }

    private void SetUpToolBar() {
        String title;
        if (caSi.getIdCaSi() == null)
            title = "Thêm ";
        else
            title = "Cập Nhật ";
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title + "Ca Sĩ");

        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }

    private void EventListener() {
        edtLink.addTextChangedListener(new TextWatcher() {
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

        rdHinh.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rd_link_hinh_casi) {
                btnGetFile.setVisibility(View.GONE);
                edtLink.setVisibility(View.VISIBLE);
                SetImage();
            } else {
                edtLink.setVisibility(View.GONE);
                btnGetFile.setVisibility(View.VISIBLE);
                if (RealPath == null) {
                    imgCaSi.setImageResource(R.drawable.ic_image);
                    return;
                }
                Glide.with(UpdateCaSiActivity.this).load(uriHinh).into(imgCaSi);
            }
        });

        btnGetFile.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            FileAnhResult.launch(Intent.createChooser(intent, " Chọn Hình Ảnh"));
        });


        btnFinish.setOnClickListener(v -> {
            if (CheckValidate()) {
                progressDialog = ProgressDialog.show(UpdateCaSiActivity.this, "Đang Cập Nhật", " Vui Lòng Chờ");
                if (caSi.getIdCaSi() == null) {
                    if (rdHinh.getCheckedRadioButtonId() == R.id.rd_file_hinh_casi) {
                        UpLoadFile(edtTitle.getText().toString() + System.currentTimeMillis() + ".jpg");
                    } else {
                        AddSinger();
                    }
                } else {
                    if (CheckChange()) {
                        if (rdHinh.getCheckedRadioButtonId() == R.id.rd_file_hinh_casi) {
                            UpLoadFile(caSi.getIdCaSi() + "CaSi" + caSi.getTenCaSi() + ".jpg");
                        } else {
                            UpdateCaSi();
                        }
                    } else {
                        Log.e("BBB", "Không thay đổi");
                        progressDialog.dismiss();
                        finish();
                    }
                }
            }
        });

        btnCancel.setOnClickListener(v -> {
            if (CheckChange()) {
                OpenDialogFinish();
            } else
                finish();
        });
    }

    private void UpdateCaSi() {
        caSi.setTenCaSi(edtTitle.getText().toString());
        caSi.setHinhCaSi(edtLink.getText().toString());
        DataService dataService = APIService.getService();
        Call<String> callback = dataService.UpdateSinger(caSi.getIdCaSi(), caSi.getTenCaSi(), caSi.getHinhCaSi());
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                SingerFragment.UpdateCaSi(caSi);
                Toast.makeText(UpdateCaSiActivity.this, "Cập Nhật Thành Công", Toast.LENGTH_SHORT).show();
                finish();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(UpdateCaSiActivity.this, "Cập Nhật Thất Bại", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                Log.e("BBBB", t.getMessage());
            }
        });
    }

    private void AddSinger() {
        caSi.setTenCaSi(edtTitle.getText().toString());
        caSi.setHinhCaSi(edtLink.getText().toString());
        DataService dataService = APIService.getService();
        Call<String> callback = dataService.AddSinger(caSi.getTenCaSi(), caSi.getHinhCaSi());
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String id = response.body();
                if (id != null) {
                    caSi.setIdCaSi(id);
                    SingerFragment.AddCaSi(caSi);
                    Toast.makeText(UpdateCaSiActivity.this, "Cập Nhật Thành Công", Toast.LENGTH_SHORT).show();
                    finish();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(UpdateCaSiActivity.this, "Cập Nhật Thất Bại", Toast.LENGTH_SHORT).show();
                Log.e("BBB", "add album failed");
            }
        });
    }

    private void UpLoadFile(String FileName) {
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
                String Linkroot = "https://filenhacmp3.000webhostapp.com/file/";
                String LinkHinh = Linkroot + FileName;
                edtLink.setText(LinkHinh);
                if (caSi.getIdCaSi() == null)
                    AddSinger();
                else
                    UpdateCaSi();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    private boolean CheckValidate() {
        if (edtTitle.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Tên Ca Sĩ Trống!!!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (rdHinh.getCheckedRadioButtonId() == R.id.rd_link_hinh_casi) {
            if (!LoadImageSuccess) {
                Toast.makeText(this, "Link Hình Không Hợp Lệ", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (rdHinh.getCheckedRadioButtonId() == R.id.rd_file_hinh_casi) {
            if (RealPath == null) {
                Toast.makeText(this, "Không Tìm Được File Hình", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }


    private boolean CheckChange() {
        if (!edtTitle.getText().toString().equals(caSi.getTenCaSi()))
            return true;

        if (rdHinh.getCheckedRadioButtonId() == R.id.rd_file_hinh_casi)
            return true;


        if (rdHinh.getCheckedRadioButtonId() == R.id.rd_link_hinh_casi) {
            if (!edtLink.getText().toString().equals(caSi.getHinhCaSi()))
                return true;
        }

        return false;
    }


    private void OpenDialogFinish() {
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(this);
        dialog.setBackground(getResources().getDrawable(R.drawable.custom_diaglog_background));
        dialog.setTitle("Thoát");
        dialog.setIcon(R.drawable.ic_exit);
        dialog.setMessage("Bạn có chắc muốn thoát?");
        dialog.setNegativeButton("Đồng Ý", (dialog1, which) -> {
            finish();
        });

        dialog.setPositiveButton("Hủy", (dialog12, which) -> dialog12.dismiss());
        dialog.show();
    }


    private Uri uriHinh;
    private String RealPath;
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
                    Glide.with(this).load(uriHinh).into(imgCaSi);
                }
            });


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.from_left, R.anim.to_right);
    }
}
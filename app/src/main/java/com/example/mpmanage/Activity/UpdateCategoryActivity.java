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
import com.example.mpmanage.Adapter.AddSongCategoryAdapter;
import com.example.mpmanage.Fragment.MainFragment.CategoryFragment;
import com.example.mpmanage.Model.BaiHat;
import com.example.mpmanage.Model.ChuDeTheLoai;
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
    ProgressDialog progressDialog;

    ChuDeTheLoai category;
    ChuDeTheLoai tempcategory;
    public ArrayList<BaiHat> arrayList;
    private ArrayList<BaiHat> temparrayList;
    public AddSongCategoryAdapter adapter;
    String loai;
    boolean LoadImageSuccess;
    private String RealPath;
    private Uri uriHinh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_category);
        overridePendingTransition(R.anim.from_right, R.anim.to_left);
        AnhXa();
        GetDataCategory();
        EventListener();
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

    private void SetRv() {
        adapter = new AddSongCategoryAdapter(UpdateCategoryActivity.this, arrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(UpdateCategoryActivity.this, RecyclerView.VERTICAL, false));
    }

    @SuppressLint("SetTextI18n")
    private void SetToolBar() {
        String title;
        if (category.getId() != null)
            title = "C???p Nh???t ";
        else
            title = "Th??m ";

        if (loai.equals("chude")) {
            title = title + "Ch??? ?????";
            Title.setText("T??n Ch??? ?????");
        } else {
            title = title + "Th??? Lo???i";
            Title.setText("T??n Th??? Lo???i");
        }

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);

        toolbar.setNavigationOnClickListener(v -> {
            if (category.getId() == null) {
                OpenDialogFinish();
                return;
            }
            if (CheckChange()) {
                OpenDialogFinish();
            } else
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
                for (BaiHat baiHat : arrayList)
                    temparrayList.add(baiHat);
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
                for (BaiHat baiHat : arrayList)
                    temparrayList.add(baiHat);
                SetRv();
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {

            }
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
            if (checkedId == R.id.rd_link_hinh_category) {
                btnGetFile.setVisibility(View.GONE);
                edtLink.setVisibility(View.VISIBLE);
                SetImage();
            } else {
                edtLink.setVisibility(View.GONE);
                btnGetFile.setVisibility(View.VISIBLE);
                if (RealPath == null) {
                    imgCategory.setImageResource(R.drawable.ic_image);
                    return;
                }
                Glide.with(getApplicationContext()).load(uriHinh).into(imgCategory);
            }
        });

        btnGetFile.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            FileAnhResult.launch(Intent.createChooser(intent, " Ch???n H??nh ???nh"));
        });

        btnAddSong.setOnClickListener(v -> {
            OpenDialogAddSong();
        });

        btnFinish.setOnClickListener(v -> {
            if (CheckValidate()) {
                progressDialog = ProgressDialog.show(UpdateCategoryActivity.this, "??ang C???p Nh???t", " Vui L??ng Ch???");
                if (category.getId() == null) {
                    if (rdHinh.getCheckedRadioButtonId() == R.id.rd_file_hinh_category) {
                        UpLoadFile(loai + category.getTen() + System.currentTimeMillis() + ".jpg");
                    } else {
                        AddCategory();
                    }
                } else {
                    if (CheckChange()) {
                        if (rdHinh.getCheckedRadioButtonId() == R.id.rd_file_hinh_category) {
                            UpLoadFile(category.getId() + loai + category.getTen() + ".jpg");
                        } else {
                            UpdateCategory();
                        }
                    } else {
                        progressDialog.dismiss();
                        finish();
                    }
                }
            }
        });

        btnCancel.setOnClickListener(v -> {
            if (category.getId() == null) {
                OpenDialogFinish();
                return;
            }
            if (CheckChange()) {
                OpenDialogFinish();
            } else
                finish();
        });
    }

    @SuppressLint("SetTextI18n")
    private void OpenDialogAddSong() {
        Dialog dialog = new Dialog(UpdateCategoryActivity.this);
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

        // C??c Th??nh Ph???n Trong Dialog
        ArrayList<BaiHat> baiHats;
        AddSongCategoryAdapter songAdapter;
        TextView tvFinish, tvTitle;
        SearchView searchView;
        RecyclerView recyclerView;
        RelativeLayout layoutNoinfo;

        tvTitle = dialog.findViewById(R.id.title_dialog);
        tvFinish = dialog.findViewById(R.id.tv_finish_dialog);
        searchView = dialog.findViewById(R.id.search_view_change_song);
        recyclerView = dialog.findViewById(R.id.rv_change_song_dialog);
        layoutNoinfo = dialog.findViewById(R.id.txt_noinfo_baihat);

        baiHats = MainActivity.baiHatArrayList;
        songAdapter = new AddSongCategoryAdapter(UpdateCategoryActivity.this, baiHats);
        songAdapter.setDialog(true);
        recyclerView.setAdapter(songAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(UpdateCategoryActivity.this, RecyclerView.VERTICAL, false));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                songAdapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                songAdapter.getFilter().filter(newText);
                Handler handler = new Handler();
                final int[] i = {0};
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (i[0] >= 4)
                            handler.removeCallbacks(this);

                        if (songAdapter.getItemCount() == 0)
                            layoutNoinfo.setVisibility(View.VISIBLE);
                        else
                            layoutNoinfo.setVisibility(View.GONE);

                        i[0]++;
                    }
                }, 120);
                return true;
            }
        });
        tvFinish.setText(R.string.xong);
        tvFinish.setOnClickListener(v -> dialog.dismiss());
        tvTitle.setText(R.string.add_song);
        dialog.show();
    }

    private void OpenDialogFinish() {
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(this);
        dialog.setBackground(getResources().getDrawable(R.drawable.custom_diaglog_background));
        dialog.setTitle("Tho??t");
        dialog.setIcon(R.drawable.ic_exit);
        dialog.setMessage("B???n C?? Ch???c mu???n Tho??t M?? Kh??ng L??u K???t Qu????");
        dialog.setNegativeButton("?????ng ??", (dialog1, which) -> {
            finish();
        });

        dialog.setPositiveButton("H???y", (dialog12, which) -> dialog12.dismiss());
        dialog.show();
    }

    public boolean CheckAddBefore(BaiHat baiHat) {
        for (BaiHat baiHat1 : arrayList) {
            if (baiHat1.getIdBaiHat().equals(baiHat.getIdBaiHat()))
                return true;
        }

        return false;
    }

    public void AddSong(BaiHat baihat) {
        arrayList.add(baihat);
        adapter.notifyDataSetChanged();
    }

    public void DeleteSong(BaiHat baiHat) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getIdBaiHat().equals(baiHat.getIdBaiHat())) {
                arrayList.remove(i);
                adapter.notifyItemRemoved(i);
                adapter.notifyItemRangeChanged(i, arrayList.size());
                return;
            }
        }
    }

    private boolean CheckValidate() {
        if (edtTitle.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Ti??u ????? Tr???ng!!!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (rdHinh.getCheckedRadioButtonId() == R.id.rd_link_hinh_category) {
            if (!LoadImageSuccess) {
                Toast.makeText(this, "Link H??nh Kh??ng H???p L???", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (rdHinh.getCheckedRadioButtonId() == R.id.rd_file_hinh_category) {
            if (RealPath == null) {
                Toast.makeText(this, "Kh??ng T??m ???????c File H??nh", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (arrayList.size() == 0) {
            Toast.makeText(this, "Ph???i c?? ??t nh???t m???t b??i h??t trong danh s??ch", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean CheckChange() {
        if (!edtTitle.getText().toString().equals(category.getTen()))
            return true;

        if (rdHinh.getCheckedRadioButtonId() == R.id.rd_file_hinh_category)
            return true;


        if (rdHinh.getCheckedRadioButtonId() == R.id.rd_link_hinh_category) {
            if (!edtLink.getText().toString().equals(category.getHinh()))
                return true;
        }

        if (!arrayList.equals(temparrayList))
            return true;


        return false;
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
                if (category.getId() == null)
                    AddCategory();
                else
                    UpdateCategory();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    private void AddCategory() {
        category.setTen(edtTitle.getText().toString().trim());
        category.setHinh(edtLink.getText().toString().trim());
        DataService dataService = APIService.getService();
        Call<String> callback;
        if (loai.equals("chude"))
            callback = dataService.AddChuDe(category.getTen(), category.getHinh());
        else
            callback = dataService.AddTheLoai(category.getTen(), category.getHinh());

        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String Id = response.body();
                if (Id != null) {
                    category.setId(Id);
                    CategoryFragment.AddCategory(loai, category);
                    UpdateBaiHatCategory();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(UpdateCategoryActivity.this, "Th??m Th???t B???i", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(UpdateCategoryActivity.this, "Th??m Th???t B???i", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void UpdateCategory() {
        category.setTen(edtTitle.getText().toString().trim());
        category.setHinh(edtLink.getText().toString().trim());
        DataService dataService = APIService.getService();
        Call<String> callback;
        if (loai.equals("chude"))
            callback = dataService.UpdateChuDe(category.getId(), category.getTen(), category.getHinh());
        else
            callback = dataService.UpdateTheLoai(category.getId(), category.getTen(), category.getHinh());

        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                CategoryFragment.UpdateCategoty(loai, category);
                if (!arrayList.equals(temparrayList)) {
                    DeleteBaiHatCategory();
                    return;
                }
                finish();
                progressDialog.dismiss();
                Toast.makeText(UpdateCategoryActivity.this, "C???p Nh???t Th??nh C??ng", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    private void DeleteBaiHatCategory() {
        DataService dataService = APIService.getService();
        Call<String> callback;
        if (loai.equals("chude")) {
            callback = dataService.DeleteBaiHatChuDe(category.getId());
        } else
            callback = dataService.DeleteBaiHatTheLoai(category.getId());

        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UpdateBaiHatCategory();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });

    }

    private void UpdateBaiHatCategory() {
        DataService dataService = APIService.getService();
        if (loai.equals("chude")) {
            for (int i = 0; i < arrayList.size(); i++) {
                Call<String> callback = dataService.UpdateBaiHatChuDe(category.getId(), arrayList.get(i).getIdBaiHat());
                int finalI = i;
                callback.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (finalI >= arrayList.size() - 1) {
                            finish();
                            progressDialog.dismiss();
                            Toast.makeText(UpdateCategoryActivity.this, "C???p Nh???t Th??nh C??ng", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("BBB", "Update B??i H??t Th???t B???i");
                    }
                });
            }
        } else {
            for (int i = 0; i < arrayList.size(); i++) {
                Call<String> callback = dataService.UpdateBaiHatTheLoai(category.getId(), arrayList.get(i).getIdBaiHat());
                int finalI = i;
                callback.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (finalI >= arrayList.size() - 1) {
                            finish();
                            progressDialog.dismiss();
                            Toast.makeText(UpdateCategoryActivity.this, "C???p Nh???t Th??nh C??ng", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("BBB", "Update Th???t B???i");
                    }
                });
            }
        }

    }

    private final ActivityResultLauncher<Intent> FileAnhResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    assert data != null;
                    uriHinh = data.getData();
                    RealPath = RealPathUtil.getRealPath(this, uriHinh);
                    if (RealPath == null) {
                        Toast.makeText(this, "Kh??ng Th??? L???y File", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(this, "L???y File Th??nh C??ng", Toast.LENGTH_SHORT).show();
                    Glide.with(this).load(uriHinh).into(imgCategory);
                }
            });

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.from_left, R.anim.to_right);
    }
}
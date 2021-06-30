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
import com.example.mpmanage.Adapter.SingerAlbumAdapter;
import com.example.mpmanage.Adapter.SongAlbumAdapter;
import com.example.mpmanage.Fragment.MainFragment.AlbumFragment;
import com.example.mpmanage.Model.Album;
import com.example.mpmanage.Model.BaiHat;
import com.example.mpmanage.Model.CaSi;
import com.example.mpmanage.Model.RealPathUtil;
import com.example.mpmanage.R;
import com.example.mpmanage.Service.APIService;
import com.example.mpmanage.Service.DataService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateAlbumActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextInputEditText edtTitle, edtLink;
    RadioGroup rdHinh;
    MaterialButton btnGetFile, btnFinish, btnCancel, btnAddSong;
    RecyclerView recyclerView;
    RoundedImageView imgAlbum;
    TextView txtCaSi;
    CircleImageView imgCaSi;
    ProgressDialog progressDialog;
    Album album;
    public CaSi caSi;
    public ArrayList<BaiHat> arrayList;
    ArrayList<BaiHat> temparrayList;
    ArrayList<BaiHat> casiarraylist;
    boolean LoadImageSuccess;
    private String RealPath;
    private Uri uriHinh;
    SongAlbumAdapter adapter;
    Dialog dialogcs;
    boolean isSingerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_album);
        AnhXa();
        GetDataAlbum();
        SetUpToolBar();
        EventListener();
    }


    private void AnhXa() {
        toolbar = findViewById(R.id.update_album_toolbar);
        edtLink = findViewById(R.id.edt_link_hinh_album);
        edtTitle = findViewById(R.id.edt_ten_album);
        rdHinh = findViewById(R.id.rd_hinh_album);
        btnGetFile = findViewById(R.id.btn_get_hinh_album);
        btnAddSong = findViewById(R.id.btn_add_song_album);
        btnFinish = findViewById(R.id.btn_finish_update_album);
        btnCancel = findViewById(R.id.btn_cancel_update_album);
        recyclerView = findViewById(R.id.rv_baihat_album);
        imgAlbum = findViewById(R.id.img_hinh_album);
        txtCaSi = findViewById(R.id.tv_casi);
        imgCaSi = findViewById(R.id.img_hinh_casi_album);

    }

    private void GetDataAlbum() {
        Intent intent = getIntent();
        album = new Album();
        caSi = new CaSi();
        if (intent.hasExtra("casifragment"))
            isSingerFragment = true;
        else
            isSingerFragment = false;
        if (intent.hasExtra("album")) {
            album = (Album) intent.getSerializableExtra("album");
            edtTitle.setText(album.getTenAlbum());
            edtLink.setText(album.getHinhAlbum());
            SetImage();
            GetBaiHatAlbum();
        }

    }

    private void GetBaiHatAlbum() {
        temparrayList = new ArrayList<>();
        Call<List<BaiHat>> callback = APIService.getService().GetBaiHatAlbum(album.getIdAlbum());
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                arrayList = (ArrayList<BaiHat>) response.body();
                if (arrayList == null)
                    arrayList = new ArrayList<>();
                for (BaiHat baiHat : arrayList)
                    temparrayList.add(baiHat);
                GetDataCaSi();
                SetRV();
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void GetDataCaSi() {
        for (int i = 0; i < MainActivity.caSiArrayList.size(); i++) {
            if (MainActivity.caSiArrayList.get(i).getIdCaSi().equals(album.getIdCaSi())) {
                caSi = MainActivity.caSiArrayList.get(i);
                GetDataBaiHatCaSi();
                txtCaSi.setText("Ca Sĩ: " + caSi.getTenCaSi());
                Glide.with(getApplicationContext()).load(caSi.getHinhCaSi()).error(R.drawable.song).into(imgCaSi);
                break;
            }
        }
    }

    private void GetDataBaiHatCaSi() {
        Call<List<BaiHat>> callback = APIService.getService().GetBaiHatCaSi(caSi.getIdCaSi());
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                casiarraylist = (ArrayList<BaiHat>) response.body();
                if (casiarraylist == null)
                    casiarraylist = new ArrayList<>();
                btnAddSong.setClickable(true);
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {

            }
        });
    }

    private void SetRV() {
        adapter = new SongAlbumAdapter(UpdateAlbumActivity.this, arrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(UpdateAlbumActivity.this, RecyclerView.VERTICAL, false));
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
                }).into(imgAlbum);
    }

    private void SetUpToolBar() {
        String title;
        if (album.getIdAlbum() == null)
            title = "Thêm ";
        else
            title = "Cập Nhật ";
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title + "Album");

        toolbar.setNavigationOnClickListener(v -> {
            if (CheckChange()) {
                OpenDialogFinish();
            } else
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
            if (checkedId == R.id.rd_link_hinh_album) {
                btnGetFile.setVisibility(View.GONE);
                edtLink.setVisibility(View.VISIBLE);
                SetImage();
            } else {
                edtLink.setVisibility(View.GONE);
                btnGetFile.setVisibility(View.VISIBLE);
                if (RealPath == null) {
                    imgAlbum.setImageResource(R.drawable.ic_image);
                    return;
                }
                Picasso.with(getApplicationContext()).load(uriHinh).into(imgAlbum);
            }
        });

        btnGetFile.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            FileAnhResult.launch(Intent.createChooser(intent, " Chọn Hình Ảnh"));
        });

        btnAddSong.setOnClickListener(v -> {
            if (caSi == null) {
                Toast.makeText(this, "Vui Lòng Chọn Ca Sĩ", Toast.LENGTH_SHORT).show();
            } else {
                if (caSi.getIdCaSi() == null)
                    Toast.makeText(this, "Vui Lòng Chọn Ca Sĩ", Toast.LENGTH_SHORT).show();
                else
                    OpenDialogAddSong();
            }

        });

        imgCaSi.setOnClickListener(v -> {
            if (caSi.getIdCaSi() != null) {
                MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(UpdateAlbumActivity.this);
                dialog.setBackground(getResources().getDrawable(R.drawable.custom_diaglog_background));
                dialog.setTitle("Đổi Ca Sĩ");
                dialog.setIcon(R.drawable.ic_exit);
                dialog.setMessage("Việc Này Sẽ Khiến Các Ca Khúc Đã Thêm Sẽ Bị Xóa?");
                dialog.setNegativeButton("Đồng Ý", (dialog1, which) -> {
                    OpenDialogChangeSinger();
                });

                dialog.setPositiveButton("Hủy", (dialog12, which) -> dialog12.dismiss());
                dialog.show();
            } else {
                OpenDialogChangeSinger();
            }
        });

        btnFinish.setOnClickListener(v -> {
            if (CheckValidate()) {
                progressDialog = ProgressDialog.show(UpdateAlbumActivity.this, "Đang Cập Nhật", " Vui Lòng Chờ");
                if (album.getIdAlbum() == null) {
                    if (rdHinh.getCheckedRadioButtonId() == R.id.rd_file_hinh_album) {
                        UpLoadFile(edtTitle.getText().toString() + System.currentTimeMillis() + ".jpg");
                    } else {
                        AddAlbum();
                    }
                } else {
                    if (CheckChange()) {
                        if (rdHinh.getCheckedRadioButtonId() == R.id.rd_file_hinh_album) {
                            UpLoadFile(album.getIdAlbum() + "Album" + album.getTenAlbum() + ".jpg");
                        } else {
                            UpdateAlbum();
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

    private void AddAlbum() {
        album.setIdCaSi(caSi.getIdCaSi());
        album.setTenCaSi(caSi.getTenCaSi());
        album.setTenAlbum(edtTitle.getText().toString());
        album.setHinhAlbum(edtLink.getText().toString());

        DataService dataService = APIService.getService();
        Call<String> callback = dataService.AddAlbum(album.getIdCaSi(), album.getTenAlbum(), album.getHinhAlbum());
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String id = response.body();
                if (id != null) {
                    album.setIdAlbum(id);
                    AlbumFragment.AddAlbum(album);
                    UpdateBaiHatAlbum();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("BBB", "add album failed");
            }
        });
    }

    private void UpdateBaiHatAlbum() {
        Log.e("BBB", "update danh sách bài hất");
        for (int i = 0; i < arrayList.size(); i++) {
            Call<String> callback = APIService.getService().UpdateBaiHatAlbum(album.getIdAlbum(), arrayList.get(i).getIdBaiHat());
            int finalI = i;
            callback.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (finalI >= arrayList.size() - 1) {
                        Toast.makeText(UpdateAlbumActivity.this, "Cập Nhật Thành Công", Toast.LENGTH_SHORT).show();
                        finish();
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        }
    }

    private void UpdateAlbum() {
        album.setIdCaSi(caSi.getIdCaSi());
        album.setTenCaSi(caSi.getTenCaSi());
        album.setTenAlbum(edtTitle.getText().toString());
        album.setHinhAlbum(edtLink.getText().toString());
        Call<String> callback = APIService.getService().UpdateAlbum(album.getIdAlbum(), album.getIdCaSi(), album.getTenAlbum(), album.getHinhAlbum());
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (arrayList.equals(temparrayList)) {
                    if (isSingerFragment) {
                        UpdateSingerActivity.UpdateAlbum(album);
                    }
                    AlbumFragment.UpdateAlbum(album);
                    Toast.makeText(UpdateAlbumActivity.this, "Cập Nhật Thành Công", Toast.LENGTH_SHORT).show();
                    finish();
                    progressDialog.dismiss();
                } else {
                    AlbumFragment.UpdateAlbum(album);
                    DeleteBaiHatAlbum();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void DeleteBaiHatAlbum() {
        Call<String> callback = APIService.getService().DeleteBaiHatAlbum(album.getIdAlbum());
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UpdateBaiHatAlbum();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private boolean CheckValidate() {
        if (edtTitle.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Tiêu Đề Trống!!!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (rdHinh.getCheckedRadioButtonId() == R.id.rd_link_hinh_album) {
            if (!LoadImageSuccess) {
                Toast.makeText(this, "Link Hình Không Hợp Lệ", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (rdHinh.getCheckedRadioButtonId() == R.id.rd_file_hinh_album) {
            if (RealPath == null) {
                Toast.makeText(this, "Không Tìm Được File Hình", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (caSi.getIdCaSi() == null) {
            Toast.makeText(this, "Chưa Chọn Ca Sĩ", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (arrayList.size() == 0) {
            Toast.makeText(this, "Phải có ít nhất một bài hát trong danh sách", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean CheckChange() {
        if (!edtTitle.getText().toString().equals(album.getTenAlbum()))
            return true;

        if (rdHinh.getCheckedRadioButtonId() == R.id.rd_file_hinh_album)
            return true;


        if (rdHinh.getCheckedRadioButtonId() == R.id.rd_link_hinh_album) {
            if (!edtLink.getText().toString().equals(album.getHinhAlbum()))
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
                if (album.getIdAlbum() == null) {
                    AddAlbum();
                } else {
                    UpdateAlbum();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    private void OpenDialogAddSong() {
        Dialog dialog = new Dialog(UpdateAlbumActivity.this);
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
        ArrayList<BaiHat> baiHats;
        SongAlbumAdapter songAdapter;
        TextView tvFinish, tvTitle;
        SearchView searchView;
        RecyclerView recyclerView;
        RelativeLayout layoutNoinfo;

        tvTitle = dialog.findViewById(R.id.title_dialog);
        tvFinish = dialog.findViewById(R.id.tv_finish_dialog);
        searchView = dialog.findViewById(R.id.search_view_change_song);
        recyclerView = dialog.findViewById(R.id.rv_change_song_dialog);
        layoutNoinfo = dialog.findViewById(R.id.txt_noinfo_baihat);

        songAdapter = new SongAlbumAdapter(UpdateAlbumActivity.this, casiarraylist);
        songAdapter.setDialog(true);
        recyclerView.setAdapter(songAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(UpdateAlbumActivity.this, RecyclerView.VERTICAL, false));

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
        dialog.setTitle("Thoát");
        dialog.setIcon(R.drawable.ic_exit);
        dialog.setMessage("Bạn có chắc muốn thoát?");
        dialog.setNegativeButton("Đồng Ý", (dialog1, which) -> {
            finish();
        });

        dialog.setPositiveButton("Hủy", (dialog12, which) -> dialog12.dismiss());
        dialog.show();
    }

    private void OpenDialogChangeSinger() {
        dialogcs = new Dialog(UpdateAlbumActivity.this);
        dialogcs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogcs.setContentView(R.layout.dialog_add_casi_baihat);
        dialogcs.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialogcs.getWindow();

        if (window == null)
            return;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);
        dialogcs.setCancelable(true);

        // Các Thành Phần Trong Dialog
        TextView tvFinish;
        SearchView searchView;
        RecyclerView recyclerView;
        RelativeLayout layoutNoinfo;

        // Dữ Liệu
        ArrayList<CaSi> arrayList = MainActivity.caSiArrayList;
        SingerAlbumAdapter singeradapter = new SingerAlbumAdapter(UpdateAlbumActivity.this, arrayList);

        // Ánh Xạ

        tvFinish = dialogcs.findViewById(R.id.tv_finish_dialog);
        searchView = dialogcs.findViewById(R.id.search_view_add_ca_si);
        recyclerView = dialogcs.findViewById(R.id.rv_add_casi_dialog);
        layoutNoinfo = dialogcs.findViewById(R.id.txt_noinfo_casi);

        // Set Dữ Liệu
        recyclerView.setAdapter(singeradapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));

        tvFinish.setOnClickListener(v -> {
            dialogcs.dismiss();
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                singeradapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                singeradapter.getFilter().filter(newText);
                Handler handler = new Handler();
                final int[] i = {0};
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (i[0] >= 4)
                            handler.removeCallbacks(this);

                        if (singeradapter.getItemCount() == 0)
                            layoutNoinfo.setVisibility(View.VISIBLE);
                        else
                            layoutNoinfo.setVisibility(View.GONE);

                        i[0]++;
                    }
                }, 120);

                return true;
            }
        });


        dialogcs.show();

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

    public void ChangeSinger(CaSi singer) {
        if (caSi.getIdCaSi() == null) {
            caSi = singer;
            Glide.with(getApplicationContext()).load(caSi.getHinhCaSi()).error(R.drawable.song).into(imgCaSi);
            arrayList = new ArrayList<>();
            SetRV();
            txtCaSi.setText("Ca Sĩ: " + caSi.getTenCaSi());
            btnAddSong.setClickable(false);
            GetDataBaiHatCaSi();
            if (dialogcs != null)
                dialogcs.dismiss();
            return;
        }

        if (caSi.getIdCaSi().equals(singer.getIdCaSi())) {
            {
                if (dialogcs != null)
                    dialogcs.dismiss();
                return;
            }
        } else {
            {
                caSi = singer;
                Glide.with(getApplicationContext()).load(caSi.getHinhCaSi()).error(R.drawable.song).into(imgCaSi);
                arrayList.clear();
                adapter.notifyDataSetChanged();
                txtCaSi.setText("Ca Sĩ: " + caSi.getTenCaSi());
                btnAddSong.setClickable(false);
                GetDataBaiHatCaSi();
                if (dialogcs != null)
                    dialogcs.dismiss();
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
                        Toast.makeText(this, "Không Thể Lấy File", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(this, "Lấy File Thành Công", Toast.LENGTH_SHORT).show();
                    Picasso.with(this).load(uriHinh).into(imgAlbum);
                }
            });


}
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
import com.example.mpmanage.Adapter.SongPlaylistAdapter;
import com.example.mpmanage.Fragment.MainFragment.PlaylistFragment;
import com.example.mpmanage.Model.BaiHat;
import com.example.mpmanage.Model.Playlist;
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

public class UpdatePlaylistActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextInputEditText edtTitle, edtLink;
    RadioGroup rdHinh;
    MaterialButton btnGetFile, btnFinish, btnCancel, btnAddSong;
    RecyclerView recyclerView;
    RoundedImageView imgPlaylist;
    ProgressDialog progressDialog;

    Playlist playlist;
    public ArrayList<BaiHat> arrayList;
    private ArrayList<BaiHat> temparrayList;
    boolean LoadImageSuccess;
    private String RealPath;
    private Uri uriHinh;
    SongPlaylistAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_playlist);
        overridePendingTransition(R.anim.from_right, R.anim.to_left);
        AnhXa();
        GetDataPlaylist();
        EventListener();
    }


    private void AnhXa() {
        toolbar = findViewById(R.id.update_playlist_toolbar);
        edtLink = findViewById(R.id.edt_link_hinh_playlist);
        edtTitle = findViewById(R.id.edt_ten_playlist);
        rdHinh = findViewById(R.id.rd_hinh_playlist);
        btnGetFile = findViewById(R.id.btn_get_hinh_playlist);
        btnAddSong = findViewById(R.id.btn_add_song_playlist);
        btnFinish = findViewById(R.id.btn_finish_update_playlist);
        btnCancel = findViewById(R.id.btn_cancel_update_playlist);
        recyclerView = findViewById(R.id.rv_baihat_playlist);
        imgPlaylist = findViewById(R.id.img_hinh_playlist);
    }

    private void GetDataPlaylist() {
        Intent intent = getIntent();
        if (intent.hasExtra("playlist")) {
            playlist = (Playlist) intent.getSerializableExtra("playlist");
            edtTitle.setText(playlist.getTen());
            edtLink.setText(playlist.getHinhAnh());
            SetImage();
            SetUpToolBar("Cập Nhật Playlist");
            GetBaiHatPlaylist();
        } else {
            playlist = new Playlist();
            arrayList = new ArrayList<>();
            temparrayList = new ArrayList<>();
            SetUpToolBar("Thêm Playlist");
            SetRecycleView();
        }

    }

    private void GetBaiHatPlaylist() {
        DataService dataService = APIService.getService();
        Call<List<BaiHat>> callback = dataService.GetBaiHatPlaylist(playlist.getIdPlaylist());
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                arrayList = (ArrayList<BaiHat>) response.body();
                temparrayList = new ArrayList<>();
                if (arrayList == null) {
                    arrayList = new ArrayList<>();
                }
                for(BaiHat baiHat : arrayList)
                    temparrayList.add(baiHat);

                SetRecycleView();
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {

            }
        });

    }

    private void SetRecycleView(){
        adapter = new SongPlaylistAdapter(UpdatePlaylistActivity.this, arrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(UpdatePlaylistActivity.this, RecyclerView.VERTICAL, false));
    }

    private void SetUpToolBar(String title){
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);

        toolbar.setNavigationOnClickListener(v -> {
            if (playlist.getIdPlaylist() == null) {
                OpenDialogFinish();
                return;
            }
            if (CheckChange()) {
                OpenDialogFinish();
            } else
                finish();
        });
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
                }).into(imgPlaylist);
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
            if (checkedId == R.id.rd_link_hinh_playlist) {
                btnGetFile.setVisibility(View.GONE);
                edtLink.setVisibility(View.VISIBLE);
                SetImage();
            } else {
                edtLink.setVisibility(View.GONE);
                btnGetFile.setVisibility(View.VISIBLE);
                if (RealPath == null) {
                    imgPlaylist.setImageResource(R.drawable.ic_image);
                    return;
                }
                Glide.with(getApplicationContext()).load(uriHinh).into(imgPlaylist);
            }
        });

        btnGetFile.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            FileAnhResult.launch(Intent.createChooser(intent, " Chọn Hình Ảnh"));
        });

        btnAddSong.setOnClickListener(v -> {
            OpenDialogAddSong();
        });

        btnFinish.setOnClickListener(v -> {
            if (CheckValidate()) {
                progressDialog = ProgressDialog.show(UpdatePlaylistActivity.this, "Đang Cập Nhật", " Vui Lòng Chờ");
                if (playlist.getIdPlaylist() == null) {
                    if (rdHinh.getCheckedRadioButtonId() == R.id.rd_file_hinh_playlist) {
                        UpLoadFile(playlist.getTen() + System.currentTimeMillis() + ".jpg");
                    } else {
                        AddPlaylist();
                    }
                } else {
                    if (CheckChange()) {
                        if (rdHinh.getCheckedRadioButtonId() == R.id.rd_file_hinh_playlist) {
                            UpLoadFile(playlist.getIdPlaylist() + "Playlist" + playlist.getTen() + ".jpg");
                        } else {
                            UpdatePlaylist();
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
            if (playlist.getIdPlaylist() == null) {
                OpenDialogFinish();
                return;
            }
            if (CheckChange()) {
                OpenDialogFinish();
            } else
                finish();
        });
    }

    private void UpdatePlaylist() {
        playlist.setHinhAnh(edtLink.getText().toString());
        playlist.setTen(edtTitle.getText().toString());
        Call<String> callback = APIService.getService().UpdatePlaylist(playlist.getIdPlaylist(), playlist.getTen(), playlist.getHinhAnh());
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                PlaylistFragment.UpdatePlaylist(playlist);
                if(arrayList.equals(temparrayList)){
                    progressDialog.dismiss();
                    finish();
                    Toast.makeText(UpdatePlaylistActivity.this, "Cập Nhật Thành Công", Toast.LENGTH_SHORT).show();
                    return;
                }
                DeleteBaiHatPlaylist();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(UpdatePlaylistActivity.this, "Cập Nhật Thất Bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void DeleteBaiHatPlaylist(){
        Call<String> callback =APIService.getService().DeleteBaiHatPlaylist(playlist.getIdPlaylist());
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UpdateBaiHatPlaylist();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("BBB", "Xóa Bài Hát Playlist Thất Bại");
                progressDialog.dismiss();
                Toast.makeText(UpdatePlaylistActivity.this, "Cập Nhật Thất Bại", Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void AddPlaylist() {
        playlist.setTen(edtTitle.getText().toString().trim());
        playlist.setHinhAnh(edtLink.getText().toString().trim());
        Call<String> callback = APIService.getService().AddPlaylist(playlist.getTen(), playlist.getHinhAnh());
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String id = response.body();
                if(id != null){
                    playlist.setIdPlaylist(id);
                    UpdateBaiHatPlaylist();
                    PlaylistFragment.AddPlaylist(playlist);
                    return;
                }
                progressDialog.dismiss();
                finish();
                Toast.makeText(UpdatePlaylistActivity.this, "Thêm Playlist Thất Bại", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("BBB", "Thêm Playlist Thất Bại");
            }
        });
    }

    private void UpdateBaiHatPlaylist() {
        Log.e("BBB", "UpdateBaiHatPlaylist");

        for(int i = 0; i < arrayList.size(); i++){
        Call<String> callback = APIService.getService().UpdateBaiHatPlaylist(playlist.getIdPlaylist(), arrayList.get(i).getIdBaiHat());
            int finalI = i;
            callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(finalI >= arrayList.size()- 1){
                    Toast.makeText(UpdatePlaylistActivity.this, "Cập Nhật Thành Công", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("BBB", "cập Nật Bài Hát Playlist Thất Bại");
                progressDialog.dismiss();
                Toast.makeText(UpdatePlaylistActivity.this, "Cập Nhật Thất Bại", Toast.LENGTH_SHORT).show();

            }
        });
    }}

    private void OpenDialogAddSong() {
        Dialog dialog = new Dialog(UpdatePlaylistActivity.this);
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
        SongPlaylistAdapter songAdapter;
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
        songAdapter = new SongPlaylistAdapter(UpdatePlaylistActivity.this, baiHats);
        songAdapter.setDialog(true);
        recyclerView.setAdapter(songAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(UpdatePlaylistActivity.this, RecyclerView.VERTICAL, false));

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
        dialog.setMessage("Bạn Có Chắc muốn Thoát Mà Không Lưu Kết Quả?");
        dialog.setNegativeButton("Đồng Ý", (dialog1, which) -> {
            finish();
        });

        dialog.setPositiveButton("Hủy", (dialog12, which) -> dialog12.dismiss());
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
            Toast.makeText(this, "Tiêu Đề Trống!!!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (rdHinh.getCheckedRadioButtonId() == R.id.rd_link_hinh_playlist) {
            if (!LoadImageSuccess) {
                Toast.makeText(this, "Link Hình Không Hợp Lệ", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (rdHinh.getCheckedRadioButtonId() == R.id.rd_file_hinh_playlist) {
            if (RealPath == null) {
                Toast.makeText(this, "Không Tìm Được File Hình", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (arrayList.size() == 0) {
            Toast.makeText(this, "Phải có ít nhất một bài hát trong danh sách", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean CheckChange() {
        if (!edtTitle.getText().toString().equals(playlist.getTen()))
            return true;

        if (rdHinh.getCheckedRadioButtonId() == R.id.rd_file_hinh_playlist)
            return true;


        if (rdHinh.getCheckedRadioButtonId() == R.id.rd_link_hinh_playlist) {
            if (!edtLink.getText().toString().equals(playlist.getHinhAnh()))
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
                String Linkroot = "http://192.168.1.3/PlayerMusicProject/Server/Client/image/";
                String LinkHinh = Linkroot + FileName;
                edtLink.setText(LinkHinh);
                if(playlist.getIdPlaylist() == null)
                    AddPlaylist();
                else
                    UpdatePlaylist();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
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
                    Glide.with(this).load(uriHinh).into(imgPlaylist);
                }
            });

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.from_left, R.anim.to_right);
    }
}
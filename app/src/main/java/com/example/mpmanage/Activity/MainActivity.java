package com.example.mpmanage.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mpmanage.Fragment.MainFragment.AlbumFragment;
import com.example.mpmanage.Fragment.MainFragment.SongFragment;
import com.example.mpmanage.Model.Admin;
import com.example.mpmanage.Model.Album;
import com.example.mpmanage.Model.BaiHat;
import com.example.mpmanage.Model.CaSi;
import com.example.mpmanage.Model.ChuDeTheLoai;
import com.example.mpmanage.Model.Md5;
import com.example.mpmanage.Model.Playlist;
import com.example.mpmanage.Model.QuangCao;
import com.example.mpmanage.R;
import com.example.mpmanage.Service.APIService;
import com.example.mpmanage.Service.DataService;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final int PERMISSION_READ = 123;
    // Dữ Liệu Từ Database
    Admin admin;
    private long backtime;
    public static ArrayList<BaiHat> baiHatArrayList;
    public static ArrayList<QuangCao> quangCaoArrayList;
    public static ArrayList<Album> albumArrayList;
    public static ArrayList<CaSi> caSiArrayList;
    public static ArrayList<Playlist> playlistArrayList;
    public static ArrayList<ChuDeTheLoai> theLoaiArrayList;
    public static ArrayList<ChuDeTheLoai> chuDeArrayList;


    // Các Mục Trong layout
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    AppBarConfiguration appBarConfiguration;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(R.anim.from_bottom, R.anim.to_top);
        //Ánh Xạ
        AnhXa();
        GetAdminAcount();
        SetToolBar();
        SetupDrawer();
        checkPermission();

        // Lấy Dữ Liệu
        GetListQuangCao();
        GetListBaiHat();
        GetListAlbum();
        GetListCaSi();
        GetListPlaylist();
        GetListChuDe();
        GetListTheLoai();
    }


    private void AnhXa() {
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
    }

    private void SetToolBar() {
        setSupportActionBar(toolbar);
    }

    private void SetupDrawer() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_normal);
        navController = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.bannerFragment, R.id.songFragment,
                R.id.albumFragment, R.id.singerFragment,
                R.id.playlistFragment, R.id.categoryFragment).setDrawerLayout(drawerLayout).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        @SuppressLint("ResourceType")
        View headerLayout = navigationView.getHeaderView(0);
        TextView textView = headerLayout.findViewById(R.id.textView);
        textView.setText(admin.getEmail());
    }

    private void GetAdminAcount() {
//        Intent intent = getIntent();
//        if (intent != null && intent.hasExtra("admin"))
//            admin = intent.getParcelableExtra("admin");
        admin = new Admin("1", "1", "tdn352001@gmail.com", Md5.endcode("123456"));
    }

    private void GetListBaiHat() {
        DataService dataService = APIService.getService();
        Call<List<BaiHat>> callback = dataService.GetAllBaiHat();
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(@NonNull Call<List<BaiHat>> call, @NonNull Response<List<BaiHat>> response) {
                baiHatArrayList = (ArrayList<BaiHat>) response.body();
                SongFragment.baiHatArrayList = baiHatArrayList;
                Log.e("BBB", "SUC");
            }

            @Override
            public void onFailure(@NonNull Call<List<BaiHat>> call, @NonNull Throwable t) {
                Log.e("BBB", "Failed");
            }
        });
    }

    private void GetListQuangCao() {
        DataService dataService = APIService.getService();
        Call<List<QuangCao>> callback = dataService.GetAllBanner();
        callback.enqueue(new Callback<List<QuangCao>>() {
            @Override
            public void onResponse(@NonNull Call<List<QuangCao>> call, @NonNull Response<List<QuangCao>> response) {
                quangCaoArrayList = (ArrayList<QuangCao>) response.body();
            }

            @Override
            public void onFailure(@NonNull Call<List<QuangCao>> call, @NonNull Throwable t) {

            }
        });
    }

    private void GetListAlbum() {
        DataService dataService = APIService.getService();
        Call<List<Album>> callback = dataService.GetAllAlbum();
        callback.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(@NonNull Call<List<Album>> call, @NonNull Response<List<Album>> response) {
                albumArrayList = (ArrayList<Album>) response.body();
                AlbumFragment.arrayList = albumArrayList;
            }

            @Override
            public void onFailure(@NonNull Call<List<Album>> call, @NonNull Throwable t) {

            }
        });
    }

    private void GetListCaSi() {
        DataService dataService = APIService.getService();
        Call<List<CaSi>> callback = dataService.GetAllCaSi();
        callback.enqueue(new Callback<List<CaSi>>() {
            @Override
            public void onResponse(@NonNull Call<List<CaSi>> call, @NonNull Response<List<CaSi>> response) {
                caSiArrayList = (ArrayList<CaSi>) response.body();
            }

            @Override
            public void onFailure(@NonNull Call<List<CaSi>> call, @NonNull Throwable t) {

            }
        });
    }

    private void GetListPlaylist() {
        DataService dataService = APIService.getService();
        Call<List<Playlist>> callback = dataService.GetAllPlaylist();
        callback.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(@NonNull Call<List<Playlist>> call, @NonNull Response<List<Playlist>> response) {
                playlistArrayList = (ArrayList<Playlist>) response.body();
            }

            @Override
            public void onFailure(@NonNull Call<List<Playlist>> call, @NonNull Throwable t) {

            }
        });
    }

    private void GetListChuDe() {
        DataService dataService = APIService.getService();
        Call<List<ChuDeTheLoai>> callback = dataService.GetAllChuDe();
        callback.enqueue(new Callback<List<ChuDeTheLoai>>() {
            @Override
            public void onResponse(@NonNull Call<List<ChuDeTheLoai>> call, @NonNull Response<List<ChuDeTheLoai>> response) {
                chuDeArrayList = (ArrayList<ChuDeTheLoai>) response.body();
            }

            @Override
            public void onFailure(@NonNull Call<List<ChuDeTheLoai>> call, @NonNull Throwable t) {

            }
        });
    }

    private void GetListTheLoai() {
        DataService dataService = APIService.getService();
        Call<List<ChuDeTheLoai>> callback = dataService.GetAllTheLoai();
        callback.enqueue(new Callback<List<ChuDeTheLoai>>() {
            @Override
            public void onResponse(@NonNull Call<List<ChuDeTheLoai>> call, @NonNull Response<List<ChuDeTheLoai>> response) {
                theLoaiArrayList = (ArrayList<ChuDeTheLoai>) response.body();
            }

            @Override
            public void onFailure(@NonNull Call<List<ChuDeTheLoai>> call, @NonNull Throwable t) {

            }
        });
    }

    public boolean checkPermission() {
        int READ_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if ((READ_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ);
            return false;
        }
        return true;
    }

    public static int GetPostionBaiHatById(String IdBaiHat) {
        if (MainActivity.baiHatArrayList != null) {
            for (int i = 0; i < MainActivity.baiHatArrayList.size(); i++) {
                if (MainActivity.baiHatArrayList.get(i).getIdBaiHat().equals(IdBaiHat))
                    return i;
            }
        }

        return -1;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else{
            if (backtime + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
                return;
            } else {
                Toast.makeText(this, "Nhấn Lần Nữa Để Thoát", Toast.LENGTH_SHORT).show();
            }

            backtime = System.currentTimeMillis();
        }
    }
}
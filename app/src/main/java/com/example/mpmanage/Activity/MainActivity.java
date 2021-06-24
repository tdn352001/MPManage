package com.example.mpmanage.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.mpmanage.Model.Admin;
import com.example.mpmanage.Model.Album;
import com.example.mpmanage.Model.BaiHat;
import com.example.mpmanage.Model.CaSi;
import com.example.mpmanage.Model.ChuDeTheLoai;
import com.example.mpmanage.Model.Playlist;
import com.example.mpmanage.Model.QuangCao;
import com.example.mpmanage.R;
import com.example.mpmanage.Service.APIService;
import com.example.mpmanage.Service.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Admin admin;
    ArrayList<BaiHat> baiHatArrayList;
    ArrayList<QuangCao> quangCaoArrayList;
    ArrayList<Album> albumArrayList;
    ArrayList<CaSi> caSiArrayList;
    ArrayList<Playlist> playlistArrayList;
    ArrayList<ChuDeTheLoai> theLoaiArrayList;
    ArrayList<ChuDeTheLoai> chuDeArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GetAdminAcount();
        GetListQuangCao();
        GetListBaiHat();
        GetListAlbum();
        GetListCaSi();
        GetListPlaylist();
        GetListChuDe();
        GetListTheLoai();
    }

    private void GetAdminAcount() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("admin"))
            admin = intent.getParcelableExtra("admin");
    }

    private void GetListBaiHat(){
        DataService dataService = APIService.getService();
        Call<List<BaiHat>> callback = dataService.GetAllBaiHat();
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(@NonNull Call<List<BaiHat>> call,@NonNull Response<List<BaiHat>> response) {
                baiHatArrayList = (ArrayList<BaiHat>) response.body();
            }

            @Override
            public void onFailure(@NonNull Call<List<BaiHat>> call,@NonNull Throwable t) {

            }
        });
    }

    private void GetListQuangCao(){
        DataService dataService = APIService.getService();
        Call<List<QuangCao>> callback = dataService.GetAllBanner();
        callback.enqueue(new Callback<List<QuangCao>>() {
            @Override
            public void onResponse(@NonNull Call<List<QuangCao>> call,@NonNull Response<List<QuangCao>> response) {
                quangCaoArrayList = (ArrayList<QuangCao>) response.body();
            }

            @Override
            public void onFailure(@NonNull Call<List<QuangCao>> call,@NonNull Throwable t) {

            }
        });
    }

    private void GetListAlbum(){
        DataService dataService = APIService.getService();
        Call<List<Album>> callback = dataService.GetAllAlbum();
        callback.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(@NonNull Call<List<Album>> call,@NonNull Response<List<Album>> response) {
                albumArrayList = (ArrayList<Album>) response.body();
            }

            @Override
            public void onFailure(@NonNull Call<List<Album>> call,@NonNull Throwable t) {

            }
        });
    }
    private void GetListCaSi(){
        DataService dataService = APIService.getService();
        Call<List<CaSi>> callback = dataService.GetAllCaSi();
        callback.enqueue(new Callback<List<CaSi>>() {
            @Override
            public void onResponse(@NonNull Call<List<CaSi>> call,@NonNull Response<List<CaSi>> response) {
                caSiArrayList = (ArrayList<CaSi>) response.body();
            }

            @Override
            public void onFailure(@NonNull Call<List<CaSi>> call,@NonNull Throwable t) {

            }
        });
    }

    private void GetListPlaylist(){
        DataService dataService = APIService.getService();
        Call<List<Playlist>> callback = dataService.GetAllPlaylist();
        callback.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(@NonNull Call<List<Playlist>> call,@NonNull Response<List<Playlist>> response) {
                playlistArrayList = (ArrayList<Playlist>) response.body();
            }

            @Override
            public void onFailure(@NonNull Call<List<Playlist>> call,@NonNull  Throwable t) {

            }
        });
    }

    private void GetListChuDe(){
        DataService dataService = APIService.getService();
        Call<List<ChuDeTheLoai>> callback = dataService.GetAllChuDe();
        callback.enqueue(new Callback<List<ChuDeTheLoai>>() {
            @Override
            public void onResponse(@NonNull Call<List<ChuDeTheLoai>> call,@NonNull Response<List<ChuDeTheLoai>> response) {
                chuDeArrayList = (ArrayList<ChuDeTheLoai>) response.body();
            }

            @Override
            public void onFailure(@NonNull Call<List<ChuDeTheLoai>> call,@NonNull Throwable t) {

            }
        });
    }

    private void GetListTheLoai(){
        DataService dataService = APIService.getService();
        Call<List<ChuDeTheLoai>> callback = dataService.GetAllTheLoai();
        callback.enqueue(new Callback<List<ChuDeTheLoai>>() {
            @Override
            public void onResponse(@NonNull Call<List<ChuDeTheLoai>> call,@NonNull Response<List<ChuDeTheLoai>> response) {
                theLoaiArrayList = (ArrayList<ChuDeTheLoai>) response.body();
            }

            @Override
            public void onFailure(@NonNull Call<List<ChuDeTheLoai>> call,@NonNull Throwable t) {

            }
        });
    }


}
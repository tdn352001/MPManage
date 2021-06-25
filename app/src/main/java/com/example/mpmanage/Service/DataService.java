package com.example.mpmanage.Service;


import com.example.mpmanage.Model.Admin;
import com.example.mpmanage.Model.Album;
import com.example.mpmanage.Model.BaiHat;
import com.example.mpmanage.Model.CaSi;
import com.example.mpmanage.Model.ChuDeTheLoai;
import com.example.mpmanage.Model.Playlist;
import com.example.mpmanage.Model.QuangCao;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DataService {

    @FormUrlEncoded
    @POST("login.php")
    Call<Admin> Login(@Field("email") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("forgotpassword.php")
    Call<String> ForgotPassword(@Field("email") String Email);

    @FormUrlEncoded
    @POST("changepassword.php")
    Call<String> ChangePassword(@Field("idadmin") String idadmin, @Field("password") String password);


    @GET("getallbanner.php")
    Call<List<QuangCao>> GetAllBanner();


    @GET("getallsong.php")
    Call<List<BaiHat>> GetAllBaiHat();


    @GET("getallsinger.php")
    Call<List<CaSi>> GetAllCaSi();


    @GET("getallalbums.php")
    Call<List<Album>> GetAllAlbum();


    @GET("getallplaylist.php")
    Call<List<Playlist>> GetAllPlaylist();


    @GET("getallchude.php")
    Call<List<ChuDeTheLoai>> GetAllChuDe();


    @GET("getalltheloai.php")
    Call<List<ChuDeTheLoai>> GetAllTheLoai();

    @FormUrlEncoded
    @POST("updatesong.php")
    Call<String> UpdateSong(@Field("idbaihat") String id, @Field("tenbaihat") String ten, @Field("hinhbaihat") String hinh, @Field("linkbaihat") String nhac);

    @FormUrlEncoded
    @POST("deletesongsinger.php")
    Call<String> DeleteSongSinger(@Field("idbaihat") String id);

    @FormUrlEncoded
    @POST("updatesongsinger.php")
    Call<String> UpdateSongSinger(@Field("idbaihat") String idbaihat, @Field("idcasi") String idcasi);
}

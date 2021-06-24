package com.example.mpmanage.Service;



import com.example.mpmanage.Model.Admin;
import com.example.mpmanage.Model.BaiHat;
import com.example.mpmanage.Model.CaSi;
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
    Call<Admin> Login(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("forgotpassword.php")
    Call<String> ForgotPassword(@Field("email") String Email);

    @FormUrlEncoded
    @POST("changepassword.php")
    Call<String> ChangePassword(@Field("idadmin") String idadmin, @Field("password") String password);

    @FormUrlEncoded
    @GET("getallbanner.php")
    Call<List<QuangCao>> GetAllBanner();

    @FormUrlEncoded
    @GET("getallsong.php")
    Call<List<BaiHat>> GetAllBaiHat();

    @FormUrlEncoded
    @GET("getallsinger.php")
    Call<List<CaSi>> GetAllCaSi();

    @FormUrlEncoded
    @GET("getallalbums.php")
    Call<List<BaiHat>> GetAllAlbum();

    @FormUrlEncoded
    @GET("getallplaylist.php")
    Call<List<BaiHat>> GetAllPlaylist();

    @FormUrlEncoded
    @GET("getallchude.php")
    Call<List<BaiHat>> GetAllChuDe();

    @FormUrlEncoded
    @GET("getalltheloai.php")
    Call<List<BaiHat>> GetAllTheLoai();
}

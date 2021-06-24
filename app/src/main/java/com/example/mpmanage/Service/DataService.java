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
    Call<List<BaiHat>> GetAllAlbum();


    @GET("getallplaylist.php")
    Call<List<BaiHat>> GetAllPlaylist();


    @GET("getallchude.php")
    Call<List<BaiHat>> GetAllChuDe();


    @GET("getalltheloai.php")
    Call<List<BaiHat>> GetAllTheLoai();
}

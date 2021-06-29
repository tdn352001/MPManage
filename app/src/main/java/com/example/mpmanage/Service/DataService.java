package com.example.mpmanage.Service;


import com.example.mpmanage.Model.Admin;
import com.example.mpmanage.Model.Album;
import com.example.mpmanage.Model.BaiHat;
import com.example.mpmanage.Model.CaSi;
import com.example.mpmanage.Model.ChuDeTheLoai;
import com.example.mpmanage.Model.Playlist;
import com.example.mpmanage.Model.QuangCao;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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


    @GET("songbanner.php")
    Call<List<QuangCao>> GetAllBanner();


    @GET("getallsong.php")
    Call<List<BaiHat>> GetAllBaiHat();


    @GET("getallsinger.php")
    Call<List<CaSi>> GetAllCaSi();


    @GET("getallalbum.php")
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

    @FormUrlEncoded
    @POST("addsong.php")
    Call<String> AddSong(@Field("tenbaihat") String ten, @Field("hinhbaihat") String hinh, @Field("linkbaihat") String nhac);

    @Multipart
    @POST("uploadfile.php")
    Call<String> UploadFile(@Part MultipartBody.Part Anh);

    @FormUrlEncoded
    @POST("deletesong.php")
    Call<String> DeleteSong(@Field("idbaihat") String IdBaiHat);


    @FormUrlEncoded
    @POST("addbanner.php")
    Call<String> AddBanner(@Field("poster") String poster, @Field("noidung") String noidung, @Field("idbaihat") String idbaihat);

    @FormUrlEncoded
    @POST("updatebanner.php")
    Call<String> UpdateBanner(@Field("idbanner")String idbanner, @Field("poster") String poster, @Field("noidung") String noidung, @Field("idbaihat") String idbaihat);

    @FormUrlEncoded
    @POST("deletebanner.php")
    Call<String> DeleteBanner(@Field("idbanner") String idbanner);

    @FormUrlEncoded
    @POST("getdanhsachbaihat.php")
    Call<List<BaiHat>> GetBaiHatPlaylist(@Field("IdPlaylist") String IdPlaylist);

    @FormUrlEncoded
    @POST("getdanhsachbaihat.php")
    Call<List<BaiHat>> GetBaiHatAlbum(@Field("IdAlbum") String IdAlbum);

    @FormUrlEncoded
    @POST("getdanhsachbaihat.php")
    Call<List<BaiHat>> GetBaiHatCaSi(@Field("IdCaSi") String IdCaSi);

    @FormUrlEncoded
    @POST("getalbumcasi.php")
    Call<List<Album>> GetAlbumCaSi(@Field("IdCaSi") String IdCaSi);

    @FormUrlEncoded
    @POST("getdanhsachbaihat.php")
    Call<List<BaiHat>> GetBaiHatChuDe(@Field("IdChuDe") String IdChuDe);


    @FormUrlEncoded
    @POST("getdanhsachbaihat.php")
    Call<List<BaiHat>> GetBaiHatTheLoai(@Field("IdTheLoai") String IdPlaylist);

    @FormUrlEncoded
    @POST("updatetheloai.php")
    Call<String> UpdateTheLoai(@Field("id") String idchude, @Field("ten") String tenchude, @Field("hinh") String hinhanh);

    @FormUrlEncoded
    @POST("updatebaihattheloai.php")
    Call<String> UpdateBaiHatTheLoai(@Field("id") String idchude, @Field("idbaihat") String idbaihat);

    @FormUrlEncoded
    @POST("deletebaihattheloai.php")
    Call<String> DeleteBaiHatTheLoai(@Field("id") String idchude);

    @FormUrlEncoded
    @POST("deletetheloai.php")
    Call<String> DeleteTheLoai(@Field("id") String idtheloai);

    @FormUrlEncoded
    @POST("addtheloai.php")
    Call<String> AddTheLoai(@Field("ten") String tenchude, @Field("hinh") String hinhchude);

    @FormUrlEncoded
    @POST("updatechude.php")
    Call<String> UpdateChuDe(@Field("id") String idchude, @Field("ten") String tenchude, @Field("hinh") String hinhanh);

    @FormUrlEncoded
    @POST("updatebaihatchude.php")
    Call<String> UpdateBaiHatChuDe(@Field("id") String idchude, @Field("idbaihat") String idbaihat);

    @FormUrlEncoded
    @POST("deletebaihatchude.php")
    Call<String> DeleteBaiHatChuDe(@Field("id") String idchude);

    @FormUrlEncoded
    @POST("deletechude.php")
    Call<String> DeleteChuDe(@Field("id") String idtheloai);

    @FormUrlEncoded
    @POST("addchude.php")
    Call<String> AddChuDe(@Field("ten") String tenchude, @Field("hinh") String hinhchude);

    @FormUrlEncoded
    @POST("addplaylist.php")
    Call<String> AddPlaylist(@Field("ten") String tenchude, @Field("hinh") String hinhchude);

    @FormUrlEncoded
    @POST("updateplaylist.php")
    Call<String> UpdatePlaylist(@Field("id") String idchude, @Field("ten") String tenchude, @Field("hinh") String hinhanh);

    @FormUrlEncoded
    @POST("deleteplaylist.php")
    Call<String> DeletePlaylist(@Field("id") String idtheloai);

    @FormUrlEncoded
    @POST("deletebaihatplaylist.php")
    Call<String> DeleteBaiHatPlaylist(@Field("id") String idchude);

    @FormUrlEncoded
    @POST("updatebaihatplaylist.php")
    Call<String> UpdateBaiHatPlaylist(@Field("id") String idchude, @Field("idbaihat") String idbaihat);

    @FormUrlEncoded
    @POST("addalbum.php")
    Call<String> AddAlbum(@Field("idcasi") String idcasi, @Field("ten") String ten, @Field("hinh") String hinh);

    @FormUrlEncoded
    @POST("updatealbum.php")
    Call<String> UpdateAlbum(@Field("idalbum")String idalbum, @Field("idcasi") String idcasi, @Field("ten") String ten, @Field("hinh") String hinh);

    @FormUrlEncoded
    @POST("deletebaihatalbum.php")
    Call<String> DeleteBaiHatAlbum(@Field("idalbum") String idalbum);

    @FormUrlEncoded
    @POST("updatebaihatalbum.php")
    Call<String> UpdateBaiHatAlbum(@Field("idalbum") String idalbum,  @Field("idbaihat") String idbaihat);

    @FormUrlEncoded
    @POST("deleltealbum.php")
    Call<String> DeleteAlbum(@Field("idalbum") String idalbum);

}

package com.example.mpmanage.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChuDeTheLoai implements Serializable {

@SerializedName("Id")
@Expose
private String id;
@SerializedName("Ten")
@Expose
private String ten;
@SerializedName("Hinh")
@Expose
private String hinh;

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getTen() {
return ten;
}

public void setTen(String ten) {
this.ten = ten;
}

public String getHinh() {
return hinh;
}

public void setHinh(String hinh) {
this.hinh = hinh;
}

}
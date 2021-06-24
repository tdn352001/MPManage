package com.example.mpmanage.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Admin implements Serializable {

    @SerializedName("IdAdmin")
    @Expose
    private String idAdmin;
    @SerializedName("IsSupper")
    @Expose
    private String isSupper;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("Password")
    @Expose
    private String password;

    public Admin(String idAdmin, String isSupper, String email, String password) {
        this.idAdmin = idAdmin;
        this.isSupper = isSupper;
        this.email = email;
        this.password = password;
    }

    public String getIdAdmin() {
        return idAdmin;
    }

    public String getIsSupper() {
        return isSupper;
    }

    public void setIsSupper(String isSupper) {
        this.isSupper = isSupper;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
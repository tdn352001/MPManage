package com.example.mpmanage.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BaiHat implements Parcelable {

    @SerializedName("IdBaiHat")
    @Expose
    private String idBaiHat;
    @SerializedName("TenBaiHat")
    @Expose
    private String tenBaiHat;
    @SerializedName("HinhBaiHat")
    @Expose
    private String hinhBaiHat;
    @SerializedName("LinkBaiHat")
    @Expose
    private String linkBaiHat;
    @SerializedName("IdCaSi")
    @Expose
    private List<String> idCaSi = null;
    @SerializedName("CaSi")
    @Expose
    private List<String> caSi = null;
    @SerializedName("LuotThich")
    @Expose
    private String luotThich;

    public BaiHat() {
        luotThich = "0";
    }

    protected BaiHat(Parcel in) {
        idBaiHat = in.readString();
        tenBaiHat = in.readString();
        hinhBaiHat = in.readString();
        linkBaiHat = in.readString();
        caSi = in.createStringArrayList();
    }

    public static final Creator<BaiHat> CREATOR = new Creator<BaiHat>() {
        @Override
        public BaiHat createFromParcel(Parcel in) {
            return new BaiHat(in);
        }

        @Override
        public BaiHat[] newArray(int size) {
            return new BaiHat[size];
        }
    };

    public String getIdBaiHat() {
        return idBaiHat;
    }

    public void setIdBaiHat(String idBaiHat) {
        this.idBaiHat = idBaiHat;
    }

    public String getTenBaiHat() {
        return tenBaiHat;
    }

    public void setTenBaiHat(String tenBaiHat) {
        this.tenBaiHat = tenBaiHat;
    }

    public String getHinhBaiHat() {
        return hinhBaiHat;
    }

    public void setHinhBaiHat(String hinhBaiHat) {
        this.hinhBaiHat = hinhBaiHat;
    }

    public String getLinkBaiHat() {
        return linkBaiHat;
    }

    public void setLinkBaiHat(String linkBaiHat) {
        this.linkBaiHat = linkBaiHat;
    }
    public List<String> getIdCaSi() {
        return idCaSi;
    }

    public void setIdCaSi(List<String> idCaSi) {
        this.idCaSi = idCaSi;
    }
    public List<String> getCaSi() {
        return caSi;
    }

    public String getTenAllCaSi() {
        String TenCaSi = "";
        if (caSi != null) {
            if (caSi.size() > 0) {
                for (int i = 0; i < caSi.size(); i++) {
                    if (i != 0)
                        TenCaSi = TenCaSi + ", ";

                    TenCaSi = TenCaSi + getCaSi().get(i);
                }
                return TenCaSi;
            }
        }
        TenCaSi = "Unknown";
        return TenCaSi;
    }


    public void setCaSi(List<String> caSi) {
        this.caSi = caSi;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idBaiHat);
        dest.writeString(tenBaiHat);
        dest.writeString(hinhBaiHat);
        dest.writeString(linkBaiHat);
        dest.writeStringList(caSi);
    }

    public String getLuotThich() {
        return luotThich;
    }

    public void setLuotThich(String luotThich) {
        this.luotThich = luotThich;
    }

}
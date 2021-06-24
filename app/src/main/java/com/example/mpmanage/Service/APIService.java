package com.example.mpmanage.Service;

public class APIService {
    private static  String base_url= "https://tiendung352001.000webhostapp.com/Admin/";

    public static DataService getService(){
        return APIRetrofitClient.getclient(base_url).create(DataService.class);
    }


}

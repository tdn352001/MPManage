package com.example.mpmanage.Service;

public class APIService {

    public static DataService getService(){
        String base_url = "https://tiendung352001.000webhostapp.com/SupperUser/";
        return APIRetrofitClient.getclient(base_url).create(DataService.class);
    }


}

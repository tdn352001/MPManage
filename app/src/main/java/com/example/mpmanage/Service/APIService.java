package com.example.mpmanage.Service;

public class APIService {

    public static DataService getService(){
        String base_url = "http://192.168.1.3/PlayerMusicProject/Server/SupperUser/";
        return APIRetrofitClient.getclient(base_url).create(DataService.class);
    }

    public static DataService getFile(){
        String base_url = "http://192.168.1.3/PlayerMusicProject/Server/Client/";
        return APIRetrofitClient.getclient(base_url).create(DataService.class);
    }

}

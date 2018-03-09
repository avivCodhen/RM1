package com.strongest.savingdata.Server;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Cohen on 8/31/2017.
 */

public class ApiClient {

    public static final String BASE_URL = "http://10.0.2.2:80/site/_db/";
    //public static final String BASE_URL = "http://159.65.207.28/_db/";
    //public static final String BASE_URL = "http://localhost/site/_db/";

    public static Retrofit retrofit = null;

    public static Retrofit getApiClient(){

        if(retrofit == null){

            retrofit = new Retrofit.
                    Builder().
                    baseUrl(BASE_URL).
                    addConverterFactory(GsonConverterFactory.create(new Gson())).
                    build();
        }
        return retrofit;
    }


}

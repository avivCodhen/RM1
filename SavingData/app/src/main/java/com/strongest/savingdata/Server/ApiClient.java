package com.strongest.savingdata.Server;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import youruserpools.AppHelper;

/**
 * Created by Cohen on 8/31/2017.
 */

public class ApiClient {

    public static final String BASE_URL = "http://10.0.2.2:80/site/_db/";
    public static final String BASE_SERVER_URL = "http://192.168.1.23:8080/";
    //public static final String BASE_URL = "http://localhost/site/_db/";

    public static Retrofit retrofit = null;
    public static Retrofit retrofitServerApi = null;

    public static Retrofit getApiClient() {

        if (retrofit == null) {

            retrofit = new Retrofit.
                    Builder().
                    baseUrl(BASE_URL).
                    addConverterFactory(GsonConverterFactory.create(new Gson())).
                    build();
        }
        return retrofit;
    }
    //private static String token = "eyJraWQiOiI0bjF2WG9jR3dYTmc2TVJzSmQ3RHlXS042M1l4K2NpYllFNFRTNGIzWEtZPSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiJmMGJjNzJjMi02Mjc2LTQ1MjUtOWY3Mi05OGE0MjMyZTYzMWEiLCJhdWQiOiIzNGdqNWNlMW5la2UzZnMwZTBybmlxcmhlNiIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJldmVudF9pZCI6IjM5OGRkZjMzLTk2NGMtMTFlOC05NDVkLTI1MDBkNjNmMWFkZCIsInRva2VuX3VzZSI6ImlkIiwiYXV0aF90aW1lIjoxNTMzMjExNDc2LCJpc3MiOiJodHRwczpcL1wvY29nbml0by1pZHAudXMtZWFzdC0xLmFtYXpvbmF3cy5jb21cL3VzLWVhc3QtMV9PbVpzSmRtejgiLCJjb2duaXRvOnVzZXJuYW1lIjoiZm9vYmFyeWFyaW4iLCJleHAiOjE1MzMyMTg4MTMsImlhdCI6MTUzMzIxNTIxMywiZW1haWwiOiJ1c3VzdWV1QGRoZGguY29tIn0.MDpTJIt189Kwd3VwCyOdsQkR2OTmqvCLr8B9XX8RoYlcBCLBhcElbB_diw5yJpFzC12uPfGW-z5vFfef1RQzwHQNgFC5xP9KscancomHyYpbh1-L-VNPMy9BJgo5M3Rk7vYxLhw02naPQPB2YZw7sRzDSSzrmub2ECF3qtlJ4ugWYTaKzwsk-ByZWI5RrnDWaKs47uoRIuTryDVlsW0HYhzB2KLS4h3kqBoj0H4ypTVTN8IpMAYoD3TyWI8YiSC1h54Q0tB38_7JD9L3t8Z7Snl0-U3fxBbDT4vmgowZONw96JqsCbklurKEHqnglyuAsRucOlydVY7GJUjCxq2BEw";

    public static Retrofit getApiClientTmp() {
        if (retrofitServerApi == null) {
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    String token = null;
                    try {
                        token = AppHelper.getCurrSession().getIdToken().getJWTToken();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //todo remove
                    if (token == null || token.isEmpty()) {
                        Log.d("intercept", "token is : null");
                       // return null;
                    } else {
                        Log.d("intercept", "token is :" + token);
                    }
                    Request newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + (token==null ? " ":token))
                            .build();
                    return chain.proceed(newRequest);
                }
            }).build();
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofitServerApi = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(BASE_SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofitServerApi;

    }


}

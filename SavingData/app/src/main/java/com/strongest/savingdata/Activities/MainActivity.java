package com.strongest.savingdata.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.strongest.savingdata.R;
import com.strongest.savingdata.Server.ApiClient;
import com.strongest.savingdata.Server.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends FragmentActivity {
    private static int SPLASH_SCREEN = 3000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ApiInterface apiInterface = ApiClient.getApiClientTmp().create(ApiInterface.class);
        Call<String> call = apiInterface.getTmp();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                Log.d("aviv", "onResponse: "+  response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("aviv", "onFailure: "+ t.toString()+" ");
            }
        });
        if(true) return;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }, SPLASH_SCREEN);





        }
}


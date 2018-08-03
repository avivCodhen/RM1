package youruserpools;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.strongest.savingdata.Server.ApiClient;
import com.strongest.savingdata.Server.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckApiConnectionUtil {


    public static void checkIFServerResponse(Context context) {
        String token = null;
        try {
            token = AppHelper.getCurrSession().getIdToken().getJWTToken();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        if (token != null)
            apiCall(context);


    }

    private static void apiCall(Context context) {
        ApiInterface apiInterface = ApiClient.getApiClientTmp().create(ApiInterface.class);
        Call<String> call = apiInterface.getTmp();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                Toast.makeText(context, "onResponse body: " + response.body() + " onResponse code:" + response.code(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context, "onFailure: " + t.toString(), Toast.LENGTH_LONG).show();

                Log.d("aviv", "onFailure: " + t.toString() + " ");
            }
        });
    }
}

package com.strongest.savingdata.Server;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.strongest.savingdata.AlgorithmStats.Calculator.ProgressStatsMinMaxBean;
import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.BaseWorkout.NewMuscle;
import com.strongest.savingdata.Database.Exercise.Beans;
import com.strongest.savingdata.Database.Managers.DataManager;

import java.util.ArrayList;
import java.util.Collection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_STATS;

/**
 * Created by Cohen on 8/31/2017.
 */

public class Download {


    private Context context;
    private ApiInterface apiInterface;
    private ArrayList<Muscle> e;
    private ArrayList<Beans> beans;
    private Collection<ProgressStatsMinMaxBean> stats;
    private DataManager dm;

    public Download(Context context) {
        this.context = context;
        //exerciseHelper = new DBExercisesHelper(context);
        dm = new DataManager(context);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        //downloadStats();
    }

    private void downloadStats() {
        dm.getStatsDataManager().delete(TABLE_STATS);
        getAllStats();
    }

    public void refreshData(final String... tables) {

        deleteBeansTable(tables);
        saveTable(tables);
        dm.getPrefsEditor().putBoolean("download", false).commit();


    }

    private void deleteBeansTable(String... tables) {
        for (String t : tables) {
            if(t.equals("muscles")){
                dm.getMuscleDataManager().delete();
            }else{
                dm.getExerciseDataManager().delete(t);

            }
        }
    }

    private void saveTable(String... tables) {
        for (String table : tables)
            getAllBeans(table);

    }

    private void getAllStats() {
        Call<Collection<ProgressStatsMinMaxBean>> call = apiInterface.getStats();
        call.enqueue(new Callback<Collection<ProgressStatsMinMaxBean>>() {
            @Override
            public void onResponse(Call<Collection<ProgressStatsMinMaxBean>> call, Response<Collection<ProgressStatsMinMaxBean>> response) {
                stats = response.body();
                for (ProgressStatsMinMaxBean stat : stats){
                    insertStats(stat, TABLE_STATS);
                }

            }

            @Override
            public void onFailure(Call<Collection<ProgressStatsMinMaxBean>> call, Throwable t) {

            }
        });
    }

    private void getAllBeans(final String table) {
        if(table.equals("muscles")){
            Call<ArrayList<Muscle>> call = apiInterface.getMuscles(table);
            call.enqueue(new Callback<ArrayList<Muscle>>() {
                @Override
                public void onResponse(Call<ArrayList<Muscle>> call, Response<ArrayList<Muscle>> response) {
                    e = response.body();
                    dm.getMuscleDataManager().insertData(e);
                    Log.d("aviv", "onResponse: "+ table);
                }

                @Override
                public void onFailure(Call<ArrayList<Muscle>> call, Throwable t) {
                    Log.d("aviv", "onFailure: "+ t.toString()+" "+ table);
                }
            });
        }else if(table.equals("reps") || table.equals("sets") || table.equals("rest")){
            Call<ArrayList<Beans>> call = apiInterface.getTables(table);
            call.enqueue(new Callback<ArrayList<Beans>>() {
                @Override
                public void onResponse(Call<ArrayList<Beans>> call, Response<ArrayList<Beans>> response) {
                    beans = response.body();
                    for (Beans b : beans){
                        insertBeans(table, b);
                    }
                    Log.d("aviv", "onResponse: "+ table);

                }

                @Override
                public void onFailure(Call<ArrayList<Beans>> call, Throwable t) {
                    Log.d("aviv", "onFailure: "+ t.toString()+" "+ table);

                }
            });

        }else{
            Call<ArrayList<Beans>> call = apiInterface.getData(table);
            call.enqueue(new Callback<ArrayList<Beans>>() {
                @Override
                public void onResponse(Call<ArrayList<Beans>> call, Response<ArrayList<Beans>> response) {
                    beans = response.body();
                    for (Beans b : beans){
                        insertBeans(table, b);
                    }
                    Log.d("aviv", "onResponse: "+ table);


                }

                @Override
                public void onFailure(Call<ArrayList<Beans>> call, Throwable t) {
                    Log.d("aviv", "onFailure: "+ t.toString()+" "+ table);

                }
            });
        }

       /* Call<Collection<Beans>> call = apiInterface.getData("chest");
        call.enqueue(new Callback<Collection<Beans>>() {

            @Override
            public void onResponse(Call<Collection<Beans>> call, Response<Collection<Beans>> response) {
                  Log.d("aviv", "onResponse: " +response.toString());

                e = response.body();
                //Beans[] eb = e.toArray(new Beans[e.size()]);
                //Log.d("aviv", "" + e.size());
                for (Beans eB : e) {
                    //  Log.d("aviv","this "+eB.getM_biceps());
                    insertBeans(table, eB);
                }
            }

            @Override
            public void onFailure(Call<Collection<Beans>> call, Throwable t) {
                Log.d("aviv", "onFailure חליסי: " + t.toString());
            }
        });*/


    }

    private void insertStats(final ProgressStatsMinMaxBean stat, final String table) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                dm.getStatsDataManager().insertData(stat);
            }
        }).start();

    }

    private void insertBeans(final String table, final Beans eb) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                dm.getExerciseDataManager().insertData(table, eb);
            }
        }).start();
    }

  /*  private void insertMuscles(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                dm.getMuscleDataManager();
            }
        })
    }*/
}

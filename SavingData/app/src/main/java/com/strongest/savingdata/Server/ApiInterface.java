package com.strongest.savingdata.Server;

import com.strongest.savingdata.AlgorithmStats.Calculator.ProgressStatsMinMaxBean;
import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.BaseWorkout.NewMuscle;
import com.strongest.savingdata.Database.Exercise.Beans;

import java.util.ArrayList;
import java.util.Collection;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Cohen on 8/31/2017.
 */

public interface ApiInterface {


    /*@GET("exercise/")
    Call<Collection<Beans>> getData(@Query("table") String table);

    @GET("stats/")
    Call<Collection<ProgressStatsMinMaxBean>> getStats();

    @GET("test/")
    Call<Integer> isSuccessful();

    @GET("stats/")
    Call<Integer> toUpdate();*/

    @GET("getExercisesDB.php/")
    Call<ArrayList<Beans>> getData(@Query("muscles") String muscle);

    @POST("getMusclesDB.php/")
    Call<ArrayList<Muscle>> getMuscles(@Query("muscles") String muscle);

    @GET("getTablesDB.php/")
    Call<ArrayList<Beans>> getTables(@Query("muscles") String table);

    @GET("stats/")
    Call<Collection<ProgressStatsMinMaxBean>> getStats();


}

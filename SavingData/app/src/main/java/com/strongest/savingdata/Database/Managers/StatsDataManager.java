package com.strongest.savingdata.Database.Managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.strongest.savingdata.AlgorithmStats.Calculator.ProgressStatsMinMaxBean;
import com.strongest.savingdata.Database.Exercise.DBExercisesHelper;

import java.util.Collection;

import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.LEVEL;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.MAX_B;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.MAX_C;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.MAX_EXERCISE;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.MAX_METHOD;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.MAX_S;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.MIN_B;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.MIN_C;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.MIN_EXERCISE;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.MIN_S;

/**
 * Created by Cohen on 10/17/2017.
 */

public class StatsDataManager extends DataManager implements DataManagerListener<ProgressStatsMinMaxBean> {

    private Context context;
    private DBExercisesHelper helper;
    private SQLiteDatabase db;

    public StatsDataManager(Context context) {
        super(context);
        this.context = context;
        helper = new DBExercisesHelper(context);
        db = helper.getWritableDatabase();
    }

    public void insertData(ProgressStatsMinMaxBean sbp){
        insertData(DBExercisesHelper.TABLE_STATS, sbp);

    }

    @Deprecated
    @Override
    public void insertData(String tableName,  ProgressStatsMinMaxBean stat) {
        if (context != null) {
            db.insert(tableName,
                    null,
                    getContentValues(stat));

        }
    }

    @Override
    public void delete(String table) {
        db.delete(table, null, null);
    }

    @Override
    public Object readObjectFromFile(String file) {
        return null;
    }

    @Override
    public void saveObjectToFile(Object obj, String file) {

    }

    @Override
    public void close() {
        if(db != null){
            db.close();
        }
    }

    @Override
    public ProgressStatsMinMaxBean fetchById(String table, int id) {
        return null;
    }

    @Override
    public Collection<ProgressStatsMinMaxBean> readByTable(String table) {
        return null;
    }

    @Override
    public Collection<ProgressStatsMinMaxBean> readByString(String tableName, String s, int c) {
        return null;
    }

    @Override
    public void removeById(String table, int id) {

    }

    @Override
    public Collection<ProgressStatsMinMaxBean> readByConstraint(String tableName, String[] constraints, String[] values) {
        return null;
    }

    @Override
    public Collection<ProgressStatsMinMaxBean> parse(String tableNale, Cursor c) {
        return null;
    }

    public ProgressStatsMinMaxBean fetchStats(String routine, int level) {

        String sRoutine = String.valueOf(routine);
        String sLevel = String.valueOf(level);
        String sql = "SELECT * FROM " + "stats" + " WHERE " + ROUTINE+"="+sRoutine+" and "+ helper.LEVEL+"="+sLevel ;
        Cursor c = db.rawQuery(sql,null);
        return parseStats(c);
    }

    private ProgressStatsMinMaxBean parseStats(Cursor c) {
        ProgressStatsMinMaxBean stats = new ProgressStatsMinMaxBean();
        if (c != null && c.moveToFirst()) {
            //needs to be 11
            stats.setLevel(c.getInt(c.getColumnIndex(LEVEL)));
            stats.setRoutine(c.getString(c.getColumnIndex(ROUTINE)));

            stats.setMinExercise(c.getInt(c.getColumnIndex(MIN_EXERCISE)));
            stats.setMaxExercise(c.getInt(c.getColumnIndex(MAX_EXERCISE)));
            stats.setMinB(c.getInt(c.getColumnIndex(MIN_B)));
            stats.setMinS(c.getInt(c.getColumnIndex(MIN_S)));
            stats.setMinC(c.getInt(c.getColumnIndex(MIN_C)));
            stats.setMaxB(c.getInt(c.getColumnIndex(MAX_B)));
            stats.setMaxS(c.getInt(c.getColumnIndex(MAX_S)));
            stats.setMaxC(c.getInt(c.getColumnIndex(MAX_C)));
            stats.setMaxMethod(c.getInt(c.getColumnIndex(MAX_METHOD)));
        }
        return stats;
    }

    public ContentValues getContentValues(ProgressStatsMinMaxBean stat) {
        ContentValues v = new ContentValues();

        //v.put(ID,stat.getWorkoutId());

        v.put(ROUTINE, stat.getRoutine());
        v.put(LEVEL, stat.getLevel());
        v.put(MIN_EXERCISE, stat.getMinExercise());
        v.put(MAX_EXERCISE, stat.getMaxExercise());
        v.put(MIN_S, stat.getMinS());
        v.put(MIN_B, stat.getMinB());
        v.put(MIN_C, stat.getMinC());
        v.put(MAX_B, stat.getMaxB());
        v.put(MAX_S, stat.getMaxS());
        v.put(MAX_C, stat.getMaxC());
        v.put(MAX_METHOD, stat.getMaxMethod());

        return v;
    }
}

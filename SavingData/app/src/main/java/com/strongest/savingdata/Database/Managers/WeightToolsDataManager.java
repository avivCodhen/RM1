package com.strongest.savingdata.Database.Managers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.strongest.savingdata.Database.WeightTools.DBWeightToolsHelper;
import com.strongest.savingdata.MyViews.WeightKeyBoard.WeightKeyBoardToolsView;

import java.util.ArrayList;

/**
 * Created by Cohen on 3/25/2018.
 */

public class WeightToolsDataManager {

    private Context context;
    public SQLiteDatabase db;
    private DBWeightToolsHelper helper;

    public WeightToolsDataManager(Context context){
        this.context = context;
        db = helper.getWritableDatabase();
    }

    public ArrayList<WeightKeyBoardToolsView.WeightKeyBoardAction> getWeightToolsList(){
        ArrayList<WeightKeyBoardToolsView.WeightKeyBoardAction> arr = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM "+DBWeightToolsHelper.TABLE_NAME, null);

        if(c != null && c.moveToFirst()){
            do{
                WeightKeyBoardToolsView.WeightKeyBoardAction ac = new WeightKeyBoardToolsView.WeightKeyBoardAction();
                ac.setAction(c.getString(c.getColumnIndex(DBWeightToolsHelper.ACTION)));
                ac.setNumber(c.getInt(c.getColumnIndex(DBWeightToolsHelper.NUMBER)));
                arr.add(ac);
            }while(c.moveToNext());
        }
        return arr;
    }

    public void close(){
        if (db != null) {
            db.close();
        }
    }


}

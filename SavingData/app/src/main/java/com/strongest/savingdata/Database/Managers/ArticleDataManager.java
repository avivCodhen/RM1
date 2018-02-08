package com.strongest.savingdata.Database.Managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.strongest.savingdata.Database.Articles.ArticleObj;
import com.strongest.savingdata.Database.Articles.DBArticleHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.strongest.savingdata.Database.Articles.DBArticleHelper.IMAGE;
import static com.strongest.savingdata.Database.Articles.DBArticleHelper.LINK;
import static com.strongest.savingdata.Database.Articles.DBArticleHelper.PAGE;
import static com.strongest.savingdata.Database.Articles.DBArticleHelper.SUMMARY;
import static com.strongest.savingdata.Database.Articles.DBArticleHelper.TITLE;

/**
 * Created by Cohen on 10/13/2017.
 */


public class ArticleDataManager extends DataManager implements DataManagerListener<ArticleObj> {

    private SQLiteDatabase db;
    private DBArticleHelper helper;
    private Context context;

    public ArticleDataManager(Context context) {
        super(context);
        this.context = context;
        this.helper = new DBArticleHelper(context);
        db = helper.getWritableDatabase();
    }


    @Override
    public void insertData(String tableName, ArticleObj obj) {
        db.insert(tableName,
                null,
                getContentValues(obj));

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
        if (db != null) {
            db.close();
        }
    }

    @Override
    public ArticleObj fetchById(String table, int id) {
        return null;
    }

    @Override
    public Collection<ArticleObj> readByTable(String table) {
        return readByConstraint(table, null, null);
    }

    @Override
    public Collection<ArticleObj> readByString(String tableName, String s, int c) {
        return null;
    }

    @Override
    public void removeById(String table, int id) {

    }

    @Override
    public Collection<ArticleObj> readByConstraint(String tableName, String[] constraints, String[] values) {
        String sql = "SELECT * FROM " + tableName;
        String where = " WHERE";
        String w = "=? ";
        String condition = " and ";


        if (constraints != null) {
            sql += where;
            for (int i = 0; i < constraints.length; i++) {
                sql += constraints[i] + w;
                if (constraints.length > 1 && i != constraints.length - 1) {
                    sql += condition;
                }
            }

        }
        sql += " ORDER BY _id ASC";

        Cursor cursor = db.rawQuery(sql, values);
        return parse(tableName, cursor);
    }

    @Override
    public Collection<ArticleObj> parse(String tableNale, Cursor c) {
        List<ArticleObj> arr = new ArrayList<>();
        if (c != null && c.moveToFirst()) {
            do {
                ArticleObj a = new ArticleObj();
                a.setTitle(c.getString(c.getColumnIndex(TITLE)));
                a.setSummary(c.getString(c.getColumnIndex(SUMMARY)));
                a.setLink(c.getString(c.getColumnIndex(LINK)));
                a.setPage(c.getString(c.getColumnIndex(PAGE)));
                a.setBitmap(c.getBlob(c.getColumnIndex(IMAGE)));

                arr.add(a);
            } while (c.moveToNext());
        }
        return arr;

    }

    public ContentValues getContentValues(ArticleObj a) {
        ContentValues v = new ContentValues();
        v.put(TITLE, a.getTitle());
        v.put(SUMMARY, a.getSummary());
        v.put(LINK, a.getLink());
        v.put(PAGE, a.getPage());
        v.put(IMAGE, a.getBytes());
        return v;
    }

    public SQLiteDatabase getDb() {
        return db;
    }
}

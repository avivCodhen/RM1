package com.strongest.savingdata.Database.Managers;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Collection;

/**
 * Created by Cohen on 10/13/2017.
 */

public interface DataManagerListener<T> {

    void insertData(String tableName, T t);

    void delete(String table);


    void close();

    T fetchById(String table, int id);

    Collection<T> readByTable(String table);

    Collection<T> readByString(String tableName, String s, int c);

    void removeById(String table, int id);

    Collection<T> readByConstraint(String tableName, String[] constraints, String[] values);

    Collection<T>  parse(String tableNale, Cursor c);



}

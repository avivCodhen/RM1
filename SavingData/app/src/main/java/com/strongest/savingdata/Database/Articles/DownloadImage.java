package com.strongest.savingdata.Database.Articles;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.strongest.savingdata.Database.Managers.ArticleDataManager;
import com.strongest.savingdata.Database.Managers.DataManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Cohen on 10/13/2017.
 */

public class DownloadImage extends Thread{

    private ArticleObj arobj;
    private DataManager dataManager;

    public DownloadImage(ArticleObj arobj, DataManager dataManager){
        this.arobj = arobj;
        this.dataManager = dataManager;
    }

    @Override
    public synchronized void run() {
        URL url = null;
        try {
            url = new URL(arobj.getLink());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream in = connection.getInputStream();
            final Bitmap bitmap = BitmapFactory.decodeStream(in);
            arobj.setBitmap(bitmap);
            dataManager.getArticleDataManager().insertData(DBArticleHelper.TABLE_NAME,arobj);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

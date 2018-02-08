package com.strongest.savingdata.Database.Articles;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by Cohen on 10/13/2017.
 */

public class ArticleObj {

    public static final String LINK = "";
    public static final String SUMMARY = "";
    public static final String TITLE = "h2";
    public static final String IMAGE = "";
    private String title;
    private String summary;
    private String link;
    private Bitmap bitmap;
    private String page;

    public ArticleObj() {

    }

    public String getLink() {
        return link;
    }

    public String getSummary() {
        return summary;
    }

    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    public void setBitmap(byte[] bitmap){
        this.bitmap = BitmapFactory.decodeByteArray(bitmap,0,bitmap.length);
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public byte[] getBytes(){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        this.bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }
}


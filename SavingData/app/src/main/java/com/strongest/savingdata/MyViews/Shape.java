package com.strongest.savingdata.MyViews;

/**
 * Created by Cohen on 10/26/2017.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.widget.ImageView;


public class Shape {

    private Bitmap bmp;
    private ImageView img;
    public Shape(Bitmap bmp, ImageView img) {

        this.bmp=bmp;
        this.img=img;
        onDraw();
    }


    private void onDraw(){

        if (bmp.getWidth() == 0 || bmp.getHeight() == 0) {
            return;
        }
        int w = bmp.getWidth(), h = bmp.getHeight();

        Bitmap roundBitmap = getHexagonalCroppedBitmap(bmp, w);

        img.setImageBitmap(roundBitmap);

    }

    public static Bitmap getHexagonalCroppedBitmap(Bitmap bitmap, int radius) {
        Bitmap finalBitmap;
        if (bitmap.getWidth() != radius || bitmap.getHeight() != radius)
            finalBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius,
                    false);
        else
            finalBitmap = bitmap;
        Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(),
                finalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, finalBitmap.getWidth(),
                finalBitmap.getHeight());

        Point point1_draw = new Point(75, 0);
        Point point2_draw = new Point(0, 50);
        Point point3_draw = new Point(0, 100);
        Point point4_draw = new Point(75, 150);
        Point point5_draw = new Point(150, 100);
        Point point6_draw = new Point(150, 50);

        Path path = new Path();
        path.moveTo(point1_draw.x, point1_draw.y);
        path.lineTo(point2_draw.x, point2_draw.y);
        path.lineTo(point3_draw.x, point3_draw.y);
        path.lineTo(point4_draw.x, point4_draw.y);
        path.lineTo(point5_draw.x, point5_draw.y);
        path.lineTo(point6_draw.x, point6_draw.y);

        path.close();
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawPath(path, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(finalBitmap, rect, rect, paint);

        return output;
    }

}
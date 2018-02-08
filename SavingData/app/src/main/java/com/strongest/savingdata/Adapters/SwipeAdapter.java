package com.strongest.savingdata.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.strongest.savingdata.R;

/**
 * Created by Cohen on 4/21/2017.
 */

public class SwipeAdapter extends PagerAdapter {

    private Context context;
    private int[] images;
    private String[] info;
    private LayoutInflater layoutInflater;

    private int imageView;
    private int textView;

    public SwipeAdapter(Context context, int[] images, String[] info, int imageView, int textView) {
       this.context = context;
        this.images = images;
        this.info = info;
        this.imageView = imageView;
        this.textView = textView;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view ==(LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.swipe_layout, container, false);
        ImageView image = (ImageView) view.findViewById(imageView);
        TextView text = (TextView) view.findViewById(R.id.swipe_layout_textView);
        image.setImageResource(images[position]);
        text.setText(info[position]+"");
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }
}

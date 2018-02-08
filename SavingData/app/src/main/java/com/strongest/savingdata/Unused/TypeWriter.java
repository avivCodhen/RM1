package com.strongest.savingdata.Unused;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Cohen on 7/3/2017.
 */

public class TypeWriter extends android.support.v7.widget.AppCompatTextView {

    private long delay;
    private int index;
    private CharSequence text;
    private Handler handler = new Handler();

    public TypeWriter(Context context) {
        super(context);
    }

    public TypeWriter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private Runnable charAdder = new Runnable() {
        @Override
        public void run() {
            setText(text.subSequence(0, index++));

            if(index < text.length()){
                handler.postDelayed(charAdder, delay);
            }
            if(index == text.length()-3){
                delay *= 50;
            }
        }
    };

    public void animateText(CharSequence text){
        this.text = text;
        index = 0;
        setText("");
        handler.removeCallbacks(charAdder);
        handler.postDelayed(charAdder, delay);

    }

    public void setDelay(long delay){
        this.delay = delay;
    }

}

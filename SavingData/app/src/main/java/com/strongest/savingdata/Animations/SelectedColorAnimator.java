package com.strongest.savingdata.Animations;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.strongest.savingdata.R;

/**
 *
 * this class handles Selected animation
 * where the View's background is replaced with a selected
 * background and returns to it's former background on onSelected
 * */
public class SelectedColorAnimator {


    public static class AnimatorHolder{

        int prevColor;
        View v;
        public AnimatorHolder(int prevColor){

            this.prevColor = prevColor;
        }

        public void makeSelectedColor(View v, int selectedColor) {
            Drawable background = v.getBackground();
            if (background instanceof ColorDrawable){
                this.prevColor = ((ColorDrawable) background).getColor();
            }
            this.v = v;
            v.setBackgroundColor(selectedColor);
        }

        public void revertPrevColor() {
            v.setBackgroundColor(prevColor);
        }
    }

    public interface OnSelected{
        void onSelected();
    }

    public interface onItemClear{
        void onItemClear();
    }
}

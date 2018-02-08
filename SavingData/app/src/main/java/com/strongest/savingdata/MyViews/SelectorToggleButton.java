package com.strongest.savingdata.MyViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.strongest.savingdata.R;

/**
 * Created by Cohen on 1/1/2018.
 */

public class SelectorToggleButton extends ToggleButton {
    private ImageView iv;

    public SelectorToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        View v = inflate(context, R.layout.selector_toggle_button, null);
        iv = (ImageView) v.findViewById(R.id.selector_image_view);
    }

    public SelectorToggleButton(Context context) {
        super(context);
        View v =  inflate(context, R.layout.selector_toggle_button, null);
        iv =(ImageView) v.findViewById(R.id.selector_image_view);

    }

    public ImageView getIv() {
        return iv;
    }
}

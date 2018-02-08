package com.strongest.savingdata.MyViews;

import android.content.Context;
import android.util.TypedValue;
import android.widget.TextView;


import java.io.Serializable;

/**
 * Created by Cohen on 7/20/2017.
 */

//used to view bodypart in the main exercise layout
    @Deprecated
public class BodyTextView extends TextView implements Serializable{

    private int bodyPosition;
    public BodyTextView(Context context) {
        super(context);

        init();
    }

    private void init() {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
    }

    public int getBodyPosition() {
        return bodyPosition;
    }

    public void setBodyPosition(int bodyPosition) {
        this.bodyPosition = bodyPosition;
    }

   /* public Body.Muscle getBody() {
        return body;
    }

    public void setBody(Body.Muscle body) {
        this.body = body;
    }*/
}

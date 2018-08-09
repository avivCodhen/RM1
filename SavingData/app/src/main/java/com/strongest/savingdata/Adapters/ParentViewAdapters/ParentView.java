package com.strongest.savingdata.Adapters.ParentViewAdapters;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.zip.Inflater;

public class ParentView extends LinearLayout {

    private LinearLayout parent;
    private Adapter adapter;
    private Handler handler;

    public ParentView(Context context) {
        super(context);
    }


    public static ParentView loadParent(Context context, LinearLayout parent) {
        ParentView parentView = new ParentView(context);
        parentView.parent = parent;
        parentView.handler = new Handler();
        return parentView;
    }

    public ParentView with(ParentView.Adapter adapter) {
        this.adapter = adapter;
        return this;
    }

    public ParentView reset() {
        parent.removeAllViews();
        return this;
    }

    public void make() {
        final ViewGroup container = this;
        int length = adapter.getCount();
        if (parent.getChildCount() == length) {
            return;
        }
        validate();


        for (int i = 0; i < length; i++) {

            RecyclerView.ViewHolder vh = adapter.onCreateChildViewHolder(container);

            parent.addView(vh.itemView);


            adapter.onBindViewHolder(vh, i);

        }
    }

    private void validate() {
        if (this.parent == null) {
            throw new IllegalArgumentException("ParentView must have a valid parent");
        } else if (this.adapter == null) {
            throw new IllegalArgumentException("Adapter is null");
        } /*else if (this.parent.getLayoutParams().height == LinearLayout.LayoutParams.WRAP_CONTENT) {
            throw new IllegalArgumentException("Parent does not have wrap_content as height param");
        }*/
    }

    public static abstract class Adapter<T extends RecyclerView.ViewHolder> {

        public abstract T onCreateChildViewHolder(ViewGroup container);

        public abstract int getCount();

        public abstract void onBindViewHolder(T t, int position);
    }
}

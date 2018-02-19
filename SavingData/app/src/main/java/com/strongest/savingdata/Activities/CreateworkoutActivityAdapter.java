package com.strongest.savingdata.Activities;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.strongest.savingdata.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Cohen on 12/19/2017.
 */

public class CreateworkoutActivityAdapter extends RecyclerView.Adapter<CreateworkoutActivityAdapter.TimeLineViewHolder> {

    private Context context;
    private ArrayList<Fragment> fragList;

    public CreateworkoutActivityAdapter(Context context, ArrayList<Fragment> fragList){
        this.fragList = fragList;
        this.context = context;
    }
    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(context, R.layout.timeline, parent);
        return new TimeLineViewHolder(v, viewType);
    }

    @Override
    public void onBindViewHolder(TimeLineViewHolder holder, int position) {
      //  if()
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class TimeLineViewHolder extends RecyclerView.ViewHolder{

        private ImageView icon;
        private TextView tv;
        public TimeLineViewHolder(View itemView, int viewtype) {
            super(itemView);

        }
    }


}

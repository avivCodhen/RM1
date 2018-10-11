package com.strongest.savingdata.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.strongest.savingdata.R;
import com.strongest.savingdata.Utils.MyUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DefaultProgramsRecycAdapter extends RecyclerView.Adapter<DefaultProgramsRecycAdapter.ViewHolder> {


    private ArrayList<String> list;
    private OnDefaultWorkoutClick onDefaultWorkoutClick;

    public DefaultProgramsRecycAdapter(ArrayList<String> list, OnDefaultWorkoutClick onDefaultWorkoutClick){

        this.list = list;
        this.onDefaultWorkoutClick = onDefaultWorkoutClick;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.layout_default_workouts, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String s = list.get(position);
        holder.itemView.setOnClickListener(v->onDefaultWorkoutClick.onDefaultClick(s));
        holder.tv.setText(MyUtils.trimLineAndUnderScoreToSpace(s));
        if(position == 0){
            holder.tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.programNameTv)
        TextView tv;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnDefaultWorkoutClick{
        void onDefaultClick(String s);
    }
}

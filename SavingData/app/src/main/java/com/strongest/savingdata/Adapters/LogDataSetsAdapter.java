package com.strongest.savingdata.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.strongest.savingdata.Database.LogData;
import com.strongest.savingdata.R;

import java.util.ArrayList;

public class LogDataSetsAdapter extends RecyclerView.Adapter<LogDataSetsAdapter.ViewHolder> {


    private ArrayList<LogData.LogDataSets> list;

    public LogDataSetsAdapter(ArrayList<LogData.LogDataSets> list) {

        this.list = list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.recycler_logdatasets, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public ArrayList<LogData.LogDataSets> getList() {
        return list;
    }

    public void setList(ArrayList<LogData.LogDataSets> list) {
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}

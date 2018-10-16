package com.strongest.savingdata.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.strongest.savingdata.Controllers.LogDataAdapterOnClick;
import com.strongest.savingdata.Database.LogData;
import com.strongest.savingdata.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LogDataAdapter extends RecyclerView.Adapter<LogDataAdapter.LogDataAdapterViewHolder> {


    private ArrayList<LogData> dates;
    private LogDataAdapterOnClick logDataAdapterOnClick;

    public LogDataAdapter(ArrayList<LogData> dates) {

        this.dates = dates;
    }

    @Override
    public LogDataAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.recycler_view_log_date, parent, false);
        return new LogDataAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(LogDataAdapterViewHolder holder, int position) {
        LogData l = dates.get(position);
        String fullText;
        if (l.time.equals("")) {
            fullText = l.date;
            holder.arrowIV.setVisibility(View.GONE);
        } else {
            holder.itemView.setOnClickListener(v -> {
                logDataAdapterOnClick.dateClicked(dates.get(position).full);
            });
            holder.arrowIV.setVisibility(View.VISIBLE);

            fullText = l.date + "," + l.time;
        }
        holder.tv.setText(fullText);
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public void setLogDataAdapterOnClick(LogDataAdapterOnClick logDataAdapterOnClick) {
        this.logDataAdapterOnClick = logDataAdapterOnClick;
    }

    public static class LogDataAdapterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_date)
        TextView tv;

        @BindView(R.id.arrow_log_data_iv)
        ImageView arrowIV;

        @BindView(R.id.tv_time)
        TextView timeTV;

        public LogDataAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

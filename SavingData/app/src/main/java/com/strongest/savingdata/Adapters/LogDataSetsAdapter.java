package com.strongest.savingdata.Adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.strongest.savingdata.Controllers.CallBacks;
import com.strongest.savingdata.Database.LogData;
import com.strongest.savingdata.Handlers.MaterialDialogHandler;
import com.strongest.savingdata.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LogDataSetsAdapter extends RecyclerView.Adapter<LogDataSetsAdapter.ViewHolder> {


    private ArrayList<LogData.LogDataSets> list;
    private boolean editable;
    Context context;
    private CallBacks.Change onChange;

    public LogDataSetsAdapter(Context context, ArrayList<LogData.LogDataSets> list,boolean editable, CallBacks.Change onChange) {
        this.context = context;

        this.list = list;
        this.editable = editable;
        this.onChange = onChange;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.recycler_logdatasets, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        LogData.LogDataSets l = list.get(position);
        l.add = holder.checkBox.isChecked();

        holder.reps.setText(l.rep);
        holder.rest.setText(l.rest);
        holder.weight.setText(l.weight+"");
        holder.title.setText(l.title);

        if(!editable){
            holder.reps.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.weight.setTextColor(ContextCompat.getColor(context, R.color.black));
        }

        if(editable){
            holder.repLay.setOnClickListener(repLayView->{
                MaterialDialogHandler.get().showInputDialog(
                        context,
                        InputType.TYPE_CLASS_NUMBER,
                        2,
                        l.rep,
                        "Change Repetitions",
                        (dialog,input)-> {
                            l.rep = input.toString();
                            notifyItemChanged(position);
                            onChange.onChange();

                        }
                );
            });

            holder.weightLay.setOnClickListener(repLayView->{
                MaterialDialogHandler.get().showInputDialog(
                        context,
                        InputType.TYPE_NUMBER_FLAG_DECIMAL,
                        6,
                        String.valueOf(l.weight),
                        "Change Weight",
                        (dialog,input)-> {
                            l.weight = Double.parseDouble(input.toString());
                            notifyItemChanged(position);
                            onChange.onChange();
                        }
                );

            });


        }

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

        @BindView(R.id.title_tv)
        TextView title;

        @BindView(R.id.checkbox)
        CheckBox checkBox;

        @BindView(R.id.reps_tv)
        TextView reps;

        @BindView(R.id.rest_tv)
        TextView rest;

        @BindView(R.id.weight_tv)
        TextView weight;

        @BindView(R.id.weight_lay)
        ViewGroup weightLay;

        @BindView(R.id.reps_lay)
        ViewGroup repLay;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

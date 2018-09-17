package com.strongest.savingdata.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.strongest.savingdata.AModels.programModel.Program;
import com.strongest.savingdata.Controllers.Architecture;
import com.strongest.savingdata.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyProgramsAdapter extends RecyclerView.Adapter<MyProgramsAdapter.ViewHolder> {

    private ArrayList<Program> programs;
    private Program currentProgram;
    private Architecture.program listener;
    private boolean isShared;

    public void setShared(boolean shared) {
        isShared = shared;
    }

    public MyProgramsAdapter(ArrayList<Program> programs, Program currentProgram, Architecture.program listener, boolean isShared) {

        this.programs = programs;
        this.currentProgram = currentProgram;
        this.listener = listener;
        this.isShared = isShared;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.recycler_view_program_layout_rightmargin, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Program p = programs.get(position);
        holder.progName.setText(p.getProgramName());
        holder.date.setText("Created at " + p.getProgramDate() + ", " + p.getTime());
        if (!p.isSeen) {
            holder.alert.setVisibility(View.VISIBLE);
        } else {
            holder.alert.setVisibility(View.GONE);

        }
        holder.creatorTV.setText("Created By " + p.getCreator());

        if(isCurrentProgram(p)){
            holder.loadProgramBtn.setEnabled(false);
            holder.loadProgramBtn.setClickable(false);
            holder.loadProgramBtn.setAlpha(0.6f);
            holder.currentProgTv.setVisibility(View.VISIBLE);
            holder.deleteProgIV.setAlpha(0.6f);
        }else{
            holder.currentProgTv.setVisibility(View.GONE);
            holder.loadProgramBtn.setOnClickListener(loadProgramView->{
                if(isShared)
                listener.loadSharedProgram(p);
                else
                listener.loadProgram(p);
            });
        }

        if(!isCurrentProgram(p))
        holder.deleteProgIV.setOnClickListener(deleteV->{
            listener.deleteProgram(p);
        });

        holder.shareIV.setOnClickListener(share-> listener.shareProgram(p));
    }

    private boolean isCurrentProgram(Program p){
        if(currentProgram == null){
            return true;
        }
        if(p.equals(currentProgram)){
            return true;
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return programs.size();
    }

    public void setList(ArrayList<Program> list) {
        this.programs = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView progName, date;

        @BindView(R.id.program_creator)
        TextView creatorTV;

        @BindView(R.id.program_alert)
        ImageView alert;

        @BindView(R.id.load_program_btn)
        Button loadProgramBtn;

        @BindView(R.id.current_program_tv)
        TextView currentProgTv;

        @BindView(R.id.delete_program)
        ImageView deleteProgIV;

        @BindView(R.id.share_program)
        ImageView shareIV;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            progName = (TextView) itemView.findViewById(R.id.program_name);
            date = (TextView) itemView.findViewById(R.id.program_date);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                  /*  workoutsViewModel.setCmd(WorkoutsService.CMD.SWITCH);
                    programViewModel.switchProgram(programs.get(getAdapterPosition()));

                    getFragmentManager().popBackStack();*/
                }
            });
        }
    }

}


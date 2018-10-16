package com.strongest.savingdata.Adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.strongest.savingdata.AModels.programModel.Program;
import com.strongest.savingdata.Activities.MyProgramsActivity;
import com.strongest.savingdata.Controllers.Architecture;
import com.strongest.savingdata.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyProgramsAdapter extends RecyclerView.Adapter<MyProgramsAdapter.ViewHolder> {

    private ArrayList<Program> programs;
    private Program currentProgram;
    private Architecture.program listener;
    private boolean isShared;

    Context context;
    private String tag;

    public void setShared(boolean shared) {
        isShared = shared;
    }

    public MyProgramsAdapter(ArrayList<Program> programs, Program currentProgram, Architecture.program listener, boolean isShared, String tag) {

        this.programs = programs;
        this.currentProgram = currentProgram;
        this.listener = listener;
        this.isShared = isShared;
        this.tag = tag;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        this.context = parent.getContext();
        View v = inflater.inflate(R.layout.recycler_view_program_layout_rightmargin, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Program p = programs.get(position);
        holder.progName.setText(p.getProgramName());
        holder.date.setText("Created " + p.getProgramDate());
        if (!p.isSeen) {

            holder.alert.setVisibility(View.VISIBLE);
        } else {
            holder.alert.setVisibility(View.GONE);
        }

        holder.creatorTV.setText(p.getCreator());


       /* if (p.isUnShareable()) {
            holder.shareIV.setVisibility(View.GONE);
        }*/

        if (tag.equals(MyProgramsActivity.FRAGMENT_USER_SHARED_BY)) {
            holder.shareIV.setVisibility(View.GONE);
            holder.sharedTV.setVisibility(View.GONE);
        }
        holder.sharedTV.setText(p.numOfShared <= 0 ? "No Shares" : "Shared with " + p.numOfShared + " people");


        holder.shareIV.setOnClickListener(share -> listener.shareProgram(p));

        holder.options.setOnClickListener(optionsView -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.options);
            if (!p.isSeen) {
                popupMenu.inflate(R.menu.my_program_program_menu_unseen);

            } else {
                popupMenu.inflate(R.menu.my_program_program_menu_seen);

            }
            popupMenu.setOnMenuItemClickListener((menuItem) -> {

                switch (menuItem.getItemId()) {
                    case R.id.menu_program_delete_program:
                        listener.deleteProgram(p);
                        break;
                    case R.id.menu_program_share_program:
                        listener.shareProgram(p);
                        break;
                    case R.id.menu_program_load_program:
                        listener.loadProgram(p);
                        break;

                    case R.id.menu_program_seen_program:
                        listener.seen(p);
                        break;
                }
                return true;
            });
            popupMenu.show();

        });

        holder.itemView.setOnClickListener(clickedView -> {
            listener.loadProgram(p);
        });

        if(isCurrentProgram(p)){
            holder.progName.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            holder.currentIV.setVisibility(View.VISIBLE);
        }else{
            holder.currentIV.setVisibility(View.GONE);

        }
    }


    private boolean donotShowDeleteButton(Program p) {
        if (tag.equals(MyProgramsActivity.FRAGMENT_USER_SHARED_BY) || isCurrentProgram(p)) {
            return true;
        }
        return false;
    }

    private boolean isCurrentProgram(Program p) {
        if (currentProgram == null) {
            return false;
        }
        if (p.equals(currentProgram)) {
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

        @BindView(R.id.shared_with_tv)
        TextView sharedTV;

        @BindView(R.id.program_options)
        ImageView options;

        @BindView(R.id.program_current_iv)
        ImageView currentIV;

        /* @BindView(R.id.load_program_btn)
         Button loadProgramBtn;

         @BindView(R.id.current_program_tv)
         TextView currentProgTv;

         @BindView(R.id.delete_program)
         ImageView deleteProgIV;
 */
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


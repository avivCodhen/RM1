package com.strongest.savingdata.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.strongest.savingdata.AViewModels.ProgramViewModel;
import com.strongest.savingdata.Activities.HomeActivity;
import com.strongest.savingdata.AModels.programModel.Program;
import com.strongest.savingdata.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cohen on 3/9/2018.
 */

public class MyProgramsFragment extends BaseFragment {

    private List<Program> programs;
    private RecyclerView recyclerView;
    private Program currentProgram;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_program, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View v) {

        //TODO: fix this
       // currentProgram = ((HomeActivity) getActivity()).workoutsViewModel.getProgram().getValue();
        v.findViewById(R.id.toolbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        programViewModel = ViewModelProviders.of(getActivity()).get(ProgramViewModel.class);
        programViewModel.getProgram().observe(this, (program)->{
            currentProgram = program;
            initStaticViews(v);


        });

        programViewModel.getPrograms().observe(this, (progList)->{
            programs = progList;
            MyProgramsAdapter adapter = new MyProgramsAdapter();
            recyclerView.setAdapter(adapter);
        });
        recyclerView = (RecyclerView) v.findViewById(R.id.fragment_my_program_recyclerview);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(lm);
        ((TextView) v.findViewById(R.id.tool_bar_title)).setText(" My Programs");



    }

    private void initStaticViews(View v) {
        View includeLeftMarginProgram = v.findViewById(R.id.my_program_view);
        TextView programTitle = (TextView) includeLeftMarginProgram.findViewById(R.id.program_name);
        TextView programTimeAndDate = (TextView) includeLeftMarginProgram.findViewById(R.id.program_date);


        programTitle.setText(currentProgram.getProgramName());
        programTimeAndDate.setText(currentProgram.getProgramDate() + ", " + currentProgram.getTime());
    }



    private class MyProgramsAdapter extends RecyclerView.Adapter<MyProgramsAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View v = inflater.inflate(R.layout.recycler_view_program_layout_rightmargin, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.progName.setText(programs.get(position).getProgramName());
            holder.date.setText(programs.get(position).getProgramDate() + ", " + programs.get(position).getTime());


        }

        @Override
        public int getItemCount() {
            return programs.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView progName, date;

            public ViewHolder(View itemView) {
                super(itemView);

                progName = (TextView) itemView.findViewById(R.id.program_name);
                date = (TextView) itemView.findViewById(R.id.program_date);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((HomeActivity) getActivity()).finish();
                        ((HomeActivity) getActivity()).startActivity(((HomeActivity) getActivity()).getIntent());
                        getFragmentManager().popBackStack();
                    }
                });
            }
        }

    }


}

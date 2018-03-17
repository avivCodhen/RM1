package com.strongest.savingdata.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.strongest.savingdata.Activities.HomeActivity;
import com.strongest.savingdata.AlgorithmLayout.PLObjects;
import com.strongest.savingdata.AlgorithmLayout.ReactLayoutManager;
import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.Database.Exercise.Beans;
import com.strongest.savingdata.MyViews.WorkoutView.Choose.ChooseDialogFragment;
import com.strongest.savingdata.R;
import com.strongest.savingdata.createProgramFragments.CreateProgram.BaseCreateProgramFragment;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Cohen on 3/13/2018.
 */

public class ExerciseChooseFragment extends BaseCreateProgramFragment {

    private TextView mMuscleText;
    private CircleImageView mCircleMuscleIcon;

    ExerciseChooseFragmentAdapter mAdapter;
    public ExpandableLayout mExpandable;
    private GridView mGridView;
    private GridViewAdapter mGridViewAdapter;
    private RecyclerView recyclerView;
    private PLObjects.ExerciseProfile exerciseProfile;
    public static final String EXERCISE_PROFILE = "exercise_profile";
    private ArrayList<Beans> exerciseBeans = new ArrayList<>();

    private ReactLayoutManager reactLayoutManager;

    public static ExerciseChooseFragment newInstance(PLObjects.ExerciseProfile exerciseProfile) {
        ExerciseChooseFragment f = new ExerciseChooseFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXERCISE_PROFILE, exerciseProfile);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            exerciseProfile = (PLObjects.ExerciseProfile) getArguments().getSerializable(EXERCISE_PROFILE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_exercise, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View v) {
        reactLayoutManager = ((ChooseContainerFragment) getParentFragment()).getReactLayoutManager();
        mCircleMuscleIcon = (CircleImageView) v.findViewById(R.id.muscle_icon);
        mMuscleText = (TextView) v.findViewById(R.id.muscle_tv);
        mExpandable = (ExpandableLayout) v.findViewById(R.id.expandable);
        mGridView = (GridView) v.findViewById(R.id.fragment_choose_exercise_gridview);
        mGridViewAdapter = new GridViewAdapter();
        mGridView.setAdapter(mGridViewAdapter);

        if (exerciseProfile.getMuscle() != null) {
            initExerciseBeans(exerciseProfile.getMuscle());
        }

        recyclerView = (RecyclerView) v.findViewById(R.id.choose_recyclerview);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        mAdapter = new ExerciseChooseFragmentAdapter();
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(mAdapter);
        ChooseContainerFragment chooseContainerFragment = (ChooseContainerFragment) getParentFragment();
        ChooseDialogFragment chooseDialogFragment = (ChooseDialogFragment) chooseContainerFragment.getParentFragment();
        ViewCompat.setNestedScrollingEnabled(chooseDialogFragment.getNestedScrollView(), false);
        v.findViewById(R.id.choose_change_Tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandable.toggle();
            }
        });



    }

    private void initExerciseBeans(Muscle m){
            exerciseBeans = (ArrayList<Beans>) ((HomeActivity) getActivity()).
                    dataManager.getExerciseDataManager().
                    readByTable(m.getMuscle_name());
    }

    private void onMuscleChange(GridViewAdapter.MusclesContentHolder mch) {
        //make choosereact change the exerciseprofile muscle
        initExerciseBeans(mch.m);
        mAdapter.notifyDataSetChanged();
        mCircleMuscleIcon.setImageResource(mch.icon);
        mMuscleText.setText(mch.text);

    }


    public class ExerciseChooseFragmentAdapter extends RecyclerView.Adapter<ExerciseChooseFragmentAdapter.ViewHolder> {

        @Override
        public ExerciseChooseFragmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View v = inflater.inflate(R.layout.recycler_view_exercise_choose, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ExerciseChooseFragmentAdapter.ViewHolder holder, final int position) {

            holder.exercise.setText(exerciseBeans.get(position).getName());

            String muscle = "";
            String[] muscles = Muscle.parseMuscles(exerciseBeans.get(position).getMuscles());
            int index = 0;
            for (String m : muscles) {
                muscle +=m;
                if(index == 0 || index != muscle.length() && index != 0){
                    muscle+= ", ";
                }
                index++;
                if(index == muscle.length()){
                    muscle = muscle.substring(0, muscle.length()-2);
                }
            }
            holder.muscles.setText(muscle);

            holder.type.setText(exerciseBeans.get(position).getType());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 //   reactLayoutManager.updateExercise(exerciseBeans.get(position));
                }
            });

        }

        @Override
        public int getItemCount() {
           return exerciseBeans.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView exercise, muscles, type;

            public ViewHolder(View itemView) {
                super(itemView);
               exercise = (TextView) itemView.findViewById(R.id.exercise_tv);
                muscles = (TextView) itemView.findViewById(R.id.exercise_muscles_tv);
                type = (TextView) itemView.findViewById(R.id.choose_type);
            }
        }

    }

    class GridViewAdapter extends BaseAdapter {

        ArrayList<MusclesContentHolder> list;
        ArrayList<Muscle> muscles;

        GridViewAdapter() {

            muscles = ((HomeActivity) getActivity()).dataManager.getMuscleDataManager().getAllMuscles();
            list = new ArrayList<>();
            for (int i = 0; i < muscles.size(); i++) {
                Muscle.MuscleUI mui = Muscle.provideMuscleUI(muscles.get(i));
                if (mui != null) {
                    list.add(new MusclesContentHolder(mui.getImage(), muscles.get(i).getMuscle_display(), muscles.get(i)));
                }
            }

        }

        class MusclesContentHolder {

            public String text;
            public int icon;
            public Muscle m;

            MusclesContentHolder(int icon, String text, Muscle m) {

                this.icon = icon;
                this.text = text;
                this.m = m;
            }

        }

        class ViewHolder {
            TextView tv;
            ViewGroup cv;
            CircleImageView civ;

            ViewHolder(View v, int position) {
                civ = (CircleImageView) v.findViewById(R.id.muscle_card_civ);
                tv = (TextView) v.findViewById(R.id.muscle_card_tv);
                cv = (ViewGroup) v.findViewById(R.id.recycler_view_muscle_card_layout);

            }
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater li = LayoutInflater.from(getContext());
            View v = convertView;
            ViewHolder holder = null;
            if (v == null) {
                v = li.inflate(R.layout.recycler_view_muscle_card, parent, false);
                holder = new ViewHolder(v, position);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }

            holder.tv.setText(list.get(position).text);
            holder.civ.setImageResource(list.get(position).icon);
            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onMuscleChange(list.get(position));
                    mExpandable.collapse();
                }
            });

            return v;
        }
    }
}

package com.strongest.savingdata.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.strongest.savingdata.Activities.HomeActivity;
import com.strongest.savingdata.Adapters.ExerciseListAdapter;
import com.strongest.savingdata.Adapters.GridViewMusclesAdapter;
import com.strongest.savingdata.Adapters.OnExerciseListAdapterClickListener;
import com.strongest.savingdata.Adapters.OnGridViewMuscleAdapterClickListener;
import com.strongest.savingdata.AlgorithmLayout.PLObject;
import com.strongest.savingdata.AlgorithmLayout.ReactLayoutManager;
import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.Database.Exercise.Beans;
import com.strongest.savingdata.Database.Exercise.DBExercisesHelper;
import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.MyViews.WorkoutView.Choose.ExerciseSearchSuggestion;
import com.strongest.savingdata.MyViews.WorkoutView.Choose.OnExerciseSetChange;
import com.strongest.savingdata.R;
import com.strongest.savingdata.createProgramFragments.CreateProgram.BaseCreateProgramFragment;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

import br.com.mauker.materialsearchview.MaterialSearchView;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.widget.GridLayout.HORIZONTAL;

/**
 * Created by Cohen on 3/13/2018.
 */

public class ExerciseChooseFragment extends BaseCreateProgramFragment implements
        OnExerciseListAdapterClickListener, OnGridViewMuscleAdapterClickListener {

    private OnExerciseSetChange onExerciseSetChange;
    private TextView mMuscleText, mChangeMuscleTV, mCancelMuscleTV;
    public ExerciseListAdapter mAdapter;
    public ExpandableLayout mExpandable;
    private GridView mGridView;
    private GridViewMusclesAdapter mGridViewAdapter;
    private RecyclerView recyclerView;
    private PLObject.ExerciseProfile exerciseProfile;
    public static final String EXERCISE_PROFILE = "exercise_profile";
    public ArrayList<Beans> exerciseBeans = new ArrayList<>();
    private CheckBox commentCB;
    private EditText commentET;
    private DataManager dataManager;
    private int selectedIndex = -1;
    private ArrayList<Beans> allExercisesList;
    private ArrayList<ExerciseSearchSuggestion> allExercisesListString;
    //private LayoutManager.LayoutManagerHelper helper;

    private ReactLayoutManager reactLayoutManager;

    public static ExerciseChooseFragment newInstance(PLObject.ExerciseProfile exerciseProfile, OnExerciseSetChange onExerciseSetChange) {
        ExerciseChooseFragment f = new ExerciseChooseFragment();
        f.setOnExerciseSetChange(onExerciseSetChange);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXERCISE_PROFILE, exerciseProfile);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            exerciseProfile = (PLObject.ExerciseProfile) getArguments().getSerializable(EXERCISE_PROFILE);
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
        dataManager = ((HomeActivity) getActivity()).dataManager;
        initAllExercises();
        //    helper =  ((BaseActivity) getActivity()).programmer.layoutManager.mLayoutManagerHelper;
        //  reactLayoutManager = ((ChooseContainerFragment) getParentFragment()).getReactLayoutManager();
        mMuscleText = (TextView) v.findViewById(R.id.muscle_tv);
        mExpandable = (ExpandableLayout) v.findViewById(R.id.expandable);
        mGridView = (GridView) v.findViewById(R.id.fragment_choose_exercise_gridview);
        int height = ((HomeActivity) getActivity()).getScreenHeight();
        mGridViewAdapter = new GridViewMusclesAdapter(height, getContext(), dataManager, this);
        mGridView.setAdapter(mGridViewAdapter);

        if (exerciseProfile.getMuscle() != null) {
            mMuscleText.setText(exerciseProfile.getMuscle().getMuscle_display());
        }

        if (exerciseProfile.getMuscle() != null) {
            initExerciseBeans(exerciseProfile.getMuscle());
        }
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);

        recyclerView = (RecyclerView) v.findViewById(R.id.choose_recyclerview);
        recyclerView.addItemDecoration(itemDecor);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        mAdapter = new ExerciseListAdapter(getSelectedIndex(), getContext(), this, onExerciseSetChange, exerciseBeans);
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(mAdapter);
        //      ChooseContainerFragment chooseContainerFragment = (ChooseContainerFragment) getParentFragment();
//        ChooseDialogFragment chooseDialogFragment = (ChooseDialogFragment) chooseContainerFragment.getParentFragment();
        //  ViewCompat.setNestedScrollingEnabled(((ChooseDialogFragment) getParentFragment()).getNestedScrollView(), false);
        mChangeMuscleTV = (TextView) v.findViewById(R.id.choose_change_Tv);
        mCancelMuscleTV = (TextView) v.findViewById(R.id.choose_cancel_Tv);
        mChangeMuscleTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandable.toggle();
                mChangeMuscleTV.setVisibility(View.GONE);
                mCancelMuscleTV.setVisibility(View.VISIBLE);
            }
        });
        mCancelMuscleTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCancelMuscleTV.setVisibility(View.GONE);
                mChangeMuscleTV.setVisibility(View.VISIBLE);
                mExpandable.toggle();
            }
        });
        v.findViewById(R.id.choose_change_user_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserCustomExercises();
            }
        });
        initComment(v);
    }

    public void initAllExercises() {
        allExercisesList = new ArrayList<>();
        allExercisesListString = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                allExercisesList.addAll(dataManager.getExerciseDataManager().getAllExercises());
                for (int i = 0; i < allExercisesList.size(); i++) {
                    allExercisesListString.add(new ExerciseSearchSuggestion(allExercisesList.get(i).getName()));
                }
            }
        }).start();

    }

    private int getSelectedIndex() {
        if (exerciseProfile.getExercise() != null) {
            for (int i = 0; i < exerciseBeans.size(); i++) {
                if (exerciseProfile.getExercise().getName().equals(exerciseBeans.get(i).getName())) {
                    selectedIndex = i;
                    return selectedIndex;
                }
            }
        }
        selectedIndex = -1;
        return selectedIndex;
    }

    private void initComment(View v) {
        commentCB = (CheckBox) v.findViewById(R.id.choose_exercise_comment_cb);
        commentET = (EditText) v.findViewById(R.id.choose_exercise_comment_et);
        commentET.setText(exerciseProfile.comment);
        commentCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                commentET.setEnabled(isChecked);
                exerciseProfile.showComment = isChecked;
                if (isChecked) {
                    exerciseProfile.comment = commentET.getText().toString();
                } else {
                    //exerciseProfile.comment = "";
                }
            }
        });
        commentCB.setChecked(exerciseProfile.showComment);
        commentET.setEnabled(commentCB.isChecked());
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                exerciseProfile.comment = commentET.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                commentET.setSelection(s.length());
                exerciseProfile.comment = commentET.getText().toString();

            }
        };
        commentET.addTextChangedListener(textWatcher);
    }

    private void initExerciseBeans(Muscle m) {
        exerciseBeans = (ArrayList<Beans>)
                dataManager.getExerciseDataManager().
                        readByTable(m.getMuscle_name());
        exerciseBeans = Beans.sortByAccessory(exerciseBeans);
    }

    private void showUserCustomExercises() {
        exerciseBeans = (ArrayList<Beans>) dataManager.getExerciseDataManager().readByTable(DBExercisesHelper.TABLE_EXERCISES_CUSTOM);
        mAdapter.setExerciseBeans(exerciseBeans);


        collapseExpandableLayout();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMuscleChange(GridViewMusclesAdapter.MusclesContentHolder mch) {
        //make choosereact change the exerciseprofile muscle
        mCancelMuscleTV.setVisibility(View.GONE);
        mChangeMuscleTV.setVisibility(View.VISIBLE);
        initExerciseBeans(mch.m);
        mAdapter.setExerciseBeans(exerciseBeans);
        mMuscleText.setText(mch.text);
        exerciseProfile.setMuscle(mch.m);
        exerciseProfile.setExercise(null);
        mAdapter.setSelectedIndex(-1);
        mAdapter.notifyDataSetChanged();

        onExerciseSetChange.notifyExerciseSetChange();
        //LayoutManagerHelper.onChange(exerciseProfile);
    }

    @Override
    public void collapseExpandableLayout() {
        mExpandable.collapse();
    }

    public void setOnExerciseSetChange(OnExerciseSetChange onExerciseSetChange) {
        this.onExerciseSetChange = onExerciseSetChange;
    }

    @Override
    public void setExercise(Beans beans) {
        exerciseProfile.setExercise(beans);
        if(beans != null)
            exerciseProfile.setMuscle(beans.getMuscle());
        
    }

    public ArrayList<Beans> getAllExercisesList() {
        return allExercisesList;
    }

    public void setAllExercisesList(ArrayList<Beans> allExercisesList) {
        this.allExercisesList = allExercisesList;
    }

    public ArrayList<ExerciseSearchSuggestion> getAllExercisesListString() {
        return allExercisesListString;
    }


    // class GridViewAdapter extends BaseAdapter {

     /*   ArrayList<MusclesContentHolder> list;
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
        }*/
    //  }
}

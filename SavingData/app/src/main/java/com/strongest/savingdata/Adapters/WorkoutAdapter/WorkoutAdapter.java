package com.strongest.savingdata.Adapters.WorkoutAdapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.strongest.savingdata.AlgorithmStats.ProgressStats;
import com.strongest.savingdata.Database.Exercise.Beans;
import com.strongest.savingdata.AlgorithmLayout.PLObjects;
import com.strongest.savingdata.AlgorithmLayout.PLObjects.BodyText;
import com.strongest.savingdata.AlgorithmLayout.WorkoutLayoutTypes;
import com.strongest.savingdata.Database.Exercise.BeansHolder;
import com.strongest.savingdata.MyViews.CreateCustomBeansView.NumberChooseManager;
import com.strongest.savingdata.MyViews.CreateCustomBeansView.SingleNumberChooseView;
import com.strongest.savingdata.MyViews.WeightKeyBoard.WeightKeyboard;
import com.strongest.savingdata.MyViews.WorkoutView.OnChooseClickListener;
import com.strongest.savingdata.R;
import com.strongest.savingdata.createProgramFragments.Create.OnPositionViewListener;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.Collections;

import static android.view.View.GONE;
import static android.view.animation.Animation.RELATIVE_TO_SELF;
import static com.strongest.savingdata.AlgorithmLayout.PLObjects.*;

/**
 * Created by Cohen on 10/15/2017.
 */

/*
public class WorkoutAdapter extends MultiTypeExpandableRecyclerViewAdapter<WorkoutAdapter.ExerciseViewHolder,
        ChildViewHolder> implements ItemTouchHelperAdapter {


    private final String TAG = "aviv";
    private final int TITLE = 3, BODY = 4, EXERCISE = 5, CUSTOM_BUILD = 6;
    private Context context;
    private OnChooseClickListener listener;
    private ArrayList<PLObjects> exArray;
    private boolean showStats;
    private int[][] stats;
    private ProgressStats progressProgressStats;
    private OnPositionViewListener lonListener;
    private ArrayList<Integer> selectedPoses;
    private OnDragListener onDragListener;
    private ScrollToPositionListener scrollListener;
    private boolean hideIcon;
    private boolean disable;
    private int[] exerciseIcons;
    private int currentPosition = 0;
    private boolean progressMode;

    private int fromPos;
    private int toPos;


    public WorkoutAdapter(ArrayList<ExpandableGroup> exArray, Context context, OnChooseClickListener listener,
                          OnPositionViewListener lonListener,
                          boolean showStats) {
        super(exArray);
        this.context = context;
        this.listener = listener;
        this.lonListener = lonListener;
        selectedPoses = new ArrayList<>();
       // this.exArray = exArray;
        this.showStats = showStats;
    }

    public WorkoutAdapter(ArrayList<ExpandableGroup> exArray, Context context, OnChooseClickListener listener,
                          OnPositionViewListener lonListener, OnDragListener onDragListener,
                          ScrollToPositionListener scrollListener,
                          boolean showStats,
                          boolean hideIcon,
                          boolean disable
    ) {
        super(exArray);
        this.context = context;
        this.listener = listener;
        this.lonListener = lonListener;
        this.onDragListener = onDragListener;
        this.scrollListener = scrollListener;
        this.hideIcon = hideIcon;
        this.disable = disable;
        selectedPoses = new ArrayList<>();
      //  this.exArray = exArray;
        this.showStats = showStats;
    }
*/
/*
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TITLE) {
            View v1 = inflater.inflate(R.layout.recycler_view_workouts, parent, false);
            holder = new ViewHolderWorkout(v1);
        } else if (viewType == BODY) {
            View v2 = inflater.inflate(R.layout.recyclerview_body_parts, parent, false);
            holder = new MuscleViewHolder(v2);
        } else {
            View v3 = inflater.inflate(R.layout.recycler_view_exercises_left_margin, parent, false);
            holder = new ExerciseViewHolder(v3);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TITLE) {
            WorkoutAdapter.ViewHolderWorkout vh1 = (ViewHolderWorkout) holder;
            configureViewHolder1(vh1, position);
        } else if (holder.getItemViewType() == BODY) {
            MuscleViewHolder vh2 = (MuscleViewHolder) holder;
            configureViewHolder2(vh2, position);
        } else {
            ExerciseViewHolder vh3 = (ExerciseViewHolder) holder;
            configureViewHolder3(vh3, position);
        }
    }*//*


    private void configureViewHolder1(ViewHolderWorkout vh1, final int position) {


        PLObjects.WorkoutText workoutText = (PLObjects.WorkoutText) exArray.get(position);
        vh1.workoutTv.setText(workoutText.getWorkoutName());


    }

    private void configureViewHolder3(final ExerciseViewHolder vh3, final int position, final ExerciseProfile exerciseProfile) {
            //final ExerciseProfile exerciseProfile = (ExerciseProfile) exArray.get(position);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                vh3.itemView.setTransitionName(exerciseProfile.getExerciseProfileId() + "");
                vh3.name.setTransitionName(exerciseProfile.getExerciseProfileId() + vh3.name.getId() + "");
            }

            if (hideIcon) {
                vh3.icon.setVisibility(GONE);
            } else {
                vh3.icon.setVisibility(View.VISIBLE);
            }
            if (disable) {
                vh3.layout.setEnabled(false);
                vh3.layout.setAlpha(0.5f);
            } else {
                vh3.layout.setEnabled(true);
            }
            //    ViewCompat.setTransitionName(vh3.sets, exerciseProfile.getBeansHolder().getExercise().getName()+position);

          */
/* vh3.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        //listener.click(v, exerciseProfile, "");
                    }
                }
            });*//*


        vh3.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                lonListener.longClick(v, position);
                return true;
            }
        });


            // ExerciseProfileView epv = exerciseProfile.getExerciseProfileView();
            if (exerciseProfile.getBeansHolders().get(0) != null) {
                BeansHolder beansHolder = exerciseProfile.getBeansHolders().get(0);

                // Typeface type = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/asesine.ttf");
                // vh3.name.setTypeface(type);
                //    if (exerciseProfile.getBeansHolder().getExercise().getImage() != null) {
               // vh3.icon.setImageResource(exerciseProfile.getBeansHolder().getExercise().getImage());
                //      }
                vh3.name.setText(beansHolder.getExercise().getName());
                vh3.reps.setText(beansHolder.getRep().getName());
                vh3.sets.setText(beansHolder.getSets().getName());
                vh3.rest.setText(beansHolder.getRest().getName());
               */
/* if (exerciseProfile.getBeansHolder().getMethod() != null) {
                    vh3.method.setText(exerciseProfile.getBeansHolder().getMethod().getName());
                } else {
                    vh3.method.setText("");
                }*//*

             */
/*   if ((exerciseProfile.getBeansHolder().getWeight() == 0)) {
                    vh3.weight.setText("W: -");
                } else {
                    vh3.weight.setText("W: " + exerciseProfile.getBeansHolder().getWeight());
                }*//*



                    vh3.icon.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            onDragListener.startDrag(vh3);
                            return true;
                        }
                    });


            } else {
                vh3.name.setText("");
                vh3.reps.setText("");
                vh3.method.setText("");
                vh3.sets.setText("");
                vh3.rest.setText("");
                vh3.weight.setText("W: -");
                vh3.icon.setImageResource(R.drawable.ic_add_black_18dp);
            }

            vh3.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.click(v, exerciseProfile, "");
                }
            });

            //Currently not showing weight && method
            vh3.weight.setVisibility(GONE);
            vh3.method.setVisibility(GONE);

            vh3.edit.setTag(position);

    }

    private boolean isBean(Beans b) {
        if (b == null) {
            return false;
        }
        return true;
    }

    private void configureViewHolder2(MuscleViewHolder vh2, int position, BodyText bodyText) {
        //BodyText bodyText = (BodyText) exArray.get(position);
        vh2.bodyTv.setText(bodyText.getMuscle().getMuscle_display());
        ObjectAnimator anim;
        if (showStats) {
            animate(true, vh2.damage, vh2.mechanical, vh2.metabolic);

            vh2.damage.setVisibility(View.VISIBLE);
            vh2.mechanical.setVisibility(View.VISIBLE);
            vh2.metabolic.setVisibility(View.VISIBLE);
            vh2.damage.setMax(bodyText.getStatsHolder().getProgressStats().getMaxDamage());
            vh2.mechanical.setMax(bodyText.getStatsHolder().getProgressStats().getMaxMechanical());
            vh2.metabolic.setMax(bodyText.getStatsHolder().getProgressStats().getMaxMetabolic());

            vh2.damage.setProgress(bodyText.getStatsHolder().getDamage());
            vh2.mechanical.setProgress(bodyText.getStatsHolder().getMechanical());
            vh2.metabolic.setProgress(bodyText.getStatsHolder().getMetabolic());

            vh2.damage.setProgressDrawable(ContextCompat.getDrawable(context, determineColorForDamage(vh2.damage, bodyText)));
            vh2.mechanical.setProgressDrawable(ContextCompat.getDrawable(context, determineColorForMechanical(vh2.mechanical, bodyText)));
            vh2.metabolic.setProgressDrawable(ContextCompat.getDrawable(context, determineColorForMetabolic(vh2.metabolic, bodyText)));

            Log.d(TAG, "damage: " + bodyText.getStatsHolder().getDamage());

        } else {

            animate(false, vh2.damage, vh2.mechanical, vh2.metabolic);

        }

    }


    private void animate(boolean mode, View... views) {
        for (int i = 0; i < views.length; i++) {
            if (!mode) {
                views[i].animate().alpha(0f).start();
            } else {
                views[i].animate().alpha(1f).start();
            }
        }
    }

    private int determineColorForDamage(ProgressBar pb, PLObjects.BodyText b) {
        float p = pb.getProgress();


        if (p >= b.getStatsHolder().getProgressStats().getMaxDamage()) {
            return R.drawable.progress_bar_drawable_max;
        }
        if (p <= b.getStatsHolder().getProgressStats().getMinDamage()) {
            return R.drawable.progress_bar_drawable_min;
        } else {
            return R.drawable.progress_bar_drawable_correct;
        }
    }

    private int determineColorForMechanical(ProgressBar pb, PLObjects.BodyText b) {
        float p = pb.getProgress();

        if (p >= b.getStatsHolder().getProgressStats().getMaxMechanical()) {
            return R.drawable.progress_bar_drawable_max;
        }
        if (p <= b.getStatsHolder().getProgressStats().getMinMechanical()) {
            return R.drawable.progress_bar_drawable_min;
        } else {
            return R.drawable.progress_bar_drawable_correct;
        }
    }

    private int determineColorForMetabolic(ProgressBar pb, PLObjects.BodyText b) {
        float p = pb.getProgress();


        if (p >= b.getStatsHolder().getProgressStats().getMaxMetabolic()) {
            return R.drawable.progress_bar_drawable_max;
        }
        if (p <= b.getStatsHolder().getProgressStats().getMinMetabolic()) {
            return R.drawable.progress_bar_drawable_min;
        } else {
            return R.drawable.progress_bar_drawable_correct;
        }
    }

    public void hideIcon(boolean hideIcon) {
        this.hideIcon = hideIcon;
        notifyDataSetChanged();
    }

    public void setScrollListener(ScrollToPositionListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    public int getFromPos() {
        return fromPos;
    }

    public int getToPos() {
        return toPos;
    }



    @Override
    public int getGroupViewType(int position, ExpandableGroup group) {
     */
/*   Log.d(TAG, "getGroupViewType: " + position + " group: "+ group.getTitle());
        if (((PLObjects) group).getType() == WorkoutLayoutTypes.BodyView) {
            return BODY;
        } else if (((PLObjects) group).getType() == WorkoutLayoutTypes.WorkoutView) {
            return TITLE;
        } else if (((PLObjects) group).getType() == WorkoutLayoutTypes.ExerciseView) {
            return EXERCISE;
        }*//*

        return CUSTOM_BUILD;
    }

     @Override
        public int getChildViewType(int position, ExpandableGroup group, int childIndex) {
            return CUSTOM_BUILD;
        }

        @Override
        public boolean isChild(int viewType) {
            return viewType == CUSTOM_BUILD;
        }


        @Override
        public boolean isGroup(int viewType) {
            //Log.d(TAG, "isGroup: " + viewType);
            return viewType == TITLE ||
                    viewType == BODY ||
                    viewType == EXERCISE;
        }

    @Override
    public ExerciseViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TITLE) {
            View v1 = inflater.inflate(R.layout.recycler_view_workouts, parent, false);
            return  new ViewHolderWorkout(v1);
        } else if (viewType == BODY) {
            View v2 = inflater.inflate(R.layout.recyclerview_body_parts, parent, false);
            return  new MuscleViewHolder(v2);

        } else {
            View v3 = inflater.inflate(R.layout.recycler_view_exercises_left_margin, parent, false);
            return new ExerciseViewHolder(v3);

        }
    }

    @Override
    public ChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        */
/*
        View v = inflater.inflate(R.layout.recycler_view_exercise_details_left_margin, parent, false);


        return new BeansHolderViewHolder(v);
        *//*

       View v = inflater.inflate(R.layout.progress_set, parent, false);
        return new ProgressViewHolder(v);

    }

    @Override
    public void onBindChildViewHolder(ChildViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
       */
/* ((BeansViewHolder) holder).setText("dasdsad");*//*


    }

    @Override
    public void onBindGroupViewHolder(ExerciseViewHolder holder, int flatPosition, ExpandableGroup group) {
        currentPosition = flatPosition;
        int viewType = getItemViewType(flatPosition);
        if (viewType == TITLE) {
            WorkoutAdapter.ViewHolderWorkout vh1 = (ViewHolderWorkout) holder;
            configureViewHolder1(vh1, flatPosition);
      */
/*  } else if (viewType == BODY) {
            MuscleViewHolder vh2 = (MuscleViewHolder) holder;
            configureViewHolder2(vh2, flatPosition, (BodyText)group);
        } else if (viewType == EXERCISE){
            ExerciseViewHolder vh3 = (ExerciseViewHolder) holder;
            configureViewHolder3(vh3, flatPosition, (ExerciseProfile) group);
        }*//*

        }
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public boolean isProgressMode() {
        return progressMode;
    }

    public void setProgressMode(boolean progressMode) {
        this.progressMode = progressMode;
    }

    public static class ExerciseViewHolder extends GroupViewHolder implements ItemTouchHelperListener {


        public TextView name;
        public TextView sets;
        public TextView reps;
        public TextView method;
        public TextView weight;
        public TextView rest;
        public ImageView icon;
        public ImageView arrow;
        public ImageView edit;


        public RelativeLayout layout;

        public ExerciseViewHolder(View itemView) {
            super(itemView);
//            layout = (RelativeLayout) itemView.findViewById(R.id.exerciseView_layout);
            layout = (RelativeLayout) itemView.findViewById(R.id.exerciseView_layout);
            arrow = (ImageView) itemView.findViewById(R.id.ex_arrow);
            edit = (ImageView) itemView.findViewById(R.id.ex_choose);
            // layout.setOnLongClickListener(this);
            //layout.setOnClickListener(listener);
           */
/* layout.setOnTouchListener(new View.OnTouchListener() {
                private GestureDetector gestureDetector = new GestureDetector(context,
                        new GestureDetector.SimpleOnGestureListener() {
                            @Override
                            public boolean onDoubleTap(MotionEvent e) {
                                return super.onDoubleTap(e);
                            }
                        });


                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (gestureDetector.onTouchEvent(event)) {
                        return true;
                    } else {
                        return false;
                    }

                }
            });*//*


            //delete = (ImageView) itemView.findViewById(R.id.recycler_view_exercise_delete);

            icon = (ImageView) itemView.findViewById(R.id.ex1);
            name = (TextView) itemView.findViewById(R.id.exerciseView_nameTV);
            sets = (TextView) itemView.findViewById(R.id.exerciseView_setsTV);
            reps = (TextView) itemView.findViewById(R.id.exerciseView_repsTV);
            method = (TextView) itemView.findViewById(R.id.exerciseView_methodTV);
            weight = (TextView) itemView.findViewById(R.id.exerciseView_weightTV);
            rest = (TextView) itemView.findViewById(R.id.exerciseView_restTV);

        }

       */
/* @Override
        public boolean onLongClick(View v) {
           // lonListener.longClick(v, this.getLayoutPosition());
            return true;
        }*//*


        @Override
        public void onItemSelected() {

        }

        @Override
        public void onItemClear() {

        }

        @Override
        public void expand() {

            animateExpand();

        }


        @Override
        public void collapse() {
            animateCollapse();
        }

        private void animateExpand() {
            RotateAnimation rotate =
                    new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            if(arrow != null)
                arrow.startAnimation(rotate);
        }

        private void animateCollapse() {
            RotateAnimation rotate =
                    new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            if(arrow != null)
                arrow.startAnimation(rotate);
        }
    }

    public class BeansViewHolder extends com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder {
        private TextView tv;

        public BeansViewHolder(View itemView) {
            super(itemView);
          //  tv = (TextView) itemView.findViewById(R.id.list_view_exercise_current_sets_tv);
        }

        public void setText(String text) {
            //tv.setText(text);
        }
    }

    */
/*@Override
    public int getItemCount() {
        return exArray.size();
    }*//*



    private class MuscleViewHolder extends ExerciseViewHolder {
        private TextView bodyTv;
        private ProgressBar damage, metabolic, mechanical;
        private MuscleViewHolder vh;

        public MuscleViewHolder(View itemView) {
            super(itemView);
            vh = this;
            damage = (ProgressBar) itemView.findViewById(R.id.stats_damage_progress);
            metabolic = (ProgressBar) itemView.findViewById(R.id.stats_metabolic_progress);
            mechanical = (ProgressBar) itemView.findViewById(R.id.stats_mechanical_progress);
            bodyTv = (TextView) itemView.findViewById(R.id.recycler_view_body_parts_TV);


            if (showStats) {

            }

        }


    }

    private class ProgressViewHolder extends com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder {

        private SingleNumberChooseView singleNumberChooseView_rep, singleNumberChooseView_rest, singleNumberChooseView_weight;
        private ImageView calcImage;
        private ExpandableLayout expandableLayout;
        private WeightKeyboard weightKeyboard;
        public ProgressViewHolder(View itemView) {
            super(itemView);
            calcImage = (ImageView) itemView.findViewById(R.id.expand_weight);
            expandableLayout = (ExpandableLayout) itemView.findViewById(R.id.progress_set_keyboardexpandlayout);
            singleNumberChooseView_weight = (SingleNumberChooseView) itemView.findViewById(R.id.progress_set_single_choose_WEIGHT);
            singleNumberChooseView_rest = (SingleNumberChooseView) itemView.findViewById(R.id.progress_set_single_choose_REST);
            singleNumberChooseView_rep = (SingleNumberChooseView) itemView.findViewById(R.id.progress_set_single_choose_REP);

            singleNumberChooseView_weight.setUpWithNumberChooseManager(new NumberChooseManager());
            singleNumberChooseView_rest.setUpWithNumberChooseManager(new NumberChooseManager());
            singleNumberChooseView_rep.setUpWithNumberChooseManager(new NumberChooseManager());
            singleNumberChooseView_weight.setToDouble(true, 0.5);
            weightKeyboard = (WeightKeyboard)  itemView.findViewById(R.id.progress_set_weightkeyboard);

            calcImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expandableLayout.toggle();
                    if(expandableLayout.isExpanded()){
                        singleNumberChooseView_weight.setEnabled(false);
                    }else{
                        singleNumberChooseView_weight.setEnabled(true);

                    }
                   // singleNumberChooseView.setClickable(false);

                    weightKeyboard.setUpwithTextView(singleNumberChooseView_weight.getNumberTV());


                }
            });

        }
    }


    private class ViewHolderWorkout extends ExerciseViewHolder {
        private TextView workoutTv;

        public ViewHolderWorkout(View itemView) {
            super(itemView);
            workoutTv = (TextView) itemView.findViewById(R.id.recycler_view_workouts_tv);

        }
    }


    public ArrayList<Integer> getSelectedPoses() {
        return selectedPoses;
    }

    public void setSelectedPoses(ArrayList<Integer> selectedPoses) {
        this.selectedPoses = selectedPoses;
        //notifyDataSetChanged();
    }

    public void setShowStats(boolean showStats) {
        this.showStats = showStats;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (exArray.get(fromPosition).getType() == WorkoutLayoutTypes.BodyView) {
            return false;
        }
      */
/*  if (toPosition == 0 || fromPosition == 0) {
            return false;
        }*//*

        ExerciseProfile toEp;
        ExerciseProfile fromEp = (ExerciseProfile) exArray.get(fromPosition);
        try {
            toEp = (ExerciseProfile) exArray.get(toPosition);
        } catch (Exception e) {
            e.toString();
            return false;
        }
        if (fromEp.getMuscle() != toEp.getMuscle()) {
            return false;
        }
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(exArray, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(exArray, i, i - 1);
            }
        }
        fromPos = fromPosition;
        toPos = toPosition;
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }


    @Override
    public void onItemDismiss(int position) {
        //null
    }

    public void setOnDragListener(OnDragListener onDragListener) {
        this.onDragListener = onDragListener;
    }

    public void setExArray(ArrayList<PLObjects> exArray) {
        this.exArray = exArray;
    }

    public ArrayList<PLObjects> getExArray() {
        return exArray;
    }

    public void myNotifyItemInserted(ArrayList<PLObjects> exArray, int position) {
        this.exArray = exArray;
        notifyItemInserted(position);
    }

    public void myNotifyItemRemoved(ArrayList<PLObjects> exArray, int position) {
        this.exArray = exArray;
        notifyItemRemoved(position);
    }

   */
/* public class ExerciseViewHolder extends GroupViewHolder {

        public ExerciseViewHolder(View itemView) {
            super(itemView);
        }
    }*/



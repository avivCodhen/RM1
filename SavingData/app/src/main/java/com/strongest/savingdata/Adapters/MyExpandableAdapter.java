package com.strongest.savingdata.Adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Point;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.strongest.savingdata.Adapters.WorkoutAdapter.ItemTouchHelperAdapter;
import com.strongest.savingdata.Adapters.WorkoutAdapter.ItemTouchHelperListener;
import com.strongest.savingdata.Adapters.WorkoutAdapter.OnDragListener;
import com.strongest.savingdata.Adapters.WorkoutAdapter.OnExerciseProfileEditClick;
import com.strongest.savingdata.Adapters.WorkoutAdapter.OnExpandCollapseListener;
import com.strongest.savingdata.Adapters.WorkoutAdapter.ScrollToPositionListener;
import com.strongest.savingdata.AlgorithmLayout.PLObjects;
import com.strongest.savingdata.AlgorithmLayout.PLObjects.BodyText;
import com.strongest.savingdata.AlgorithmLayout.PLObjects.ExerciseProfile;
import com.strongest.savingdata.AlgorithmLayout.WorkoutLayoutTypes;
import com.strongest.savingdata.AlgorithmStats.ProgressStats;
import com.strongest.savingdata.Animations.MyAnimator;
import com.strongest.savingdata.Animations.MyJavaAnimator;
import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.Database.Exercise.BeansHolder;
import com.strongest.savingdata.MyViews.CreateCustomBeansView.NumberChooseManager;
import com.strongest.savingdata.MyViews.CreateCustomBeansView.SingleNumberChooseView;
import com.strongest.savingdata.MyViews.WeightKeyBoard.WeightKeyboard;
import com.strongest.savingdata.MyViews.WorkoutView.OnChooseClickListener;
import com.strongest.savingdata.R;
import com.strongest.savingdata.createProgramFragments.Create.OnPositionViewListener;
import com.strongest.savingdata.tabFragments.WorkoutFragment;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.view.View.GONE;

/**
 * Created by Cohen on 1/28/2018.
 */

/**
 * Created by Cohen on 10/15/2017.
 */

public class MyExpandableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        ItemTouchHelperAdapter, OnExpandCollapseListener {


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
    private OnExerciseProfileEditClick onExerciseProfileEditClick;
    private boolean hideIcon;
    private boolean disable;
    private int[] exerciseIcons;
    private int currentPosition = 0;
    private boolean progressMode;
    private int workoutFragmentHeight;

    private int fromPos;
    private int toPos;


    public MyExpandableAdapter(ArrayList<PLObjects> exArray, Context context, OnChooseClickListener listener,
                               OnPositionViewListener lonListener,
                               boolean showStats) {
        this.context = context;
        this.listener = listener;
        this.lonListener = lonListener;
        selectedPoses = new ArrayList<>();
        this.exArray = exArray;
        this.showStats = showStats;
    }

    public MyExpandableAdapter(ArrayList<PLObjects> exArray, Context context, OnChooseClickListener listener,
                               OnPositionViewListener lonListener, OnDragListener onDragListener,
                               ScrollToPositionListener scrollListener,
                               boolean showStats,
                               boolean hideIcon,
                               boolean disable
    ) {
        this.context = context;
        this.listener = listener;
        this.lonListener = lonListener;
        this.onDragListener = onDragListener;
        this.scrollListener = scrollListener;
        this.hideIcon = hideIcon;
        this.disable = disable;
        selectedPoses = new ArrayList<>();
        this.exArray = exArray;
        this.showStats = showStats;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == WorkoutLayoutTypes.WorkoutView.ordinal()) {
            View v1 = inflater.inflate(R.layout.recycler_view_workouts, parent, false);
            return new ViewHolderWorkout(v1);
        } /*else if (viewType == WorkoutLayoutTypes.BodyView.ordinal()) {
            View v2 = inflater.inflate(R.layout.recyclerview_body_parts, parent, false);
            return new MyExpandableAdapter.MuscleViewHolder(v2);
        */ else if (viewType == WorkoutLayoutTypes.BeansHolderLeftMargin.ordinal()) {
            View v4 = inflater.inflate(R.layout.recycler_view_exercise_details_left_margin, parent, false);
            return new BeansHolderPlObjectViewHolder(v4);

        } else if (viewType == WorkoutLayoutTypes.BeansHolderRightMargin.ordinal()) {
            View v4 = inflater.inflate(R.layout.recycler_view_exercise_details_right_margin, parent, false);
            return new BeansHolderPlObjectViewHolder(v4);
        } else if (viewType == WorkoutLayoutTypes.ProgressPLObject.ordinal()) {
            View v5 = inflater.inflate(R.layout.progress_set, parent, false);
            return new ProgressPLObjectViewHolder(v5);

        } else if (viewType == WorkoutLayoutTypes.ExerciseViewLeftMargin.ordinal()) {
            View v3 = inflater.inflate(R.layout.recycler_view_exercises_left_margin, parent, false);
            return new ExerciseViewHolder(v3, this);

        } else if (viewType == WorkoutLayoutTypes.ExerciseViewRightMargin.ordinal()) {
            View v3 = inflater.inflate(R.layout.recycler_view_exercises_right_margin, parent, false);
            return new ExerciseViewHolder(v3, this);

        } else if (viewType == WorkoutLayoutTypes.AddExercise.ordinal()) {
            View v3 = inflater.inflate(R.layout.a_test, parent, false);
            return new AddExerciseViewHolder(v3);
        } else if (viewType == WorkoutLayoutTypes.Method.ordinal()) {
            View v3 = inflater.inflate(R.layout.recycler_view_exercises_dropset_superset, parent, false);
            return new ExerciseViewHolder(v3, this);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (payloads.size() != 0) {
            ExerciseProfile ep = ((ExerciseProfile) exArray.get(position));
            ExerciseViewHolder vh = (ExerciseViewHolder) holder;
            switch (payloads.get(0).toString()) {
                case "exercise":
                    Muscle.MuscleUI mui = Muscle.provideMuscleUI(ep.getmBeansHolder().getExercise().getMuscle());
                    vh.name.setText(ep.getmBeansHolder().getExercise().getName());
                    MyAnimator.Companion.fadeInOut(new View[]{
                            vh.name
                    });
                    break;
                case "reps":
                    vh.reps.setText("Reps: " + ep.getmBeansHolder().getRep().getName());
                    MyAnimator.Companion.fadeInOut(new View[]{
                            vh.reps
                    });
                    break;
                case "rest":
                    vh.rest.setText("Rest: " + ep.getmBeansHolder().getRest().getName());
                    MyAnimator.Companion.fadeInOut(new View[]{
                            vh.rest
                    });
                case "sets":
                    vh.sets.setText("Sets: " + ep.getmBeansHolder().getSets().getName());
                    MyAnimator.Companion.fadeInOut(new View[]{
                            vh.sets
                    });
            }

        }
        if (payloads.size() > 0 && payloads.get(0).toString().equals("exercise")) {
            ((ExerciseViewHolder) holder).name.setText(((ExerciseProfile) exArray.get(position)).getmBeansHolder().getExercise().getName());

        } else {
            onBindViewHolder(holder, position);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == WorkoutLayoutTypes.WorkoutView.ordinal()) {
            ViewHolderWorkout vh1 = (ViewHolderWorkout) holder;
            configureViewHolder1(vh1, position);
        } else if (holder.getItemViewType() == WorkoutLayoutTypes.BodyView.ordinal()) {
            MuscleViewHolder vh2 = (MuscleViewHolder) holder;
            configureViewHolder2(vh2, position);

        } else if (holder.getItemViewType() == WorkoutLayoutTypes.ExerciseViewLeftMargin.ordinal() ||
                holder.getItemViewType() == WorkoutLayoutTypes.ExerciseViewRightMargin.ordinal()) {
            ExerciseViewHolder vh3 = (ExerciseViewHolder) holder;
            configureViewHolder3(vh3, position);

        } else if (holder.getItemViewType() == WorkoutLayoutTypes.BeansHolderLeftMargin.ordinal() ||
                holder.getItemViewType() == WorkoutLayoutTypes.BeansHolderRightMargin.ordinal()) {
            // Muscle m;
            int childPosition = -1;
            ExerciseProfile ep = null;
            outloop:
            for (int i = position; i >= 0; i--) {
                if (exArray.get(i).getType() == WorkoutLayoutTypes.ExerciseView) {
                    ep = ((ExerciseProfile) exArray.get(i));
                    //m = ep.ge();
                    break outloop;
                } else {
                    childPosition++;
                }
            }
            BeansHolderPlObjectViewHolder vh4 = (BeansHolderPlObjectViewHolder) holder;
            configurateBeansHolder(vh4, position, childPosition, ep);

        } else if (holder.getItemViewType() == WorkoutLayoutTypes.ProgressPLObject.ordinal()) {


        } else if (holder.getItemViewType() == WorkoutLayoutTypes.AddExercise.ordinal()) {
            AddExerciseViewHolder vh5 = (AddExerciseViewHolder) holder;
            configurateAddExerciseViewHolder(vh5, position);

        } else if (holder.getItemViewType() == WorkoutLayoutTypes.Method.ordinal()) {
            AddExerciseViewHolder vh5 = (AddExerciseViewHolder) holder;
            configurateAddExerciseViewHolder(vh5, position);

        }

    }


    @Override
    public int getItemCount() {
        return exArray.size();
    }

    @Override
    public int getItemViewType(int position) {
        WorkoutLayoutTypes type = exArray.get(position).getType();
        int bodyId = exArray.get(position).getBodyId();

        if (type == WorkoutLayoutTypes.ExerciseView) {
            ExerciseProfile ep = (ExerciseProfile) exArray.get(position);
            if (ep.getInnerType() == WorkoutLayoutTypes.ExerciseViewLeftMargin) {
                return WorkoutLayoutTypes.ExerciseViewLeftMargin.ordinal();
            } else {
                return WorkoutLayoutTypes.ExerciseViewRightMargin.ordinal();
            }
        }
        if (type == WorkoutLayoutTypes.BeansHolderPLObject) {
            boolean found = false;
            outloop:
            for (int i = position; i >= 0; i--) {
                if (exArray.get(i).getType() == WorkoutLayoutTypes.ExerciseView || exArray.get(i).getType() == WorkoutLayoutTypes.Method) {
                    bodyId = exArray.get(i).getBodyId();
                    found = true;
                    break outloop;
                }

            }
            if (found == true && bodyId % 2 != 0) {
                return WorkoutLayoutTypes.BeansHolderLeftMargin.ordinal();
            } else {
                return WorkoutLayoutTypes.BeansHolderRightMargin.ordinal();

            }

        }
        return type.ordinal();
    }

    public void configurateAddExerciseViewHolder(AddExerciseViewHolder vh, int position) {
        // onExerciseProfileEditClick.transition(vh, position,(ExerciseProfile) exArray.get(position-1));
    }


    private void configureViewHolder1(ViewHolderWorkout vh1, final int position) {


        PLObjects.WorkoutText workoutText = (PLObjects.WorkoutText) exArray.get(position);
        vh1.workoutTv.setText(workoutText.getWorkoutName());


    }

    private void configureViewHolder2(MuscleViewHolder vh2, int position) {
        BodyText bodyText = (BodyText) exArray.get(position);
        vh2.bodyTv.setText(bodyText.getMuscle().getMuscle_display());
        ObjectAnimator anim;
        if (showStats) {
            animate(true, vh2.damage, vh2.mechanical, vh2.metabolic);

            vh2.damage.setVisibility(View.VISIBLE);
            vh2.mechanical.setVisibility(View.VISIBLE);
            vh2.metabolic.setVisibility(View.VISIBLE);
              /*  vh2.damage.setMax(bodyText.getStatsHolder().getProgressStats().getMaxDamage());
                vh2.mechanical.setMax(bodyText.getStatsHolder().getProgressStats().getMaxMechanical());
                vh2.metabolic.setMax(bodyText.getStatsHolder().getProgressStats().getMaxMetabolic());

                vh2.damage.setProgress(bodyText.getStatsHolder().getDamage());
                vh2.mechanical.setProgress(bodyText.getStatsHolder().getMechanical());
                vh2.metabolic.setProgress(bodyText.getStatsHolder().getMetabolic());

                vh2.damage.setProgressDrawable(ContextCompat.getDrawable(context, determineColorForDamage(vh2.damage, bodyText)));
                vh2.mechanical.setProgressDrawable(ContextCompat.getDrawable(context, determineColorForMechanical(vh2.mechanical, bodyText)));
                vh2.metabolic.setProgressDrawable(ContextCompat.getDrawable(context, determineColorForMetabolic(vh2.metabolic, bodyText)));
*/
            //  Log.d(TAG, "damage: " + bodyText.getStatsHolder().getDamage());

        } else {

            animate(false, vh2.damage, vh2.mechanical, vh2.metabolic);

        }

    }

    public void configurateBeansHolder(BeansHolderPlObjectViewHolder vh, int position, int childPosition, ExerciseProfile ep) {
        Muscle.MuscleUI mui = Muscle.provideMuscleUI(ep.getmBeansHolder().getExercise().getMuscle());
        vh.fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, mui.getColor())));
        vh.reps.setText("Reps: " + ep.getBeansHolders().get(childPosition).getRep().getName());
        vh.rest.setText("Rest: " + ep.getBeansHolders().get(childPosition).getRest().getName());
        vh.weight.setText("Wgh: " + ep.getBeansHolders().get(childPosition).getWeight() + "");
        vh.set.setText(childPosition + 1 + "");
    }


    private void configureViewHolder3(final ExerciseViewHolder vh3, final int position) {
        final ExerciseProfile exerciseProfile = (ExerciseProfile) exArray.get(position);

           /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                vh3.itemView.setTransitionName(exerciseProfile.getExerciseProfileId() + "");
                vh3.name.setTransitionName(exerciseProfile.getExerciseProfileId() + vh3.name.getId() + "");
            }*/

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

        /*   vh3.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(exerciseProfile.isExpand()){
                        collapseAba(exerciseProfile.getBeansHolders(), position);
                        exerciseProfile.setExpand(false);
                    }else{
                        expandBens( (exerciseProfile).getBeansHolders(), position);
                        exerciseProfile.setExpand(true);
                    }
                }
            });*/
       /* vh3.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onExerciseProfileEditClick.transition(position, vh3.name.getText().toString(),
                        vh3.name, exerciseProfile);
            }
        });*/


        vh3.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                lonListener.longClick(v, position);
                return true;
            }
        });


        // ExerciseProfileView epv = exerciseProfile.getExerciseProfileView();
        if (exerciseProfile.isHasBeansHolders()) {
            BeansHolder beansHolder = exerciseProfile.getmBeansHolder();
            Muscle.MuscleUI mui = Muscle.provideMuscleUI(exerciseProfile.getmBeansHolder().getExercise().getMuscle());
            vh3.icon.setImageResource(mui.getImage());
            vh3.name.setText(beansHolder.getExercise().getName());
            vh3.reps.setText("Reps: " + beansHolder.getRep().getName());
            vh3.sets.setText("Sets: " + beansHolder.getSets().getId() + "");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                vh3.sets.setBackgroundTintList(ContextCompat.getColorStateList(context, mui.getColor()));
            }
            vh3.rest.setText("Rest: " + beansHolder.getRest().getName());
            vh3.muscle.setText(exerciseProfile.getmBeansHolder().getExercise().getMuscle().getMuscle_display());
            ViewCompat.setTransitionName(vh3.name, "unique" + vh3.name.getText().toString() + position);
            vh3.icon.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    onDragListener.startDrag(vh3);
                    return true;
                }
            });
            vh3.weight.setText("Wgh: 0.0kg");


        } else {
            vh3.name.setText("Configurate New Exercise");
            vh3.reps.setText("Reps: -");
            vh3.sets.setText("Sets: -");
            vh3.rest.setText("Rest: -");
            vh3.weight.setText("Wgh: -");
        }
        if (vh3.method != null) {
            //   vh3.method.setText(((PLObjects.Method) exArray.get(position)).getMethod());
        }

        vh3.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.click(v, exerciseProfile, "");
            }
        });

        //Currently not showing weight && method

        vh3.edit.setTag(position);

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

    private int determineColorForDamage(ProgressBar pb, BodyText b) {
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

    private int determineColorForMechanical(ProgressBar pb, BodyText b) {
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

    private int determineColorForMetabolic(ProgressBar pb, BodyText b) {
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

    public int getCurrentPosition() {
        return currentPosition;
    }

    public boolean isProgressMode() {
        return progressMode;
    }

    public void setProgressMode(boolean progressMode) {
        this.progressMode = progressMode;
    }

    @Override
    public void onExpand(ExerciseViewHolder vh) {
        int position = vh.getAdapterPosition();
        ArrayList<BeansHolder> beansHolders = ((ExerciseProfile) exArray.get(position)).getBeansHolders();
        if (beansHolders != null || beansHolders.get(0) != null) {
            for (int i = 0; i < beansHolders.size(); i++) {
                PLObjects.BeansHolderPLObject obj = new PLObjects.BeansHolderPLObject(beansHolders.get(i));
                exArray.add(position + i + 1, obj);
            }
        }
        ExerciseProfile ep = (ExerciseProfile) exArray.get(position);
        ep.setExpand(true);
        animateExpand(vh);
        notifyItemRangeInserted(position + 1, beansHolders.size());
        scrollListener.scrollToPosition(position, true);

    }

    private void animateExpand(ExerciseViewHolder vh) {
        if (vh.arrow != null)
            MyJavaAnimator.rotateView(vh.arrow, 360, 180);
    }

    private void animateCollapse(ExerciseViewHolder vh) {

        if (vh.arrow != null)
            MyJavaAnimator.rotateView(vh.arrow, 180, 360);
    }

    @Override
    public void onCollapse(ExerciseViewHolder vh) {
        int position = vh.getAdapterPosition();
        ArrayList<BeansHolder> beansHolders = ((ExerciseProfile) exArray.get(position)).getBeansHolders();
        for (int i = 0; i < beansHolders.size(); i++) {
            exArray.remove(position + 1);
        }
        ExerciseProfile ep = (ExerciseProfile) exArray.get(position);
        ep.setExpand(false);
        animateCollapse(vh);
        notifyItemRangeRemoved(position + 1, beansHolders.size());
        // scrollListener.scrollToPosition(position);
    }

    @Override
    public void deTailExpand(RecyclerView.ViewHolder vh) {
        int position = vh.getAdapterPosition();
        PLObjects.AddExercise a = new PLObjects.AddExercise(null);
        ExerciseProfile ep = (ExerciseProfile) exArray.get(position);
        exArray.add(position + 1, a);
        notifyItemInserted(position + 1);
        onExerciseProfileEditClick.transition(vh, position, ep);
        scrollListener.scrollToPosition(position, false);

    }

    @Override
    public void detailCollapse(RecyclerView.ViewHolder vh) {
        int position = vh.getAdapterPosition();
        PLObjects.AddExercise a = new PLObjects.AddExercise(null);
        ExerciseProfile ep = (ExerciseProfile) exArray.get(position);
        exArray.remove(position + 1);
        notifyItemRemoved(position + 1);
        onExerciseProfileEditClick.transition(null, position, ep);
        scrollListener.scrollToPosition(-1, true);
    }

    public void setOnExerciseProfileEditClick(OnExerciseProfileEditClick onExerciseProfileEditClick) {
        this.onExerciseProfileEditClick = onExerciseProfileEditClick;
    }

    public void setWorkoutFragmentHeight(int workoutFragmentHeight) {
        this.workoutFragmentHeight = workoutFragmentHeight;
    }

   /* private void expandBens(ArrayList<BeansHolder> beansHolders, int position) {
        if(beansHolders != null || beansHolders.get(0) != null) {
            for (int i = 0; i < beansHolders.size(); i++) {
                PLObjects.BeansHolderPLObject obj = new PLObjects.BeansHolderPLObject(beansHolders.get(i));
                exArray.add(position+i+1, obj);
            }
        }
        notifyItemRangeInserted(position+1, beansHolders.size());
    }

    private void collapseAba(ArrayList<BeansHolder> beansHolders, int position){
        for (int i = 0; i <beansHolders.size() ; i++) {
            exArray.remove(position);
        }
        notifyItemRangeRemoved(position+1, beansHolders.size());
    }*/


    public static class ExerciseViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperListener {


        public TextView muscle;
        public TextView name;
        public TextView sets;
        public TextView reps;
        public TextView method;
        public TextView weight;
        public TextView rest;
        private View itemView;
        private final OnExpandCollapseListener collapseExpandListener;
        public ImageView icon;
        public ImageView arrow;
        public ImageView edit;


        public ViewGroup layout;
        private boolean expand;
        private ExerciseViewHolder vh = this;

        public ExerciseViewHolder(final View itemView, final OnExpandCollapseListener collapseExpandListener) {
            super(itemView);
//            layout = (RelativeLayout) itemView.findViewById(R.id.exerciseView_layout);
            layout = (ViewGroup) itemView.findViewById(R.id.list_view_exercise_lay1);
            arrow = (ImageView) itemView.findViewById(R.id.ex_arrow);
            edit = (ImageView) itemView.findViewById(R.id.ex_choose);
            this.collapseExpandListener = collapseExpandListener;
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (expand) {
                        collapseExpandListener.detailCollapse(vh);

                        //collapseExpandListener.onCollapse(vh);
                        expand = false;
                    } else {
                        collapseExpandListener.deTailExpand(vh);

                        //collapseExpandListener.onExpand(vh);
                        expand = true;
                    }


                }
            });

            // layout.setOnLongClickListener(this);
          /*  layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                }
            });*/
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
            });*/

            //delete = (ImageView) itemView.findViewById(R.id.recycler_view_exercise_delete);
            icon = (ImageView) itemView.findViewById(R.id.ex1);
            name = (TextView) itemView.findViewById(R.id.exerciseView_nameTV);
            sets = (TextView) itemView.findViewById(R.id.exerciseView_setsTV);
            reps = (TextView) itemView.findViewById(R.id.exerciseView_repsTV);
            method = (TextView) itemView.findViewById(R.id.exerciseView_methodTV);
            weight = (TextView) itemView.findViewById(R.id.exerciseView_weightTV);
            rest = (TextView) itemView.findViewById(R.id.exerciseView_restTV);
            muscle = (TextView) itemView.findViewById(R.id.exerciseView_muscleTV);

            this.itemView = itemView;
            //    this.listener = listener;
        }



       /* @Override
        public boolean onLongClick(View v) {
           // lonListener.longClick(v, this.getLayoutPosition());
            return true;
        }*/

        @Override
        public void onItemSelected() {

        }

        @Override
        public void onItemClear() {

        }

                /*@Override
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
                    if (arrow != null)
                        arrow.startAnimation(rotate);
                }

                private void animateCollapse() {
                    RotateAnimation rotate =
                            new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
                    rotate.setDuration(300);
                    rotate.setFillAfter(true);
                    if (arrow != null)
                        arrow.startAnimation(rotate);
                }*/
    }

  /*  public class BeansViewHolder extends com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder {
        private TextView tv;

        public BeansViewHolder(View itemView) {
            super(itemView);
            //  tv = (TextView) itemView.findViewById(R.id.list_view_exercise_current_sets_tv);
        }

        public void setText(String text) {
            //tv.setText(text);
        }
    }*/

    private class MuscleViewHolder extends RecyclerView.ViewHolder {
        private TextView bodyTv;
        private ProgressBar damage, metabolic, mechanical;

        public MuscleViewHolder(View itemView) {
            super(itemView);
            damage = (ProgressBar) itemView.findViewById(R.id.stats_damage_progress);
            metabolic = (ProgressBar) itemView.findViewById(R.id.stats_metabolic_progress);
            mechanical = (ProgressBar) itemView.findViewById(R.id.stats_mechanical_progress);
            bodyTv = (TextView) itemView.findViewById(R.id.recycler_view_body_parts_TV);


            if (showStats) {

            }

        }


    }

    private class BeansHolderPlObjectViewHolder extends RecyclerView.ViewHolder {
        private FloatingActionButton fab;
        private TextView reps, rest, weight, set;

        public BeansHolderPlObjectViewHolder(View itemView) {
            super(itemView);
            reps = (TextView) itemView.findViewById(R.id.exerciseView_repsTV);
            rest = (TextView) itemView.findViewById(R.id.exerciseView_restTV);
            weight = (TextView) itemView.findViewById(R.id.exerciseView_weightTV);
            set = (TextView) itemView.findViewById(R.id.recycler_view_exercise_details_sets_tv);
            fab = (FloatingActionButton) itemView.findViewById(R.id.recycler_view_exercise_details_fab);
        }
    }

    private class ProgressPLObjectViewHolder extends RecyclerView.ViewHolder {

        private SingleNumberChooseView singleNumberChooseView_rep, singleNumberChooseView_rest, singleNumberChooseView_weight;
        private ImageView calcImage;
        private ExpandableLayout expandableLayout;
        private WeightKeyboard weightKeyboard;

        public ProgressPLObjectViewHolder(View itemView) {
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
            weightKeyboard = (WeightKeyboard) itemView.findViewById(R.id.progress_set_weightkeyboard);

            calcImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expandableLayout.toggle();
                    if (expandableLayout.isExpanded()) {
                        singleNumberChooseView_weight.setEnabled(false);
                    } else {
                        singleNumberChooseView_weight.setEnabled(true);

                    }
                    // singleNumberChooseView.setClickable(false);

                    weightKeyboard.setUpwithTextView(singleNumberChooseView_weight.getNumberTV());


                }
            });

        }
    }

    public class AddExerciseViewHolder extends RecyclerView.ViewHolder {
        private Button btn;
        private TextView tv;
        public ViewGroup layout;

        public AddExerciseViewHolder(View itemView) {
            super(itemView);
            layout = (ViewGroup) itemView.findViewById(R.id.choose_container);
        }
    }

    private class ViewHolderWorkout extends RecyclerView.ViewHolder {
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
        if (exArray.get(toPosition).getType() == WorkoutLayoutTypes.BeansHolderPLObject
                ) {
            return false;
        }

        if (exArray.get(toPosition).isExpand()) {
            return false;
        }
      /*  if (toPosition == 0 || fromPosition == 0) {
            return false;
        }*/
        ExerciseProfile toEp;
        ExerciseProfile fromEp = (ExerciseProfile) exArray.get(fromPosition);
        try {
            toEp = (ExerciseProfile) exArray.get(toPosition);
        } catch (Exception e) {
            e.toString();
            return false;
        }
       /* if (fromEp.getMuscle() != toEp.getMuscle()) {
            return false;
        }*/
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

   /* public class ExerciseViewHolder extends GroupViewHolder {

        public ExerciseViewHolder(View itemView) {
            super(itemView);
        }
    }*/


}





      /*  @Override
        public int getGroupViewType(int position, ExpandableGroup group) {
            Log.d(TAG, "getGroupViewType: " + position + " group: "+ group.getTitle());
            if (((PLObjects) group).getType() == WorkoutLayoutTypes.BodyView) {
                return BODY;
            } else if (((PLObjects) group).getType() == WorkoutLayoutTypes.WorkoutView) {
                return TITLE;
            } else if (((PLObjects) group).getType() == WorkoutLayoutTypes.ExerciseView) {
                return EXERCISE;
            }
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
        }*/

     /*   @Override
        public com.strongest.savingdata.Adapters.WorkoutAdapter.WorkoutAdapter.ExerciseViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            if (viewType == TITLE) {
                View v1 = inflater.inflate(R.layout.recycler_view_workouts, parent, false);
                return  new com.strongest.savingdata.Adapters.WorkoutAdapter.WorkoutAdapter.ViewHolderWorkout(v1);
            } else if (viewType == BODY) {
                View v2 = inflater.inflate(R.layout.recyclerview_body_parts, parent, false);
                return  new com.strongest.savingdata.Adapters.WorkoutAdapter.WorkoutAdapter.MuscleViewHolder(v2);

            } else {
                View v3 = inflater.inflate(R.layout.recycler_view_exercises_left_margin, parent, false);
                return new com.strongest.savingdata.Adapters.WorkoutAdapter.WorkoutAdapter.ExerciseViewHolder(v3);

            }
        }
*/
    /*    @Override
        public ChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        *//*
        View v = inflater.inflate(R.layout.recycler_view_exercise_details_left_margin, parent, false);


        return new BeansHolderViewHolder(v);
        *//*
            View v = inflater.inflate(R.layout.progress_set, parent, false);
            return new com.strongest.savingdata.Adapters.WorkoutAdapter.WorkoutAdapter.ProgressViewHolder(v);

        }*/

      /*  @Override
        public void onBindChildViewHolder(ChildViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
       *//* ((BeansViewHolder) holder).setText("dasdsad");*//*

        }*/

      /*  @Override
        public void onBindGroupViewHolder(com.strongest.savingdata.Adapters.WorkoutAdapter.WorkoutAdapter.ExerciseViewHolder holder, int flatPosition, ExpandableGroup group) {
            currentPosition = flatPosition;
            int viewType = getItemViewType(flatPosition);
            if (viewType == TITLE) {
                com.strongest.savingdata.Adapters.WorkoutAdapter.WorkoutAdapter.ViewHolderWorkout vh1 = (com.strongest.savingdata.Adapters.WorkoutAdapter.WorkoutAdapter.ViewHolderWorkout) holder;
                configureViewHolder1(vh1, flatPosition);
            } else if (viewType == BODY) {
                com.strongest.savingdata.Adapters.WorkoutAdapter.WorkoutAdapter.MuscleViewHolder vh2 = (com.strongest.savingdata.Adapters.WorkoutAdapter.WorkoutAdapter.MuscleViewHolder) holder;
                configureViewHolder2(vh2, flatPosition);
            } else if (viewType == EXERCISE){
                com.strongest.savingdata.Adapters.WorkoutAdapter.WorkoutAdapter.ExerciseViewHolder vh3 = (com.strongest.savingdata.Adapters.WorkoutAdapter.WorkoutAdapter.ExerciseViewHolder) holder;
                configureViewHolder3(vh3, flatPosition);
            }
        }*/

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
    }*/

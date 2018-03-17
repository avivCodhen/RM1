package com.strongest.savingdata.MyViews.MySelector;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.strongest.savingdata.Animations.MyJavaAnimator;
import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.Database.Exercise.Beans;
import com.strongest.savingdata.Dialogs.CreateRepsDialog;
import com.strongest.savingdata.MyViews.MySelector.MySelector.SelectorTypes;
import com.strongest.savingdata.MyViews.WeightKeyBoard.WeightKeyboard;
import com.strongest.savingdata.R;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

/**
 * Created by Cohen on 11/1/2017.
 */


public class ChooseSelectorAdapter extends MySelector.Adapter<MySelector.ViewHolder> {


    private final MySelectorOnBeansHolderChange mySelectorOnBeansHolderChange;
    // private MySelector.CheckedHolder[] checkedHolders;
    private final SelectorTypes[] types;
    private final Context context;

    public ChooseSelectorAdapter(MySelectorOnBeansHolderChange mySelectorOnBeansHolderChange,
                                 FragmentManager fm,
                                 Context context,
                                 Muscle muscle,
                                 MySelector.CheckedHolder[] checkedHolders,
                                 SelectorTypes... types) {
        super(fm, context, muscle, checkedHolders, types);
        this.mySelectorOnBeansHolderChange = mySelectorOnBeansHolderChange;
        this.types = types;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return types.length;
    }

    @Override
    public SelectorTypes[] getTypes() {
        return types;
    }

    @Override
    public MySelector.ViewHolder onCreateViewHolder(ViewGroup parent, SelectorTypes type) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v;
        switch (type) {
            case Exercise:
                v = inflater.inflate(R.layout.my_selector_exercise, parent, false);
                return new ViewHolderExercise(v, mySelectorOnBeansHolderChange);
            case Reps:
                v = inflater.inflate(R.layout.my_selector_reps, parent, false);
                return new ViewHolderReps(v, mySelectorOnBeansHolderChange);
            case Method:
                v = inflater.inflate(R.layout.my_selector_method, parent, false);
                return new ViewHolderMethod(v, mySelectorOnBeansHolderChange);
            case Sets:
                v = inflater.inflate(R.layout.my_selector_sets, parent, false);
                return new ViewHolderSets(v, mySelectorOnBeansHolderChange);
            case Rest:
                v = inflater.inflate(R.layout.my_selector_rest, parent, false);
                return new ViewHolderRest(v, mySelectorOnBeansHolderChange);
            case Weight:
                v = new WeightKeyboard(context);
                return new ViewHolderWeight(v, mySelectorOnBeansHolderChange);
        }

        return null;
    }

    @Override
    public void bindTypeViews(MySelector.ViewHolder viewHolder) {

        switch (viewHolder.getType()) {
            case Exercise:

        }
    }

    @Override
    public void bindView(MySelector.ViewHolder viewHolder, Beans bean, boolean sendNull) {
        switch (viewHolder.getType()) {
            case Exercise:
                configExerciseViewHolder((ViewHolderExercise) viewHolder, bean, sendNull);
                break;
            case Reps:
                configRepsViewHolder((ViewHolderReps) viewHolder, bean, sendNull);
                break;
            case Method:
                configMethodViewHolder((ViewHolderMethod) viewHolder, sendNull);
                break;
            case Rest:
                configurateRestViewHolder(viewHolder, bean, sendNull);
                break;
            case Sets:
                configurateSetsViewHolder(viewHolder, bean, sendNull);
                break;
        }
    }

    private void configurateSetsViewHolder(MySelector.ViewHolder viewHolder, Beans bean, boolean sendNull) {

        if (viewHolder.isInitiated()) {
            viewHolder.getMySelectorOnBeansHolderChange().notifyExerciseProfileBeanChange("sets", sendNull ? null : bean);
        } else {
            //  viewHolder.setInitiated(true);
        }
    }

    private void configurateRestViewHolder(MySelector.ViewHolder viewHolder, Beans bean, boolean sendNull) {
        if (viewHolder.isInitiated()) {
            viewHolder.getMySelectorOnBeansHolderChange().notifyExerciseProfileBeanChange("rest",sendNull ? null : bean);
        } else {
            //   viewHolder.setInitiated(true);
        }
    }


    private void configRepsViewHolder(ViewHolderReps viewHolder, Beans b, boolean sendNull) {
        if (viewHolder.isInitiated()) {
            viewHolder.getMySelectorOnBeansHolderChange().notifyExerciseProfileBeanChange("reps",sendNull ? null : b);
        } else {
            // viewHolder.setInitiated(true);
        }
    }

    private void configMethodViewHolder(ViewHolderMethod viewHolder, boolean sendNull) {
    }

    private void configExerciseViewHolder(ViewHolderExercise viewHolder, Beans bean, boolean sendNull) {
        //viewHolder.exercise_tv.setText(val);
        //viewHolder.toggleCollapseExpand();
        if (bean == null && sendNull) {
            viewHolder.el.expand();
            viewHolder.isExpand = true;
            viewHolder.toggleCollapseExpand();
            viewHolder.exercise_tv.setText("Choose a muscle to display exercises");
        } else {
            viewHolder.el.collapse();
            viewHolder.exercise_tv.setText("Displaying " + this.getMuscleType().getMuscle_display() + " Exercises");
            viewHolder.type_tv.setText(bean.getType() + " Exercise");
            MyJavaAnimator.fadeIn(viewHolder.muscleChange_tv);
            if (viewHolder.isInitiated()) {
                viewHolder.getMySelectorOnBeansHolderChange().
                        notifyExerciseProfileBeanChange("exercise", sendNull ? null : bean);
            }
        }
    }


    public class ViewHolderExercise extends MySelector.ViewHolder {

        private TextView exercise_tv, type_tv, muscleChange_tv;
        private RecyclerView recyclerView;
        public ExpandableLayout el;
        private ImageView exercise_description_img;
        private ImageView arrow;
        private HorizontalScrollView sc;
        private boolean isExpand = true;
        private SelectorExerciseAdapter adapter;

        public ViewHolderExercise(View v, MySelectorOnBeansHolderChange mySelectorOnBeansHolderChange) {
            super(v, mySelectorOnBeansHolderChange);
            sc = (HorizontalScrollView) v.findViewById(R.id.my_selector_exercise_scrollview);
            el = (ExpandableLayout) v.findViewById(R.id.expandable);
            recyclerView = (RecyclerView) v.findViewById(R.id.my_selector_exercises_recyclerview);
            RecyclerView.LayoutManager lm = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            adapter = new SelectorExerciseAdapter(Muscle.getAllMuscles(getDataManager().getMuscleDataManager()));
            recyclerView.setLayoutManager(lm);
            recyclerView.setAdapter(adapter);


            el.collapse();
            arrow = (ImageView) v.findViewById(R.id.arrow);
            exercise_tv = (TextView) v.findViewById(R.id.my_selector_exercise_muscle_text_view);
            type_tv = (TextView) v.findViewById(R.id.my_selector_exercise_type_tv);
            muscleChange_tv = (TextView) v.findViewById(R.id.my_selector_exercise_muscle_change_text_view);
            muscleChange_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    el.toggle();
                    toggleCollapseExpand();
                    type_tv.setText("Exercise Type");
                }
            });
            // exercise_description_img = (ImageView) v.findViewById(R.id.my_selector_muscle_dialog_imageview);
        }

        public void toggleCollapseExpand() {
            if (isExpand) {
                collapse(sc);
                isExpand = false;
            } else {
                expand(sc);
                isExpand = true;
            }
        }


        public HorizontalScrollView getSc() {
            return sc;
        }

        private class SelectorExerciseAdapter extends RecyclerView.Adapter<SelectorExerciseAdapter.ViewHolder> {
            private ArrayList<Muscle> muscles;

            public SelectorExerciseAdapter(ArrayList<Muscle> muscles) {

                this.muscles = muscles;
            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(context).inflate(R.layout.recycler_view_muscle,
                        parent, false);
                return new ViewHolder(v);
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, final int position) {
                holder.tv.setText(muscles.get(position).getMuscle_display());
                holder.cv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateMuscle(muscles.get(position));
                    }
                });
            }

            @Override
            public int getItemCount() {
                return muscles.size();
            }

            class ViewHolder extends RecyclerView.ViewHolder {
                TextView tv;
                CardView cv;

                public ViewHolder(View itemView) {
                    super(itemView);
                    //tv = (TextView) itemView.findViewById(R.id.muscle_select_tv);
                    //cv = (CardView) itemView.findViewById(R.id.muscle_select_cardview);
                }
            }

        }
    }


    public class ViewHolderReps extends MySelector.ViewHolder implements View.OnClickListener {

        private Button custom_btn;
        private HorizontalScrollView sc;


        public ViewHolderReps(View v, MySelectorOnBeansHolderChange mySelectorOnBeansHolderChange) {
            super(v, mySelectorOnBeansHolderChange);
            custom_btn = (Button) v.findViewById(R.id.my_selector_custom_reps_btn);
            custom_btn.setOnClickListener(this);
            sc = (HorizontalScrollView) v.findViewById(R.id.my_selector_reps_scrollview);

        }

        @Override
        public void onClick(View v) {
            CreateRepsDialog d = CreateRepsDialog.newInstance(getmSelector().getDataManager(), getOnSelectorDialogListener());
            d.show(getFm(), "repDialog");
            notifyTypeListChanged(SelectorTypes.Reps);
            Log.d("aviv", "onClick: ");
        }

        public HorizontalScrollView getSc() {
            return sc;
        }
    }

    public class ViewHolderMethod extends MySelector.ViewHolder implements View.OnClickListener {
        private Button custom_btn;
        private HorizontalScrollView sc;


        private TextView method_description_tv;

        public ViewHolderMethod(View v, MySelectorOnBeansHolderChange mySelectorOnBeansHolderChange) {
            super(v, mySelectorOnBeansHolderChange);
            method_description_tv = (TextView) v.findViewById(R.id.my_selector_method_description_tv);
            custom_btn = (Button) v.findViewById(R.id.my_selector_custom_method_btn);
            custom_btn.setOnClickListener(this);
            sc = (HorizontalScrollView) v.findViewById(R.id.my_selector_method_scrollview);

        }

        @Override
        public void onClick(View v) {

        }
    }

    public class ViewHolderWeight extends MySelector.ViewHolder {

        public ViewHolderWeight(View v, MySelectorOnBeansHolderChange mySelectorOnBeansHolderChange) {
            super(v, mySelectorOnBeansHolderChange);
        }

    }

    private class ViewHolderSets extends MySelector.ViewHolder implements View.OnClickListener {
        private Button custom_btn;
        private HorizontalScrollView sc;


        public ViewHolderSets(View v, MySelectorOnBeansHolderChange mySelectorOnBeansHolderChange) {
            super(v, mySelectorOnBeansHolderChange);
            custom_btn = (Button) v.findViewById(R.id.my_selector_custom_sets_btn);
            custom_btn.setOnClickListener(this);
            sc = (HorizontalScrollView) v.findViewById(R.id.my_selector_sets_scrollview);

        }

        @Override
        public void onClick(View v) {

        }
    }

    private class ViewHolderRest extends MySelector.ViewHolder implements View.OnClickListener {
        private Button custom_btn;
        private HorizontalScrollView sc;


        public ViewHolderRest(View v, MySelectorOnBeansHolderChange mySelectorOnBeansHolderChange) {
            super(v, mySelectorOnBeansHolderChange);
            sc = (HorizontalScrollView) v.findViewById(R.id.my_selector_rest_scrollview);

            custom_btn = (Button) v.findViewById(R.id.my_selector_custom_rest_btn);
            custom_btn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

        public HorizontalScrollView getSc() {
            return sc;
        }

    }

    public static void expand(final View v) {
        v.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final int targetWidth = v.getMeasuredWidth();

        v.getLayoutParams().width = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().width = interpolatedTime == 1
                        ? LayoutParams.WRAP_CONTENT
                        : (int) (targetWidth * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration((int) (targetWidth / v.getContext().getResources().getDisplayMetrics().density));
        //a.setDuration(2000);
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialWidth = v.getMeasuredWidth();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().width = initialWidth - (int) (initialWidth * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration((int) (initialWidth / v.getContext().getResources().getDisplayMetrics().density));
        //a.setDuration(2000);
        v.startAnimation(a);
    }
}

    /*public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
*/


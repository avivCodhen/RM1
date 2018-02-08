package com.strongest.savingdata.MyViews.MySelector;

import android.content.Context;
import android.support.v4.app.FragmentManager;
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

import com.strongest.savingdata.Database.Exercise.Beans;
import com.strongest.savingdata.Database.Exercise.DBExercisesHelper;
import com.strongest.savingdata.Database.Managers.ExercisesDataManager;
import com.strongest.savingdata.Dialogs.CreateRepsDialog;
import com.strongest.savingdata.MyViews.MySelector.MySelector;
import com.strongest.savingdata.MyViews.MySelector.MySelector.SelectorTypes;
import com.strongest.savingdata.MyViews.WeightKeyBoard.WeightKeyboard;
import com.strongest.savingdata.R;

import net.cachapa.expandablelayout.ExpandableLayout;

/**
 * Created by Cohen on 11/1/2017.
 */


public class ChooseSelectorAdapter extends MySelector.Adapter<MySelector.ViewHolder> {


    private final int muscle;
    // private MySelector.CheckedHolder[] checkedHolders;
    private final SelectorTypes[] types;
    private final Context context;

    public ChooseSelectorAdapter(FragmentManager fm, Context context, int muscle, MySelector.CheckedHolder[] checkedHolders, SelectorTypes... types) {
        super(fm, context, muscle, checkedHolders, types);
        this.muscle = muscle;
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
                return new ViewHolderExercise(v);
            case Reps:
                v = inflater.inflate(R.layout.my_selector_reps, parent, false);
                return new ViewHolderReps(v);
            case Method:
                v = inflater.inflate(R.layout.my_selector_method, parent, false);
                return new ViewHolderMethod(v);
            case Sets:
                v = inflater.inflate(R.layout.my_selector_sets, parent, false);
                return new ViewHolderSets(v);
            case Rest:
                v = inflater.inflate(R.layout.my_selector_rest, parent, false);
                return new ViewHolderRest(v);
            case Weight:
                v = new WeightKeyboard(context);
                return new ViewHolderWeight(v);
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
    public int getMuscleType() {
        return muscle;
    }

    @Override
    public void bindView(MySelector.ViewHolder viewHolder, String val) {
        switch (viewHolder.getType()) {
            case Exercise:
                configExerciseViewHolder((ViewHolderExercise) viewHolder, val);
                break;
            case Reps:
                configRepsViewHolder((ViewHolderReps) viewHolder);
                break;
            case Method:
                configMethodViewHolder((ViewHolderMethod) viewHolder);
                break;
        }
    }


    private void configRepsViewHolder(ViewHolderReps viewHolder) {
    }

    private void configMethodViewHolder(ViewHolderMethod viewHolder) {
    }

    private void configExerciseViewHolder(ViewHolderExercise viewHolder, String val) {
        viewHolder.exercise_tv.setText(val);
    }


    public class ViewHolderExercise extends MySelector.ViewHolder {

        private TextView exercise_tv;
        private ExpandableLayout el;
        private ImageView exercise_description_img;
        private ImageView arrow;
        private HorizontalScrollView sc;
        private boolean isExpand = true;

        public ViewHolderExercise(View v) {
            super(v);
            sc = (HorizontalScrollView) v.findViewById(R.id.my_selector_exercise_scrollview);
            el = (ExpandableLayout) v.findViewById(R.id.expandable);
            arrow = (ImageView) v.findViewById(R.id.arrow);
            arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    el.toggle();
                    toggleCollapseExpand();
                }
            });
            exercise_tv = (TextView) v.findViewById(R.id.my_selector_exercise_muscle_text_view);
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
    }


    public class ViewHolderReps extends MySelector.ViewHolder implements View.OnClickListener {

        private Button custom_btn;
        private HorizontalScrollView sc;


        public ViewHolderReps(View v) {
            super(v);
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

        public ViewHolderMethod(View v) {
            super(v);
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

        public ViewHolderWeight(View v) {
            super(v);
        }

    }

    private class ViewHolderSets extends MySelector.ViewHolder implements View.OnClickListener {
        private Button custom_btn;
        private HorizontalScrollView sc;


        public ViewHolderSets(View v) {
            super(v);
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


        public ViewHolderRest(View v) {
            super(v);
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


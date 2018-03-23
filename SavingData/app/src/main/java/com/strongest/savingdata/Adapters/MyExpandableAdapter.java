package com.strongest.savingdata.Adapters;

import android.animation.ObjectAnimator;
import android.app.Service;
import android.content.Context;
import android.content.res.ColorStateList;
import android.media.Image;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.strongest.savingdata.Activities.HomeActivity;
import com.strongest.savingdata.Adapters.WorkoutAdapter.ItemTouchHelperAdapter;
import com.strongest.savingdata.Adapters.WorkoutAdapter.ItemTouchHelperListener;
import com.strongest.savingdata.Adapters.WorkoutAdapter.OnDragListener;
import com.strongest.savingdata.Adapters.WorkoutAdapter.OnExerciseProfileEditClick;
import com.strongest.savingdata.Adapters.WorkoutAdapter.OnExpandCollapseListener;
import com.strongest.savingdata.Adapters.WorkoutAdapter.ScrollToPositionListener;
import com.strongest.savingdata.AlgorithmLayout.LayoutManager;
import com.strongest.savingdata.AlgorithmLayout.PLObject;
import com.strongest.savingdata.AlgorithmLayout.PLObject.BodyText;
import com.strongest.savingdata.AlgorithmLayout.PLObject.ExerciseProfile;
import com.strongest.savingdata.AlgorithmLayout.WorkoutLayoutTypes;
import com.strongest.savingdata.Animations.MyJavaAnimator;
import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.Database.Exercise.ExerciseSet;
import com.strongest.savingdata.Gestures.MyGestureTouchListener;
import com.strongest.savingdata.MyViews.BodyTextView;
import com.strongest.savingdata.MyViews.CreateCustomBeansView.NumberChooseManager;
import com.strongest.savingdata.MyViews.CreateCustomBeansView.SingleNumberChooseView;
import com.strongest.savingdata.MyViews.ExerciseView;
import com.strongest.savingdata.MyViews.WeightKeyBoard.WeightKeyboard;
import com.strongest.savingdata.MyViews.WorkoutView.OnWorkoutViewInterfaceClicksListener;
import com.strongest.savingdata.R;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Handler;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Cohen on 1/28/2018.
 */

/**
 * Created by Cohen on 10/15/2017.
 */

public class MyExpandableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        ItemTouchHelperAdapter, OnExpandCollapseListener {


    private final String TAG = "aviv";
    private Context context;
    private ArrayList<PLObject> exArray;
    private OnWorkoutViewInterfaceClicksListener onWorkoutViewInterfaceClicksListener;
    private OnDragListener onDragListener;
    private ScrollToPositionListener scrollListener;
    private OnExerciseProfileEditClick onExerciseProfileEditClick;

    private int currentPosition = 0;
    private boolean progressMode;
    private int fromPos;
    private int toPos;


    public MyExpandableAdapter(ArrayList<PLObject> exArray, Context context
            , OnDragListener onDragListener,
                               ScrollToPositionListener scrollListener,
                               OnWorkoutViewInterfaceClicksListener onWorkoutViewInterfaceClicksListener
    ) {
        this.context = context;
        this.onDragListener = onDragListener;
        this.scrollListener = scrollListener;
        this.exArray = exArray;
        this.onWorkoutViewInterfaceClicksListener = onWorkoutViewInterfaceClicksListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == WorkoutLayoutTypes.WorkoutView.ordinal()) {
            View v1 = inflater.inflate(R.layout.recycler_view_workouts, parent, false);
            return new ViewHolderWorkout(v1);
        } else if (viewType == WorkoutLayoutTypes.BodyView.ordinal()) {
            View v2 = inflater.inflate(R.layout.recyclerview_body_parts, parent, false);
            return new MyExpandableAdapter.MuscleViewHolder(v2);
        } else if (viewType == WorkoutLayoutTypes.SetsPLObject.ordinal()) {
            View v4 = inflater.inflate(R.layout.recycler_view_exercise_set_left_margin, parent, false);
            return new SetsViewHolder(v4);

        } else if (viewType == WorkoutLayoutTypes.IntraSet.ordinal()) {
            View v4 = inflater.inflate(R.layout.recycler_view_intra_set, parent, false);
            return new SetsViewHolder(v4);
        } else if (viewType == WorkoutLayoutTypes.ProgressPLObject.ordinal()) {
            View v5 = inflater.inflate(R.layout.progress_set, parent, false);
            return new ProgressPLObjectViewHolder(v5);

        } else if (viewType == WorkoutLayoutTypes.ExerciseProfile.ordinal()) {
            View v3 = inflater.inflate(R.layout.recycler_view_exercises_left_margin, parent, false);
            return new ExerciseViewHolder(v3, this);

        } else if (viewType == WorkoutLayoutTypes.IntraExerciseProfile.ordinal()) {
            View v3 = inflater.inflate(R.layout.recycler_view_intra_exercise, parent, false);
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

/*
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position, List<Object> payloads) {
        if (payloads.size() != 0) {
            ExerciseProfile ep = ((ExerciseProfile) exArray.get(position));
            ExerciseViewHolder vh = null;
            View v = null;

            switch (payloads.get(0).toString()) {
                case "muscle":
                    vh = (ExerciseViewHolder) holder;
                    v = vh.icon;
                    if (ep.getMuscle() != null) {
                        Muscle.MuscleUI mui = Muscle.provideMuscleUI(ep.getMuscle());
                        MyJavaAnimator.fadeInAndOutTextView(ep.getMuscle().getMuscle_display(), vh.muscle);
                        MyJavaAnimator.fadeInAndOutImageView(mui.getImage(), -1, (ImageView) v);
                    }
                    break;
                case "exercise":
                    vh = (ExerciseViewHolder) holder;
                    v = vh.name;

                    if (ep.getmSets().getExercise() != null) {
                        Muscle.MuscleUI mui = Muscle.provideMuscleUI(ep.getmSets().getExercise().getMuscle());
                        MyJavaAnimator.fadeInAndOutTextView(ep.getmSets().getExercise().getName(), (TextView) v);
                    } else {
                        MyJavaAnimator.fadeInAndOutTextView("Configurate New Excercise", (TextView) v);
                    }
                    break;
                case "reps":
                    vh = (ExerciseViewHolder) holder;
                    v = vh.reps;
                    if (ep.getmSets().getRep() != null) {

                        MyJavaAnimator.fadeInAndOutTextView( ep.getmSets().getRep().getName(), (TextView) v);

                        // vh.reps.setText("Reps: " + ep.getmBeansHolder().getRep().getName());

                    } else {
                        MyJavaAnimator.fadeInAndOutTextView("-", (TextView) v);

                        // vh.reps.setText("Reps: -");
                    }
                    break;
                case "rest":
                    vh = (ExerciseViewHolder) holder;
                    v = vh.rest;
                    if (ep.getmSets().getRest() != null) {

                        MyJavaAnimator.fadeInAndOutTextView( ep.getmSets().getRest().getName(), (TextView) v);

                    } else {
                        MyJavaAnimator.fadeInAndOutTextView("-", (TextView) v);

                        //   vh.rest.setText("Rest: ");
                    }
                    break;
                case "sets":
                    vh = (ExerciseViewHolder) holder;
                    v = vh.sets;
                    if (ep.getmSets().getSets() != null) {

                        MyJavaAnimator.fadeInAndOutTextView( ep.getmSets().getSets().getName(), (TextView) v);
                    } else {
                        MyJavaAnimator.fadeInAndOutTextView("-", vh.sets);

                        //  vh.sets.setText("Sets: ");
                    }
                    break;
            }

        }
       */
/* if (payloads.size() > 0 && payloads.get(0).toString().equals("exercise")) {
            ((ExerciseViewHolder) holder).name.setText(((ExerciseProfile) exArray.get(position)).getmBeansHolder().getExercise().getName());
        }

        *//*


        else {
            onBindViewHolder(holder, position);
        }
    }

*/

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == WorkoutLayoutTypes.WorkoutView.ordinal()) {
            ViewHolderWorkout vh1 = (ViewHolderWorkout) holder;
            configureViewHolder1(vh1, position);
        } else if (holder.getItemViewType() == WorkoutLayoutTypes.BodyView.ordinal()) {
            MuscleViewHolder vh2 = (MuscleViewHolder) holder;
            configureViewHolder2(vh2, position);

        } else if (holder.getItemViewType() == WorkoutLayoutTypes.ExerciseProfile.ordinal() ||
                holder.getItemViewType() == WorkoutLayoutTypes.IntraExerciseProfile.ordinal()) {
            ExerciseViewHolder vh3 = (ExerciseViewHolder) holder;
            configureViewHolder3(vh3, position);

        } else if (holder.getItemViewType() == WorkoutLayoutTypes.SetsPLObject.ordinal()) {
            int childPosition = 0;
            PLObject.SetsPLObject setsPLObject = (PLObject.SetsPLObject) exArray.get(position);
            for (int i = 0; i < setsPLObject.getParent().getSets().size(); i++) {
                if (setsPLObject == setsPLObject.getParent().getSets().get(i)) {
                    childPosition = i;
                    break;
                }
            }
            SetsViewHolder vh4 = (SetsViewHolder) holder;
            configurateSets(vh4, position, childPosition, setsPLObject.getParent());

        } else if (holder.getItemViewType() == WorkoutLayoutTypes.ProgressPLObject.ordinal()) {


        } else if (holder.getItemViewType() == WorkoutLayoutTypes.AddExercise.ordinal()) {
            AddExerciseViewHolder vh5 = (AddExerciseViewHolder) holder;
            configurateAddExerciseViewHolder(vh5, position);

        } else if (holder.getItemViewType() == WorkoutLayoutTypes.Method.ordinal()) {
            AddExerciseViewHolder vh5 = (AddExerciseViewHolder) holder;
            configurateAddExerciseViewHolder(vh5, position);

        } else if (holder.getItemViewType() == WorkoutLayoutTypes.IntraSet.ordinal()) {
            SetsViewHolder setsVH = (SetsViewHolder) holder;
            int childPosition = 0;
            PLObject.IntraSetPLObject intraSetPLObject = (PLObject.IntraSetPLObject) exArray.get(position);
            if (intraSetPLObject.getInnerType() == WorkoutLayoutTypes.SuperSetIntraSet) {

                for (int i = 0; i < intraSetPLObject.getParent().getIntraSets().size(); i++) {
                    if (intraSetPLObject.getParent().getIntraSets().get(i) == intraSetPLObject) {
                        childPosition = i;
                    }
                }
            } else {
                for (int i = 0; i < intraSetPLObject.getParent().getSets().size(); i++) {
                    if (intraSetPLObject.getParentSet().getIntraSets().get(i) == intraSetPLObject) {
                        childPosition = i;
                    }
                }
            }
            configurateSets(setsVH, position, childPosition, intraSetPLObject.getParent());
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

        if (type == WorkoutLayoutTypes.ExerciseProfile) {
            ExerciseProfile ep = (ExerciseProfile) exArray.get(position);
            if (ep.getInnerType() == WorkoutLayoutTypes.ExerciseProfile) {
                return WorkoutLayoutTypes.ExerciseProfile.ordinal();
            } else {
                return WorkoutLayoutTypes.IntraExerciseProfile.ordinal();
            }
        }
        if (type == WorkoutLayoutTypes.SetsPLObject) {
            boolean found = false;
            WorkoutLayoutTypes innerType = null;
            outloop:
            for (int i = position; i >= 0; i--) {
                if (exArray.get(i).getType() == WorkoutLayoutTypes.ExerciseProfile || exArray.get(i).getType() == WorkoutLayoutTypes.Method) {
                    innerType = ((ExerciseProfile) exArray.get(i)).getInnerType();
                    found = true;
                    break outloop;
                }

            }
            if (found == true && innerType == WorkoutLayoutTypes.ExerciseProfile) {
                return WorkoutLayoutTypes.SetsPLObject.ordinal();
            }

        }
        return type.ordinal();
    }

    public void configurateAddExerciseViewHolder(AddExerciseViewHolder vh, int position) {
    }


    private void configureViewHolder1(ViewHolderWorkout vh1, final int position) {


        PLObject.WorkoutText workoutText = (PLObject.WorkoutText) exArray.get(position);
        vh1.workoutTv.setText(workoutText.getWorkoutName());


    }

    private void configureViewHolder2(final MuscleViewHolder vh2, final int position) {
        BodyText bodyText = (BodyText) exArray.get(position);
        vh2.bodyTv.setText(bodyText.getTitle());

        vh2.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exArray.remove(position);
                notifyItemRemoved(position);
            }
        });
      vh2.easyFlipView.setOnFlipListener(new EasyFlipView.OnFlipAnimationListener() {
          @Override
          public void onViewFlipCompleted(EasyFlipView easyFlipView, EasyFlipView.FlipState newCurrentSide) {
              InputMethodManager imm = (InputMethodManager) context
                      .getSystemService(Context.INPUT_METHOD_SERVICE);
              if(!easyFlipView.isBackSide()){

                  imm.hideSoftInputFromWindow(vh2.bodyTv.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                  vh2.bodyTv.setSelected(false);
                  vh2.bodyTv.setFocusable(false);
                  vh2.bodyTv.setFocusableInTouchMode(true);
                  vh2.bodyTv.clearFocus();
                  vh2.dummy.requestFocus();
                  vh2.bodyTv.setClickable(false);
                  vh2.bodyTv.setEnabled(false);
              }else{
                  vh2.bodyTv.setEnabled(true);

                  //vh2.bodyTv.setClickable(false);
                  vh2.bodyTv.requestFocus();
                  vh2.bodyTv.setSelected(true);
                  vh2.bodyTv.setFocusable(true);
                  vh2.bodyTv.setFocusableInTouchMode(true);
                  imm.showSoftInput(vh2.bodyTv,0);
              }


          }
      });

      vh2.bodyTv.setOnKeyListener(new View.OnKeyListener() {
          public boolean onKey(View view, int keyCode, KeyEvent event) {
              if (keyCode == KeyEvent.KEYCODE_ENTER) {
                  InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                  imm.hideSoftInputFromWindow(vh2.bodyTv.getWindowToken(), 0);
                  vh2.bodyTv.setFocusable(false);
                  vh2.bodyTv.setFocusableInTouchMode(true);
                  return true;
              } else {
                  return false;
              }
          }
      });
        vh2.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onWorkoutViewInterfaceClicksListener.onBodyViewLongClick(vh2, false);
                return true;
            }
        });

    }

    public void configurateSets(final SetsViewHolder vh, final int position, final int childPosition, ExerciseProfile ep) {
        if (ep.getMuscle() != null) {
            Muscle.MuscleUI mui = Muscle.provideMuscleUI(ep.getMuscle());
            vh.fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, mui.getColor())));
        }
        PLObject.SetsPLObject setsPLObject = null;
        PLObject.IntraSetPLObject intraSetPLObject;
        if (exArray.get(position) instanceof PLObject.SetsPLObject) {
            setsPLObject = (PLObject.SetsPLObject) exArray.get(position);
        }
        if (exArray.get(position) instanceof PLObject.IntraSetPLObject) {
            intraSetPLObject = (PLObject.IntraSetPLObject) exArray.get(position);
            if (intraSetPLObject.getInnerType() == WorkoutLayoutTypes.SuperSetIntraSet) {
                vh.tag.setText("INTRA " + intraSetPLObject.getParent().getTag());
                // vh.card.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
                ((CardView) vh.card).setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
            } else {
                vh.tag.setText("INTRA SET");
            }
        }

        if (setsPLObject != null) {
            vh.card.setOnTouchListener(new MyGestureTouchListener(context) {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        v.setPressed(true);
                    } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_MOVE) {
                        v.setPressed(false);
                    }
                    return super.onTouch(v, event);
                }

                @Override
                public void onLongClick() {
                    super.onLongClick();
                    onWorkoutViewInterfaceClicksListener.onSetsLongClick(vh, childPosition, false);
                }

                @Override
                public void onDoubleClick() {
                    super.onDoubleClick();
                    onWorkoutViewInterfaceClicksListener.onSetsDoubleClick(vh, childPosition);
                }
            });
        }

        vh.settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onWorkoutViewInterfaceClicksListener.onSettingsClick(vh);
            }
        });

        ExerciseSet exerciseSet = new ExerciseSet();
        exerciseSet.setRep("5");
        exerciseSet.setWeight(105.5);
        exerciseSet.setRest("02:10");

        //   vh.reps.setText(ep.getSets().get(childPosition).getExerciseSet().getRep());
        vh.reps.setText("Reps: " + exerciseSet.getRep());
        //  vh.rest.setText(ep.getSets().get(childPosition).getExerciseSet().getRest());
        vh.rest.setText("Rest: " + exerciseSet.getRest());

        //vh.weight.setText(ep.getSets().get(childPosition).getExerciseSet().getWeight() + "");
        vh.weight.setText("Weight: " + exerciseSet.getWeight() + "");
        vh.set.setText(childPosition + 1 + "");
    }


    private void configureViewHolder3(final ExerciseViewHolder vh3, final int position) {
        final ExerciseProfile exerciseProfile = (ExerciseProfile) exArray.get(position);
        int color = ContextCompat.getColor(context, R.color.colorAccent);
        final int repositionWidth = vh3.expandableLayout.getWidth();
        Muscle.MuscleUI mui;

        if (exerciseProfile.getInnerType() == WorkoutLayoutTypes.IntraExerciseProfile) {
            int pos = 0;
            for (int i = 0; i < exerciseProfile.getParent().getExerciseProfiles().size(); i++) {
                pos++;
                if (exerciseProfile.getParent().getExerciseProfiles().get(i) == exerciseProfile) {
                    break;
                }
            }
            vh3.tag.setText(LayoutManager.intraWorkoutsLetters[pos]);
            exerciseProfile.setTag(vh3.tag.getText().toString());
        }
        if (exerciseProfile.getMuscle() != null) {
            mui = Muscle.provideMuscleUI(exerciseProfile.getMuscle());
            vh3.icon.setImageResource(mui.getImage());
            vh3.muscle.setText(exerciseProfile.getMuscle().getMuscle_display());

        }

        vh3.settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onWorkoutViewInterfaceClicksListener.onSettingsClick(vh3);
            }
        });

        vh3.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onWorkoutViewInterfaceClicksListener.onExerciseClick(vh3);
            }
        });

        vh3.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (exerciseProfile.getInnerType() == WorkoutLayoutTypes.IntraExerciseProfile) {
                    onWorkoutViewInterfaceClicksListener.onLongSupersetClick(vh3, false);
                } else {
                    onWorkoutViewInterfaceClicksListener.onLongClick(vh3, false);
                }
                return true;
            }
        });


        vh3.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onWorkoutViewInterfaceClicksListener.onMoreClick(vh3);

            }
        });


    }


    @Override
    public void onExpand(ExerciseViewHolder vh) {
        int position = vh.getAdapterPosition();
        ExerciseProfile ep = (ExerciseProfile) exArray.get(position);
        ArrayList<PLObject.SetsPLObject> sets = ep.getSets();
        ArrayList<PLObject> block = new ArrayList<>();

        int newPosition = ep.getExerciseProfiles().size() + 1;
        int row = 0;
        if (sets != null) {
            for (int i = 0; i < sets.size(); i++) {
                block.add(sets.get(i));
                if (ep.getExerciseProfiles().size() != 0) {
                    for (int j = 0; j < ep.getExerciseProfiles().size(); j++) {
                        Log.d(TAG, "onExpand: " + ep.getExerciseProfiles().size());
                        Log.d(TAG, "onExpand: " + ep.getExerciseProfiles().get(j).getIntraSets().size());
                        block.add(ep.getExerciseProfiles().get(j).getIntraSets().get(i));

                    }
                }
                ++row;
                for (int j = 0; j < sets.get(i).getIntraSets().size(); j++) {
                    ++row;

                    if (sets.get(i).getIntraSets().size() != 0 && sets.get(i).getIntraSets().size() > i) {
                        block.add(sets.get(i).getIntraSets().get(j));
                        ++row;

                    }
                    //exArray.add(position + i + newPosition, obj);
                }
            }
            int i = 0;
            for (PLObject plObject : block) {
                exArray.add(position + newPosition + i, plObject);
                i++;
            }
            notifyItemRangeInserted(position + newPosition, row);
            scrollListener.scrollToPosition(position, true, true);
        }
        ep.setExpand(true);
        animateExpand(vh);
        vh.expand = true;
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
        ExerciseProfile ep = (ExerciseProfile) exArray.get(position);
        ArrayList<PLObject.SetsPLObject> sets = ep.getSets();
        int newPosition = ep.getExerciseProfiles().size() + 1;
        int count = 0;
        if (sets != null) {
            for (int i = 0; i < sets.size(); i++) {
                exArray.remove(position + newPosition);
                count++;
                if (ep.getExerciseProfiles().size() != 0) {
                    for (int j = 0; j < ep.getExerciseProfiles().size(); j++) {
                        exArray.remove(position + newPosition);
                        count++;
                    }
                }
                for (int k = 0; k < sets.get(i).getIntraSets().size(); k++) {
                    exArray.remove(position + newPosition);
                    count++;

                }

            }
            notifyItemRangeRemoved(position + newPosition, count);
            scrollListener.scrollToPosition(position, true, true);

        }
        ep.setExpand(false);
        animateCollapse(vh);
        vh.expand = false;

    }

    @Override
    public void deTailExpand(RecyclerView.ViewHolder vh) {
        int position = vh.getAdapterPosition();
        PLObject.AddExercise a = new PLObject.AddExercise(null);
        //ExerciseProfile ep = (ExerciseProfile) exArray.get(position);
     /*   if (!ep.isExpand()) {
            onExpand((ExerciseViewHolder) vh);
        }*/
        exArray.add(position + 1, a);
        notifyItemInserted(position + 1);
        onExerciseProfileEditClick.onEditExerciseClick(true, vh, position, exArray.get(position));
        scrollListener.scrollToPosition(position, false, false);
    }

    @Override
    public void detailCollapse(RecyclerView.ViewHolder vh) {
        int position = vh.getAdapterPosition();
        PLObject.AddExercise a = new PLObject.AddExercise(null);
        //ExerciseProfile ep = (ExerciseProfile) exArray.get(position);
        exArray.remove(position + 1);
        notifyItemRemoved(position + 1);
        notifyItemChanged(position);
        onExerciseProfileEditClick.onEditExerciseClick(false, vh, position, exArray.get(position));
        scrollListener.scrollToPosition(position, true, false);
    }


    public class ExerciseViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperListener {


        public TextView muscle;
        public TextView name;
        public TextView sets;
        public TextView reps;
        public TextView method;
        public TextView weight;
        public TextView rest;
        private View itemView;
        private final OnExpandCollapseListener collapseExpandListener;
        public CircleImageView icon;
        public ImageView arrow;
        private ImageView settings, more;
        public CardView card;
        public ExpandableLayout expandableLayout;
        private ViewGroup dragMenuItem;
        private ViewGroup supersetMenu;
        private ViewGroup mLayout;
        private TextView tag;
        // public ImageView edit;
        // public CircleImageView editIv;

        private boolean expand;
        private ExerciseViewHolder vh = this;

        public ExerciseViewHolder(final View itemView, final OnExpandCollapseListener collapseExpandListener) {
            super(itemView);
            arrow = (ImageView) itemView.findViewById(R.id.ex_arrow);
            this.collapseExpandListener = collapseExpandListener;
            icon = (CircleImageView) itemView.findViewById(R.id.ex1);
            name = (TextView) itemView.findViewById(R.id.exerciseView_nameTV);
            sets = (TextView) itemView.findViewById(R.id.exerciseView_setsTV);
            reps = (TextView) itemView.findViewById(R.id.exerciseView_repsTV);
            method = (TextView) itemView.findViewById(R.id.exerciseView_methodTV);
            weight = (TextView) itemView.findViewById(R.id.exerciseView_weightTV);
            rest = (TextView) itemView.findViewById(R.id.exerciseView_restTV);
            muscle = (TextView) itemView.findViewById(R.id.exerciseView_muscleTV);
            settings = (ImageView) itemView.findViewById(R.id.ex_choose);
            more = (ImageView) itemView.findViewById(R.id.ex_more);
            card = (CardView) itemView.findViewById(R.id.list_view_exercise_lay1);
            expandableLayout = (ExpandableLayout) itemView.findViewById(R.id.exercise_menu_expandlayout);
            dragMenuItem = (ViewGroup) itemView.findViewById(R.id.recycler_view_exercise_menu_drag);
            supersetMenu = (ViewGroup) itemView.findViewById(R.id.exercise_menu_superset);
            mLayout = (ViewGroup) itemView.findViewById(R.id.exercise_main_layout);
            tag = (TextView) itemView.findViewById(R.id.big_tag_tv);

            // editIv = (CircleImageView) itemView.findViewById(R.id.recycler_view_edit);
            this.itemView = itemView;
            //    this.listener = listener;

            dragMenuItem.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    ExerciseProfile ep = (ExerciseProfile) exArray.get(getAdapterPosition());
                    if (ep.isExpand()) {
                        onCollapse(vh);
                    }
                    onDragListener.startDrag(vh);
                    return true;
                }

            });


            supersetMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ExerciseProfile ep = (ExerciseProfile) exArray.get(getAdapterPosition());
                    ExerciseProfile superset = new ExerciseProfile(null, 0, 0, 0);
                    superset.setInnerType(WorkoutLayoutTypes.IntraExerciseProfile);
                    superset.setParent(ep);
                    if (ep.isExpand()) {
                        onCollapse(vh);
                    }
                    for (int i = 0; i < ep.getSets().size(); i++) {
                        superset.getIntraSets().add(new PLObject.IntraSetPLObject(superset, new ExerciseSet(),
                                WorkoutLayoutTypes.SuperSetIntraSet, null));
                    }
                    ep.getExerciseProfiles().add(superset);
                    exArray.add(vh.getAdapterPosition() + ep.getExerciseProfiles().size(), superset);
                    notifyItemInserted(vh.getAdapterPosition() + ep.getExerciseProfiles().size());
                }
            });
        }

        @Override
        public void onItemSelected() {
            onShadowCollapse(vh.getAdapterPosition(), (ExerciseProfile) exArray.get(vh.getAdapterPosition()));
        }

        @Override
        public void onItemClear() {
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onShadowExpand(vh.getAdapterPosition(), (ExerciseProfile) exArray.get(vh.getAdapterPosition()));

                }
            },500);
        }


    }


    public class MuscleViewHolder extends RecyclerView.ViewHolder {
        private EditText bodyTv;
     //   private ImageView drag;
        private EasyFlipView easyFlipView;
        private View dummy;
        public ImageView delete;

        public MuscleViewHolder(View itemView) {
            super(itemView);
            bodyTv = (EditText) itemView.findViewById(R.id.recycler_view_body_parts_TV);
          //  drag = (ImageView) itemView.findViewById(R.id.body_part_drag);
            easyFlipView = (EasyFlipView) itemView.findViewById(R.id.body_part_edit);
            dummy = itemView.findViewById(R.id.dummy);
            delete = (ImageView) itemView.findViewById(R.id.body_view_delete);
        }


    }

    public class SetsViewHolder extends RecyclerView.ViewHolder {
        private FloatingActionButton fab;
        private TextView reps, rest, weight, set;
        public ViewGroup card;
        public TextView tag;
        private SetsViewHolder vh = this;
        private ImageView settings;

        public SetsViewHolder(View itemView) {
            super(itemView);
            reps = (TextView) itemView.findViewById(R.id.exerciseView_repsTV);
            rest = (TextView) itemView.findViewById(R.id.exerciseView_restTV);
            weight = (TextView) itemView.findViewById(R.id.exerciseView_weightTV);
            set = (TextView) itemView.findViewById(R.id.recycler_view_exercise_details_sets_tv);
            fab = (FloatingActionButton) itemView.findViewById(R.id.recycler_view_exercise_details_fab);
            card = (ViewGroup) itemView.findViewById(R.id.recycler_view_set_card);
            tag = (TextView) itemView.findViewById(R.id.intra_set_tv);
            settings = (ImageView) itemView.findViewById(R.id.sets_settings);
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

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
      //  ExerciseProfile toEp;
     //   toEp = (ExerciseProfile) exArray.get(fromPosition);
        if(exArray.get(fromPosition).getType() == WorkoutLayoutTypes.BodyView){
            if(!validateBodyViewDrag(toPosition)){
                return false;
            }
        }
        if (exArray.get(toPosition).getType() == WorkoutLayoutTypes.SetsPLObject
                ) {
            return false;
        }

        if (exArray.get(toPosition).getType() == WorkoutLayoutTypes.IntraSet
                ) {
            return false;
        }

        if (exArray.get(toPosition).isExpand()) {
            return false;
        }

        /*if (  exArray.get(toPosition).getInnerType() == WorkoutLayoutTypes.IntraExerciseProfile) {
            return false;
        }*/


      /*  if (toPosition == 0 || fromPosition == 0) {
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
        //onWorkoutViewInterfaceClicksListener.onSwapExercise(fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    private boolean validateBodyViewDrag(int toPosition) {
        if(exArray.size() > toPosition+1){
           if(exArray.get(toPosition).getInnerType() == WorkoutLayoutTypes.ExerciseProfile &&
                   exArray.get(toPosition+1).getInnerType() == WorkoutLayoutTypes.IntraExerciseProfile){
               return false;
           }
            if(exArray.get(toPosition).getInnerType() == WorkoutLayoutTypes.IntraExerciseProfile &&
                    exArray.get(toPosition+1).getInnerType() == WorkoutLayoutTypes.IntraExerciseProfile){
                return false;
            }
        }
        if(toPosition-1 != -1){

          /*  if(exArray.get(toPosition).innerType == WorkoutLayoutTypes.IntraExerciseProfile&&
                    exArray.get(toPosition-1).innerType == WorkoutLayoutTypes.IntraExerciseProfile){
                return false;
            }
            */
           /*
            if(exArray.get(toPosition).innerType == WorkoutLayoutTypes.IntraExerciseProfile&&
                    exArray.get(toPosition-1).type == WorkoutLayoutTypes.ExerciseProfile){
                return false;
            }*/
        }
        return true;
    }

    public void onShadowCollapse(int pos, ExerciseProfile ep) {
        if (!ep.isShadowExpand()) {
            for (int i = 0; i < ep.getExerciseProfiles().size(); i++) {
                exArray.remove(pos + 1);
            }
            ep.setShadowExpand(true);
            notifyItemRangeRemoved(pos + 1, ep.getExerciseProfiles().size());
        }

    }

    public void onShadowExpand(int pos, ExerciseProfile ep) {
        if (ep.isShadowExpand()) {
            for (int j = 0; j < ep.getExerciseProfiles().size(); j++) {
                exArray.add(pos + j + 1, ep.getExerciseProfiles().get(j));
            }
        }
        notifyItemRangeInserted(pos+1, ep.getExerciseProfiles().size());
        ep.setShadowExpand(false);

    }


    @Override
    public void onItemDismiss(int position) {
        //null
    }

    public void setOnDragListener(OnDragListener onDragListener) {
        this.onDragListener = onDragListener;
    }


    public void setExArray(ArrayList<PLObject> exArray) {
        this.exArray = exArray;
    }

    public ArrayList<PLObject> getExArray() {
        return exArray;
    }

    public void myNotifyItemInserted(ArrayList<PLObject> exArray, int position) {
        this.exArray = exArray;
        notifyItemInserted(position);
    }

    public void myNotifyItemRemoved(ArrayList<PLObject> exArray, int position) {
        this.exArray = exArray;
        notifyItemRemoved(position);
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

    public void setOnExerciseProfileEditClick(OnExerciseProfileEditClick onExerciseProfileEditClick) {
        this.onExerciseProfileEditClick = onExerciseProfileEditClick;
    }

    public void setScrollListener(ScrollToPositionListener scrollListener) {
        this.scrollListener = scrollListener;
    }

}


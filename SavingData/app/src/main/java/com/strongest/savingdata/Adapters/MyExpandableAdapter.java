package com.strongest.savingdata.Adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.strongest.savingdata.AModels.workoutModel.ExerciseProfileStats;
import com.strongest.savingdata.Adapters.WorkoutAdapter.ItemTouchHelperListener;
import com.strongest.savingdata.Adapters.WorkoutItemAdapters.WorkoutItemAdapter;
import com.strongest.savingdata.Adapters.ParentViewAdapters.ExerciseParentViewAdapter;
import com.strongest.savingdata.Adapters.ParentViewAdapters.ParentView;
import com.strongest.savingdata.Adapters.ParentViewAdapters.SetParentViewAdapter;
import com.strongest.savingdata.Adapters.WorkoutAdapter.ItemTouchHelperAdapter;
import com.strongest.savingdata.Adapters.WorkoutAdapter.OnDragListener;
import com.strongest.savingdata.Adapters.WorkoutAdapter.ScrollToPositionListener;
import com.strongest.savingdata.AModels.workoutModel.LayoutManagerAlertdialog;
import com.strongest.savingdata.AModels.workoutModel.OnLayoutManagerDialogPress;
import com.strongest.savingdata.AModels.workoutModel.PLObject;
import com.strongest.savingdata.AModels.workoutModel.PLObject.BodyText;
import com.strongest.savingdata.AModels.workoutModel.PLObject.ExerciseProfile;
import com.strongest.savingdata.AModels.workoutModel.WorkoutLayoutTypes;
import com.strongest.savingdata.Animations.SelectedColorAnimator;
import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.Controllers.OnExerciseInfo;
import com.strongest.savingdata.Controllers.UISetsClickHandler;
import com.strongest.savingdata.Controllers.UiExerciseClickHandler;
import com.strongest.savingdata.Database.Exercise.ExerciseSet;
import com.strongest.savingdata.Handlers.MaterialDialogHandler;
import com.strongest.savingdata.MyViews.CreateCustomBeansView.SingleNumberChooseView;
import com.strongest.savingdata.MyViews.LongClickMenu.LongClickMenuView;
import com.strongest.savingdata.MyViews.WeightKeyBoard.WeightKeyboard;
import com.strongest.savingdata.R;
import com.strongest.savingdata.ViewHolders.ExerciseViewHolder;
import com.strongest.savingdata.ViewHolders.LeanExerciseViewHolder;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyExpandableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        ItemTouchHelperAdapter, WorkoutItemAdapter.ItemAdapter {


    private final String TAG = "aviv";
    private Context context;
    private ArrayList<PLObject> exArray;
    private UiExerciseClickHandler uiExerciseClickHandler;
    private UISetsClickHandler uiSetsClickHandler;
    private OnDragListener onDragListener;
    private ScrollToPositionListener scrollListener;
    private boolean isLeanLayout;
    private OnExerciseInfo onExerciseInfo;
    private LongClickMenuView longClickMenuView;
    private ArrayList<ExerciseProfile> supersets;

    public void setSupersets(ArrayList<ExerciseProfile> supersets) {
        this.supersets = supersets;
    }

    public void setLeanLayout(boolean leanLayout) {
        isLeanLayout = leanLayout;
    }

    public MyExpandableAdapter(ArrayList<PLObject> exArray, Context context) {
        this.context = context;

        this.exArray = exArray;

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
            View v4 = inflater.inflate(R.layout.recycler_view_set, parent, false);
            return new SetsViewHolder(v4);

        } else if (viewType == WorkoutLayoutTypes.IntraSet.ordinal()) {
            View v4 = inflater.inflate(R.layout.recycler_view_intra_set, parent, false);
            return new SetsViewHolder(v4);
        } else if (viewType == WorkoutLayoutTypes.ProgressPLObject.ordinal()) {
            View v5 = inflater.inflate(R.layout.progress_set, parent, false);
            return new ProgressPLObjectViewHolder(v5);

        } else if (viewType == WorkoutLayoutTypes.ExerciseProfile.ordinal()) {
            View v3 = inflater.inflate(R.layout.recycler_view_exercises_left_margin, parent, false);
            return new com.strongest.savingdata.ViewHolders.ExerciseViewHolder(v3, uiExerciseClickHandler);

        } else if (viewType == WorkoutLayoutTypes.IntraExerciseProfile.ordinal()) {

            View v3;
            v3 = inflater.inflate(R.layout.recycler_view_lean_exercise, parent, false);
            return new LeanExerciseViewHolder(v3);

        } else if (viewType == WorkoutLayoutTypes.AddExercise.ordinal()) {
            View v3 = inflater.inflate(R.layout.a_test, parent, false);
            return new AddExerciseViewHolder(v3);
        } else if (viewType == WorkoutLayoutTypes.Method.ordinal()) {
            View v3 = inflater.inflate(R.layout.recycler_view_exercises_dropset_superset, parent, false);
            return new ExerciseViewHolder(v3);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == WorkoutLayoutTypes.WorkoutView.ordinal()) {
            ViewHolderWorkout vh1 = (ViewHolderWorkout) holder;
            configureViewHolder1(vh1, position);
        } else if (holder.getItemViewType() == WorkoutLayoutTypes.BodyView.ordinal()) {
            MuscleViewHolder vh2 = (MuscleViewHolder) holder;
            configureViewHolder2(vh2, position);

        } else if (holder.getItemViewType() == WorkoutLayoutTypes.ExerciseProfile.ordinal()) {
            com.strongest.savingdata.ViewHolders.ExerciseViewHolder vh3 = (com.strongest.savingdata.ViewHolders.ExerciseViewHolder) holder;
            configureViewHolder3(vh3, position);

        } else if (holder.getItemViewType() == WorkoutLayoutTypes.IntraExerciseProfile.ordinal()) {
            LeanExerciseViewHolder leanExerciseViewHolder = (LeanExerciseViewHolder) holder;
            configurateSuperset(leanExerciseViewHolder, position);


        } else if (holder.getItemViewType() == WorkoutLayoutTypes.SetsPLObject.ordinal()) {

            SetsViewHolder vh4 = (SetsViewHolder) holder;
            configurateSets(vh4, position);

        } else if (holder.getItemViewType() == WorkoutLayoutTypes.ProgressPLObject.ordinal()) {


        } else if (holder.getItemViewType() == WorkoutLayoutTypes.AddExercise.ordinal()) {
            AddExerciseViewHolder vh5 = (AddExerciseViewHolder) holder;
            configurateAddExerciseViewHolder(vh5, position);

        } else if (holder.getItemViewType() == WorkoutLayoutTypes.Method.ordinal()) {
            AddExerciseViewHolder vh5 = (AddExerciseViewHolder) holder;
            configurateAddExerciseViewHolder(vh5, position);

        } else if (holder.getItemViewType() == WorkoutLayoutTypes.IntraSet.ordinal()) {
            SetsViewHolder setsVH = (SetsViewHolder) holder;
            configurateSets(setsVH, position);
        }

    }

    private void configurateSuperset(LeanExerciseViewHolder supersetViewHolder, int position) {
        ExerciseProfile ep = (ExerciseProfile) exArray.get(position);

        if (ep.getMuscle() != null) {
            Muscle.MuscleUI mui = Muscle.provideMuscleUI(ep.getMuscle());
            supersetViewHolder.icon.setImageResource(mui.getImage());
        }

        if (ep.getExercise() != null)
            supersetViewHolder.name.setText(ep.getExercise().getName());

        if (ep.getExercise() != null)
            supersetViewHolder.exerciseInfo.setOnClickListener(v -> {
                onExerciseInfo.transitionToExerciseInfo(ep.getExercise());
            });

    }


    @Override
    public int getItemCount() {
        return exArray.size();
    }

    @Override
    public int getItemViewType(int position) {
        WorkoutLayoutTypes type = exArray.get(position).getType();

        return type.ordinal();
    }

    public void configurateAddExerciseViewHolder(AddExerciseViewHolder vh, int position) {
    }

    private void configureViewHolder1(ViewHolderWorkout vh1, final int position) {


        PLObject.WorkoutPLObject workoutPLObject = (PLObject.WorkoutPLObject) exArray.get(position);
        vh1.workoutTv.setText(workoutPLObject.getWorkoutName());
    }

    private void configureViewHolder2(final MuscleViewHolder vh2, final int position) {
        final ExerciseProfile bodyText = (ExerciseProfile) exArray.get(position);
        String title = bodyText.getTitle();
        if (title == null || title.equals("")) {
            title = "This Is A Title";
        }

        vh2.edit.setOnClickListener((View v) -> {
            MaterialDialogHandler.get().defaultBuilder(context, "Edit Title", "CHANGE")
                    .addInput(999,InputType.TYPE_CLASS_TEXT, vh2.bodyTv.getText().toString(), ((dialog, input) -> {
                        bodyText.setTitle(input.toString());
                        vh2.bodyTv.setText(input.toString());
                    }))
                    .buildDialog()
                    .show();
        });

        vh2.bodyTv.setText(title);

        vh2.itemView.setOnLongClickListener(v -> {
            uiExerciseClickHandler.onLongClick(bodyText, vh2);
            return false;
        });

        vh2.itemView.setOnClickListener(v -> {
            if (longClickMenuView.isOn()) {
                uiExerciseClickHandler.onLongClick(bodyText, vh2);
            }
        });
    }

    public void configurateSets(final SetsViewHolder vh, final int position) {

        final PLObject.SetsPLObject setsPLObject = (PLObject.SetsPLObject) exArray.get(position);
        ExerciseSet exerciseSet = null;
        exerciseSet = setsPLObject.getExerciseSet();


        vh.card.setOnClickListener((clickedV) -> {
            if (longClickMenuView.isOn()) {
                uiSetsClickHandler.onSetsLongClick(setsPLObject, vh);
            } else if (uiSetsClickHandler != null) {
                uiSetsClickHandler.onSetsClick(vh, setsPLObject);
            }
        });

        vh.card.setOnLongClickListener(longClicked -> {
            uiSetsClickHandler.onSetsLongClick(setsPLObject, vh);
            return true;
        });
        vh.reps.setText(exerciseSet.getRep());
        if (position != (exArray.size() - 1)) {
            vh.rest.setText(exerciseSet.getRest());
        } else {
            vh.rest.setText("none");
        }
        vh.weight.setText(exerciseSet.getWeight() + "kg");
        vh.set.setText(position + 1 + "");

        if (!exerciseSet.hasSomething()) {
            vh.editContainer.setVisibility(View.VISIBLE);
            vh.dataLayout.setVisibility(View.INVISIBLE);
        } else {
            vh.dataLayout.setVisibility(View.VISIBLE);
            vh.editContainer.setVisibility(View.GONE);

        }

        int color;
        if (longClickMenuView.isOn() && longClickMenuView.objectIsSelected(setsPLObject)) {
            color = ContextCompat.getColor(context, R.color.colorAccent);
        } else {
            color = ContextCompat.getColor(context, R.color.colorPrimary);
        }
        vh.mainLayout.setBackgroundColor(color);


        ParentView.loadParent(context, vh.parentViewContainer)
                .reset()
                .with(new SetParentViewAdapter(supersets, setsPLObject, uiSetsClickHandler, vh, longClickMenuView))
                .make();


        vh.settings.setOnClickListener((menu) -> {

            PopupMenu popupMenu = new PopupMenu(context, vh.settings);
            popupMenu.inflate(R.menu.sets_menu);
            popupMenu.setOnMenuItemClickListener((menuItem) -> {
                if (uiSetsClickHandler != null) {

                    switch (menuItem.getItemId()) {

                        case R.id.add_dropset:
                            uiSetsClickHandler.onAddingIntraSet(setsPLObject, vh.getAdapterPosition());
                            break;
                        case R.id.edit_set:
                            uiSetsClickHandler.onSetsClick(vh, setsPLObject);
                            break;
                        case R.id.duplicate_set:
                            uiSetsClickHandler.onDuplicate(setsPLObject);
                            break;
                    }
                    return true;
                }
                return false;
            });

            popupMenu.show();
        });


    }


    private void configureViewHolder3(final com.strongest.savingdata.ViewHolders.ExerciseViewHolder vh3, final int position) {
        final ExerciseProfile exerciseProfile = (ExerciseProfile) exArray.get(position);

        Muscle.MuscleUI mui = null;

        if (position == 0) {
            vh3.icon.setTransitionName("q1");
        }
        if (exerciseProfile.getMuscle() != null) {
            mui = Muscle.provideMuscleUI(exerciseProfile.getMuscle());
            vh3.icon.setImageResource(mui.getImage());

        } else {
            vh3.icon.setImageResource(android.R.color.transparent);
        }

        vh3.reps.setText("Reps: " + ExerciseProfileStats.getAllRepsString(exerciseProfile));

        if (exerciseProfile.getExercise() != null) {
            vh3.editExerciseTV.setVisibility(View.GONE);
            vh3.name.setVisibility(View.VISIBLE);

            vh3.name.setText(exerciseProfile.getExercise().getName());
        } else {
            vh3.editExerciseTV.setVisibility(View.VISIBLE);
            vh3.name.setVisibility(View.INVISIBLE);
        }

        vh3.name.setTransitionName("a" + position);


        vh3.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (longClickMenuView.isOn()) {
                    uiExerciseClickHandler.onLongClick(exerciseProfile, vh3);
                } else if (uiExerciseClickHandler != null) {

                    if (exerciseProfile.getExercise() == null) {
                        uiExerciseClickHandler.onExerciseEdit(vh3.getAdapterPosition(), exerciseProfile);
                    } else {
                        uiExerciseClickHandler.onExerciseDetails(vh3, exerciseProfile);

                    }
                }

            }
        });

        if (uiExerciseClickHandler != null) {

            vh3.mainLayout.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    uiExerciseClickHandler.onLongClick(exerciseProfile, vh3);
                    return true;
                }
            });
        }

        int color;
        if (longClickMenuView.isOn() && longClickMenuView.objectIsSelected(exerciseProfile)) {
            color = ContextCompat.getColor(context, R.color.colorAccent);
        } else {
            color = ContextCompat.getColor(context, R.color.colorPrimary);
        }
        vh3.mainLayout.setBackgroundColor(color);


        ParentView
                .loadParent(context, vh3.parentContainer)
                .with(new ExerciseParentViewAdapter(vh3, exerciseProfile, uiExerciseClickHandler))
                .reset()
                .make();


        vh3.sets.setText("Sets: " + exerciseProfile.getSets().size());

        vh3.icon.setTransitionName("tag" + position);

        vh3.edit.setOnClickListener((menu) -> {

            PopupMenu popupMenu = new PopupMenu(context, vh3.edit);
            popupMenu.inflate(R.menu.exercise_menu);
            popupMenu.setOnMenuItemClickListener((menuItem) -> {
                if (uiExerciseClickHandler != null) {

                    switch (menuItem.getItemId()) {

                        case R.id.editExercise:
                            uiExerciseClickHandler.onExerciseEdit(vh3.getAdapterPosition(), exerciseProfile);
                            break;
                        case R.id.addSuperset:
                            uiExerciseClickHandler.onAddSuperset(vh3, exerciseProfile);
                            break;

                        case R.id.duplicate_exercise:
                            uiExerciseClickHandler.onDuplicateExercise(exerciseProfile, vh3.getAdapterPosition());
                            break;
                    }
                    return true;
                }
                return false;
            });

            popupMenu.show();
        });


    }

    @Override
    public boolean adapterNotifyDataSetChanged() {
        return false;

    }

    @Override
    public boolean adapterNotifyItemChanged(int pos) {
        notifyItemChanged(pos);
        return true;
    }

    @Override
    public boolean adapterNotifyItemInserted(int positionTo) {
        notifyItemInserted(positionTo);
        return true;
    }

    @Override
    public boolean adapterNotifyItemRemoved(int fromPos) {

        notifyItemRemoved(fromPos);
        return false;
    }

    @Override
    public boolean adapterNotifyItemRangeInserted(int start, int count) {
        notifyItemRangeInserted(start, count);

        return true;
    }

    @Override
    public boolean adapterNotifyItemRangeRemoved(int from, int count) {

        notifyItemRangeRemoved(from, count);
        return false;
    }

    public void setUiExerciseClickHandler(UiExerciseClickHandler uiExerciseClickHandler) {
        this.uiExerciseClickHandler = uiExerciseClickHandler;
    }

    public void setUiSetsClickHandler(UISetsClickHandler uiSetsClickHandler) {
        this.uiSetsClickHandler = uiSetsClickHandler;
    }

    public void setOnExerciseInfo(OnExerciseInfo onExerciseInfo) {
        this.onExerciseInfo = onExerciseInfo;
    }

    public void setLongClickMenuView(LongClickMenuView longClickMenuView) {
        this.longClickMenuView = longClickMenuView;
    }


/*

    public class ExerciseViewHolder extends MyExpandableViewHolder implements ItemTouchHelperListener {


        public TextView muscle;
        public TextView name;
        public TextView sets;
        public TextView reps;
        public TextView method;
        public TextView weight;
        public TextView rest;
        private View itemView;
        public CircleImageView icon;
        public ImageView arrow;
        private ImageView settings;
        public ViewGroup card;
        public ExpandableLayout expandableLayout;
        public EasyFlipView flipView;
        // private ViewGroup dragMenuItem;
        // private ViewGroup supersetMenu;
        private ViewGroup mLayout;
        private TextView tag;
        public TextView comment_tv, dropsetTag;
        public ImageView comment_tag;
        public CircleImageView dragIcon;
        public ImageView drag_iv;
        // public ImageView edit;
        // public CircleImageView editIv;

        public boolean expand;
        private ExerciseViewHolder vh = this;

        @Override
        public View getMainLayout() {
            return null;
        }

        public ExerciseViewHolder(final View itemView) {
            super(itemView);
            arrow = (ImageView) itemView.findViewById(R.id.ex_arrow);
            icon = (CircleImageView) itemView.findViewById(R.id.ex1);
            name = (TextView) itemView.findViewById(R.id.exerciseView_nameTV);
            sets = (TextView) itemView.findViewById(R.id.exerciseView_setsTV);
            reps = (TextView) itemView.findViewById(R.id.exerciseView_repsTV);
            method = (TextView) itemView.findViewById(R.id.exerciseView_methodTV);
            weight = (TextView) itemView.findViewById(R.id.exerciseView_weightTV);
            rest = (TextView) itemView.findViewById(R.id.exerciseView_restTV);
            muscle = (TextView) itemView.findViewById(R.id.exerciseView_muscleTV);
            settings = (ImageView) itemView.findViewById(R.id.ex_choose);
            //more = (ImageView) itemView.findViewById(R.id.ex_more);
            card = (ViewGroup) itemView.findViewById(R.id.list_view_exercise_lay1);
            expandableLayout = (ExpandableLayout) itemView.findViewById(R.id.exercise_menu_expandlayout);
            //    dragMenuItem = (ViewGroup) itemView.findViewById(R.id.recycler_view_exercise_menu_drag);
            //     supersetMenu = (ViewGroup) itemView.findViewById(R.id.exercise_menu_superset);
            mLayout = (ViewGroup) itemView.findViewById(R.id.exercise_main_layout);
            tag = (TextView) itemView.findViewById(R.id.big_tag_tv);
            dragLayout = (View) itemView.findViewById(R.id.recyclerview_exercise_drag_layout);
            flipView = (EasyFlipView) itemView.findViewById(R.id.recyclerview_exercise_flipview);
            dragIcon = (CircleImageView) itemView.findViewById(R.id.recyclerview_exercise_drag_icon);
            comment_tag = (ImageView) itemView.findViewById(R.id.ex_icon_tag);
            comment_tv = (TextView) itemView.findViewById(R.id.ex_comment_tv);
            dropsetTag = (TextView) itemView.findViewById(R.id.recycler_view_exercise_intraset_tag);
            // editIv = (CircleImageView) itemView.findViewById(R.id.recycler_view_edit);
            drag_iv = itemView.findViewById(R.id.drag_iv);

            */

    /**
     * drag should be on onBind
     *//*

     */
/*drag_iv.setOnTouchListener((view, motionEvent) -> {
                if (onDragListener != null)
                    onDragListener.startDrag(vh);

                return false;
            });*//*

            this.itemView = itemView;


        }

        @Override
        public void onItemSelected() {
        }

        @Override
        public void onItemClear() {

        }
    }
*/


    public class MuscleViewHolder extends MyExpandableViewHolder implements OnLayoutManagerDialogPress {
        private TextView bodyTv;
        //   private ImageView drag;
        //  private EasyFlipView easyFlipView;
        private View dummy;
        public ImageView edit;
        public MuscleViewHolder _this = this;

        @BindView(R.id.body_parts_linear_layout)
        public ViewGroup mainLayout;
        //  public ImageView delete;

        @Override
        public View getMainLayout() {
            return mainLayout;
        }

        public MuscleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            bodyTv = itemView.findViewById(R.id.recycler_view_body_parts_TV);
            //  drag = (ImageView) itemView.findViewById(R.id.body_part_drag);
            //   easyFlipView = (EasyFlipView) itemView.findViewById(R.id.body_part_edit);
            edit = (ImageView) itemView.findViewById(R.id.body_view_edit_iv);
            dragLayout = (View) itemView.findViewById(R.id.recyclerview_exercise_drag_layout);
          /*  edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutManagerAlertdialog.getInputAlertDialog(context, MuscleViewHolder.this, bodyTv.getText().toString(), getAdapterPosition());
                }
            });*/


           /* itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    uiExerciseClickHandler.onLongClick(exArray.get(getAdapterPosition()),
                            MuscleViewHolder.this);
                    return true;
                }
            });*/

            mainLayout.setOnClickListener((v) -> {
                if (longClickMenuView.isOn()) {
                    uiExerciseClickHandler.onLongClick(exArray.get(getAdapterPosition()),
                            MuscleViewHolder.this);
                }
            });

        }


        @Override
        public void onLMDialogOkPressed(int viewHolderPosition) {
        }

        @Override
        public void onLMDialogOkPressed(String input, int position) {
            bodyTv.setText(input);
            ((BodyText) exArray.get(position)).setTitle(input);

            //TODO: have this save the program, or else it will not save!!

        }

        @Override
        public void onItemSelected() {

        }

        @Override
        public void onItemClear() {

        }
    }

    public class SetsViewHolder extends MyExpandableViewHolder {
        //    private FloatingActionButton fab;
        public TextView reps, rest, weight, set;
        public ViewGroup card;
        // public TextView tag;
        public TextView supersetTag;
        private SetsViewHolder vh = this;
        private ImageView settings;
        //public ImageView childIcon;
        @BindView(R.id.sets_weight_icon)
        public ImageView weightIcon;
        @BindView(R.id.sets_rest_icon)
        public ImageView restIcon;
        @BindView(R.id.sets_reps_icon)
        public ImageView repsIcon;
        @BindView(R.id.sets_main_layout)
        public ViewGroup mainLayout;

        @BindView(R.id.reps_rest_weight_layout)
        ViewGroup dataLayout;

        @BindView(R.id.parent_view_container)
        public LinearLayout parentViewContainer;

        @BindView(R.id.edit_layout)
        public ViewGroup editContainer;

        @Override
        public View getMainLayout() {
            return mainLayout;
        }

        public SetsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            reps = (TextView) itemView.findViewById(R.id.exerciseView_repsTV);
            rest = (TextView) itemView.findViewById(R.id.exerciseView_restTV);
            weight = (TextView) itemView.findViewById(R.id.exerciseView_weightTV);
            set = (TextView) itemView.findViewById(R.id.recycler_view_exercise_details_sets_tv);
//            fab = (FloatingActionButton) itemView.findViewById(R.id.recycler_view_exercise_details_fab);
            card = (ViewGroup) itemView.findViewById(R.id.recycler_view_set_card);
            // tag = (TextView) itemView.findViewById(R.id.intra_set_tv);
            settings = (ImageView) itemView.findViewById(R.id.sets_settings);
            dragLayout = (View) itemView.findViewById(R.id.recyclerview_exercise_drag_layout);
            supersetTag = (TextView) itemView.findViewById(R.id.big_tag_tv);
            //childIcon = itemView.findViewById(R.id.child_iv);


        }

        @Override
        public void onItemSelected() {

        }

        @Override
        public void onItemClear() {

        }
    }

    private class ProgressPLObjectViewHolder extends RecyclerView.ViewHolder {

        private SingleNumberChooseView singleNumberChooseView_rep, singleNumberChooseView_rest, singleNumberChooseView_weight;
        private ImageView calcImage;
        private ExpandableLayout expandableLayout;
        private WeightKeyboard weightKeyboard;

        public ProgressPLObjectViewHolder(View itemView) {
            super(itemView);

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


    public static abstract class ExerciseStatsViewHolder extends MyExpandableViewHolder {

        @Override
        public View getMainLayout() {
            return null;
        }

        public ExerciseStatsViewHolder(View itemView) {
            super(itemView);
        }
    }


    public static abstract class MyExpandableViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperListener {

        public View dragLayout;
        public SelectedColorAnimator.AnimatorHolder animatorHolder;

        public abstract View getMainLayout();

        public MyExpandableViewHolder(View itemView) {
            super(itemView);
            animatorHolder = new SelectedColorAnimator.AnimatorHolder(itemView.getSolidColor());
        }
    }

    @Override
    public boolean onItemMove(RecyclerView.ViewHolder fromVh, RecyclerView.ViewHolder toVh) {
        int fromPosition = fromVh.getAdapterPosition();
        int toPosition = toVh.getAdapterPosition();

        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(exArray, i, i + 1);

            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(exArray, i, i - 1);

            }
        }
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


    public void setScrollListener(ScrollToPositionListener scrollListener) {
        this.scrollListener = scrollListener;
    }

}


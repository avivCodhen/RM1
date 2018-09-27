package com.strongest.savingdata.MyViews.WorkoutView;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;
import com.strongest.savingdata.AModels.workoutModel.WorkoutsModel.Actions;
import com.strongest.savingdata.Animations.MyJavaAnimator;
import com.strongest.savingdata.Controllers.Architecture;
import com.strongest.savingdata.R;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

/**
 * Created by Cohen on 2/17/2018.
 */

public class ProgramToolsView extends LinearLayout {

    public enum Action {
        NewExercise, NewDivider, NewWorkout, Share, Advanced
    }

    private Context context;


    private ExpandableLayout openProgramToolsEL /*, showExpandedToolsButton*/;


    FloatingActionButton fab;
    FrameLayout clickSpaceView;

    private RecyclerView mRecyclerview;
    private ArrayList<ProgramButton> buttons;

    private WorkoutViewModes mWorkoutViewModes;

    private Architecture.view.ProgramTools listener;

    public ProgramToolsView(Context context) {
        super(context);
        this.context = context;
    }

    public ProgramToolsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void initDropMenu() {

        DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(context, fab);

        for (ProgramButton pbtn : buttons) {
            droppyBuilder.addMenuItem(new DroppyMenuItem(pbtn.tv_name, R.drawable.plus_gray_24px))
                    .addSeparator();
        }

// Set Callback handler
        droppyBuilder.setOnClick((v, id) -> Log.d("aviv", "call: " + id));

        DroppyMenuPopup droppyMenu = droppyBuilder.build();

    }

    public void instantiate(FloatingActionButton programToolsBtn, Architecture.view.ProgramTools listener) {
        this.fab = programToolsBtn;
        this.listener = listener;
        initViews();
    }

    private void initViews() {
        inflate(context, R.layout.layout_program_tools_view, this);
        initProgramToolsViews();
        clickSpaceView.setOnClickListener(v->{
            fab.setClickable(false);
            new Handler()
                    .postDelayed(() -> fab.setClickable(true),300);
            openProgramToolsEL.toggle();
            if (openProgramToolsEL.isExpanded()) {
                setVisibility(VISIBLE);
                fab.setImageResource(R.drawable.cancel_48px_white);

            } else {
                fab.setImageResource(R.drawable.edit_48px);
                display(GONE);
                //MyJavaAnimator.rotateView(programToolsBtn, 315, 360);

            }
        });
        mWorkoutViewModes = new WorkoutViewModes();

    }

    public void display(final int visi) {
        new Handler().postDelayed(() -> setVisibility(visi), 200);
    }

    private void initProgramToolsViews() {
        buttons = new ArrayList<>();
        clickSpaceView = findViewById(R.id.clickSpace);
        int green = ContextCompat.getColor(context, R.color.green_dark);
        int red = ContextCompat.getColor(context, R.color.red);
        int exerciseIcon = R.drawable.icon_exercise;
        int workoutIcon = R.drawable.icon_benchpress;
        int settingsIcon = R.drawable.icon_settings_white;
        int titleIcon = R.drawable.icon_text;
        buttons.add(new ProgramButton(Actions.NewExercise, "New Exercise", exerciseIcon));
        buttons.add(new ProgramButton(Actions.NewDivider, "New Title", titleIcon));
        buttons.add(new ProgramButton(Actions.NewWorkout, "New Workout", workoutIcon));
        buttons.add(new ProgramButton(Actions.Advanced, "Advanced", settingsIcon));
        mRecyclerview = (RecyclerView) findViewById(R.id.program_tools_view_recyclerview);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(context);
        ProgramToolsAdapter adapter = new ProgramToolsAdapter();
        mRecyclerview.setLayoutManager(lm);
        mRecyclerview.setAdapter(adapter);
        openProgramToolsEL = (ExpandableLayout) findViewById(R.id.workout_view_program_tools_expandable);
        openProgramToolsEL.setOrientation(ExpandableLayout.HORIZONTAL);
    }

    //this function expands the expandable layout
    //it also takes into consideration the
    public void expand() {

        openProgramToolsEL.toggle();
        if (openProgramToolsEL.isExpanded()) {
            setVisibility(VISIBLE);
            fab.setImageResource(R.drawable.cancel_48px_white);

          /*  int cx = (getLeft() + getRight()) / 2;
            int y = getTop() / 2;*/
            //MyJavaAnimator.animateRevealShowParams(this, true,R.color.colorAccent,cx, y, null);
            //MyJavaAnimator.rotateView(programToolsBtn, 360, 315);


        } else {
            fab.setImageResource(R.drawable.edit_48px);
            display(GONE);
            //MyJavaAnimator.rotateView(programToolsBtn, 315, 360);
        }

    }


    public WorkoutViewModes getmWorkoutViewModes() {
        return mWorkoutViewModes;
    }

    public void setProgramToolsBtn(FloatingActionButton view) {
        view.setOnClickListener(v -> {
            if (fab != null)
                expand();
        });
        this.fab = view;
    }

    public class WorkoutViewModes {

        private boolean edit;
        private boolean progress;
        private boolean none;
        private boolean showAnimation;

        public WorkoutViewModes() {

        }

        public boolean isShowAnimation() {
            return showAnimation;
        }

        public void setShowAnimation(boolean showAnimation) {
            this.showAnimation = showAnimation;
        }

        public boolean isEdit() {
            return edit;
        }

        public void setEdit(boolean edit) {
            this.edit = edit;
        }

        public boolean isProgress() {
            return progress;
        }

        public void setProgress(boolean progress) {
            this.progress = progress;
        }

        public boolean isNone() {
            return none;
        }

        public void setNone(boolean none) {
            this.none = none;
        }
    }

    private class ProgramToolsAdapter extends RecyclerView.Adapter<ProgramToolsAdapter.ViewHolder> {

        @Override
        public ProgramToolsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.recycler_view_program_menu_button, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ProgramToolsAdapter.ViewHolder holder, int position) {
            final ProgramButton pBtn = buttons.get(position);
            holder.imageView.setImageResource(buttons.get(position).image);
            //holder.ImageView.setCircleBackgroundColor(buttons.get(position).color);
            holder.tv.setText(buttons.get(position).tv_name);
            holder.itemView.setOnClickListener(v -> listener.onProgramToolsAction(pBtn.type));
        }

        @Override
        public int getItemCount() {
            return buttons.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView imageView;
            private TextView tv;

            public ViewHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.program_menu_image);
                tv = (TextView) itemView.findViewById(R.id.program_menu_tv);
            }
        }
    }

    class ProgramButton {
        String tv_name;
        Actions type;

        int image;

        ProgramButton(Actions type, String tv_name, int image) {

            this.type = type;
            this.tv_name = tv_name;
            this.image = image;
        }
    }

}

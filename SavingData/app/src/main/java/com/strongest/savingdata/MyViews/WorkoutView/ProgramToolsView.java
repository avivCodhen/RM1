package com.strongest.savingdata.MyViews.WorkoutView;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
        NewExercise, NewDivider, NewWorkout, Advanced
    }

    private Context context;


    private ExpandableLayout openProgramToolsEL /*, showExpandedToolsButton*/;


    View programToolsBtn;

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

        DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(context, programToolsBtn);

        for (ProgramButton pbtn : buttons) {
            droppyBuilder.addMenuItem(new DroppyMenuItem(pbtn.tv_name, R.drawable.plus_gray_24px))
                    .addSeparator();
        }

// Set Callback handler
        droppyBuilder.setOnClick((v, id) -> Log.d("aviv", "call: " + id));

        DroppyMenuPopup droppyMenu = droppyBuilder.build();

    }

    public void instantiate(View programToolsBtn, Architecture.view.ProgramTools listener) {
        this.programToolsBtn = programToolsBtn;
        this.listener = listener;
        initViews();
    }

    private void initViews() {
        inflate(context, R.layout.layout_program_tools_view, this);
        initProgramToolsViews();
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    openProgramToolsEL.toggle();
                    if (openProgramToolsEL.isExpanded()) {
                        setVisibility(VISIBLE);

                    } else {
                        setVisibility(GONE);
                        MyJavaAnimator.rotateView(programToolsBtn, 315, 360);

                    }
                    return false;
                }

                return false;
            }
        });
        mWorkoutViewModes = new WorkoutViewModes();

    }

    public void display(final int visi) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setVisibility(visi);
            }
        }, 200);
    }

    private void initProgramToolsViews() {
        buttons = new ArrayList<>();
        int green = ContextCompat.getColor(context, R.color.green_dark);
        int red = ContextCompat.getColor(context, R.color.red);
        int plusIcon = R.drawable.plus_gray_24px;
        buttons.add(new ProgramButton(green, Actions.NewExercise, "New Exercise", plusIcon));
        buttons.add(new ProgramButton(green, Actions.NewDivider, "New Divider", plusIcon));
        buttons.add(new ProgramButton(green, Actions.NewWorkout, "New Workout", plusIcon));
        //  buttons.add(new ProgramButton(red, DELETE_WORKOUT, "Workout", R.drawable.minus_white_24px));
        buttons.add(new ProgramButton(Color.WHITE, Actions.Advanced, "Advanced", R.drawable.settings_24px_gray));
        mRecyclerview = (RecyclerView) findViewById(R.id.program_tools_view_recyclerview);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(context);
        ProgramToolsAdapter adapter = new ProgramToolsAdapter();
        mRecyclerview.setLayoutManager(lm);
        mRecyclerview.setAdapter(adapter);
        openProgramToolsEL = (ExpandableLayout) findViewById(R.id.workout_view_program_tools_expandable);
    }

    //this function expands the expandable layout
    //it also takes into consideration the
    public void expand() {
        openProgramToolsEL.toggle();
        if (openProgramToolsEL.isExpanded()) {
            setVisibility(VISIBLE);
            MyJavaAnimator.rotateView(programToolsBtn, 360, 315);


        } else {
            display(GONE);
            MyJavaAnimator.rotateView(programToolsBtn, 315, 360);
        }

    }


    public WorkoutViewModes getmWorkoutViewModes() {
        return mWorkoutViewModes;
    }

    public void setProgramToolsBtn(View programToolsBtn) {
        this.programToolsBtn = programToolsBtn;
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
        int color;
        int image;

        ProgramButton(int color, Actions type, String tv_name, int image) {

            this.color = color;
            this.type = type;
            this.tv_name = tv_name;
            this.image = image;
        }
    }

}

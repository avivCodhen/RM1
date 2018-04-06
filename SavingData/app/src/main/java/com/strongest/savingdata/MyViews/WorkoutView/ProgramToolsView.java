package com.strongest.savingdata.MyViews.WorkoutView;

import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.strongest.savingdata.AlgorithmLayout.LayoutManager;
import com.strongest.savingdata.AlgorithmLayout.LayoutManagerAlertdialog;
import com.strongest.savingdata.Fragments.ProgramSettingsFragment;
import com.strongest.savingdata.MyViews.WorkoutView.Choose.ChooseDialogFragment;
import com.strongest.savingdata.R;
import com.strongest.savingdata.Utils.MyUtils;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.strongest.savingdata.AlgorithmLayout.LayoutManager.*;

/**
 * Created by Cohen on 2/17/2018.
 */

public class ProgramToolsView extends LinearLayout {

    private android.support.v4.app.FragmentManager fragmentManager;
    private Context context;


    private View openProgramToolsIV;
    private ExpandableLayout openProgramToolsEL /*, showExpandedToolsButton*/;
    private View addExercise;
    private View addExerciseSplit;
    private View attachSuperset;
    private View attachDropset;
    private View addWorkout;
    private View deleteWorkout;
    private View drawDivider;

    private RecyclerView mRecyclerview;
    private ArrayList<ProgramButton> buttons;

    private WorkoutViewModes mWorkoutViewModes;

    private OnProgramToolsActionListener onProgramToolsActionListener;
    private boolean expandedToolsButtonOn;
    private boolean programToolsOn;
    private LayoutManager layoutManager;

    public ProgramToolsView(Context context) {
        super(context);
        this.context = context;
        initViews();
    }

    public ProgramToolsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initViews();
    }

    private void initViews() {
        inflate(context, R.layout.layout_program_tools_view, this);
        initProgramToolsViews();

       setOnTouchListener(new OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {
               if(event.getAction() == MotionEvent.ACTION_DOWN){
                   openProgramToolsEL.toggle();
                   if (openProgramToolsEL.isExpanded()) {
                       setVisibility(VISIBLE);

                   } else {
                       setVisibility(GONE);
                       //display(GONE);

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
        int plusIcon = R.drawable.plus_16px;
        buttons.add(new ProgramButton(green, NEW_EXERCISE, "Exercise", plusIcon));
        buttons.add(new ProgramButton(green, DRAW_DIVIDER, "Divider", plusIcon));
        buttons.add(new ProgramButton(green, NEW_WORKOUT, "Workout", plusIcon));
      //  buttons.add(new ProgramButton(red, DELETE_WORKOUT, "Workout", R.drawable.minus_white_24px));
        buttons.add(new ProgramButton(Color.WHITE, "advanced", "Advanced", R.drawable.settings_24px_gray));
        mRecyclerview = (RecyclerView) findViewById(R.id.program_tools_view_recyclerview);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(context);
        ProgramToolsAdapter adapter = new ProgramToolsAdapter();
        mRecyclerview.setLayoutManager(lm);
        mRecyclerview.setAdapter(adapter);
        openProgramToolsEL = (ExpandableLayout) findViewById(R.id.workout_view_program_tools_expandable);
    }

    public void toggleMode(boolean changeMode) {
        if (changeMode) {
            if (mWorkoutViewModes.isEdit()) {
                layoutManager.saveLayoutToDataBase(true);
                mWorkoutViewModes.setEdit(false);
            } else {
                mWorkoutViewModes.setEdit(true);

            }
        }


    }


    public void setOnProgramToolsActionListener(OnProgramToolsActionListener onProgramToolsActionListener) {
        this.onProgramToolsActionListener = onProgramToolsActionListener;
    }

    public void expand() {
        openProgramToolsEL.toggle();
        if (openProgramToolsEL.isExpanded()) {
            setVisibility(VISIBLE);


        } else {
            //setVisibility(GONE);
            display(GONE);

        }

    }


    public WorkoutViewModes getmWorkoutViewModes() {
        return mWorkoutViewModes;
    }

    public void setLayoutManager(LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public void setFragmentManager(android.support.v4.app.FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
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
            holder.circleImageView.setImageResource(buttons.get(position).image);
            holder.circleImageView.setCircleBackgroundColor(buttons.get(position).color);
            holder.tv.setText(buttons.get(position).tv_name);
            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pBtn.type.equals("advanced")) {
                        ProgramSettingsFragment f = ProgramSettingsFragment.getInstance(onProgramToolsActionListener);
                        fragmentManager.beginTransaction()
                                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                                .replace(R.id.activity_home_framelayout, f, "unique")
                                .addToBackStack("unique")
                                .commit();

                        MyUtils.Interface.disableClick(holder.itemView, 1000);

                    }

                    else{
                            onProgramToolsActionListener.onProgramToolsAction(pBtn.type, null);
                        }

                }
            });
        }

        @Override
        public int getItemCount() {
            return buttons.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private CircleImageView circleImageView;
            private TextView tv;

            public ViewHolder(View itemView) {
                super(itemView);
                circleImageView = (CircleImageView) itemView.findViewById(R.id.program_menu_image);
                tv = (TextView) itemView.findViewById(R.id.program_menu_tv);
            }
        }
    }

    class ProgramButton {
        String tv_name;
        String type;
        int color;
        int image;

        ProgramButton(int color, String type, String tv_name, int image) {

            this.color = color;
            this.type = type;
            this.tv_name = tv_name;
            this.image = image;
        }
    }

}

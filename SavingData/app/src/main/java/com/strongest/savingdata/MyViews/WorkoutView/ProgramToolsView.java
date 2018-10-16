package com.strongest.savingdata.MyViews.WorkoutView;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

   /* private TextView usernameTV;
    private TextView emailTV;
    private Button logInBtn;
    private ImageView logoutIV;
*/

    public boolean isOpen() {
        return openProgramToolsEL.isExpanded();
    }


    public enum Action {
        NewExercise, NewDivider, NewWorkout, Share, Advanced,
    }

    private Context context;


    private ExpandableLayout openProgramToolsEL /*, showExpandedToolsButton*/;


    View view;
    View include;
    View arrowIV;
    //FrameLayout clickSpaceView;
    //Button shareProgramBtn;

    private RecyclerView mRecyclerview;
   // private RecyclerView mRecyclerview2;
    private ArrayList<ProgramButton> buttons;
    ArrayList<ProgramButton> menuButtonList;

    ProgramToolsAdapter menuListAdapter;


    private Architecture.view.ProgramTools listener;

    private boolean isOpen;


    public ProgramToolsView(Context context) {
        super(context);
        this.context = context;
    }

    public ProgramToolsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void initDropMenu() {

        DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(context, view);

        for (ProgramButton pbtn : buttons) {
            droppyBuilder.addMenuItem(new DroppyMenuItem(pbtn.tv_name, R.drawable.plus_gray_24px))
                    .addSeparator();
        }

// Set Callback handler
        droppyBuilder.setOnClick((v, id) -> Log.d("aviv", "call: " + id));

        DroppyMenuPopup droppyMenu = droppyBuilder.build();

    }

    public void instantiate(Architecture.view.ProgramTools listener) {
        this.listener = listener;
        initViews();
    }

    private void initViews() {
        inflate(context, R.layout.layout_program_tools_view, this);
        initProgramToolsViews();
       /* clickSpaceView.setOnClickListener(v -> {
            view.setClickable(false);
            new Handler()
                    .postDelayed(() -> view.setClickable(true), 300);
            openProgramToolsEL.toggle();
            if (openProgramToolsEL.isExpanded()) {
                setVisibility(VISIBLE);
                MyJavaAnimator.rotateView(view, 360, 315);

            } else {
                display(GONE);
                MyJavaAnimator.rotateView(view, 315, 360);

            }
        });*/

    }

    public void display(final int visi) {
        new Handler().postDelayed(() -> setVisibility(visi), 200);
    }

    private void initProgramToolsViews() {
        buttons = new ArrayList<>();
        //clickSpaceView = findViewById(R.id.clickSpace);
        int green = ContextCompat.getColor(context, R.color.green_dark);
        int red = ContextCompat.getColor(context, R.color.red);
        int exerciseIcon = R.drawable.icon_exercise;
        int workoutIcon = R.drawable.icon_benchpress;
        int settingsIcon = R.drawable.icon_settings_white;
        int titleIcon = R.drawable.icon_text;
        int plusIcon = R.drawable.plus_coloraccent_48px;
        //buttons.add(new ProgramButton(Actions.NewExercise, "New Exercise", plusIcon, true));
        buttons.add(new ProgramButton(Actions.NewDivider, "Add New Title", plusIcon, true));
        buttons.add(new ProgramButton(Actions.NewWorkout, "Add New Workout", plusIcon, true));
        //buttons.add(new ProgramButton(Actions.Advanced, "Advanced", plusIcon, true));
        mRecyclerview = (RecyclerView) findViewById(R.id.program_tools_view_recyclerview);
       // mRecyclerview2 = findViewById(R.id.programtoolsview_recycler2);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(context);
       // RecyclerView.LayoutManager lm2 = new LinearLayoutManager(context);
        ProgramToolsAdapter adapter = new ProgramToolsAdapter(buttons);
        mRecyclerview.setLayoutManager(lm);
        //mRecyclerview2.setLayoutManager(lm2);


        mRecyclerview.setAdapter(adapter);


        menuButtonList = new ArrayList<>();
        int folder = R.drawable.icon_folder;
        int share = R.drawable.icon_share;
        int newDoc = R.drawable.icon_new_document;

        menuButtonList.add(new ProgramButton(Actions.MyProgram, "MyPrograms", folder, false));
        menuButtonList.add(new ProgramButton(Actions.CustomExercise, "Custom Exercise", newDoc, false));
        menuButtonList.add(new ProgramButton(Actions.Share, "Share Program", share, true));

       // menuListAdapter = new ProgramToolsAdapter(menuButtonList);
       // mRecyclerview2.setAdapter(menuListAdapter);

        openProgramToolsEL = (ExpandableLayout) findViewById(R.id.workout_view_program_tools_expandable);
        openProgramToolsEL.setOrientation(ExpandableLayout.VERTICAL);

       /* usernameTV = findViewById(R.id.usernameTV);
        emailTV = findViewById(R.id.emailTV);
        logInBtn = findViewById(R.id.programtoolsview_login);
        logoutIV = findViewById(R.id.programtoolsview_logoutIV);

*/
        /*shareProgramBtn = findViewById(R.id.share_program_programtoolsview);

        shareProgramBtn.setOnClickListener(shareView->{
            listener.onProgramToolsAction(Actions.Share);
        });*/

        include = findViewById(R.id.programtoolsview_include);
        include.setOnClickListener(includeView->{
            listener.onProgramToolsAction(new ProgramButton(Actions.NewExercise, "Add New Exercise", plusIcon, true));
        });
        arrowIV = findViewById(R.id.arrowIV);
        arrowIV.setOnClickListener(arrowView-> expand());
    }

    //this function expands the expandable layout
    //it also takes into consideration the
    public void expand() {

        openProgramToolsEL.toggle();
        if (openProgramToolsEL.isExpanded()) {
            //setVisibility(VISIBLE);
            MyJavaAnimator.rotateView(arrowIV, 360, 180);


        } else {
            //close();
            MyJavaAnimator.rotateView(arrowIV, 180, 360);
        }

    }

    /*public void close() {
        openProgramToolsEL.collapse();
        MyJavaAnimator.rotateView(view, 315, 360);
        display(GONE);
    }*/



    public void updateMenuAlertCount(Actions a, int count) {
        for (ProgramButton p : menuButtonList) {
            if (p.type == a) {
                p.alertCount = count;
            }
        }
        menuListAdapter.notifyDataSetChanged();
    }



    public void setProgramToolsBtn(View view) {
        view.setOnClickListener(v -> {
            if (this.view != null)
                expand();
        });
        this.view = view;
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

        ArrayList<ProgramButton> buttons;

        public ProgramToolsAdapter(ArrayList<ProgramButton> buttons) {
            this.buttons = buttons;
        }

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
            holder.itemView.setOnClickListener(v -> listener.onProgramToolsAction(pBtn));
           /* if (pBtn.alertCount != 0) {
                holder.wrapper.setVisibility(VISIBLE);
                holder.alertTV.setText(pBtn.alertCount + "");
            }*/
        }

        @Override
        public int getItemCount() {
            return buttons.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView imageView;
            private TextView tv, alertTV;
            private ViewGroup wrapper;

            public ViewHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.program_menu_image);
                tv = (TextView) itemView.findViewById(R.id.program_menu_tv);
                wrapper = itemView.findViewById(R.id.alert_wrapper);
                alertTV = itemView.findViewById(R.id.button_alert);
            }
        }
    }

    public class ProgramButton {
        public String tv_name;
        public Actions type;
        public int alertCount;
        public boolean requiresLogin;
        public boolean requiresProgram;

        int image;

        ProgramButton(Actions type, String tv_name, int image, boolean requiresProgram) {

            this.type = type;
            this.tv_name = tv_name;
            this.image = image;
            this.requiresProgram = requiresProgram;
        }
    }

}

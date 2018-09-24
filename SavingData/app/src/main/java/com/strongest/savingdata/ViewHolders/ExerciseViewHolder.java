package com.strongest.savingdata.ViewHolders;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.Controllers.UiExerciseClickHandler;
import com.strongest.savingdata.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ExerciseViewHolder extends MyExpandableAdapter.MyExpandableViewHolder {

    public ImageView drag_iv;

    @BindView(R.id.list_view_exercise_lay1)
    public ViewGroup card;

    /*@BindView(R.id.ex_choose)
    public ImageView settings;
*/
 /*   @BindView(R.id.exerciseView_muscleTV)
    public TextView muscle;*/

    @BindView(R.id.exerciseView_repsTV)
    public TextView reps;

    @BindView(R.id.exercise_set_tv)
    public TextView sets;

    @BindView(R.id.exerciseView_nameTV)
    public TextView name;


    @BindView(R.id.ex1)
    public CircleImageView icon;

    @BindView(R.id.exercise_main_viewgroup_layout)
    public ViewGroup mainLayout;


    @BindView(R.id.recyclerview_exercise_drag_layout)
    public View dl;

    @BindView(R.id.parent_view_container)
    public LinearLayout parentContainer;

    @BindView(R.id.add_superset)
    public Button addSuperset;

    @BindView(R.id.exercise_edit_btn)
    public ImageView edit;


    @BindView(R.id.exerciseView_edit_TV)
    public TextView editExerciseTV;

    @Override
    public View getMainLayout() {
        return mainLayout;
    }

    public UiExerciseClickHandler uiExerciseClickHandler;

    public void setUiExerciseClickHandler(UiExerciseClickHandler uiExerciseClickHandler) {
        this.uiExerciseClickHandler = uiExerciseClickHandler;
    }

    public ExerciseViewHolder(View itemView, UiExerciseClickHandler uiExerciseClickHandler) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.uiExerciseClickHandler = uiExerciseClickHandler;


    }

    public ExerciseViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        dragLayout = dl;
        drag_iv = itemView.findViewById(R.id.drag_iv);


    }

    @Override
    public void onItemSelected() {
        int color = ContextCompat.getColor(parentContainer.getContext(), R.color.background_color);
        this.animatorHolder.makeSelectedColor(mainLayout, mainLayout.getSolidColor());
    }

    @Override
    public void onItemClear() {
        this.animatorHolder.revertPrevColor();
    }
}

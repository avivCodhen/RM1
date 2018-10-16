package com.strongest.savingdata.ViewHolders;

import android.view.View;
import android.widget.TextView;

import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class LeanExerciseViewHolder extends MyExpandableAdapter.MyExpandableViewHolder{


    @BindView(R.id.ex1)
    public CircleImageView icon;

    @BindView(R.id.exerciseView_nameTV)
    public TextView name;

 /*   @BindView(R.id.exercise_info)
    public View exerciseInfo;
*/


    public LeanExerciseViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public View getMainLayout() {
        return null;
    }

    @Override
    public void onItemSelected() {

    }

    @Override
    public void onItemClear() {

    }
}

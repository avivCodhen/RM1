package com.strongest.savingdata.ViewHolders;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class SupersetViewHolder extends MyExpandableAdapter.MyExpandableViewHolder {

    public ImageView drag_iv;

    @BindView(R.id.exerciseView_nameTV)
    public TextView name;


    @BindView(R.id.ex1)
    public CircleImageView icon;

    @BindView(R.id.exercise_edit_btn)

    public ImageView edit;


    @BindView(R.id.delete)
    public ImageView delete;

    @Override
    public View getMainLayout() {
        return null;
    }

    public SupersetViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onItemSelected() {

    }

    @Override
    public void onItemClear() {

    }
}

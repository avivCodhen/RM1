package com.strongest.savingdata.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.strongest.savingdata.Activities.HomeActivity;
import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.Fragments.ExerciseChooseFragment;
import com.strongest.savingdata.R;
import com.strongest.savingdata.createProgramFragments.CreateProgram.OnMuscleClickListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GridViewMusclesAdapter extends BaseAdapter{

    ArrayList<GridViewMusclesAdapter.MusclesContentHolder> list;
    ArrayList<Muscle> muscles;
    private OnGridViewMuscleAdapterClickListener onGridViewMuscleAdapterClickListener;
    private int minViewHeight;
    private Context context;


    public GridViewMusclesAdapter(int minViewHeight, Context context, DataManager dataManager,  OnGridViewMuscleAdapterClickListener onGridViewMuscleAdapterClickListener) {
        this.minViewHeight = minViewHeight;
        this.context = context;
        muscles = dataManager.getMuscleDataManager().getAllMuscles();
        this.onGridViewMuscleAdapterClickListener = onGridViewMuscleAdapterClickListener;
        list = new ArrayList<>();
        for (int i = 0; i < muscles.size(); i++) {
            Muscle.MuscleUI mui = Muscle.provideMuscleUI(muscles.get(i));
            if (mui != null) {
                list.add(new GridViewMusclesAdapter.MusclesContentHolder(mui.getImage(), muscles.get(i).getMuscle_display(), muscles.get(i)));
            }
        }

    }

    public class MusclesContentHolder {

        public String text;
        public int icon;
        public Muscle m;

        MusclesContentHolder(int icon, String text, Muscle m) {

            this.icon = icon;
            this.text = text;
            this.m = m;
        }

    }

    class ViewHolder {
        TextView tv;
        ViewGroup cv;
        CircleImageView civ;

        ViewHolder(View v, int position) {
            civ = (CircleImageView) v.findViewById(R.id.muscle_card_civ);
            tv = (TextView) v.findViewById(R.id.muscle_card_tv);
            cv = (ViewGroup) v.findViewById(R.id.recycler_view_muscle_card_layout);

        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater li = LayoutInflater.from(context);
        View v = convertView;
        GridViewMusclesAdapter.ViewHolder holder = null;
        if (v == null) {
            v = li.inflate(R.layout.recycler_view_muscle_card, parent, false);
            v.setMinimumHeight(minViewHeight/4);
            holder = new GridViewMusclesAdapter.ViewHolder(v, position);
            v.setTag(holder);
        } else {
            holder = (GridViewMusclesAdapter.ViewHolder) v.getTag();
        }

        holder.tv.setText(list.get(position).text);
        holder.civ.setImageResource(list.get(position).icon);
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGridViewMuscleAdapterClickListener.onMuscleChange(list.get(position));
                onGridViewMuscleAdapterClickListener.collapseExpandableLayout();
            }
        });

        return v;
    }
}

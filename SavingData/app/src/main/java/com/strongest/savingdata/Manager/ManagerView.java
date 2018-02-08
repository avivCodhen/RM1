package com.strongest.savingdata.Manager;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.strongest.savingdata.Database.Exercise.Beans;
import com.strongest.savingdata.Database.Exercise.DBExercisesHelper;
import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.R;

import java.util.ArrayList;

/**
 * Created by Cohen on 1/9/2018.
 */


public class ManagerView extends LinearLayout implements OnManagerButtonClickListener{

    public DataManager mDataManager;
    private ViewType[] viewTypes;
    private Context context;
    private ArrayList<ManagerViewHolder> managerViewHolderList = new ArrayList<>();



    public enum ViewType {
        Reps, Sets, Rest,
    }

    public ManagerView(Context context) {
        super(context);
        this.context = context;
        setOrientation(VERTICAL);
    }

    public ManagerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setOrientation(VERTICAL);
    }

    @Override
    public void changeTextView(ViewType vType, int position) {
        for (int i = 0; i <viewTypes.length ; i++) {
            if(viewTypes[i] == vType){
                managerViewHolderList.get(i).getTv().setText("asdasd");
            }
        }
    }

    private void initHolderViews() {
        View v;
        ManagerAdapter managerAdapter = null;
        ArrayList<Beans> beansList = new ArrayList<>();
        for (int i = 0; i < viewTypes.length; i++) {
            v = LayoutInflater.from(context).inflate(R.layout.manager_view, this, false);
            switch (viewTypes[i]) {
                case Reps:
                    beansList = (ArrayList<Beans>) mDataManager.getExerciseDataManager()
                            .readByTable(DBExercisesHelper.TABLE_REPS);
                    managerAdapter = new ManagerAdapter(viewTypes[i],beansList);
                    break;
                case Rest:
                    beansList = (ArrayList<Beans>) mDataManager.getExerciseDataManager()
                            .readByTable(DBExercisesHelper.TABLE_REST);
                    managerAdapter = new ManagerAdapter(viewTypes[i],beansList);
                    break;
            }
            managerAdapter.setOnManagerButtonClickListener(this);
            addView(v);
            managerViewHolderList.add(new ManagerViewHolder(viewTypes[i],v, managerAdapter));
        }
    }

    public void instantiate(DataManager dataManager, ViewType... viewTypes) {
        mDataManager = dataManager;
        this.viewTypes = viewTypes;
        initHolderViews();
    }

    private class ManagerViewHolder implements OnClickListener {
        private final ViewType vType;
        private TextView tv;
        private RecyclerView recyclerView;
        private ManagerAdapter managerAdapter;
        private Button addBtn;
        private RecyclerView.LayoutManager lm;

        public ManagerViewHolder(ViewType vType,View v, ManagerAdapter managerAdapter) {
            this.vType = vType;
            tv = (TextView) v.findViewById(R.id.manager_view_tv);
            recyclerView = (RecyclerView) v.findViewById(R.id.manager_recyclerview);
            this.managerAdapter = managerAdapter;
            lm = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(lm);
            recyclerView.setHorizontalScrollBarEnabled(true);
            recyclerView.setAdapter(managerAdapter);
            addBtn = (Button) v.findViewById(R.id.manager_add_btn);
            addBtn.setOnClickListener(this);
        }

        public TextView getTv() {
            return tv;
        }

        public RecyclerView getRecyclerView() {
            return recyclerView;
        }

        public Button getAddBtn() {
            return addBtn;
        }

        public ManagerAdapter getManagerAdapter() {
            return managerAdapter;
        }


        @Override
        public void onClick(View v) {
            //TODO: dialog to create a new item
        }
    }

    public class ManagerAdapter extends RecyclerView.Adapter<ManagerAdapter.ManagerAdapterViewHolder> {

        private OnManagerButtonClickListener onManagerButtonClickListener;
        private ViewType vType;
        private ArrayList<Beans> list;

        public ManagerAdapter(ViewType vType, ArrayList<Beans> list) {
            this.vType = vType;

            this.list = list;
        }

        @Override
        public ManagerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_buttons, parent, false);
            return new ManagerAdapterViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ManagerAdapterViewHolder holder, int position) {
            holder.tb.setText(list.get(position).getName());
            holder.tb.setTextOff(list.get(position).getName());
            holder.tb.setTextOn(list.get(position).getName());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public void setOnManagerButtonClickListener(OnManagerButtonClickListener onManagerButtonClickListener) {
            this.onManagerButtonClickListener = onManagerButtonClickListener;
        }

        public class ManagerAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
            public ToggleButton tb;

            public ManagerAdapterViewHolder(View itemView) {
                super(itemView);
                tb = (ToggleButton) itemView.findViewById(R.id.manager_tv);
                tb.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                if(onManagerButtonClickListener != null){
                    onManagerButtonClickListener.changeTextView(vType, getLayoutPosition());
                }
            }
        }

    }
}

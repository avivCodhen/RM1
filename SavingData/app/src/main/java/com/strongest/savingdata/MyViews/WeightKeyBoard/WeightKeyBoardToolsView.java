package com.strongest.savingdata.MyViews.WeightKeyBoard;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.R;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

/**
 * Created by Cohen on 3/25/2018.
 */

public class WeightKeyBoardToolsView extends LinearLayout {


    private Context context;
    private GridView mGridView;
    private ExpandableLayout mExpandableLayout;
    private Button addAction;
    private DataManager dataManager;
    private ArrayList<WeightKeyBoardAction> wkbActions;
    private WeightKBToolsGridAdapter adapter;

    public WeightKeyBoardToolsView(Context context) {
        super(context);
        this.context = context;
    }

    public WeightKeyBoardToolsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void instantiate(DataManager dataManager, boolean showEditBtn) {
        inflate(context, R.layout.layout_keyboard_tools, this);
        wkbActions = dataManager.getWeightToolsDataManager().getWeightToolsList();
        mGridView = (GridView) findViewById(R.id.keyboard_tools_gridview);
        adapter = new WeightKBToolsGridAdapter();
        mGridView.setAdapter(adapter);
        mExpandableLayout = (ExpandableLayout) findViewById(R.id.keyboard_tools_expandable);

    }


    public class WeightKBToolsGridAdapter extends BaseAdapter {

        class ViewHolder {
            private TextView item;

            public ViewHolder(View v) {
                item = (TextView) v.findViewById(R.id.single_choice_tv);
            }
        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return wkbActions.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater li = null;
            View v = convertView;
            ViewHolder vh = null;
            if (convertView == null) {
                v = li.inflate(R.layout.recycler_view_single_choice_item, parent, false);
                vh = new ViewHolder(v);
            } else {
                vh.item.setText(wkbActions.get(position).action + wkbActions.get(position).number);
            }
            return null;
        }
    }

    public static class WeightKeyBoardAction {

        String action;
        int number;

        public WeightKeyBoardAction() {

        }

        public void setAction(String action) {
            this.action = action;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
        }

        public String getAction() {
            return action;
        }
    }
}

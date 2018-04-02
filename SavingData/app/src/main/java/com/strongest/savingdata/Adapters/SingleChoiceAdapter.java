package com.strongest.savingdata.Adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.strongest.savingdata.AlgorithmLayout.LayoutManager;
import com.strongest.savingdata.R;

import java.util.ArrayList;

/**
 * Created by Cohen on 3/14/2018.
 */

public class SingleChoiceAdapter extends RecyclerView.Adapter<SingleChoiceAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> list;
    private String type;
    private LayoutManager.LayoutManagerHelper helper;
    private OnSingleChoiceAdapterOnclickListener clickListener;
    private int currentPosition;
    private ArrayList<ItemHolder> items = new ArrayList<>();

    public SingleChoiceAdapter(Context context, ArrayList<String> list, OnSingleChoiceAdapterOnclickListener clickListener, String type){
        this.context = context;

        this.list = list;
        this.clickListener = clickListener;
        this.type = type;
        this.helper = helper;
        for (int i = 0; i <list.size() ; i++) {
            items.add(new ItemHolder(list.get(i), false));
        }


    }
    @Override
    public SingleChoiceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.recycler_view_single_choice_item,parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SingleChoiceAdapter.ViewHolder holder, final int position) {
        holder.item.setText(list.get(position));
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(list.get(position), type);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView item;
        public ViewHolder(View itemView) {
            super(itemView);
            item = (TextView) itemView.findViewById(R.id.single_choice_tv);
        }
    }

    public class ItemHolder{

        public String item;
        public boolean checked;
        public ItemHolder(String item, boolean checked){
            this.item = item;

            this.checked = checked;
        }


    }
}

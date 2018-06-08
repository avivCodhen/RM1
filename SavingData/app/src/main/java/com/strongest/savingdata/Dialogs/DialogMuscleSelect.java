package com.strongest.savingdata.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.strongest.savingdata.R;

import java.util.ArrayList;

public class DialogMuscleSelect extends Dialog {

    private String[] muscles;
    private int[] colors;
    private GridView gridView;
    private OnMuscleClickListener onMuscleClickListener;
    private boolean isActivated = true;

    public DialogMuscleSelect(@NonNull Context context, OnMuscleClickListener onMuscleClickListener) {
        super(context);
        this.onMuscleClickListener = onMuscleClickListener;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_muscle_select);

//        getOwnerActivity().getActionBar().hide();
      //  DisplayMetrics dm = new DisplayMetrics();
        //getDefaultDisplay().getMetrics(dm);

        //int width = dm.widthPixels;
        //int height = dm.heightPixels;

        // getWindow().setLayout((int) (width * .8), (int) (height * .6));
        if(isActivated){
            muscles = getContext().getResources().getStringArray(R.array.muscles_arr);
            colors = new int[]{
                    Color.parseColor("#CC0000"),
                    Color.parseColor("#CC6600"),
                    Color.parseColor("#CCCC00"),
                    Color.parseColor("#66CC00"),
                    Color.parseColor("#00CCCC"),
                    Color.parseColor("#0066CC"),
                    Color.parseColor("#6600CC"),
                    Color.parseColor("#CC0066"),
                    Color.parseColor("#606060"),
                    Color.parseColor("#CC0000"),
                    Color.parseColor("#CC0000"),
                    Color.parseColor("#CC0000"),
            };
            gridView = (GridView) findViewById(R.id.dialog_muscle_select_gridview);
            Adapter adapter = new Adapter();
            gridView.setAdapter(adapter);

        }


    }

    public void setOnMuscleClickListener(OnMuscleClickListener onMuscleClickListener) {
        this.onMuscleClickListener = onMuscleClickListener;
    }

    class ViewHolder {
        TextView tv;
        CardView cv;


        ViewHolder(View v, int position) {

          //  tv = (TextView) v.findViewById(R.id.muscle_select_tv);
          //  cv = (CardView) v.findViewById(R.id.muscle_select_cardview);
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onMuscleClickListener.sendMuscleName(tv.getText().toString());
                    dismiss();
                }
            });
        }
    }

    class MusclesContentHolder {

        public String text;
        public int color;

        MusclesContentHolder(int color, String text) {

            this.color = color;
            this.text = text;
        }

    }

    class Adapter extends BaseAdapter {

        ArrayList<MusclesContentHolder> list;

        Adapter() {
            list = new ArrayList<>();
            for (int i = 0; i < muscles.length; i++) {
                list.add(new MusclesContentHolder(colors[i], muscles[i]));
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
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater li = LayoutInflater.from(getContext());
            View v = convertView;
            ViewHolder holder = null;
            if(v == null){
                v = li.inflate(R.layout.recycler_view_muscle, parent, false);
                holder = new ViewHolder(v, position);
                v.setTag(holder);
            }else{
                holder = (ViewHolder) v.getTag();
            }

            holder.tv.setText(list.get(position).text);
            holder.cv.setBackgroundColor(list.get(position).color);



            return v;
        }
    }
}

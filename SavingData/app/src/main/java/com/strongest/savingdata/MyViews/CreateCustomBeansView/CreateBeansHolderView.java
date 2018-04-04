package com.strongest.savingdata.MyViews.CreateCustomBeansView;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.strongest.savingdata.R;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

/**
 * Created by Cohen on 1/25/2018.
 */

@Deprecated
public class CreateBeansHolderView extends LinearLayout {

    private ViewGroup parent = this;
    private Button addSetBtn;
    private ArrayList<CustomSetViewHolder> customSetViewHolders = new ArrayList<>();
    private LinearLayout mInnerView;

    private Context context;

    public CreateBeansHolderView(Context context) {
        super(context);
        this.context = context;
    }

    public CreateBeansHolderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void instantiate() {
        inflate(context, R.layout.choose_number_pyramid, this);
        initViews();

    }


    private void initViews() {
        //mInnerView = (LinearLayout) findViewById(R.id.choose_number_pyramid_linear_layout);
        mInnerView = (LinearLayout) findViewById(R.id.choose_number_pyramid_innerlayout);
        //mInnerView = (ScrollView) findViewById(R.id.choose_number_pyramid_sc);
        addSetBtn = (Button) findViewById(R.id.choose_number_pyramid_add_set_btn);
        addSetBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                addSingleChooseLayout();
            }
        });
        addSingleChooseLayout();
    }

    private void addSingleChooseLayout() {
        CustomSetViewHolder cs = new CustomSetViewHolder();
        mInnerView.addView(cs.getV());
        customSetViewHolders.add(cs);
    }


    private class CustomSetViewHolder {

        private ViewGroup outerLayout;
        private ExpandableLayout viewPagerLayout;
        private TextView set, reps, rest, weight;
        private Button saveBtn;
        private ViewPager viewPager;
      //  private final int customView = R.layout.choose_number_pyramid_set;
        private View v;

        public CustomSetViewHolder() {
           /* LayoutInflater li = LayoutInflater.from(context);
            v = li.inflate(customView, parent, false);
            outerLayout = (ViewGroup) v.findViewById(R.id.choose_number_pyramid_linear_layout);
            viewPagerLayout = (ExpandableLayout) v.findViewById(R.id.choose_sets_expandable);
            set = (TextView) v.findViewById(R.id.choose_number_pyramid_set_tv);
            reps = (TextView) v.findViewById(R.id.choose_reps_tv);
            rest = (TextView) v.findViewById(R.id.choose_rest_tv);
            weight = (TextView) v.findViewById(R.id.choose_weight_tv);
*/
            reps.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*View view = LayoutInflater.from(context).inflate(R.layout.choose_reps_layout, mInnerView, false);
                    outerLayout.addView(view);*/
                    viewPagerLayout.toggle();
                }
            });
        }

        public View getV() {
            return v;
        }

        public TextView getSet() {
            return set;
        }

        public TextView getReps() {
            return reps;
        }

        public TextView getRest() {
            return rest;
        }

        public TextView getWeight() {
            return weight;
        }
    }
}

package com.strongest.savingdata.createProgramFragments.Create;

import android.app.Dialog;
import android.graphics.Point;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allattentionhere.fabulousfilter.AAH_FabulousFragment;
import com.strongest.savingdata.ArtificialInteligence.AiActions;
import com.strongest.savingdata.ArtificialInteligence.ArtificialIntelligence;
import com.strongest.savingdata.ArtificialInteligence.CreatorPackage.Creator;
import com.strongest.savingdata.MyViews.MyAAHFabolousFilter;
import com.strongest.savingdata.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Cohen on 11/26/2017.
 */

public class ArtificialIntelligenceDialog extends MyAAHFabolousFilter {

    private ArtificialIntelligence ai;
    private int workoutNum;

    @Override
    public void setupDialog(Dialog dialog, int style) {

        View contentView = View.inflate(getContext(), R.layout.artificial_intelligence, null);
        RelativeLayout rl_content = (RelativeLayout) contentView.findViewById(R.id.rl_content);
        LinearLayout ll_buttons = (LinearLayout) contentView.findViewById(R.id.ll_buttons);
        TabLayout tabLayout = (TabLayout) contentView.findViewById(R.id.ai_tablayout);
        ViewPager viewPager = (ViewPager) contentView.findViewById(R.id.ai_viewpager);

        Adapter adapter = new Adapter(ai.getAiActionses());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        setViewPager(viewPager);
        ll_buttons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFilter("closed");
            }
        });

        //params to set
        setAnimationDuration(600); //optional; default 500ms
        Display display = getDialog().getWindow().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int width = size.x;
        final int height = size.y;
        setPeekHeight(400); // optional; default 400dp
        setCallbacks(null); //optional; to get back result
//        setAnimationListener((AnimationListener) getActivity()); //optional; to get animation callbacks
        setViewgroupStatic(ll_buttons); // optional; layout to stick at bottom on slide
        setViewMain(rl_content); //necessary; main bottomsheet view
        setMainContentView(contentView); // necessary; call at end before super


        super.setupDialog(dialog, style); //call super at last
    }

    public void setWorkoutNum(int workoutNum) {
        this.workoutNum = workoutNum;
    }

    public void setAi(ArtificialIntelligence ai) {
        this.ai = ai;
    }

    private class Adapter extends PagerAdapter {


        private ArrayList<AiActions.Analysis> aiActionses;

        public Adapter(ArrayList<AiActions.Analysis> aiActionses) {

            this.aiActionses = aiActionses;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.fragment_ai_recycler, container, false);
            RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.ai_recyclerview);
            ArtificialIntelligenceAdapter adapter = new ArtificialIntelligenceAdapter(getContext(),
                    aiActionses);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            //return super.instantiateItem(container, position);
            container.addView(layout);
            return layout;
        }


        private void inflateLayout(int position) {

        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Suggestions";
                case 1:
                    return "Analysis";
                case 2:
                    return "Notes";
            }
            return "";
        }

        @Override
        public int getCount() {
            return aiActionses.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}

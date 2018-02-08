package com.strongest.savingdata.Dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.strongest.savingdata.Database.Exercise.DBExercisesHelper;
import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.MyViews.CreateCustomBeansView.CreateRepsViewController;
import com.strongest.savingdata.MyViews.CreateCustomBeansView.NumberChoose;
import com.strongest.savingdata.MyViews.CreateCustomBeansView.NumberChooseManager;
import com.strongest.savingdata.MyViews.CreateCustomBeansView.PyramidNumberChooseView;
import com.strongest.savingdata.MyViews.CreateCustomBeansView.RangeNumberChooseView;
import com.strongest.savingdata.MyViews.CreateCustomBeansView.SingleNumberChooseView;
import com.strongest.savingdata.MyViews.MySelector.MySelector;
import com.strongest.savingdata.MyViews.MySelector.OnSelectorDialogListener;
import com.strongest.savingdata.MyViews.MyViewPager;
import com.strongest.savingdata.R;
import com.strongest.savingdata.TestActivity;
import com.strongest.savingdata.createProgramFragments.Unused.BaseDialogFragment;

/**
 * Created by Cohen on 1/14/2018.
 */

public class CreateRepsDialog extends BaseDialogFragment implements View.OnClickListener {

    private CreateRepsViewController repsViewController;
    private TabLayout tabLayout;
    private MyViewPager viewPager;
    private NumberChoose[] list;
    private Button createBtn;
    private DataManager dataManager;
    private OnSelectorDialogListener listener;
    private LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);


    public static CreateRepsDialog newInstance(DataManager dataManager, OnSelectorDialogListener listener) {
        CreateRepsDialog d = new CreateRepsDialog();
        d.setDataManager(dataManager);
        d.setListener(listener);
        return d;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_create_reps, container, false);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View v) {
        createBtn = (Button) v.findViewById(R.id.dialog_create_reps);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataManager.getExerciseDataManager().insertData(DBExercisesHelper.TABLE_REPS,
                        list[tabLayout.getSelectedTabPosition()].createRepBeans());
                listener.notifyTypeListChanged(MySelector.SelectorTypes.Reps);
                dismiss();
            }
        });
        list = new NumberChoose[]{
                new SingleNumberChooseView(getContext()),
                new RangeNumberChooseView(getContext()),
                new PyramidNumberChooseView(getContext())
        };
        tabLayout = (TabLayout) v.findViewById(R.id.dialog_create_reps_tablayout);
        viewPager = (MyViewPager) v.findViewById(R.id.dialog_create_reps_section_viewpager);
        viewPager.setPageTransformer(false, new FadePageTransformer());
        viewPager.setAdapter(new RepsAdapter());
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
               /* if(toInstantiate())
                viewPager.setCurrentItem(tab.getPosition());
                else*/
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        /*repsViewController = new CreateRepsViewController();
        repsViewController.instantiate(getContext());*/
    }

    private boolean toInstantiate() {
        for (int i = 0; i < list.length; i++) {
            if (!list[i].hasManager()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    public void setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void setListener(OnSelectorDialogListener listener) {
        this.listener = listener;
    }

    public class RepsAdapter extends PagerAdapter {

        public RepsAdapter() {

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (!list[position].hasManager()) {
                list[position].setUpWithNumberChooseManager(new NumberChooseManager());
            }
            container.addView(list[position].getView(), lp);
            //return super.instantiateItem(container, position);
            return list[position].getView();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
            container.removeView(list[position].getView());
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Single";
                case 1:
                    return "Range";
                case 2:
                    return "Pyramid";
                default:
                    return "naa";
            }
        }
    }

    public class FadePageTransformer implements ViewPager.PageTransformer {
        public void transformPage(View view, float position) {
            if (position <= -1.0F || position >= 1.0F) {
                view.setTranslationX(view.getWidth() * position);
                view.setAlpha(0.0F);
            } else if (position == 0.0F) {
                view.setTranslationX(view.getWidth() * position);
                view.setAlpha(1.0F);
            } else {
                // position is between -1.0F & 0.0F OR 0.0F & 1.0F
                view.setTranslationX(view.getWidth() * -position);
                view.setAlpha(1.0F - Math.abs(position));
            }
        }
    }

}

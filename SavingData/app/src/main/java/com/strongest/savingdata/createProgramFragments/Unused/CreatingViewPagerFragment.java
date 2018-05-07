package com.strongest.savingdata.createProgramFragments.Unused;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.strongest.savingdata.R;
import com.strongest.savingdata.Adapters.SwipeAdapter;
import com.strongest.savingdata.createProgramFragments.CreateProgram.BaseCreateProgramFragment;
import com.strongest.savingdata.createProgramFragments.CreateProgram.CreateFragment;

/**
 * Created by Cohen on 5/5/2017.
 */

@Deprecated
public class CreatingViewPagerFragment extends BaseCreateProgramFragment {

    private Menu menu;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        getActionBar().setTitle(R.string.createviewpager_title);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        return inflater.inflate(R.layout.creatingviewpager_fragment, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    initView(view);
    }

    private void initView(View v) {

        int[] images = {
                R.drawable.logo,
                R.drawable.one,
                R.drawable.two,
                R.drawable.three,
                R.drawable.ovr,
                R.drawable.four
        };

        String[] info = {
                "The program you are going to design is based on the routine layout that you have chosen",
                "You will get to pick exercises based on body parts and based on our recommendations.",
                "Picking an exercise follows by choosing the Accessory to perform the exercise, and the repetitions range with suggested rest time.",
                "You can choose to pick a Reps based on your level for a more advanced work.",
                "For Advanced trainees, you can choose to pick Overriders - Advanced Methods that overrides the reps x sets scheme.",
                "As you finish designing your program, you can choose to fragment_manager by increasing your workout complexity with out Progress System."
        };

        int imageView = R.id.swipe_layout_imageView;
        int textView = R.id.swipe_layout_textView;

        ViewPager viewPager = (ViewPager) v.findViewById(R.id.createviewpager_fragment_viewPager);
        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.createviewpager_fragment_tabDots);
        SwipeAdapter adapter = new SwipeAdapter(getContext(), images, info);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager, true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.next_button, menu);
        menu.findItem(R.id.action_next).setTitle("start");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
       int id = R.id.action_next;
        if(id == item.getItemId()) {
            Bundle bundle = getArguments();

           CreateFragment fragment = new CreateFragment();
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction()
                    .replace(R.id.create_activity_frameLayout, fragment)
                    .addToBackStack(null)
                    .commit();
        return true;
        }
        return false;
    }




}

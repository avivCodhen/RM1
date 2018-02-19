package com.strongest.savingdata.createProgramFragments.Create;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.strongest.savingdata.Adapters.oldWorkoutsAdapter;
import com.strongest.savingdata.ArtificialInteligence.ArtificialIntelligence;
import com.strongest.savingdata.ArtificialInteligence.ArtificialIntelligenceObserver;
import com.strongest.savingdata.BaseWorkout.Programmer;
import com.strongest.savingdata.BaseWorkout.ProgramTemplate;
import com.strongest.savingdata.Database.Exercise.Beans;
import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.Unused.ExerciseProfileEventMessage;
import com.strongest.savingdata.AlgorithmLayout.PLObjects;
import com.strongest.savingdata.R;
import com.strongest.savingdata.createProgramFragments.CreateProgram.BaseCreateProgramFragment;
import com.strongest.savingdata.tabFragments.WorkoutFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_EXERCISES_ALL;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_EXERCISES_GENERATOR;

@Deprecated
public class CreateProgramFragment extends BaseCreateProgramFragment implements View.OnClickListener{

    private boolean back;
    private MenuItem item;
    private ProgramTemplate programTemplate;
    private DataManager dm;
    private TabLayout tabLayout;
    private Programmer programmer;
    private int position;
    private FloatingActionButton fab;
    private ArtificialIntelligence ai;
    private ArrayList<ArtificialIntelligenceObserver> observers = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_create_program, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View v) {
        fab = (FloatingActionButton) v.findViewById(R.id.fragment_create_fab);
        fab.setOnClickListener(this);
        dm = new DataManager(getContext());
        //createProgram();
        //getPrefsEditor().putBoolean(INITIALIZE, true).commit();
        v.findViewById(R.id.create_create_image_view).setOnClickListener(this);
     //   final ViewPager viewPager = (ViewPager) v.findViewById(R.id.fragment_create_program_viewpager);
   //     tabLayout = (TabLayout) v.findViewById(R.id.fragment_create_tablayout);

      /*  final oldWorkoutsAdapter adapter = new oldWorkoutsAdapter(
                programmer.getLayoutManager().getSplitRecyclerWorkouts(),
                oldWorkoutsAdapter.CREATE_FRAGMENT,
                getFragmentManager(), getContext(),
                ProgramTemplate.WhatsYourName(programTemplate.getNumOfWorkouts()),
                false);*/
        // viewPager.setOffscreenPageLimit(workoutTemplate.getNumOfWorkouts());
       // viewPager.setAdapter(adapter);
       // tabLayout.setupWithViewPager(viewPager);
      /* tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                // EventBus.getDefault().post(new Object());
                position = tab.getPosition();
                Notify();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/
    }

    //also initiates ai
  /*  public void createProgram() {
        if (getPrefs().getBoolean(COSTUM, false)) {
            programTemplate = (ProgramTemplate) getArguments().getSerializable(WORKOUT);
        } else {
            programTemplate = ProgramTemplate.getWorkout(ProgramTemplate.Routine.values()[getPrefs().getInt(ROUTINE, -1)], getContext());
        }

        boolean generated = dm.getPrefs().getBoolean(GENERATED, false);
        programmer = new Programmer(getContext(), programTemplate);
        getPrefsEditor().putBoolean(INITIALIZE, false).commit();

        if (generated) {
            refreshTable();
            Generator generator = new Generator(getContext(), programTemplate);
            ProgramLayoutManager programLayoutManager = generator.generate();
            programmer.setProgramLayoutManager(programLayoutManager);
        } else {
            programmer.getProgramLayoutManager().createNewLayoutFromTemplate();

        }
        ai = new Creator(getContext(), programmer);
        Attach(ai);
        Notify();
    }
*/
    private void refreshTable() {
        dm.getExerciseDataManager().delete(TABLE_EXERCISES_GENERATOR);
        ArrayList<Beans> arr = (ArrayList<Beans>) dm.getExerciseDataManager().readByTable(TABLE_EXERCISES_ALL);
        for (Beans e : arr) {
            dm.getExerciseDataManager().insertData(TABLE_EXERCISES_GENERATOR, e);
        }
    }

    @Subscribe
    public void onEvent(ExerciseProfileEventMessage eventMessage) {
        PLObjects.ExerciseProfile exerciseProfile = eventMessage.getExerciseProfile();
       /* programmer.getLayoutManager().getSplitRecyclerWorkouts().get(position).set(eventMessage.getPosition(), exerciseProfile);
        programmer.getLayoutManager().updateLayoutStats(true);*/
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fragment_create_fab:
                ArtificialIntelligenceDialog csf = new ArtificialIntelligenceDialog();
                csf.setParentFab(fab);
                csf.setWorkoutNum(position);
                csf.setAi(ai);
               // Notify();
                csf.show(getFragmentManager(), csf.getTag());
                break;
            case R.id.create_create_image_view:
                //programmer.getLayoutManager().requestSplitToLayout(programmer.getLayoutManager().getSplitRecyclerWorkouts());
              //  programmer.getLayoutManager().saveLayoutToDataBase();
                dm.closeDataBases();
                dm.getPrefsEditor().putBoolean(WorkoutFragment.HASWORKOUT, true).commit();
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
                break;

        }

    }
/*
    @Override
    public void Attach(ArtificialIntelligenceObserver o) {
        observers.add(o);
    }

    @Override
    public void Dettach(ArtificialIntelligenceObserver o) {
        Log.d(TAG, "Dettach: ");
        observers.remove(o);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    public void Notify() {
        for (ArtificialIntelligenceObserver o : observers) {
            o.updatePosition(position);
        }
    }*/

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_create) {
            //dataBase = new DataManager(getContext());
            // mapToValue();

        } else {
            this.item = item;
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                    .setTitle(R.string.Error)
                    .setMessage(R.string.create_fragment_alert_message)
                    .setPositiveButton(R.string.im_sure, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog_muscle_description, int which) {
                            returnOption();
                        }
                    })
                    .setNegativeButton(R.string.wait, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog_muscle_description, int which) {
                            back = false;
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
            return back;
        }
        return false;
    }

    public boolean returnOption() {
        return super.onOptionsItemSelected(item);
    }
*/
}

package com.strongest.savingdata.createProgramFragments.CreateProgram;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.transition.Explode;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.strongest.savingdata.Activities.CreateWorkoutActivity;
import com.strongest.savingdata.Activities.HomeActivity;
import com.strongest.savingdata.Activities.OnCreateProgramListener;
import com.strongest.savingdata.AlgorithmGenerator.Generator;
import com.strongest.savingdata.ArtificialInteligence.ArtificialIntelligence;
import com.strongest.savingdata.BaseWorkout.Programmer;
import com.strongest.savingdata.BaseWorkout.ProgramTemplate;
import com.strongest.savingdata.MyViews.WorkoutView.WorkoutView;
import com.strongest.savingdata.Database.Exercise.Beans;
import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.AlgorithmLayout.LayoutManager;
import com.strongest.savingdata.R;
import com.strongest.savingdata.createProgramFragments.Create.ArtificialIntelligenceDialog;
import com.strongest.savingdata.tabFragments.WorkoutFragment;

import java.util.ArrayList;

import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_EXERCISES_ALL;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_EXERCISES_GENERATOR;

/**
 * Created by Cohen on 5/6/2017.
 */

public class CreateFragment extends BaseCreateProgramFragment implements View.OnClickListener {


    private ProgramTemplate programTemplate;

   /* private int[] routinesArray = new int[]{R.array.fbw_arrays, R.array.ab_arrays
            , R.array.abc_arrays, R.array.abcde_array};*/
    //  private Bundle bundle;

//    private RecyclerView r;

    private DataManager dm;

    //private WorkoutAdapter adapter;
    private Programmer programmer;
    //  private FloatingActionButton fab;

    //   private boolean toLoad = true;
    //  private Bundle mBundle;
    private boolean editMode;
    //  private ArrayList<PLObjects> layout;
    private View mainView;
    //   private ItemTouchHelper touchHelper;

    private FloatingActionButton fab;
    private ArtificialIntelligence ai;
    private WorkoutView workoutView;
    private OnCreateProgramListener onCreateProgramListener;
    private Bundle bundle;

    // private ArrayList<Object> exArray = new ArrayList<>();
    private final int w = 20;
    private int exerciseLevel;
    private int workoutPosition = 0;
    //private Programmer newProgrammer;



    public static CreateFragment newInstance(OnCreateProgramListener onCreateProgramListener, Bundle bundle) {
        CreateFragment fragment = new CreateFragment();
        fragment.setOnCreateProgramListener(onCreateProgramListener);
        fragment.setBundle(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            workoutPosition = getArguments().getInt(POSITION);
            editMode = getArguments().getBoolean(EDIT);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //setHasOptionsMenu(true);

        exerciseLevel = getPrefs().getInt(LOAD_EXERCISE_LEVEL, -1);
        //  getActionBar().setTitle(routine.getTitle());
        //  setHasOptionsMenu(true);
        // getActionBar().setDisplayHomeAsUpEnabled(true);
        // getActionBar().show();
        // popDialog();
        mainView = inflater.inflate(R.layout.fragment_create, container, false);
        return mainView;
    }

   /* private void popDialog() {
        CreateDialogFragment cdf = new CreateDialogFragment();
        FragmentManager fm = getFragmentManager();
        cdf.show(fm, "");

    }*/

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }


    private void initView(View v) {
        //fab = (FloatingActionButton) v.findViewById(R.id.fragment_create_fab);
        // fab.setOnClickListener(this);
        v.findViewById(R.id.create_create_image_view).setOnClickListener(this);
        ai = ((HomeActivity) getActivity()).getAi();
        dm = ((HomeActivity) getActivity()).getDataManager();
        programmer = ((HomeActivity) getActivity()).getProgrammer();
        boolean generated = bundle.getBoolean(MODE_GENERATED_PROGRAM, false);
        String routine = bundle.getString(ROUTINE);
        boolean custom = bundle.getBoolean(MODE_CUSTOM, false);
        if (custom) {
            programTemplate = (ProgramTemplate) bundle.getSerializable(WORKOUT);
        } else {
            programTemplate = ProgramTemplate.ProgramTemplateFactory.getStaticTemplates(routine);
        }
        //   }

        //newProgrammer = new Programmer(getContext(), dm, false);
        LayoutManager layoutManager;
        if (generated) {
            refreshTable();
            Generator generator = new Generator(getContext(), programTemplate, dm);
            layoutManager = generator.generate();
        } else {
            layoutManager = new LayoutManager(getContext(),dm,programTemplate);
         //   layoutManager.createNewLayoutFromTemplate(programTemplate);
            //newProgrammer.createLayoutFromTemplate(programTemplate);
        }

        programmer.createProgramTable();
        layoutManager.saveLayoutToDataBase(false);
        dm.closeDataBases();
        dm.getPrefsEditor().putBoolean(WorkoutFragment.HASWORKOUT, true).commit();
        Handler uiHandler = new Handler();
        uiHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                onCreateProgramListener.createProgramUI(new WorkoutFragment());

            }
        });

       // workoutView = (WorkoutView) v.findViewById(R.id.workout_test);
      //  workoutView.instantiate(-1, getFragmentManager(), true, newProgrammer.getLayoutManager());
        //  initAdapter();

        /*v.findViewById(R.id.ivtest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialogFragment pdf = new ProgressDialogFragment();
                pdf.setWorkoutPos(workoutView.getWorkoutPosition());
                pdf.show(getFragmentManager(), "");
            }
        });*/
    }

    private void refreshTable() {
        dm.getExerciseDataManager().refreshGeneratorTable();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fragment_create_fab:
                ArtificialIntelligenceDialog csf = new ArtificialIntelligenceDialog();
                csf.setParentFab(fab);
                csf.setWorkoutNum(workoutView.getWorkoutPosition());
                ((HomeActivity) getActivity()).notifyAI(workoutView.getWorkoutPosition());
                csf.setAi(ai);
                csf.show(getFragmentManager(), csf.getTag());
                break;
            case R.id.create_create_image_view:
                // newProgrammer.getLayoutManager().requestSplitToLayout(newProgrammer.getLayoutManager().getSplitRecyclerWorkouts());
                // newProgrammer.getLayoutManager().saveLayoutToDataBase(false);
                //dm.closeDataBases();
                //dm.getPrefsEditor().putBoolean(WorkoutFragment.HASWORKOUT, true).commit();
               // onCreateProgramListener.createProgramUI(new WorkoutFragment());
                /*getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();*/
                break;

        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }


    public void setOnCreateProgramListener(OnCreateProgramListener onCreateProgramListener) {
        this.onCreateProgramListener = onCreateProgramListener;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }
}


/*private OnPositionViewListener posListener = new OnPositionViewListener() {
        @Override
        public void longClick(View v, int pos) {

            Bundle muscle = new Bundle();
            muscle.putInt(InteractDialogFragment.POS, pos);
            InteractDialogFragment idf = new InteractDialogFragment();
            FragmentManager fm = getFragmentManager();
            idf.setArguments(muscle);
            idf.setTargetFragment(CreateFragment.this, INTERRACT_FRAGMENT);
            idf.show(fm, "Choose ExerciseBean");
        }
    };
*/

//opens choose dialog_muscle_description and passes information
    /*private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            saveAndShowDialog(v);

            }
        };*/

/*  public void initAdapter() {


           *//* adapter = new WorkoutAdapter(layout,
                    getContext(), listener, posListener, true, this, this);*//*
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            r.setLayoutManager(layoutManager);
            r.setAdapter(adapter);
            ItemTouchHelper.Callback callback = new DragAndSwipeCallback(adapter);
            touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(r);
        }*/
//calls dialogfragment and passes parameters on exercise click
       /* public void saveAndShowDialog(View v) {
            FragmentManager fm = getFragmentManager();
            ChooseDialogFragment dialog = new ChooseDialogFragment();

       *//* ExerciseProfile exerciseProfile = (ExerciseProfile) programmer.getProgramLayoutManager().getSplitRecyclerWorkouts()
                .get(workoutPosition)
                .get((int) v.getTag());*//*

            ExerciseProfile exerciseProfile = (ExerciseProfile) layout.get((int) v.getTag());
            //onCreateTalkListener.updateLayout(exerciseProfile, (int) v.getTag());
            bundle.putSerializable("beans_holder", exerciseProfile.getBeansHolder());
            bundle.putInt(MUSCLE, exerciseProfile.getMuscle().ordinal());
            bundle.putInt("exercise_position", Integer.parseInt(v.getTag().toString()));
            dialog.setArguments(bundle);
            dialog.setTargetFragment(this, CREATE_FRAGMENT);
            //mainView.setAlpha(0.2f);
            dialog.show(fm, "Choose ExerciseBean");
        }

        public void setandSave(Bundle bundle) {
            int pos = bundle.getInt("exercise_position");
            ExerciseProfile exerciseProfile = (ExerciseProfile) layout.get(pos);
            exerciseProfile.setBeansHolder((BeansHolder) bundle.getSerializable("beans_holder"));
            ExerciseProfileEventMessage eventM = new ExerciseProfileEventMessage(exerciseProfile, pos);
            EventBus.getDefault().post(eventM);
            //  programmer.getProgramLayoutManager().updateLayout(true);
            adapter.notifyDataSetChanged();

        }


        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == CREATE_FRAGMENT) {
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    setandSave(bundle);

                }
            } else if (requestCode == INTERRACT_FRAGMENT) {
                if (resultCode == Activity.RESULT_OK) {
                    int pos = data.getIntExtra(InteractDialogFragment.POS, -1);
                    boolean arg = data.getBooleanExtra(InteractDialogFragment.ARGS, false);
                    if (arg == true) {
                        addRow(pos);
                    } else {
                        removeRow(pos);
                    }
                }

            }
        }
*/
     /*   private void removeRow(int pos) {
            if (layout.get(pos - 1).getType() == WorkoutLayoutTypes.BodyView) {
                Toast.makeText(getContext(), "You cannot delete the first exercise", Toast.LENGTH_SHORT).show();
                return;
            }

            layout.remove(pos);
            //programmer.updateRecyclerLayout(exArray);
            adapter.notifyItemRemoved(pos);
        }

        private void addRow(int pos) {
            ExerciseProfile tempProfile = (ExerciseProfile) layout.get(pos);
            ExerciseProfile exerciseProfile = new ExerciseProfile(null, tempProfile.getMuscle(), tempProfile.getWorkoutId());
            if (layout.get(pos - 1).getType() == WorkoutLayoutTypes.BodyView) {
                pos = pos + 1;
            }
            layout.add(pos, exerciseProfile);
            adapter.notifyItemInserted(pos);
        }*/

  /*  @Override
    public void startDrag(RecyclerView.ViewHolder viewHolder) {
        touchHelper.startDrag(viewHolder);
    }

    @Override
    public void scrollToPosition(int position) {
        r.scrollToPosition(position);
    }


    public interface OnCreateTalkListener {
            void updateLayout(ExerciseProfile exerciseProfile, int position);
        }

        @Subscribe
        public void onEvent(Object o) {
        }


        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            EventBus.getDefault().register(this);

        }

        @Override
        public void onDetach() {
            super.onDetach();
            dm.closeDataBases();
            EventBus.getDefault().unregister(this);
        }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_create) {
            dataBase = new DataManager(getContext());
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
    }*/

    /*private void mapToValue() {

        if (hashMap.get(100) == null) {
            return;
        }
        Map<Integer, String[]> map = hashMap;
        for (String[] s : hashMap.values()) {
            contentValues.put(DBProgramHelper.COL_EXERCISE, s[0]);
            contentValues.put(DBProgramHelper.COL_SETS, 3);
            contentValues.put(DBProgramHelper.COL_REPS, s[1]);
            contentValues.put(DBProgramHelper.COL_METHOD, s[2]);
            contentValues.put(DBProgramHelper.MUSCLE, s[3]);
            dataBase.insertData(DBProgramHelper.TABLE_NAME, contentValues);
            contentValues.clear();
            //mapToValue();

        }
    }*/

/*
    public boolean returnOption() {
        return super.onOptionsItemSelected(item);
    }
*/

       /* @Override
        public void longClick(View v, int pos) {
            Bundle muscle = new Bundle();
            muscle.putInt(AddOrDeleteFragment.POS, pos);
            AddOrDeleteFragment idf = new AddOrDeleteFragment();
            FragmentManager fm = getFragmentManager();
            idf.setArguments(muscle);
            idf.setTargetFragment(CreateFragment.this, INTERRACT_FRAGMENT);
            idf.show(fm, "Choose ExerciseBean");
        }
*/


       /* public void saveSharedPreferencesProgram(ArrayList<Object> exArray) {
            Intent intent = new Intent(getContext(), HomeActivity.class);

            startActivity(intent);
        }

        public void setLayout(ArrayList<PLObjects> layout) {
            this.layout = layout;
        }*/





package com.strongest.savingdata.createProgramFragments.CreateProgram;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;


import com.strongest.savingdata.Activities.OnCreateProgramListener;
import com.strongest.savingdata.Adapters.*;
import com.strongest.savingdata.Adapters.WorkoutAdapter.DragAndSwipeCallback;
import com.strongest.savingdata.Adapters.WorkoutAdapter.OnDragListener;
import com.strongest.savingdata.Adapters.WorkoutAdapter.ScrollToPositionListener;
import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.BaseWorkout.ProgramTemplate;
import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.AlgorithmLayout.PLObjects;
import com.strongest.savingdata.AlgorithmLayout.WorkoutLayoutTypes;
import com.strongest.savingdata.Database.Managers.ProgramDataManager;
import com.strongest.savingdata.Dialogs.DialogMuscleSelect;
import com.strongest.savingdata.R;

import java.util.ArrayList;

import static com.strongest.savingdata.Database.Managers.ProgramDataManager.Tables.TEMPLATE;


public class ProgramTemplateFragment extends BaseCreateProgramFragment implements OnMuscleClickListener, OnDragListener, View.OnClickListener, ScrollToPositionListener {

    private ItemTouchHelper touchHelper;
    private RecyclerView recyclerView;
    private MainAdapter adapter;
    private ArrayList<PLObjects> list;
    // private BoomMenuButton bmb;
    private EditText programName_ed;
    private DataManager dataManager;
    private OnCreateProgramListener listener;
    int i = 0;

    public static ProgramTemplateFragment getInstance(OnCreateProgramListener listener) {
        ProgramTemplateFragment f = new ProgramTemplateFragment();
        f.setListener(listener);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_program_template, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View v) {
        dataManager = new DataManager(getContext());
        v.findViewById(R.id.template_fragment_save_button).setOnClickListener(this);
        v.findViewById(R.id.template_fragment_return_button).setOnClickListener(this);
        v.findViewById(R.id.fragment_template_add_workout).setOnClickListener(this);
        v.findViewById(R.id.fragment_template_add_muscle).setOnClickListener(this);
        // v.findViewById(R.id.fragment_template_add_muscle).setOnClickListener(this);
        list = new ArrayList<>();
        list.add(new PLObjects.WorkoutText(i++, "A"));
        adapter = new MainAdapter(getContext(), list, this, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView = (RecyclerView) v.findViewById(R.id.program_template_recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new DragAndSwipeCallback(adapter);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);


        //initBoom(v);

    }

    /*private void initBoom(View v) {
        programName_ed = (EditText) v.findViewById(R.id.template_program_edit_text);
        for (int i = 0; i < 12; i++) {
            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder().normalText("this is a test")
                    .normalText(getContext().getResources().getStringArray(R.array.muscles_arr)[i])
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            final Context c = getContext();
                            String bmbMuscle = bmb.getBoomButton(index).getTextView().getText().toString();
                            String muscle = parseString(bmbMuscle);
                            Body.Muscle.MainMuscleEnum m = Body.Muscle.MainMuscleEnum.valueOf(muscle.toUpperCase());
                            list.add(new PLObjects.BodyText(getContext(), m, 1, null));
                            adapter.notifyItemInserted(list.size() - 1);
                            recyclerView.scrollToPosition(list.size() - 1);
                        }
                    });

            bmb.addBuilder(builder);
        }
        float w = Util.dp2px(80);
        float h = Util.dp2px(96);
        float h_0_5 = h / 2;
        float h_1_5 = h * 1.5f;

        float hm = bmb.getButtonHorizontalMargin();
        float vm = bmb.getButtonVerticalMargin();
        float vm_0_5 = vm / 2;
        float vm_1_5 = vm * 1.5f;

        bmb.getCustomButtonPlacePositions().add(new PointF(-w - hm, -h_1_5 - vm_1_5));
        bmb.getCustomButtonPlacePositions().add(new PointF(0, -h_1_5 - vm_1_5));
        bmb.getCustomButtonPlacePositions().add(new PointF(+w + hm, -h_1_5 - vm_1_5));
        bmb.getCustomButtonPlacePositions().add(new PointF(-w - hm, -h_0_5 - vm_0_5));
        bmb.getCustomButtonPlacePositions().add(new PointF(0, -h_0_5 - vm_0_5));
        bmb.getCustomButtonPlacePositions().add(new PointF(+w + hm, -h_0_5 - vm_0_5));
        bmb.getCustomButtonPlacePositions().add(new PointF(-w - hm, +h_0_5 + vm_0_5));
        bmb.getCustomButtonPlacePositions().add(new PointF(0, +h_0_5 + vm_0_5));
        bmb.getCustomButtonPlacePositions().add(new PointF(+w + hm, +h_0_5 + vm_0_5));
        bmb.getCustomButtonPlacePositions().add(new PointF(-w - hm, +h_1_5 + vm_1_5));
        bmb.getCustomButtonPlacePositions().add(new PointF(0, +h_1_5 + vm_1_5));
        bmb.getCustomButtonPlacePositions().add(new PointF(+w + hm, +h_1_5 + vm_1_5));


    }
*/
    @Override
    public void startDrag(RecyclerView.ViewHolder viewHolder) {
        touchHelper.startDrag(viewHolder);
    }

    private String parseString(String val) {
        String result;
        for (int i = 0; i < val.length(); i++) {
            if (val.charAt(i) == ' ') {
                val = val.replace(' ', '_');
            }
            if (val.charAt(i) == '(') {
                result = val.substring(0, i);
                return result;
            }
        }
        return val;
    }

    public static ArrayList<ArrayList<String>> parsePLObjectStringToBodyTmeplate(ArrayList<PLObjects> list) {
        ArrayList<ArrayList<String>> arr = new ArrayList<>();
        int i = 0;
        for (PLObjects obj : list) {
            if (obj.getType() == WorkoutLayoutTypes.WorkoutView) {
                arr.add(new ArrayList<String>());
                if (i != 0) {
                    i++;

                }
            }
            if (obj.getType() == WorkoutLayoutTypes.BodyView) {
                arr.get(i).add(((PLObjects.BodyText) obj).getMuscle().getMuscle_name());
            }
        }
        return arr;
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.fragment_template_add_workout:
                String wN = ProgramTemplate.ProgramTemplateFactory.WhatsYourWorkoutName(i);
                list.add(new PLObjects.WorkoutText(i++, wN));
                adapter.notifyItemInserted(list.size() - 1);
                recyclerView.scrollToPosition(list.size() - 1);
                break;
            case R.id.template_fragment_return_button:
               /* FragmentManager fm = getFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }*/
                break;
            case R.id.fragment_template_add_muscle:
                DialogMuscleSelect dialog = new DialogMuscleSelect(getContext(), this);
                dialog.show();
                break;
            case R.id.template_fragment_save_button:

                if (validate()) {
                    getPrefsEditor().putBoolean("custom", true);

                    String programName = null;

                    try {
                        programName = programName_ed.getText().toString();
                    } catch (Exception e) {

                    }
                    if (programName != null && programName.equals("")) {
                        programName = "";
                    }

                    ProgramTemplate programTemplate = ProgramTemplate
                            .ProgramTemplateFactory
                            .createCustomProgramTemplate(parsePLObjectStringToBodyTmeplate(list),
                                    -1,
                                    -1,
                                    programName,
                                    null
                            );
                    programTemplate.setCustom(true);

                    dataManager.getProgramDataManager().insertTables(false, programTemplate, TEMPLATE);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(WORKOUT, programTemplate);
                    getPrefsEditor().putBoolean(MODE_GENERATED_PROGRAM, false).putBoolean(MODE_CUSTOM, true).commit();
                    bundle.putBoolean(MODE_GENERATED_PROGRAM, false);
                    bundle.putBoolean(MODE_CUSTOM, true);
                    listener.createProgramUI(CreateFragment.newInstance(listener, bundle));
                    //getPrefsEditor().putBoolean(MODE_GENERATED_PROGRAM, false).commit();
                    break;
                } else {
                    Toast.makeText(getContext(), "You did not choose any muscles", Toast.LENGTH_SHORT).show();
                }

        }
    }

    private boolean validate() {
        int count = 0;
        for (PLObjects plObj : list) {
            if (plObj.getType() == WorkoutLayoutTypes.BodyView)
                count++;
        }
        return count > 0;
    }

   /* private String readWorkoutTemplate(ProgramTemplate programTemplate) {
        String result = "custom";
        int workouts = programTemplate.getNumOfWorkouts();
        switch (workouts) {
            case 1:
                return result + "FBW";
            case 2:
                return result + "AB";
            case 3:
                return result + "ABC";
            case 4:
                return result + "ABCD";
            case 5:
                return result + "ABCDE";
        }
        return result;
    }*/

   /* private int countWorkouts(){
        int counter = 0;
        for (int i = 0; i <list.size() ; i++) {
            if(list.get(i).getType() == WorkoutLayoutTypes.WorkoutView){
                counter++;
            }
        }
        return counter;
    }
*/
    /*private void saveTemplate(){

        if(programName != null){
            dataManager.getProgramDataManager().createUserTemplate(programName, l);
        }
    }*/


    public void scrollToPosition(int position, boolean enableScroll) {
        recyclerView.scrollToPosition(position);
    }

    public void setListener(OnCreateProgramListener listener) {
        this.listener = listener;
    }

    @Override
    public void sendMuscleName(String muscleString) {
        String muscle = parseString(muscleString);
        Muscle m = Muscle.createMuscle(dataManager.getMuscleDataManager(), muscleString);
        list.add(new PLObjects.BodyText(1, -1, null, ""));
        adapter.notifyItemInserted(list.size() - 1);
        recyclerView.scrollToPosition(list.size() - 1);
    }


    @Override
    public void scrollToPosition(int position, boolean enableScroll, boolean lastVisible) {

    }
}

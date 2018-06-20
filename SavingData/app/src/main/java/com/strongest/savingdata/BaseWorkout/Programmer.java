package com.strongest.savingdata.BaseWorkout;

import android.content.Context;

import com.strongest.savingdata.AModels.AlgorithmLayout.WorkoutsModel;
import com.strongest.savingdata.Activities.OnProgramInitListener;
import com.strongest.savingdata.Database.Managers.DataManager;

import java.io.Serializable;

/**
 * Created by Cohen on 8/19/2017.
 */

@Deprecated
public class Programmer implements Serializable {

    private Context context;

    private DataManager dataManager;

    public WorkoutsModel workoutsModel;


    private Program program;
    private int counter = 0;

    private boolean toCreateProgram;

    public Programmer(Context context, DataManager dataManager) {
        this.dataManager = dataManager;
        this.context = context;
    }

    public Programmer(Context context) {
        this.context = context;

    }




    public WorkoutsModel getWorkoutsModel() {
        return workoutsModel;
    }

    public void setWorkoutsModel(WorkoutsModel workoutsModel) {
        this.workoutsModel = workoutsModel;
    }

    /*public ProgramTemplate getProgramTemplate() {
        return workoutsModel.getProgramTemplate();
    }
*/

    public void tryInitProgram(OnProgramInitListener onProgramInitListener) {
     /*   if(!toCreateProgram){

            String dbName = dataManager.getPrefs().getString(HomeActivity.CURRENT_PROGRAM_DBNAME, "no_program");
            if (!dbName.equals("no_program")) {
                program = dataManager.getProgramDataManager().readProgramTable(dbName);
                if (program != null) {
                    layoutManager = new LayoutManager(context, dataManager);
                    layoutManager.readLayoutFromDataBase(dbName);
                    onProgramInitListener.instantiateWorkView();
                } else {
                }
            }
        }else{
            createNewProgram(onProgramInitListener);
        }
        toCreateProgram = false;*/

    }

    public void createNewProgram(OnProgramInitListener onProgramInitListener) {



    }

 /*   public void createProgramTable() {

        dataManager.getProgramDataManager().createNewProgramTable("program");
    }
*/

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
     //   dataManager.getPrefsEditor().putString(HomeActivity.CURRENT_PROGRAM_DBNAME, program.getDbName()).commit();
    }

    public void resetProgram(){
        this.program = null;
    }

    public void setToCreateProgram(boolean toCreateProgram) {
        this.toCreateProgram = toCreateProgram;
    }
}

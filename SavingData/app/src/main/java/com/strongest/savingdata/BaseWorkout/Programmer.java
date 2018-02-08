package com.strongest.savingdata.BaseWorkout;

import android.content.Context;
import android.util.Log;

import com.strongest.savingdata.AlgorithmLayout.LayoutManager;
import com.strongest.savingdata.AlgorithmProgress.ProgressorManager;
import com.strongest.savingdata.Database.Managers.DataManager;

import java.io.Serializable;

import javax.inject.Inject;

/**
 * Created by Cohen on 8/19/2017.
 */

public class Programmer implements Serializable {

    private Context context;

    // 0 for latest workout, 1 for previous and so on
    private ProgramTemplate programTemplate;

    private int requestedLayoutPosition;

    private DataManager dataManager;

    @Inject
    public LayoutManager layoutManager;

    private ProgressorManager progressorManager;

    private int counter = 0;
    private boolean init;

    @Inject
    public Programmer(Context context, DataManager dataManager, boolean init) {
        this.init = init;
        this.dataManager = dataManager;
        this.context = context;
        //dataManager = new DataManager(context);
        layoutManager = new LayoutManager(context, dataManager);
     //   progressorManager = new ProgressorManager(context, dataManager, layoutManager);
        tryInitProgram();
    }



    public Programmer(Context context) {
        this.context = context;
        //dataManager = new DataManager(context);
        //programLayoutManager = new ProgramLayoutManager(context, dataManager, programTemplate);
    }


    public ProgressorManager getProgressorManager() {
        if(progressorManager == null) {
            progressorManager = new ProgressorManager(context, dataManager, layoutManager);
        }
        return progressorManager;
    }


    public LayoutManager getLayoutManager(){
        return layoutManager;
    }
    public void setLayoutManager(LayoutManager layoutManager){
        this.layoutManager = layoutManager;
    }

    public ProgramTemplate getProgramTemplate() {
        return layoutManager.getProgramTemplate();
    }

   /* public void setProgramTemplate(ProgramTemplate programTemplate){
        this.programTemplate = programTemplate;
        programLayoutManager.setProgramTemplate(programTemplate);
        tryInitProgram();
    }   */
    private void tryInitProgram() {
        if(init){
            try {
                layoutManager.readLayoutFromDataBase(requestedLayoutPosition);
           //     progressorManager.readProgressFromDataBase(0, -1);
            }catch (Exception e){
                Log.d("aviv", "tryInitProgram: " + e.toString());
            }

        }

    }

    public void createProgramTable(){
        dataManager.getProgramDataManager().createNewProgramTable("program");
    }

    public void setRequestedLayoutPosition(int requestedLayoutPosition) {
        this.requestedLayoutPosition = requestedLayoutPosition;
    }

    public int getRequestedLayoutPosition() {
        return requestedLayoutPosition;
    }

    /*public void createLayoutFromTemplate(ProgramTemplate programTemplate) {
        layoutManager.createNewLayoutFromTemplate(programTemplate);
    }*/

    public void updateLayout() {
        layoutManager = new LayoutManager(context, dataManager);
        layoutManager.readLayoutFromDataBase(0);
    }

    public class Builder{


    }


}

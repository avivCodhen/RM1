package com.strongest.savingdata.BaseWorkout;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.strongest.savingdata.Activities.HomeActivity;
import com.strongest.savingdata.AlgorithmLayout.LayoutManager;
import com.strongest.savingdata.AlgorithmProgress.ProgressorManager;
import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.Database.Program.DBProgramHelper;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by Cohen on 8/19/2017.
 */

public class Programmer implements Serializable {

    private Context context;

    private DataManager dataManager;

    @Inject
    public LayoutManager layoutManager;

    private ProgressorManager progressorManager;

    private Program program;
    private int counter = 0;

    @Inject
    public Programmer(Context context, DataManager dataManager) {
        this.dataManager = dataManager;
        this.context = context;
        tryInitProgram();
    }

    public Programmer(Context context) {
        this.context = context;

    }


    public ProgressorManager getProgressorManager() {
        if (progressorManager == null) {
            progressorManager = new ProgressorManager(context, dataManager, layoutManager);
        }
        return progressorManager;
    }


    public LayoutManager getLayoutManager() {
        return layoutManager;
    }

    public void setLayoutManager(LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public ProgramTemplate getProgramTemplate() {
        return layoutManager.getProgramTemplate();
    }


    public void tryInitProgram() {
            String dbName = dataManager.getPrefs().getString(HomeActivity.CURRENT_PROGRAM_DBNAME, "no_program");
            if (!dbName.equals("no_program")) {
                program = dataManager.getProgramDataManager().readProgramTable(dbName);
                if (program != null) {
                    layoutManager = new LayoutManager(context, dataManager);
                    layoutManager.readLayoutFromDataBase(dbName);
                } else {
                }
            }

    }

    public void createNewProgram() {

        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
        String table = date.replace("-", "_");
        String currentTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
        String time = "_" + currentTime.replace(":", "_");
        String results = "program" + table + time;

        program = new Program("My Program", currentTime, date, results);
        program.setCurrent(true);
        setLayoutManager(LayoutManager.getDefaultLayoutManagerInstance(context, dataManager));
        layoutManager.dbName = program.getDbName();
        dataManager.getPrefsEditor().putString(HomeActivity.CURRENT_PROGRAM_DBNAME, program.getDbName()).commit();
        dataManager.getProgramDataManager().insertData(
                DBProgramHelper.TABLE_PROGRAM_REFERENCE,
                dataManager.getProgramDataManager().getProgramContentValues(program));
        dataManager.getProgramDataManager().insertTables(false, layoutManager);
    }

    public void createProgramTable() {

        dataManager.getProgramDataManager().createNewProgramTable("program");
    }


    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
        dataManager.getPrefsEditor().putString(HomeActivity.CURRENT_PROGRAM_DBNAME, program.getDbName()).commit();
    }
}

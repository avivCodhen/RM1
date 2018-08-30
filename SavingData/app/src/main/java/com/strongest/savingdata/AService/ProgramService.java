package com.strongest.savingdata.AService;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.strongest.savingdata.AModels.programModel.Program;
import com.strongest.savingdata.AModels.programModel.ProgramRepository;
import com.strongest.savingdata.Utils.FireBaseUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProgramService {


    public final static String CURRENT_PROGRAM = "current_program";
    private UserService userService;
    private final ProgramRepository programRepository;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor sharedPreferencesEditor;

    @Inject
    public ProgramService(UserService userService, ProgramRepository programRepository, SharedPreferences sharedPreferences,
                          SharedPreferences.Editor sharedPreferencesEditor) {
        this.userService = userService;
        this.programRepository = programRepository;
        this.sharedPreferences = sharedPreferences;
        this.sharedPreferencesEditor = sharedPreferencesEditor;
    }


    public LiveData<Program> provideProgram() {
        String programKey = sharedPreferences.getString(CURRENT_PROGRAM, "");
        if (programKey.equals("")) {
            return provideNewProgram();
        } else {
            Program p = programRepository.getProgramByKey(programKey).getValue();
            if(p == null){
                return provideNewProgram();
            }else{
                return programRepository.getProgramByKey(programKey);
            }
        }
    }

    public LiveData<Program> provideNewProgram() {
        cleanCurrentProgramSharedPreferences();
        createNewProgram();
        String programKey = sharedPreferences.getString(CURRENT_PROGRAM, "");
        return programRepository.getProgramByKey(programKey);

    }

    public Program getNewProgram(){
        cleanCurrentProgramSharedPreferences();
        return createNewProgram();
    }

    private String getUsername() {
        return sharedPreferences.getString(UserService.USERNAME, "Anonymous");
    }

    private String getUID() {
        return sharedPreferences.getString(UserService.UID, "");
    }


    private void saveProgramKeyToSharedPreferences(String key) {
        sharedPreferencesEditor.putString(CURRENT_PROGRAM, key).commit();
    }

    private String getProgramUID(){
        return sharedPreferences.getString(CURRENT_PROGRAM, "");

    }

    private Program createNewProgram() {

        Program program = new Program(
                getUID(), getUsername(),
                "My New Program",
                new SimpleDateFormat("HH:mm:ss").format(new Date()),
                new SimpleDateFormat("mm dd, yyyy", Locale.US).format(new Date()),
                "");


        return saveProgramToFireBase(program);
    }

    private Program saveProgramToFireBase(Program program) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child(FireBaseUtils.FIRE_BASE_REFERENCE_PROGRAMS);
        String key = databaseReference.push().getKey();
        program.setKey(key);
        programRepository.insertProgram(program);
        saveProgramKeyToSharedPreferences(key);
        databaseReference.child(key).setValue(program).addOnCompleteListener(task -> Log.d(
                "aviv", "onComplete: " + task.getException())
        );

        return program;

    }

    public LiveData<List<Program>> provideAllPrograms() {
        return programRepository.getAllPrograms();
    }

    public void cleanCurrentProgramSharedPreferences() {
        sharedPreferencesEditor.remove(CURRENT_PROGRAM).commit();
    }


    public void updateProgram(){
        String p = getProgramUID();
        if(p.equals("")){
            return;
        }
        programRepository.getCurrentProgram(p)
               .observeOn(AndroidSchedulers.mainThread())
               .subscribeOn(Schedulers.io())
                .subscribe((program)->{
                    program.setCreatorUID(getUID());
                    program.setCreator(getUsername());
                    programRepository.updateProgramCreatorUID(program);
                });
    }

}

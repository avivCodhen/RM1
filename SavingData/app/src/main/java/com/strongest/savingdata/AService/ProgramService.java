package com.strongest.savingdata.AService;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.strongest.savingdata.AModels.UserModel.SharedUser;
import com.strongest.savingdata.AModels.UserModel.User;
import com.strongest.savingdata.AModels.programModel.Program;
import com.strongest.savingdata.AModels.programModel.ProgramRepository;
import com.strongest.savingdata.Activities.MyProgramsActivity;
import com.strongest.savingdata.AndroidServices.FireBaseMessageService;
import com.strongest.savingdata.Controllers.CallBacks;
import com.strongest.savingdata.Utils.FireBaseUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProgramService {


    public final static String CURRENT_PROGRAM = "current_program";
    public final static String NUM_OF_SHARED_PROGRAMS = "number_of_shared_programs";
    private UserService userService;
    private final ProgramRepository programRepository;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor sharedPreferencesEditor;
    private static final String TAG = "programservice";
    private ArrayList<SharedUser> sharedList = new ArrayList<>();
    private int count;


    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    DatabaseReference sharedProgramReference;


    @Inject
    public ProgramService(UserService userService, ProgramRepository programRepository, SharedPreferences sharedPreferences,
                          SharedPreferences.Editor sharedPreferencesEditor) {
        this.userService = userService;
        this.programRepository = programRepository;
        this.sharedPreferences = sharedPreferences;
        this.sharedPreferencesEditor = sharedPreferencesEditor;
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(FireBaseUtils.FIRE_BASE_REFERENCE_PROGRAMS);
        sharedProgramReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(FireBaseUtils.FIRE_BASE_REFERENCE_SHARED_PROGRAMS);

    }

    public void listenForSharedPrograms(CallBacks.Observer observer) {
        if (userService.isUserLoggedIn()) {
            sharedProgramReference.
                    orderByChild("recieverUID")
                    .equalTo(userService.getUserUID())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null && dataSnapshot.getChildrenCount() > 0) {
                                if (dataSnapshot.getChildrenCount() > getNumberOfSharedPrograms()) {
                                    count = 0;
                                    sharedList.clear();
                                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                                        SharedUser sharedUser = d.getValue(SharedUser.class);
                                        if (!sharedUser.isSeen()) {
                                            count++;
                                        }
                                        sharedList.add(sharedUser);
                                    }
                                    observer.notify(count);

                                }

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(userService.context, "Error", Toast.LENGTH_SHORT).show();

                        }
                    });
        }

    }


   /* public LiveData<Program> provideProgram() {
        String programKey = sharedPreferences.getString(CURRENT_PROGRAM, "");
        if (programKey.equals("")) {
            return provideNewProgram();
        } else {
            return programRepository.getProgramByKey(programKey);
        }
    }*/

    public String provideNewProgram() {
        cleanCurrentProgramSharedPreferences();
        Program p = createNewProgram();
        saveProgramToFireBase(p);
        return p.getKey();

    }

    public LiveData<Program> getProgramByKey(String key) {
        return programRepository.getProgramByKey(key);
    }

    public Program getNewProgram() {
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

    private void saveNumberOfSharedForPrograms(int num) {
        sharedPreferencesEditor.putInt(NUM_OF_SHARED_PROGRAMS, num).commit();
    }

    private int getNumberOfSharedPrograms() {
        return sharedPreferences.getInt(NUM_OF_SHARED_PROGRAMS, -1);
    }

    public String getProgramUID() {
        return sharedPreferences.getString(CURRENT_PROGRAM, "");

    }

    private Program createNewProgram() {

        Program program = new Program(
                getUID(),
                getUsername(),
                "My New Program",
                new SimpleDateFormat("HH:mm:ss").format(new Date()),
                new SimpleDateFormat("MMMM dd, yyyy", Locale.US).format(new Date()),
                "");

        return program;
    }

    public void updateProgram(Program p) {
        if (isuserAllowedToSaveProgramToDatabase()) {
            databaseReference.child(p.getKey()).setValue(p);
        }
        programRepository.updateProgram(p);
    }

    public void updateSharedProgram(Program p){
        sharedProgramReference.
                orderByChild("programUID")
                .equalTo(p.getKey())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String key = dataSnapshot.getChildren().iterator().next().getKey();
                        sharedProgramReference.child(key).child("seen").setValue(true);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private Program saveProgramToFireBase(Program program) {
        String key = databaseReference.push().getKey();
        program.setKey(key);
        if (isUserAllowedToCreateProgram()) {
            programRepository.insertProgram(program, null);
            saveProgramKeyToSharedPreferences(key);
        }
        if (isuserAllowedToSaveProgramToDatabase()) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                    .getReference()
                    .child(FireBaseUtils.FIRE_BASE_REFERENCE_PROGRAMS);

            programRepository.insertProgram(program, null);
            databaseReference.child(key).setValue(program).addOnCompleteListener(task -> Log.d(
                    "aviv", "onComplete: " + task.getException())
            );
        }


        return program;

    }

    private boolean isuserAllowedToSaveProgramToDatabase() {
        if (userService.isUserLoggedIn()) {
            return true;
        }
        return false;
    }

    private boolean isUserAllowedToCreateProgram() {
        if (userService.isUserLoggedIn()) {
            return true;
        } else {
            if (getProgramUID().equals("")) {
                return true;
            } else {
                return false;
            }
        }
    }

    public void insertProgram(Program p, CallBacks.OnFinish onFinish) {
        saveProgramKeyToSharedPreferences(p.getKey());
        programRepository.insertProgram(p, onFinish);

        return;
    }

    public void cleanCurrentProgramSharedPreferences() {
        sharedPreferencesEditor.remove(CURRENT_PROGRAM).commit();
    }


    public void updateProgram() {
        String p = getProgramUID();
        if (p.equals("")) {
            return;
        }
        programRepository.getCurrentProgram(p)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe((program) -> {
                    program.setCreatorUID(getUID());
                    program.setCreator(getUsername());
                    programRepository.updateProgram(program);
                });
    }

    public void fetchAllPrograms(DatabaseReference databaseReference, MutableLiveData<ArrayList<Program>> allPrograms,
                                 String orderBy, String equalTo) {
        Log.d(TAG, "fetchAllPrograms: ");
        ArrayList<Program> programs = new ArrayList<>();
        databaseReference
                .orderByChild(orderBy)
                .equalTo(equalTo)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            Program p = d.getValue(Program.class);
                            programs.add(d.getValue(Program.class));
                        }
                        allPrograms.setValue(programs);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        allPrograms.setValue(new ArrayList<>());

                    }
                });
    }

    public void fetchProgramByUID(SharedUser sharedUser, CallBacks.OnFinish onFinish) {
        databaseReference
                .child(sharedUser.getProgramUID())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Program p = dataSnapshot.getValue(Program.class);
                        p.isSeen = sharedUser.isSeen();
                        onFinish.onFinish(p);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void fetchAllProgramsShared(MutableLiveData<ArrayList<Program>> allPrograms,
                                       String orderBy) {

        ArrayList<Program> programs = new ArrayList<>();
        ArrayList<SharedUser> shared = new ArrayList<>();
        sharedProgramReference
                .orderByChild(orderBy)
                .equalTo(userService.getUserUID())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            shared.add(d.getValue(SharedUser.class));
                        }

                        if (shared.size() == 0) {
                            allPrograms.setValue(programs);
                        } else {

                            for (int i = 0; i < shared.size(); i++) {
                                fetchProgramByUID(shared.get(i), prog -> {
                                    programs.add((Program) prog);
                                    if (programs.size() == shared.size()) {
                                        allPrograms.setValue(programs);

                                    }
                                });
                            }
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    public void findSharedUsers(Program p, CallBacks.OnFinish onFinish) {
        sharedProgramReference
                .orderByChild("programUID")
                .equalTo(p.getKey())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int count = 0;
                        if (dataSnapshot == null) {
                            count = 0;
                            onFinish.onFinish(count);
                            return;
                        } else {
                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                count++;
                            }
                            onFinish.onFinish(count);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        onFinish.onFinish(-1);
                    }
                });
    }

    public void fetchAllProgramsByTag(MutableLiveData<ArrayList<Program>> allPrograms, String tag) {
        if (userService.isUserLoggedIn()) {

            if (tag.equals(MyProgramsActivity.FRAGMENT_USER_PROGRAMS)) {
                fetchAllPrograms(databaseReference, allPrograms, "creatorUID", userService.getUserUID());
            } else if (tag.equals(MyProgramsActivity.FRAGMENT_USER_SHARED_BY)) {
                fetchAllProgramsShared(allPrograms, "senderUID");

            }/* else if (tag.equals(MyProgramsActivity.FRAGMENT_USER_SHARED_FOR)) {
                fetchAllProgramsShared(allPrograms, "recieverUID");
            }*/
        } else {
            allPrograms.setValue(new ArrayList<>());
        }
    }

    /*public void removeCurrentProgram(ArrayList<Program> programs) {
        Iterator<Program> iter = programs.iterator();

        while (iter.hasNext()) {
            Program p = iter.next();
            if (p.getKey().equals(getProgramUID())) {
                iter.remove();
            }
        }
    }
*/
    public void annonymouseToUser(Program p) {
        if (p.getCreatorUID().equals("")) {

            p.setCreatorUID(userService.getUserUID());
            p.setCreator(userService.getUsername());
            updateProgram(p);
        }
    }

    public void shareProgramWithUser(Context context, Program p, User user) {
        SharedUser sharedUser = new SharedUser();
        sharedUser.setProgramUID(p.getKey());
        sharedUser.setSenderUID(userService.getUserUID());
        sharedUser.setRecieverUID(user.getUID());
        sharedUser.setSenderName(user.getName());
        sharedUser.setProgramName(p.getProgramName());
        String userToken = user.getUserToken();
        if (userToken.equals("")) {
            userToken = FireBaseMessageService.getToken(context);
        }
        sharedUser.setSenderToken(userToken);
        p.setUnShareable(true);
        p.setNumOfShared(p.getNumOfShared()+1);
        updateProgram(p);
        String key = sharedProgramReference.push().getKey();
        sharedProgramReference.child(key).setValue(sharedUser);
    }


    public void deleteProgram(Program p) {
        databaseReference.child(p.getKey()).setValue(null);

    }

    public ArrayList<SharedUser> getSharedList() {
        return sharedList;
    }

    public int getCount() {
        return count;
    }

    public Program duplicateProgram(Program p) {
        String newKey = databaseReference.push().getKey();
        p.setProgramName("Duplicate " + p.getProgramName());
        p.setKey(newKey);
        p.setCreatorUID(userService.getUserUID());
        databaseReference.child(newKey).setValue(p);
        return p;
    }

    public LiveData<List<Program>> getAllPrograms() {
        return programRepository.getAllPrograms();
    }

    public void updateSeenOnSharedProgram(Program p) {
        p.isSeen = true;
        updateSharedProgram(p);
    }
}

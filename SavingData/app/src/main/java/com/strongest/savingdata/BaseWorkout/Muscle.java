package com.strongest.savingdata.BaseWorkout;

import android.database.Cursor;

import com.strongest.savingdata.Database.Managers.MusclesDataManager;
import com.strongest.savingdata.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

//
//import static com.strongest.savingdata.BaseWorkout.Body.Muscle.MainMuscleEnum.ABS;
//import static com.strongest.savingdata.BaseWorkout.Body.Muscle.MainMuscleEnum.ANTERIOR_LEGS;
//import static com.strongest.savingdata.BaseWorkout.Body.Muscle.MainMuscleEnum.ANTERIOR_SHOULDERS;
//import static com.strongest.savingdata.BaseWorkout.Body.Muscle.MainMuscleEnum.ARMS;
//import static com.strongest.savingdata.BaseWorkout.Body.Muscle.MainMuscleEnum.BACK;
//import static com.strongest.savingdata.BaseWorkout.Body.Muscle.MainMuscleEnum.BICEPS;
//import static com.strongest.savingdata.BaseWorkout.Body.Muscle.MainMuscleEnum.CHEST;
//import static com.strongest.savingdata.BaseWorkout.Body.Muscle.MainMuscleEnum.LEGS;
//import static com.strongest.savingdata.BaseWorkout.Body.Muscle.MainMuscleEnum.POSTERIOR_LEGS;
//import static com.strongest.savingdata.BaseWorkout.Body.Muscle.MainMuscleEnum.REAR_SHOULDERS;
//import static com.strongest.savingdata.BaseWorkout.Body.Muscle.MainMuscleEnum.SHOULDERS;
//import static com.strongest.savingdata.BaseWorkout.Body.Muscle.MainMuscleEnum.TRICEPS;
//import static com.strongest.savingdata.BaseWorkout.Body.Size.BIG;
//import static com.strongest.savingdata.BaseWorkout.Body.Size.SMALL;
import static com.strongest.savingdata.Database.Muscles.DBMuscleHelper.CHILDREN;
import static com.strongest.savingdata.Database.Muscles.DBMuscleHelper.IMAGE;
import static com.strongest.savingdata.Database.Muscles.DBMuscleHelper.MUSCLE_DISPLAY;
import static com.strongest.savingdata.Database.Muscles.DBMuscleHelper.MUSCLE_INT;
import static com.strongest.savingdata.Database.Muscles.DBMuscleHelper.MUSCLE_NAME;
import static com.strongest.savingdata.Database.Muscles.DBMuscleHelper.MUSCLE_SIZE;
import static com.strongest.savingdata.Database.Muscles.DBMuscleHelper.PARENT;

/**
 * Created by Cohen on 8/21/2017.
 */


public class Muscle implements Serializable {

    private int id, muscle_int;
    private String muscle_name, muscle_size, muscle_display, parent, image, children;

    public Muscle() {

    }

    public static ArrayList<Muscle> getAllMuscles(MusclesDataManager dm) {
        return dm.getAllMuscles();
    }

    public static Muscle createMuscle(MusclesDataManager dm, String muscle) {

        muscle = muscle.toLowerCase();
        Cursor c = dm.getMuscleFromDB(muscle);
        Muscle m = new Muscle();
        if (c.getCount() != 0 && c.moveToNext()) {
            m.muscle_name = c.getString(c.getColumnIndex(MUSCLE_NAME));
            m.muscle_int = c.getInt(c.getColumnIndex(MUSCLE_INT));
            m.muscle_display = c.getString(c.getColumnIndex(MUSCLE_DISPLAY));
            m.muscle_size = c.getString(c.getColumnIndex(MUSCLE_SIZE));
            m.parent = c.getString(c.getColumnIndex(PARENT));
            m.image = c.getString(c.getColumnIndex(IMAGE));
            m.children = c.getString(c.getColumnIndex(CHILDREN));
        }
        c.close();
        return m;

    }


    public String getMuscle_display() {
        return muscle_display;
    }

    public void setChildren(String children) {
        this.children = children;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setMuscle_display(String muscle_display) {
        this.muscle_display = muscle_display;
    }

    public void setMuscle_int(int muscle_int) {
        this.muscle_int = muscle_int;
    }

    public void setMuscle_name(String muscle_name) {
        this.muscle_name = muscle_name;
    }

    public void setMuscle_size(String muscle_size) {
        this.muscle_size = muscle_size;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getParent() {
        return parent;
    }


    public String getImage() {
        return image;
    }

    public String getMuscle_size() {
        return muscle_size;
    }

    public String getMuscle_name() {
        return muscle_name;
    }

    public int getMuscle_int() {
        return muscle_int;
    }

    public String getChildren() {
        return children;
    }

    /**
     * this function parses the string containing % characters
     * and muscle names. this is used to recieve an string usuable to print
     * */
    public static String[] parseMuscles(String muscles) {
        int length = 0;
        for (int i = 0; i < muscles.length(); i++) {
            if (muscles.charAt(i) == '$') {
                length++;
            }
        }
        ArrayList<String> muList = new ArrayList();
        int point = 0;
        boolean isFirst = true;
        for (int i = 0; i < muscles.length(); i++) {
            if (muscles.charAt(i) == '$') {
                    String m = muscles.substring(point, i);
                    muList.add(m);
                point = i + 1;

            }
        }
        if(muList.size() > 3){
            muList.remove(0);
        }
        String[] arr = new String[muList.size()];
        arr = muList.toArray(arr);

        return arr;
    }

    public static class MuscleUI {

        private int color;
        private int image;

        public MuscleUI(int color, int image) {
            this.color = color;

            this.image = image;
        }

        public int getImage() {
            return image;
        }

        public int getColor() {
            return color;
        }
    }

    public static MuscleUI provideMuscleUI(Muscle muscle) {
        String m = muscle.getMuscle_name();
        switch (m) {
            case "chest":
                return new MuscleUI(R.color.color_chest, R.drawable.chest);
            case "back":
                return new MuscleUI(R.color.color_back, R.drawable.back);
            case "shoulders":
                return new MuscleUI(R.color.color_shoulders, R.drawable.shoulders);
            case "anterior_shoulders":
                return new MuscleUI(R.color.color_shoulders, R.drawable.shoulders);
            case "middle_shoulders":
                return new MuscleUI(R.color.color_shoulders, R.drawable.shoulders);
            case "legs":
                return new MuscleUI(R.color.color_legs, R.drawable.legs);
            case "quadriceps":
                return new MuscleUI(R.color.color_legs, R.drawable.legs);
            case "hip":
                return new MuscleUI(R.color.color_legs, R.drawable.legs);
            case "hamstring":
                return new MuscleUI(R.color.color_legs, R.drawable.legs);
            case "calf":
                return new MuscleUI(R.color.color_legs, R.drawable.legs);
            case "arms":
                return new MuscleUI(R.color.color_arms, R.drawable.arms);
            case "biceps":
                return new MuscleUI(R.color.color_arms, R.drawable.arms);
            case "triceps":
                return new MuscleUI(R.color.color_arms, R.drawable.arms);
            case "core":
                return new MuscleUI(R.color.color_arms, R.drawable.core);

        }
        return null;
    }

    public static class MuscleStatObject {
        private Muscle m;
        private int times;
        private MuscleUI mui;

        public MuscleStatObject(Muscle m) {
            this.m = m;
            this.mui = Muscle.provideMuscleUI(m);
        }

        public void incrementTimes() {
            this.times++;
        }

        public Muscle getM() {
            return m;
        }

        public MuscleUI getMui() {
            return mui;
        }

        public int getTimes() {
            return times;
        }
    }

}



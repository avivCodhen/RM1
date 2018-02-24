package com.strongest.savingdata.BaseWorkout;

import android.content.Context;
import android.database.Cursor;

import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.Database.Muscles.MusclesDataManager;
import com.strongest.savingdata.R;

import java.io.Serializable;
import java.util.ArrayList;

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

/*public abstract class Body {

    public enum Size {
        SMALL, BIG;
    }

    public enum TransversePlane {
        //Divides the body into upper and lower body.
        LOWER_BODY, UPPER_BODY
    }

    public enum Level {
        BEGINNERS, INTERMEDIATE, ADVANCED, ANY
    }*/

public class Muscle {

    private int id, muscle_int;
    private String muscle_name, muscle_size, muscle_display, parent, image, children;

    public Muscle() {

    }

    public static ArrayList<Muscle> getAllMuscles(MusclesDataManager dm){
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

    public static String[] parseMuscles(String muscles){
        int length = 0;
        for (int i = 0; i < muscles.length(); i++) {
            if(muscles.charAt(i) == '$'){
                length++;
            }
        }
        String[] arr = new String[length];
        for (int i = 0; i < arr.length; i++) {
            if(muscles.charAt(i) == '$'){
                arr[length--] = muscles.substring(0, i);
                muscles = muscles.substring(i, muscles.length()-1);
            }
        }
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
                return new MuscleUI(R.color.color_chest, R.drawable.muscle_chest);
            case "back":
                return new MuscleUI(R.color.color_back, R.drawable.muscle_back);
            case "shoulders":
                return new MuscleUI(R.color.color_shoulders, R.drawable.muscle_shoulders);

            case "legs":
                return new MuscleUI(R.color.color_legs, R.drawable.muscle_legs);
            case "arms":
                return new MuscleUI(R.color.color_arms, R.drawable.muscle_arms);
            case "biceps":
                return new MuscleUI(R.color.color_arms, R.drawable.muscle_arms);
            case "triceps":
                return new MuscleUI(R.color.color_arms, R.drawable.muscle_arms);

        }
        return null;
    }

    public static class MuscleStatObject{
        private Muscle m;
        private int times;
        private MuscleUI mui;
        public MuscleStatObject(Muscle m){
            this.m = m;
            this.mui = Muscle.provideMuscleUI(m);
        }

        public void incrementTimes(){
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


   /* public abstract static class Muscle implements Serializable {

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Size getSize() {
            return size;
        }

        public void setSize(Size size) {
            this.size = size;
        }

        public void setMuscle(MainMuscleEnum muscle) {
            this.muscle = muscle;
        }

        public enum MainMuscleEnum {

            //BIG muscles
            CHEST, BACK, SHOULDERS,
            //SMALL muscles
            LEGS, ARMS,
            TRICEPS, BICEPS,
            ANTERIOR_SHOULDERS, REAR_SHOULDERS,
            ANTERIOR_LEGS, POSTERIOR_LEGS,
            ABS;

            public static MainMuscleEnum getEnum(int value) {

                return values()[value];
            }

            public boolean isBig() {
                if (this == CHEST || this == BACK || this == LEGS) {
                    return true;
                }
                return false;
            }
        }

        public enum Minor {

            //SYNERGIST muscles
            TRAPEZIUS, CORE, LOWER_BACK, CALVES, WRIST,
        }

        public void setContext(Context context) {
            this.context = context;
        }

        public MainMuscleEnum getMuscle() {
            return muscle;
        }

        private String name;
        private Body.Size size;
        private MainMuscleEnum muscle;


        private static Context context;

        public Muscle(Context context) {
            this.context = context;
        }


        public static Body.Muscle whichMuscle(Context c, MainMuscleEnum t) {
            switch (t) {
                case CHEST:
                    return new Body.Muscle.Chest(c);
                case BACK:
                    return new Body.Muscle.Back(c);
                case LEGS:
                    return new Body.Muscle.Legs(c);
                case SHOULDERS:
                    return new Body.Muscle.Shoulders(c);
                case ARMS:
                    return new Body.Muscle.Arms(c);
                case TRICEPS:
                    return new Body.Muscle.Triceps(c);
                case BICEPS:
                    return new Body.Muscle.Biceps(c);
                case ANTERIOR_SHOULDERS:
                    return new Body.Muscle.AnteriorShoulders(c);
                case REAR_SHOULDERS:
                    return new Body.Muscle.RearShoulders(c);
                case ANTERIOR_LEGS:
                    return new Body.Muscle.AnteriorLegs(c);
                case POSTERIOR_LEGS:
                    return new Body.Muscle.PosteriorLegs(c);
                case ABS:
                    return new Body.Muscle.Abs(c);
            }
            return null;
        }

        public static class Chest extends Muscle {

            @Override
            public String getName() {
                return context.getResources().getString(R.string.chest);
            }

            public Chest(Context context) {
                super(context);
                initMuscle();
            }

            private void initMuscle() {
                setMuscle(CHEST);
                setSize(BIG);
            }

        }

        public static class Back extends Muscle {


            @Override
            public String getName() {
                return context.getResources().getString(R.string.back);
            }

            public Back(Context context) {
                super(context);
                initMuscle();
            }

            private void initMuscle() {
                setMuscle(BACK);
                setSize(BIG);
            }
        }

        public static class Legs extends Muscle {
            @Override
            public String getName() {
                return context.getResources().getString(R.string.legs);
            }


            public Legs(Context context) {
                super(context);
                initMuscle();
            }

            private void initMuscle() {
                setMuscle(LEGS);
                setSize(BIG);
            }
        }

        public static class Shoulders extends Muscle {
            @Override
            public String getName() {
                return context.getResources().getString(R.string.shoulders);
            }

            public Shoulders(Context context) {
                super(context);
                initMuscle();
            }

            private void initMuscle() {
                setMuscle(SHOULDERS);
                setSize(SMALL);
            }
        }

        public static class Triceps extends Muscle {

            @Override
            public String getName() {
                return context.getResources().getString(R.string.triceps);
            }

            public Triceps(Context context) {
                super(context);
                initMuscle();
            }

            private void initMuscle() {
                setMuscle(TRICEPS);
                setSize(SMALL);
            }

        }

        public static class Biceps extends Muscle {

            @Override
            public String getName() {
                return context.getResources().getString(R.string.biceps);
            }

            public Biceps(Context context) {
                super(context);
                initMuscle();
            }

            private void initMuscle() {
                setMuscle(BICEPS);
                setSize(SMALL);
            }
        }

        public static class Arms extends Muscle {
            @Override
            public String getName() {
                return context.getResources().getString(R.string.arms);
            }

            public Arms(Context context) {
                super(context);
                initMuscle();
            }

            private void initMuscle() {
                setMuscle(ARMS);
                setSize(BIG);
            }

        }

        public static class PosteriorLegs extends Muscle {

            @Override
            public String getName() {
                return context.getResources().getString(R.string.posterior_legs);
            }

            public PosteriorLegs(Context context) {
                super(context);
                initMuscle();
            }

            private void initMuscle() {
                setMuscle(POSTERIOR_LEGS);
                setSize(SMALL);
            }
        }

        public static class AnteriorLegs extends Muscle {

            @Override
            public String getName() {
                return context.getResources().getString(R.string.anterior_legs);
            }

            public AnteriorLegs(Context context) {
                super(context);
                initMuscle();
            }

            private void initMuscle() {
                setMuscle(ANTERIOR_LEGS);
                setSize(SMALL);
            }
        }

        public static class RearShoulders extends Muscle {

            @Override
            public String getName() {
                return context.getResources().getString(R.string.rear_shoulders);
            }

            public RearShoulders(Context context) {
                super(context);
                initMuscle();
            }

            private void initMuscle() {
                setMuscle(REAR_SHOULDERS);
                setSize(SMALL);
            }
        }

        public static class Abs extends Muscle {

            @Override
            public String getName() {
                return context.getResources().getString(R.string.abs);
            }

            public Abs(Context context) {
                super(context);
                initMuscle();
            }

            private void initMuscle() {
                setMuscle(ABS);
                setSize(SMALL);
            }
        }

        private static class AnteriorShoulders extends Muscle {
            @Override
            public String getName() {
                return context.getResources().getString(R.string.anterior_shoulders);
            }

            public AnteriorShoulders(Context c) {
                super(c);
                initMuscle();
            }

            private void initMuscle() {
                setMuscle(ANTERIOR_SHOULDERS);
                setSize(SMALL);
            }
        }*/





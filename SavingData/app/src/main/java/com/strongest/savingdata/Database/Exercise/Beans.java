package com.strongest.savingdata.Database.Exercise;

import android.support.annotation.NonNull;

import com.strongest.savingdata.BaseWorkout.Muscle;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Cohen on 9/12/2017.
 */

public class Beans implements Serializable {

    private int intensity;
    private int pyramid;
    private int sets;
    private int default_int;

    private double weight;
    public String image;
    public String muscles;
    public String name;
    public int id;
    public String details;
    public Muscle muscle;
    public String primaryMuscle;
    public int level;
    public String type;
    public int mechanicalStress;
    public int metabolicStress;
    private int value;

    public int m_biceps;
    public int m_triceps;
    public int m_anterior_shoulders;
    public int m_rear_shoulders;
    public int p_legs_posterior;
    public int p_legs_anterior;

    public int chest;
    public int back;
    public int anteriorLegs;
    public int posteriorLegs;
    public int a_shoulders;
    public int r_shoulders;
    public int biceps;
    public int triceps;
    public int wrist;
    public int calves;
    public int core;
    public int lower_back;
    public int trapezius;

    public boolean loaded;

    public Beans() {

    }

    public static ArrayList<Beans> sortByAccessory(ArrayList<Beans> original) {
        ArrayList<Beans> sortedList = new ArrayList<>();
        String[] acs = {
                "Barbell",
                "Dumbbell",
                "Cable",
                "Machine"
        };
        ArrayList<ArrayList<Beans>> matrix = new ArrayList<>();
        for (int i = 0; i < acs.length + 1; i++) {
            matrix.add(new ArrayList<Beans>());
        }
        for (int i = 0; i < original.size(); i++) {
            boolean flag = true;
            innerloop:
            for (int j = 0; j < acs.length; j++) {
                if (original.get(i).getName().contains(acs[j])) {
                    matrix.get(j).add(original.get(i));
                    flag = false;
                    break innerloop;
                }
            }
            if (flag)
                matrix.get(matrix.size() - 1).add(original.get(i));
        }
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.get(i).size(); j++) {
                sortedList.add(matrix.get(i).get(j));
            }
        }
        return sortedList;
    }

    public static Beans createReps(int defaultInt, int id, String name, int intensity, String detail, int pyramid, int mec, int met) {
        Beans beans = new Beans();
        beans.setDefault_int(defaultInt);
        beans.setId(id);
        beans.setName(name);
        beans.setIntensity(intensity);
        beans.setDetails(detail);
        beans.setPyramid(pyramid);
        beans.setMechanicalStress(mec);
        beans.setMetabolicStress(met);
        return beans;
    }

    public static Beans createRest(int id, String name, String type, int defaultInt) {
        Beans beans = new Beans();
        beans.setId(id);
        beans.setName(name);
        beans.setType(type);
        beans.setDefault_int(defaultInt);

        return beans;
    }

    public static Beans createSets(int id, String name, String type, int defaultInt) {
        Beans beans = new Beans();
        beans.setId(id);
        beans.setName(name);
        beans.setType(type);
        beans.setDefault_int(defaultInt);

        return beans;
    }

    public static Beans createExercise(String muscles, String primaryMuscle, int id, String name, String type,
                                       int level, String detail, Muscle muscle, String image,
                                       double weight,
                                       int m_a_s, int m_r_s, int m_biceps, int m_triceps,
                                       int posteriorLegs, int anteriorLegs, int p_p_l, int p_a_l) {
        Beans beans = new Beans();

        beans.setLevel(level);
        beans.setDetails(detail);
        beans.setId(id);
        beans.setType(type);
        beans.setName(name);
        beans.setWeight(weight);
        beans.setImage(image);
        beans.setMuscle(muscle);
        beans.setPrimaryMuscle(primaryMuscle);
        beans.setMuscles(muscles);
        beans.setM_anterior_shoulders(m_a_s);
        beans.setM_rear_shoulders(m_r_s);
        beans.setM_biceps(m_biceps);
        beans.setM_triceps(m_triceps);
        beans.setPosteriorLegs(posteriorLegs);
        beans.setAnteriorLegs(anteriorLegs);
        beans.setP_legs_anterior(p_p_l);
        beans.setP_legs_posterior(p_a_l);
        return beans;


    }


    public static ArrayList<Beans> sortList(ArrayList<Beans> list) {
        ArrayList<Beans> sortedList = new ArrayList<>();
        int start = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getDefault_int() == 0) {
                sortedList.add(list.get(i));
                list.remove(i);
                i--;
            }
        }
        for (int i = 0; i < list.size(); i++) {
            sortedList.add(list.get(i));
        }
        return sortedList;
    }


    public static String parsePrimaryMuscle(String muscles) {
        if (muscles != null) {

            for (int i = 0; i < muscles.length(); i++) {
                if (muscles.charAt(i) == '$') {
                    return muscles.substring(0, i);
                }

            }
        }
        return muscles;
    }

    public String getPrimaryMuscle() {
        return primaryMuscle;
    }

    public void setPrimaryMuscle(String primaryMuscle) {
        this.primaryMuscle = primaryMuscle;
    }

    public String getMuscles() {
        return muscles;
    }

    public void setMuscles(String muscles) {
        this.muscles = muscles;
    }

    public int getDefault_int() {
        return default_int;
    }

    public void setDefault_int(int default_int) {
        this.default_int = default_int;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public int[] damages;


    public int getIntensity() {
        return intensity;
    }

    public int getPyramid() {
        return pyramid;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    public void setPyramid(int pyramid) {
        this.pyramid = pyramid;
    }

    public int getChest() {
        return chest;
    }

    public void setChest(int chest) {
        this.chest = chest;
    }

    public int getBack() {
        return back;
    }

    public void setBack(int back) {
        this.back = back;
    }

    public int getAnteriorLegs() {
        return anteriorLegs;
    }

    public void setAnteriorLegs(int anteriorLegs) {
        this.anteriorLegs = anteriorLegs;
    }

    public int getPosteriorLegs() {
        return posteriorLegs;
    }

    public void setPosteriorLegs(int posteriorLegs) {
        this.posteriorLegs = posteriorLegs;
    }

    public int getA_shoulders() {
        return a_shoulders;
    }

    public void setA_shoulders(int a_shoulders) {
        this.a_shoulders = a_shoulders;
    }

    public int getR_shoulders() {
        return r_shoulders;
    }

    public void setR_shoulders(int r_shoulders) {
        this.r_shoulders = r_shoulders;
    }

    public int getBiceps() {
        return biceps;
    }

    public void setBiceps(int biceps) {
        this.biceps = biceps;
    }

    public int getTriceps() {
        return triceps;
    }

    public void setTriceps(int triceps) {
        this.triceps = triceps;
    }

    public int getWrist() {
        return wrist;
    }

    public void setWrist(int wrist) {
        this.wrist = wrist;
    }

    public int getCalves() {
        return calves;
    }

    public void setCalves(int calves) {
        this.calves = calves;
    }

    public int getCore() {
        return core;
    }

    public void setCore(int core) {
        this.core = core;
    }

    public int getLower_back() {
        return lower_back;
    }

    public void setLower_back(int lower_back) {
        this.lower_back = lower_back;
    }

    public int getTrapezius() {
        return trapezius;
    }

    public void setTrapezius(int trapezius) {
        this.trapezius = trapezius;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setMuscle(Muscle muscle) {
        this.muscle = muscle;
    }


    public Muscle getMuscle() {
        return muscle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDetails() {
        return details;
    }


    public int getLevel() {
        return level;
    }

    public String getType() {
        return type;
    }

    public int getMechanicalStress() {
        return mechanicalStress;
    }

    public int getMetabolicStress() {
        return metabolicStress;
    }

    public int[] getDamages() {
        return damages;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMechanicalStress(int mechanicalStress) {
        this.mechanicalStress = mechanicalStress;
    }

    public void setMetabolicStress(int metabolicStress) {
        this.metabolicStress = metabolicStress;
    }

    public void setDamages(int... damages) {
        this.damages = damages;
    }


    public int getM_biceps() {
        return m_biceps;
    }

    public void setM_biceps(int m_biceps) {
        this.m_biceps = m_biceps;
    }

    public int getM_triceps() {
        return m_triceps;
    }

    public void setM_triceps(int m_triceps) {
        this.m_triceps = m_triceps;
    }

    public int getM_anterior_shoulders() {
        return m_anterior_shoulders;
    }

    public void setM_anterior_shoulders(int m_anterior_shoulders) {
        this.m_anterior_shoulders = m_anterior_shoulders;
    }

    public int getM_rear_shoulders() {
        return m_rear_shoulders;
    }

    public void setM_rear_shoulders(int m_rear_shoulders) {
        this.m_rear_shoulders = m_rear_shoulders;
    }

    public int getP_legs_posterior() {
        return p_legs_posterior;
    }

    public void setP_legs_posterior(int p_legs_posterior) {
        this.p_legs_posterior = p_legs_posterior;
    }

    public int getP_legs_anterior() {
        return p_legs_anterior;
    }

    public void setP_legs_anterior(int p_legs_anterior) {
        this.p_legs_anterior = p_legs_anterior;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight += weight;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }


}

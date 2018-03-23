package com.strongest.savingdata.AlgorithmStats;

import android.content.Context;

import com.strongest.savingdata.AlgorithmLayout.LayoutManager;
import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.Database.Exercise.Beans;
import com.strongest.savingdata.AlgorithmLayout.PLObject;

import java.util.ArrayList;

/**
 * Created by Cohen on 10/8/2017.
 */

public class Calculator extends StatsConstants {

    private StatsHolder statsHolder;
    private ProgressStats progressStats;
    private ProgressProcessor progressProgressor;
    private BodyStatsProcessor bodyStatsProcessor;
    //private DataManager dataManager;
    private LayoutManager pLayoutManager;
    private Context contexts;

    private enum Stress {
        MECHANICAL, MEDIUM, METABOLIC;
    }

    public enum Types {
        Beginners, Isolation, Complex;
    }

    public Calculator(LayoutManager pLayoutManager) {
        this.contexts = contexts;
        //this.cleanLayout = cleanLayout;
        //   dataManager = new DataManager(contexts);
        this.pLayoutManager = pLayoutManager;
        statsHolder = new StatsHolder();
    }

    public void processProgressStats(Muscle muscle, ProgressStatsMinMaxBean s, int position) {

        progressProgressor = new ProgressProcessor(s, muscle);
        this.progressStats = new ProgressStats(
                progressProgressor.getMinDamage(),
                progressProgressor.getMaxDamage(),
                progressProgressor.getMinMetabolic(),
                progressProgressor.getMinMechanical(),
                progressProgressor.getMaxMetabolic(),
                progressProgressor.getMaxMechanical()
        );
        this.statsHolder.setProgressStats(progressStats);
    }

    private void processExerciseViewsStats(int position) {
        bodyStatsProcessor = new BodyStatsProcessor(pLayoutManager, position);
        statsHolder.setDamage(bodyStatsProcessor.getBlockDamage());
        statsHolder.setMechanical(bodyStatsProcessor.blockMechanical);
        statsHolder.setMetabolic(bodyStatsProcessor.getBlockMetabolic());
        statsHolder.setExerciseStats(bodyStatsProcessor.getExerciseStats());
    }

    public StatsHolder calculateStats(Muscle muscle, int position, ProgressStatsMinMaxBean s, boolean hasStatsHolder) {
        statsHolder = new StatsHolder();
        if (hasStatsHolder) {
            processExerciseViewsStats(position);
        }
        statsHolder.setMinMaxBean(s);
        processProgressStats(muscle, s, position);

        return statsHolder;
    }

    private void calculateBlock() {

    }


    /**
     * this class is responsible to calculate minimum amd maximum
     * of damage, mechanical and metabolic stress of an entire muscle exercise block
     * for the progress bar
     */
    private class ProgressProcessor {

        private int minDamage;
        private int maxDamage;

        private int minMetabolic;
        private int minMechanical;

        private int maxMetabolic;
        private int maxMechanical;
        private ProgressStatsMinMaxBean s;
        private Muscle muscle;


        //this constructor provides penaltys or bonuses for levels and muscle size
        public ProgressProcessor(ProgressStatsMinMaxBean s, Muscle muscle) {

            this.s = s;
            this.muscle = muscle;
            if (muscle.getMuscle_size() != "big") {
                minDamage -= 11;
                maxDamage -= 10;
                maxMechanical -= 10;
                maxMetabolic -= 10;
            } else {
                maxDamage += 5;
                maxMechanical += 5;
                maxMetabolic += 5;
            }
            calculate();
        }

        public void calculate() {
            calcDamage();
            calcMinMetabolic();
            calcMaxMetabolic();
            calcMaxMechanical();
        }

        public void calcDamage() {
            minDamage += s.getMinB() * B_DAMAGE + s.getMinS() * S_DAMAGE + s.getMinC() * C_DAMAGE;
            maxDamage += s.getMaxB() * B_DAMAGE + s.getMaxS() * S_DAMAGE + s.getMaxC() * C_DAMAGE + s.getMaxMethod() * METHOD_DAMAGE;

        }

        private void calcMinMetabolic() {
            int sumExerciseMetabolic = s.getMinB() * B_METABOLIC_STRESS + s.getMinS() * S_METABOLIC_STRESS + s.getMinC() * C_METABOLIC_STRESS;
            int sumMinRep = s.getMinExercise() * MIN_STRESS_POINTS;
            //int sumMethod = maxMethod * 2;
            minMetabolic = sumExerciseMetabolic + sumMinRep;
            minMechanical = minMetabolic;


        }

        private void calcMaxMetabolic() {
            int sumExerciseMetabolic = s.getMinB() * B_METABOLIC_STRESS + s.getMaxS() * S_METABOLIC_STRESS +
                    s.getMaxC() * C_METABOLIC_STRESS;

            int sumMaxRep = s.maxExercise * MAX_METABOLIC_POINTS;
            int sumMethod = s.maxMethod * METHOD_METABOLIC_STRESS;
            maxMetabolic += (sumExerciseMetabolic + sumMaxRep + sumMethod);
        }

        private void calcMaxMechanical() {
            int sumExerciseMetabolic = s.getMinB() * B_MECHANICAL_STRESS + s.getMaxS() * S_MECHANICAL_STRESS +
                    s.getMaxC() * C_MECHANICAL_STRESS;
            int sumRep = s.maxExercise * MAX_MECHANICAL_POINTS;
            int sumMethod = s.maxMethod * METHOD_MECHANICAL_STRESS;
            maxMechanical += (sumExerciseMetabolic + sumRep + sumMethod);
        }

        public void calcMinMechanical() {

        }

        public int getMinDamage() {
            return minDamage;
        }

        public int getMaxDamage() {
            return maxDamage;
        }

        public int getMinMetabolic() {
            return minMetabolic;
        }

        public int getMaxMetabolic() {
            return maxMetabolic;
        }

        public int getMinMechanical() {
            return minMechanical;
        }

        public int getMaxMechanical() {
            return maxMechanical;
        }
    }

    /**
     * this class is responsible to calculate all the exercises stats
     * of damage, mechanical and metabolic
     * for the progress value
     * on the way, calculates stats for exercisestats class
     */
    private class BodyStatsProcessor {

        private int blockDamage;
        private int blockMechanical;
        private int blockMetabolic;
        private ExerciseStats exerciseStats;
        private LayoutManager layoutManager;

        public BodyStatsProcessor(LayoutManager layoutManager, int index) {

            this.layoutManager = layoutManager;
            calculateBlock(index);
        }

        public void calculateBlock(int index) {
            exerciseStats = new ExerciseStats();
            ArrayList<PLObject> cleanLayout = layoutManager.getCleanSplitWorkout(index);
            for (int i = 0; i < cleanLayout.size(); i++) {
                PLObject.ExerciseProfile ep = (PLObject.ExerciseProfile) cleanLayout.get(i);
                getBeansStats(ep, exerciseStats);
            }
        }


        //0 = damage, 1 = metabolic, 3 = mechanical
        private void getBeansStats(PLObject.ExerciseProfile ep, ExerciseStats exerciseStats) {
            //  ExerciseProfileView epv = ep.getExerciseProfileView();
            if (ep.getSets() == null || ep.getSets().get(0) == null) {
                return;
            }
            exerciseStats.setNumOfExercises(1);
          /*  Sets sets = ep.getSets().get(0);
            int damage_method = 0;
            int stress_method = 0;


            if (sets.getMethod() != null) {
                damage_method = METHOD_DAMAGE;
                stress_method = METHOD_METABOLIC_STRESS;
                exerciseStats.setNumOfMethod(1);
            }*/

           /* if (beansHolder.getExercise().getType() == Types.Complex.ordinal()) {
                exerciseStats.setNumOfComplex(1);
            }*/
          /*  blockDamage += calcDamage(sets.getExercise(), damage_method);
            blockMetabolic += calcMetabolic(sets.getExercise(), sets.getRep(), stress_method, Stress.METABOLIC);
            blockMechanical += calcMechanical(sets.getExercise(), sets.getRep(), stress_method, Stress.MECHANICAL);
*/        }

        private int calcDamage(Beans ex, int method) {
            if (ex != null) {
                switch (ex.getLevel()) {
                    case 0:
                        return B_DAMAGE + method;

                    case 1:
                        return S_DAMAGE + method;
                    case 2:
                        return C_DAMAGE + method;
                }
            }
            return 0;
        }

        private int calcMetabolic(Beans ex, Beans rep, int method, Stress stress) {
            int metabolic = 0;
            if (ex == null) {
                metabolic = 0;
            } else {

               /* switch (ex.getType()) {
                    case 0:
                        metabolic += B_METABOLIC_STRESS;
                        break;
                    case 1:
                        metabolic += S_METABOLIC_STRESS;
                        break;
                    case 2:
                        metabolic += C_METABOLIC_STRESS;
                        break;
                }
*/

                //metabolic += rep.getMetabolicStress();
                metabolic += calcStress(rep.getIntensity(), stress);
                metabolic += method;
            }
            return metabolic;
        }

        private int calcMechanical(Beans ex, Beans rep, int method, Stress stress) {
            int mechanical = 0;

            if (ex == null) {
                mechanical = 0;
            } else {
              /*  switch (ex.getType()) {
                    case 0:
                        mechanical += B_MECHANICAL_STRESS;
                        break;
                    case 1:
                        mechanical += S_MECHANICAL_STRESS;
                        break;
                    case 2:
                        mechanical += C_MECHANICAL_STRESS;
                        break;
                }*/

                //mechanical += rep.getMechanicalStress();
                mechanical += calcStress(rep.getIntensity(), stress);
                mechanical += method;
            }
            return mechanical;
        }

        private int calcStress(int r, Stress stress) {
            if (stress.ordinal() == Stress.MECHANICAL.ordinal()) {
                if (r == stress.ordinal()) {
                    //  return MAX_MECHANICAL_POINTS + 5; //so mechanical will need less points
                    return MAX_MECHANICAL_POINTS;
                } else if (r == Stress.METABOLIC.ordinal()) {
                    return 0;
                } else {
                    return MAX_MEDIUM_STRESS;
                }
            }

            if (stress.ordinal() == Stress.METABOLIC.ordinal()) {
                if (r == stress.ordinal()) {
                    return MAX_METABOLIC_POINTS;
                } else if (r == Stress.MECHANICAL.ordinal()) {
                    return MIN_STRESS_POINTS;
                } else {
                    return MAX_MEDIUM_STRESS;
                }
            }
            return MAX_MEDIUM_STRESS;
        }

        public int getBlockDamage() {
            return blockDamage;
        }

        public int getBlockMechanical() {
            return blockMechanical;
        }

        public int getBlockMetabolic() {
            return blockMetabolic;
        }

        public ExerciseStats getExerciseStats() {
            return exerciseStats;
        }
    }

    public static class ProgressStatsMinMaxBean {


        private String routine;
        private int level;
        private int minExercise;
        private int maxExercise;

        private int minB;
        private int minS;
        private int minC;
        private int maxB;
        private int maxS;
        private int maxC;

        private int maxMethod;

        public ProgressStatsMinMaxBean() {

        }

        public int getMinExercise() {
            return minExercise;
        }

        public int getMaxExercise() {
            return maxExercise;
        }

        public int getMinB() {
            return minB;
        }

        public int getMinC() {
            return minC;
        }

        public int getMinS() {
            return minS;
        }

        public int getMaxB() {
            return maxB;
        }

        public int getMaxS() {
            return maxS;
        }

        public int getMaxC() {
            return maxC;
        }

        public int getMaxMethod() {
            return maxMethod;
        }

        public void setMinExercise(int minExercise) {
            this.minExercise = minExercise;
        }

        public void setMaxExercise(int maxExercise) {
            this.maxExercise = maxExercise;
        }

        public void setMinB(int minB) {
            this.minB = minB;
        }

        public void setMinS(int minS) {
            this.minS = minS;
        }

        public void setMinC(int minC) {
            this.minC = minC;
        }

        public void setMaxB(int maxB) {
            this.maxB = maxB;
        }

        public void setMaxS(int maxS) {
            this.maxS = maxS;
        }

        public void setMaxC(int maxC) {
            this.maxC = maxC;
        }

        public void setMaxMethod(int maxMethod) {
            this.maxMethod = maxMethod;
        }

        public String getRoutine() {
            return routine;
        }

        public void setRoutine(String routine) {
            this.routine = routine;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }
    }


}


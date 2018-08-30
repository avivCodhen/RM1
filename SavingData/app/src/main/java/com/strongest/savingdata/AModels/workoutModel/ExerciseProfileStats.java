package com.strongest.savingdata.AModels.workoutModel;

import com.strongest.savingdata.AModels.workoutModel.PLObject.ExerciseProfile;
import com.strongest.savingdata.Database.Exercise.ExerciseSet;

import java.util.ArrayList;

/**
 * Created by Cohen on 3/21/2018.
 */

public class ExerciseProfileStats {

    private int totalSets;
    private int totalReps;
    private String totalRest;
    private double totalVolume;
    private String allReps;

    public static ExerciseProfileStats getInstance(ExerciseProfile ep) {
        ExerciseProfileStats epStats = new ExerciseProfileStats();
        epStats.totalSets = calculateTotalSets(ep);
        epStats.totalReps = calculateTotalReps(ep);
        epStats.totalRest = calculateTotalRest(ep);
        epStats.totalVolume = calculateTotalVolume(ep);
        epStats.allReps = writeAllReps(ep);
        return epStats;
    }

    public ExerciseProfileStats() {

    }

    public static String writeAllReps(ExerciseProfile ep) {
        String all_reps = "";
        ArrayList<String> repsList = new ArrayList<>();
        for (int i = 0; i < ep.getSets().size(); i++) {
            String rep = ep.getSets().get(i).getExerciseSet().getRep();
            all_reps += rep;
            all_reps += ", ";
            repsList.add(rep);
        }
        boolean notSame = false;
        for (int j = 1; j < repsList.size(); j++) {
            if(repsList.get(j-1).equals(repsList.get(j))){

            }else{
                notSame = true;
                break;
            }
        }
        if(!notSame){
            if(repsList.size() != 0)
            all_reps = repsList.get(0);
        }else{
            all_reps = all_reps.substring(0, all_reps.length() - 2);
        }

        return all_reps;
    }

    public static double calcSetVolume(ExerciseSet exerciseSet) {
        double totalSetVolume = 0;
        int rep = calcRep(exerciseSet.getRep());
        totalSetVolume = rep * exerciseSet.getWeight();
        return totalSetVolume;
    }

    public static double calculateTotalVolume(ExerciseProfile ep) {
        double totalVolume = 0;
        for (int i = 0; i < ep.getSets().size(); i++) {
            totalVolume += calcSetVolume(ep.getSets().get(i).getExerciseSet());
            for (int j = 0; j < ep.getSets().get(i).intraSets.size(); j++) {
                totalVolume += calcSetVolume(ep.getSets().get(i).intraSets.get(j).getExerciseSet());
            }
        }
        return totalVolume;
    }


    public static String calculateTotalRest(ExerciseProfile ep) {

        int totalRest = 0;
        for (int i = 0; i < ep.getSets().size(); i++) {
            String rest = ep.getSets().get(i).getExerciseSet().getRest();
            totalRest = calcRest(rest);
            for (int j = 0; j < ep.getSets().get(i).intraSets.size(); j++) {
                String intraRest = ep.getSets().get(i).intraSets.get(j).getExerciseSet().getRest();
                totalRest += calcRest(intraRest);
            }
        }
        return restToString(totalRest);
    }

    public static int calcRest(String rest) {
        String[] timeList = rest.split(":");
        int m = Integer.parseInt(timeList[0]);
        int s = Integer.parseInt(timeList[1]);
        return m * 60 + s;
    }

    public static String restToString(int pTime) {
        return String.format("%02d:%02d", pTime / 60, pTime % 60);
    }


    public static int calcRep(String rep) {
        int singleRep = 0;
        int leftNum = 0;
        int rightNum = 0;
        int flag = 0;
        if (rep.contains("-")) {
            for (int i = 0; i < rep.length(); i++) {
                if (rep.charAt(i) == '-') {
                    leftNum = Integer.parseInt(rep.substring(flag, i));
                    flag = i;
                }
            }
            rightNum = Integer.parseInt(rep.substring(flag + 1, rep.length()));

        } else {
            singleRep = Integer.parseInt(rep);
            return singleRep;
        }
        return (leftNum + rightNum) / 2;
    }

    public static int calculateTotalReps(ExerciseProfile ep) {
        int totalReps = 0;
        for (int i = 0; i < ep.getSets().size(); i++) {
            String setRep = ep.getSets().get(i).getExerciseSet().getRep();
            if (setRep == null) {
                return 0;
            }
            totalReps += calcRep(setRep);
            for (int j = 0; j < ep.getSets().get(i).intraSets.size(); j++) {
                String intraRep = ep.getSets().get(i).intraSets.get(j).getExerciseSet().getRep();
                totalReps += calcRep(intraRep);
            }
        }
        return totalReps;

    }

    public static int calculateTotalSets(ExerciseProfile ep) {
        int totalSets = 0;
        int numOfSets = ep.getSets().size();
        for (int i = 0; i < numOfSets; i++) {
            totalSets++;
            for (int j = 0; j < ep.getSets().get(i).intraSets.size(); j++) {
                totalSets++;
            }
        }
        return totalSets;
    }


    public int getTotalReps() {
        return totalReps;
    }

    public String getTotalRest() {
        return totalRest;
    }

    public double getTotalVolume() {
        return totalVolume;
    }

    public int getTotalSets() {
        return totalSets;
    }

    public String getAllReps() {
        return allReps;
    }

    public void setAllReps(String allReps) {
        this.allReps = allReps;
    }
}
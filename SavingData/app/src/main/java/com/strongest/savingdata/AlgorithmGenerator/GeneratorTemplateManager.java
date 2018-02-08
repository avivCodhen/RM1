package com.strongest.savingdata.AlgorithmGenerator;


import com.strongest.savingdata.BaseWorkout.Muscle;

/**
 * Created by Cohen on 10/18/2017.
 */

public class GeneratorTemplateManager {

    private int numOfExercises;
    private final Muscle currentBody;
    private final Load load;
    private ExerciseTemplate exerciseTemplate;
    private RepsTemplate repsTemplate;
    private MethodTemplate methodTemplate;
    private RestTemplate restTemplate;

    public GeneratorTemplateManager(int numOfExercises, Muscle currentMuscle, Load load){
        this.numOfExercises = numOfExercises;

        this.currentBody = currentMuscle;
        this.load = load;
    }


    public BParams[][] getExerciseTemplate(){
            exerciseTemplate = new ExerciseTemplate(currentBody, load);
            return exerciseTemplate.getTemplate(numOfExercises);

    }
    public BParams[][] getRepsTemplate(){
        repsTemplate = new RepsTemplate(currentBody, load);
        return repsTemplate.getTemplate(numOfExercises);
    }
    public BParams[][] getMethoTemplate(){
        methodTemplate = new MethodTemplate(currentBody, load);
        return methodTemplate.getTemplate(numOfExercises);
    }

    public BParams[] getRestTemplate(int repType){
        restTemplate = new RestTemplate(load);
        return restTemplate.getTemplate(repType);

    }
}

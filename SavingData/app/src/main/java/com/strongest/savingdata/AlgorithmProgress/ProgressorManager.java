package com.strongest.savingdata.AlgorithmProgress;

import android.content.Context;

import com.strongest.savingdata.AlgorithmLayout.PLObject;
import com.strongest.savingdata.AlgorithmLayout.PLObject.ExerciseProfile;
import com.strongest.savingdata.Database.Exercise.ExerciseSet;
import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.AlgorithmLayout.LayoutManager;
import com.strongest.savingdata.Database.Progress.ProgressDataManager;
import com.strongest.savingdata.Database.Progress.ProgressModelNode;
import com.strongest.savingdata.Database.Progress.ProgressModelsData;

import java.util.ArrayList;

/**
 * Created by Cohen on 10/14/2017.
 */
public class ProgressorManager implements ProgressorObserver{




    public enum Action {
        Read, Save
    }

    private DataManager dataManager;
    private Context context;
    private LayoutManager originalLayoutManager;
    private LayoutManager fakeLayoutManager;
    private int currentPosition;
    private ArrayList<ProgressModelNode> progressModelsNodes = new ArrayList();
    private ArrayList<ArrayList<PLObject>> modelsList = new ArrayList();
    private int currentBlockPosition;
    private ProgressDataManager progressDataManager;
    private ProgressDataReader progressDataReader;

    // private WeightProgressor weightProgressor;
    //private ProgressorData progressorData;

    public ProgressorManager(Context context, DataManager dataManager, LayoutManager originalLayoutManager) {
        this.context = context;
        this.originalLayoutManager = originalLayoutManager;
        copyProgramLayoutManager();
        this.dataManager = dataManager;
        fakeLayoutManager = new LayoutManager(context, dataManager);
        //fakeLayoutManager.readLayoutFromDataBase(0);
        progressDataManager = dataManager.getProgressDataManager();
        progressDataReader = new ProgressDataReader();
        //  initProgressModels();
        // progressorData = ProgressorData.getProgressDataInstance();
    }

   /* public void initProgressModels() {
        progressModelsNodes = new HashMap<>();
        for (int i = 0; i < fakeLayoutManager.getAllBodys().size(); i++) {
            PLObjects.BodyText body = fakeLayoutManager.getAllBodys().get(i);
            progressModelsNodes.put(body.getBodyId(), new ProgressModel());
        }
    }*/

    @Override
    public void notifyProgressorChanged(ExerciseSet prev, ExerciseSet next) {

    }

    @Override
    public void notifyProgressorInserted(int pos, ExerciseProfile ep) {

    }

    @Override
    public void notifyProgressorRemoved(int pos) {

    }

    @Override
    public void notifyProgressorSwap(int from, int to) {

    }


    public ArrayList<PLObject> CreateProgressModel() {
        fakeLayoutManager = new LayoutManager(context, dataManager);
        //fakeLayoutManager.readLayoutFromDataBase(0);
        return fakeLayoutManager.getLayout();
    }
    private void copyProgramLayoutManager() {

    }

    public void readProgressFromDataBase(int position, int workout) {
        fakeLayoutManager = new LayoutManager(context, dataManager);
        //fakeLayoutManager.readLayoutFromDataBase(position);
        //readProgressFromDataBase(dataManager.getProgramDataManager().getCurrentProgramTable());
    }

   /* public void createProgressModels() {
        if (progressModelsNodes == null) {
            map.put(new Object(), new Object());
            progressModelsNodes = new HashMap();
            for (int i = 0; i < fakeLayoutManager.getNumOfWorkouts(); i++) {
                int key = fakeLayoutManager.getAllBodys().get(i).getBodyId();
                progressModelsNodes.put(key, new ProgressModel(order));
            }
        }

    }*/


    /*public WeightProgressor getWeightProgressor() {
        if (weightProgressor == null) {
            weightProgressor = new WeightProgressor(dataManager, programLayoutManager.getSplitRecyclerWorkouts());
        }
        return weightProgressor;
    }*/

    public void updateProgramLayoutWithProgress() {

    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public void saveModelList(ArrayList<ArrayList<PLObject>> models){

    }

    public ArrayList<ArrayList<PLObject>> requestModeList() {
        //ArrayList<ArrayList<PLObjects>> arr = fakeLayoutManager.getCleanWorkout();
      //  progressModelsNodes = progressDataManager.readProgresModelNode(currentPosition);
        modelsList.add(fakeLayoutManager.getLayout());
      /*  if (progressModelsNodes.size() == 0) {

        } else {*/
          //  ProgressModelsData data = progressDataReader.readModelList();
         /*   for (int i = 0; i < data.getNumOfPhases(); i++) { //provides the number of phases of a single workout
                fakeLayoutManager = new LayoutManager(context, dataManager);
                modelsList.add(fakeLayoutManager.readLayoutFromDataBase(0, currentPosition));
                if(i == 0){
                    continue;
                }
                for (int j = 0; j < progressModelsNodes.size(); j++) {
                    for (int k = 0; k < progressModelsNodes.size(); k++) {
                        if (((ExerciseProfile) modelsList.get(i).get(j)).getExerciseProfileId() == progressModelsNodes.get(k).getExerciseProfileId()
                                &&
                                progressModelsNodes.get(j).getPhase() == i) {
                            applyProgressModelNode(i, j);
                        }
                    }
                }

            }
        }*/
  //      }
       return modelsList;

    }

   /* private void applyProgressModelNode(int phase, int position) {
        ExerciseProfile ep = (ExerciseProfile) modelsList.get(phase).get(position);
        ep.setBeansHolder(progressModelsNodes.get(position).getNewBeansHolder());
    }*/

    public void deconstructProgressModels(ArrayList<ArrayList<PLObject>> modelsList) {
        modelsList.size();
    }

      /*
    * this is a new class
    * this class provides the ability to save, and read the data and changes
    * of the fragment_manager models
    * it helps reading the fragment_manager model nodes, and to provide additional information
    * */

    private class ProgressDataReader {

        public ProgressDataReader() {

        }

        public void saveModelsList() {
            int changes = 0;
            int phases = 0;
            for (int i = 0; i < progressModelsNodes.size(); i++) {
                ProgressModelNode node = progressModelsNodes.get(i);
                if (node.getWorkoutId() > changes) {
                    changes = node.getWorkoutId();
                }
                phases++;
            }
            progressDataManager.saveProgressModelsData(phases, changes);
        }

        public ProgressModelsData readModelList() {
            ProgressModelsData data = progressDataManager.readProgresModelsData();
            return data;
        }
    }

    /*
    * this is a new class
    * this class provides the ability to compare between the original
    * beansholder with the supposedly new beansholder (if a user has made a change)
    * it helps detect the change, if a change as been made
    * */

    public class LayoutComperator {

        public LayoutComperator(LayoutManager plm) {
        }

      /*  public boolean compareLayout(ArrayList<ExerciseProfile> original, ArrayList<ExerciseProfile> arr) {
            ArrayList<ProgressModelNode> modelNodes = new ArrayList<>();
            boolean hasChanged = false;
            if (original.size() != arr.size()) {
                modelNodes = createProgressModelList(original, arr);
                hasChanged = true;
            } else {
                for (int i = 0; i < arr.size(); i++) {

                    ExerciseProfile oEP = original.get(i);
                    ExerciseProfile arrEP = arr.get(i);
                    if (changed(oEP, arrEP)) {
                        hasChanged = true;
                    }
                }
                if (hasChanged) {
                    for (int i = 0; i < modelNodes.size(); i++) {
                        ProgressModelNode node = modelNodes.get(i);
                        progressModelsNodes.get(node.getWorkoutId()).getModelNodes().add(node);
                        progressModelsNodes.get(node.getWorkoutId()).getModelNodes().add(node);
                    }

                }


            }
           *//**//* ProgressModel progressModel = new ProgressModel(order);
            progressModel.setModelNodes();*//**//*
        }
*/
      /*  private ArrayList<ProgressModelNode> listProgressModelNodes(ArrayList<ExerciseProfile> original, ArrayList<ExerciseProfile> arr) {
            ArrayList<ProgressModelNode> listNodes = new ArrayList<>();
            for (int i = 0; i < arr.size(); i++) {
                *//*ExerciseProfile ep = arr.get(i);
                ExerciseProfile originalEP = original.get(i);
                listNodes.add(createProgressNode(originalEP, ep));*//*
            }
            for (int i = 0; i < listNodes.size(); i++) {
                ExerciseProfile ep = arr.get(i);
                if (progressModelsNodes.get(ep.getBodyId()) == null) {
                    progressModelsNodes.put(ep.getBodyId(), new ProgressModel(currentBlockPosition));
                }
                if (progressModelsNodes.get(ep.getBodyId()).getModelNodes() == null) {


                }
            }
        }*/

       /* private ArrayList<ProgressModelNode> createProgressModelList(ArrayList<ExerciseProfile> original, ArrayList<ExerciseProfile> arr) {
            ArrayList<ProgressModelNode> listNodes = new ArrayList<>();
            for (int i = 0; i < arr.size(); i++) {
                ExerciseProfile ep = arr.get(i);
                ExerciseProfile originalEP = original.get(i);
                listNodes.add(createProgressNode(originalEP, ep));
            }
            return listNodes;
        }*/

       /* private boolean changed(ExerciseProfile oEP, ExerciseProfile arrEP) {
            BeansHolder old = oEP.getBeansHolder();
            BeansHolder ne = arrEP.getBeansHolder();
            if (old.getExercise().getBean() != ne.getExercise().getBean()) {
                return true;
            }
            if (old.getRep().getBean() != ne.getRep().getBean()) {
                return true;
            }
            if (old.getMethod() != ne.getMethod() || old.getMethod().getBean() != ne.getMethod().getBean()) {
                return true;
            }
            if (old.getRest().getBean() != ne.getRest().getBean()) {
                return true;
            }
            if (old.getSets() != ne.getSets()) {
                return true;
            }
            if (old.getWeight() != ne.getWeight()) {
                return true;
            }
            return false;
        }

        private ProgressModelNode createProgressNode(ExerciseProfile oEP, ExerciseProfile arrEP) {
            BeansHolder origBeans = new BeansHolder();
            BeansHolder newBeans = new BeansHolder();
            transferBeans(newBeans, arrEP.getBeansHolder());
            transferBeans(origBeans, oEP.getBeansHolder());

            ProgressModelNode p = new ProgressModelNode(origBeans, newBeans, currentPosition, oEP.getExerciseProfileId(), currentBlockPosition);
            return p;
        }
*/
        private ExerciseSet transferBeans(ExerciseSet bean1, ExerciseSet bean2) {
            bean1.setExercise(bean2.getExercise());
            bean1.setSets(bean2.getSets());
            bean1.setRep(bean2.getRep());
            bean1.setRest(bean2.getRest());
            bean1.setWeight(bean2.getWeight());
            return bean1;
        }

    }
}


/*
    public ProgressorData getProgressorData() {
        return progressorData;
    }

    //saves all the information


    private static class ProgressorData {


        //    private Data[] datas = initDatas();

        public static ProgressorData instance = new ProgressorData();

        public static ProgressorData getProgressDataInstance() {
            return instance;
        }


        private ProgressorData() {

        }





    */

// first program
// each block

      /*  public void suckProgressDetails(int exerciseId, int numOfSets, String repsPerSet, String weightPerSet, String restPerSet) {
            updateProgressDetails(exerciseId, numOfSets, repsPerSet, weightPerSet, restPerSet);
        }*/


     /*   private void updateProgressDetails(int exerciseId, int numOfSets, String repsPerSet, String weightPerSet, String restPerSet) {
            for (int i = 0; i < datas.length; i++) {
                if (datas[i].getExerciseId() == exerciseId) {
                    datas[i].setExerciseId(numOfSets);
                    datas[i].setRepsPerSet(repsPerSet);
                    datas[i].setWeightPerSet(weightPerSet);
                    datas[i].setRestPerSet(restPerSet);
                }
            }
        }*/

       /* public Data[] initDatas() {
            int length = programLayoutManager.getNumOfExercises();
            Data[] arr = new Data[length];
            for (int i = 0; i < length; i++) {
                arr[i] = new Data();
            }
            return arr;
        }*/


      /*  public void saveProgressDetails() {
            for (int i = 0; i < datas.length; i++) {
                dataManager.getProgramDataManager().saveProgressorData(datas[i]);
            }
        }*/


        /*public Data[] getDatas() {
            return datas;
        }*/


/*    public static class Data {
        private int exerciseId = -1;
        private int numberOfSets;
        private String repsPerSet = "-";
        private String weightPerSet = "-";
        private String restPerSet = "-";
        private String date;

        public Data() {

        }

        public char[] getParsedRepsPerSet() {
            return parser(repsPerSet);
        }

        public String getRepsPerSet() {
            return repsPerSet;
        }

        public void setRepsPerSet(String repsPerSet) {
            this.repsPerSet = repsPerSet;
        }

        public int getNumberOfSets() {
            return numberOfSets;
        }

        public void setNumberOfSets(int numberOfSets) {
            this.numberOfSets = numberOfSets;
        }

        public char[] getParsedWeightPerSet() {
            return parser(weightPerSet);
        }

        public String getWeightPerSet() {
            return weightPerSet;
        }

        public void setWeightPerSet(String weightPerSet) {
            this.weightPerSet = weightPerSet;
        }

        public char[] getParsedRestsPerSet() {
            return parser(restPerSet);
        }

        public String getRestsPerSet() {
            return restPerSet;
        }

        public void setRestPerSet(String restPerSet) {
            this.restPerSet = restPerSet;
        }


        public int getExerciseId() {
            return exerciseId;
        }

        public void setExerciseId(int exerciseId) {
            this.exerciseId = exerciseId;
        }

        private char[] parser(String param) {
            char[] arr = new char[param.length()];
            if (!param.equals("-")) {
                for (int i = 0; i < arr.length; i++) {
                    char c = param.charAt(i);
                    arr[i] = c;

                }
            }
            return arr;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }*/


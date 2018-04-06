package com.strongest.savingdata.AlgorithmLayout;


import com.strongest.savingdata.Adapters.MyExpandableAdapter;
import com.strongest.savingdata.Database.Exercise.ExerciseSet;

import java.util.ArrayList;

import static com.strongest.savingdata.AlgorithmLayout.WorkoutLayoutTypes.BodyView;
import static com.strongest.savingdata.AlgorithmLayout.WorkoutLayoutTypes.IntraExerciseProfile;
import static com.strongest.savingdata.AlgorithmLayout.WorkoutLayoutTypes.IntraSet;
import static com.strongest.savingdata.AlgorithmLayout.WorkoutLayoutTypes.IntraSetNormal;
import static com.strongest.savingdata.AlgorithmLayout.WorkoutLayoutTypes.SetsPLObject;
import static com.strongest.savingdata.AlgorithmLayout.WorkoutLayoutTypes.SuperSetIntraSet;

public class LayoutManagerHelper {


    public LayoutManagerHelper() {

    }

    public static void onChange(PLObject plObject, ArrayList<PLObject> layout,
                         MyExpandableAdapter adapter) {
        int position = findPLObjectPosition(plObject, layout);
        if (position != -1) {
            adapter.notifyItemChanged(position);
        }
    }


    public static void onOnlyItemChange(MyExpandableAdapter adapter) {
        adapter.notifyItemChanged(0);
    }


   /* public void onExerciseSetChange() {
        adapter.notifyItemChanged(0);
    }*/

    public static int findPLObjectPosition(PLObject plObject, ArrayList<PLObject> layout) {
        for (int i = 0; i < layout.size(); i++) {
            PLObject current = layout.get(i);
            if (current == plObject) {
                return i;
            }
        }
        return -1;
    }

    public static int countExerciseRangeChange(int position, ArrayList<PLObject> layout) {
        int count = 1;
        for (int i = position + 1; i < layout.size(); i++) {
            if (layout.get(i).getType() != WorkoutLayoutTypes.ExerciseProfile &&
                    layout.get(i).getInnerType() != WorkoutLayoutTypes.IntraExerciseProfile) {
                count++;
            }
        }
        return count;
    }

    public static int findSetPosition(PLObject.SetsPLObject setsPLObject) {
        for (int i = 0; i < setsPLObject.getParent().getSets().size(); i++) {
            PLObject.SetsPLObject temp = setsPLObject.getParent().getSets().get(i);
            if (setsPLObject == temp) {
                return i;
            }
        }
        return -1;
    }

    public static int findIntraSetPosition(PLObject.IntraSetPLObject intraPLObject) {
        if (intraPLObject.innerType == WorkoutLayoutTypes.SuperSetIntraSet) {
            for (int i = 0; i < intraPLObject.getParent().getIntraSets().size(); i++) {
                if (intraPLObject.getParent().getIntraSets().get(i) == intraPLObject) {
                    return i;
                }
            }
        } else if (intraPLObject.innerType == WorkoutLayoutTypes.IntraSetNormal) {
            for (int i = 0; i < intraPLObject.getParentSet().getIntraSets().size(); i++) {
                PLObject.IntraSetPLObject temp = intraPLObject.getParentSet().getIntraSets().get(i);
                if (intraPLObject == temp) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static int findSupersetPosition(PLObject.ExerciseProfile superset) {
        PLObject.ExerciseProfile parent = superset.getParent();
        for (int i = 0; i < parent.getExerciseProfiles().size(); i++) {
            if (parent.getExerciseProfiles().get(i) == superset) {
                return i;
            }
        }
        return -1;
    }

    public static int findExercisePosition(PLObject.ExerciseProfile ep, ArrayList<PLObject> layout) {
        int count = 1;
        for (int i = 0; i < layout.size(); i++) {
            if (layout.get(i) != ep) {
                WorkoutLayoutTypes type = layout.get(i).getType();
                WorkoutLayoutTypes innerType = layout.get(i).getInnerType();
                if (type == WorkoutLayoutTypes.ExerciseProfile && innerType != IntraExerciseProfile) {
                    count++;
                }
            } else {
                return count;
            }
        }
        return -1;
    }

    public static String writeTitle(PLObject plObject) {
        PLObject.ExerciseProfile exerciseProfile;
        PLObject.SetsPLObject setsPLObject;
        PLObject.IntraSetPLObject intraSetPLObject;
        String title = "";
        if (plObject instanceof PLObject.ExerciseProfile) {
            exerciseProfile = (PLObject.ExerciseProfile) plObject;
            if (exerciseProfile.innerType == WorkoutLayoutTypes.IntraExerciseProfile) {
                title = "Exercise "
                        + exerciseProfile.getParent().rawPosition
                        + ", " + "Superset " + exerciseProfile.getTag();
            } else {
                title = "Exercise " + exerciseProfile.rawPosition;
            }
            return title;

        } else if (plObject instanceof PLObject.SetsPLObject) {
            setsPLObject = (PLObject.SetsPLObject) plObject;
            exerciseProfile = setsPLObject.getParent();
            String set = findSetPosition(setsPLObject) == 0 ? "First set" : "Set " + (findSetPosition(setsPLObject) + 1);
            String exerciseName = exerciseProfile.getExercise() != null ? ", " + exerciseProfile.getExercise().getName()
                    : "";
            title = set + exerciseName;
            return title;

        } else if (plObject instanceof PLObject.IntraSetPLObject) {
            intraSetPLObject = (PLObject.IntraSetPLObject) plObject;

            if (intraSetPLObject.innerType == WorkoutLayoutTypes.SuperSetIntraSet) {
                exerciseProfile = intraSetPLObject.getParent();
                String exerciseName = exerciseProfile.getExercise() != null ? ", " + exerciseProfile.getExercise().getName() : "";
                title = "Intra-Set " + exerciseProfile.getTag() + ", " + "set " + (1 + findIntraSetPosition(intraSetPLObject))
                        + exerciseName;
            } else {
                exerciseProfile = intraSetPLObject.getParentSet().getParent();
                String exerciseName = exerciseProfile.getExercise() != null ? ", " + exerciseProfile.getExercise().getName() : "";
                title = "Intra-Set "
                        + findIntraSetPosition(intraSetPLObject)
                        + ", " + "set " + (findSetPosition(intraSetPLObject.getParentSet()) + 1)
                        + exerciseName;
            }
        }
        return title;
    }

    public static void reArrangeFathers(ArrayList<PLObject> layout) {
        for (int i = 0; i < layout.size(); i++) {
            if (layout.get(i).innerType == WorkoutLayoutTypes.IntraExerciseProfile) {
                if (i - 1 < 0 || layout.get(i - 1).innerType == WorkoutLayoutTypes.ExerciseProfile) {
                    ((PLObject.ExerciseProfile) layout.get(i)).setParent(null);
                }
            }
            if (layout.get(i).getType() == WorkoutLayoutTypes.ExerciseProfile) {
                PLObject.ExerciseProfile parent = (PLObject.ExerciseProfile) layout.get(i);
                parent.getExerciseProfiles().clear();
                if (layout.size() > i + 1 && layout.get(i + 1).innerType == IntraExerciseProfile) {

                    innerloop:
                    for (int j = i + 1; j < layout.size(); j++) {
                        if (layout.get(j).innerType == IntraExerciseProfile) {
                            PLObject.ExerciseProfile superset = (PLObject.ExerciseProfile) layout.get(j);
                            parent.getExerciseProfiles().add(superset);
                            superset.setParent(parent);
                            i++;
                        } else {
                            break innerloop;
                        }

                    }
                }
            }
        }
    }

    public static ArrayList<PLObject.ExerciseProfile> getAllExerciseProfiles(ArrayList<PLObject> layout) {
        ArrayList<PLObject.ExerciseProfile> arr = new ArrayList<>();
        for (int i = 0; i < layout.size(); i++) {
            if (layout.get(i).getType() == WorkoutLayoutTypes.ExerciseProfile) {
                arr.add((PLObject.ExerciseProfile) layout.get(i));
            }
        }
        return arr;
    }

    public static WorkoutLayoutTypes findPLObjectDefaultType(PLObject plObject) {
        WorkoutLayoutTypes type = null;
        if (plObject.getType() == WorkoutLayoutTypes.ExerciseProfile) {
            PLObject.ExerciseProfile ep = (PLObject.ExerciseProfile) plObject;
            if (ep.innerType == WorkoutLayoutTypes.IntraExerciseProfile) {
                type = WorkoutLayoutTypes.IntraExerciseProfile;
            } else {
                type = WorkoutLayoutTypes.ExerciseProfile;
            }
        }
        if (plObject.getType() == SetsPLObject) {
            type = SetsPLObject;
        }
        if (plObject.getType() == IntraSet) {
            if (plObject.innerType == WorkoutLayoutTypes.SuperSetIntraSet) {
                type = WorkoutLayoutTypes.SuperSetIntraSet;
            } else {
                type = IntraSetNormal;
            }
        }

        if (plObject.getType() == BodyView) {
            type = BodyView;
        }
        return type;
    }


    public static void attachSupersetIntraSetsByParent(PLObject.ExerciseProfile superset) {
        int sets = superset.getParent().getSets().size();
        int intraSets = superset.getIntraSets().size();
        int length = sets - intraSets;
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                superset.getIntraSets().add(new PLObject.IntraSetPLObject(superset, new ExerciseSet(), SuperSetIntraSet, null));
            }
        }

    }

    public static int calcBlockLength(PLObject.ExerciseProfile ep) {
        int sets = 0;
        for (int i = 0; i < ep.getSets().size(); i++) {
            sets += 1 + ep.getSets().get(i).getIntraSets().size();
        }
        int setsWithSuperset = ep.getSets().size() * ep.getExerciseProfiles().size();
        int supersets = ep.getExerciseProfiles().size();
        return sets + setsWithSuperset + supersets + 1;
    }

    public static boolean exerciseHasDropset(PLObject.ExerciseProfile ep){
        if(ep.innerType == IntraExerciseProfile){
            return false;
        }
        for (int i = 0; i < ep.getSets().size(); i++) {
            if(ep.getSets().get(i).getIntraSets().size() > 0){
                return true;
            }
        }
        return false;

    }
}

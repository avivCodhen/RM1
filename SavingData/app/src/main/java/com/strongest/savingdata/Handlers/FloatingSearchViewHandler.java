package com.strongest.savingdata.Handlers;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.strongest.savingdata.Database.Exercise.Beans;
import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.Fragments.Choose.ExerciseSearchSuggestion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class FloatingSearchViewHandler {

    public enum Actions {
        Exercises
    }

    private FloatingSearchView mSearchView;
    private ArrayList<ExerciseSearchSuggestion> queries;
    private ArrayList<ExerciseSearchSuggestion> fullExerciseSuggestionsList;
    ArrayList<Beans> fullExercisesList;
    private DataManager dataManager;

    private FloatingSearchViewHandler(FloatingSearchView mSearchView) {

        this.mSearchView = mSearchView;
        queries = new ArrayList<>();
        fullExerciseSuggestionsList = new ArrayList<>();
    }

    public static FloatingSearchViewHandler getHandler(FloatingSearchView mSearchView) {
        return new FloatingSearchViewHandler(mSearchView);
    }

    public FloatingSearchViewHandler from(DataManager dataManager) {
        this.dataManager = dataManager;
        return this;
    }


    public void handleExercises(FloatingSearchVHandlerCallback floatingSearchVHandlerCallback) {
        new Thread(() -> {
            fullExercisesList = dataManager.getExerciseDataManager().getAllExercises();
            fullExercisesList = Beans.sortByAccessory(fullExercisesList);
            for (Beans b : fullExercisesList) {
                fullExerciseSuggestionsList.add(new ExerciseSearchSuggestion(b.getName()));

                exercisesHandlerFunc(floatingSearchVHandlerCallback);
            }
        }).start();
    }

    public void exercisesHandlerFunc(FloatingSearchVHandlerCallback floatingSearchVHandlerCallback) {
        mSearchView.setOnQueryChangeListener((oldQuery, newQuery) -> {
            if (!oldQuery.equals("") && newQuery.equals("")) {
                mSearchView.clearSuggestions();
            } else {
                mSearchView.showProgress();
                ArrayList<ExerciseSearchSuggestion> queries = new ArrayList<>();

                for (ExerciseSearchSuggestion e : fullExerciseSuggestionsList) {
                    if (e.getBody().toLowerCase().contains(newQuery.toLowerCase())) {
                        queries.add(e);
                    }
                }
                mSearchView.swapSuggestions(queries);
                mSearchView.hideProgress();
            }
        });

        mSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon, TextView textView, final SearchSuggestion item, int itemPosition) {
                suggestionView.setOnClickListener(v -> {
                    Beans exercise = null;

                    for (Beans b : fullExercisesList){
                        if(item.getBody().equals(b.getName())){
                            //exercise = b;
                            //consumer.accept(b);

                            floatingSearchVHandlerCallback.exerciseFound(b);
                        }
                    }

                });
            }
        });


    }


    public interface FloatingSearchVHandlerCallback{

        void exerciseFound(Beans b);

    }
}


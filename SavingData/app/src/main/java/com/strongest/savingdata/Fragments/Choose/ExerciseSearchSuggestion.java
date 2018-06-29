package com.strongest.savingdata.Fragments.Choose;

import android.annotation.SuppressLint;
import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

@SuppressLint("ParcelCreator")
public class ExerciseSearchSuggestion implements SearchSuggestion {
    String body;
    public ExerciseSearchSuggestion(String body){
        this.body = body;
    }
    @Override
    public String getBody() {
        return body;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}

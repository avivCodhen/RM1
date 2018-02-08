package com.strongest.savingdata.ArtificialInteligence;

/**
 * Created by Cohen on 11/29/2017.
 */

public interface ArtificialSubjectListener {

    void attachAI(ArtificialIntelligenceObserver o);
    void dettachAI(ArtificialIntelligenceObserver o);
    void notifyAI(int workoutPosition);
}

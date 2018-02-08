package com.strongest.savingdata.tabFragments;

/**
 * Created by Cohen on 11/12/2017.
 */

public class BooleanEventHolder {

    private boolean showStats;
    private boolean showDetails;
    private boolean showChanges;

    public BooleanEventHolder(){

    }

    public boolean isShowStats() {
        return showStats;
    }

    public void setShowStats(boolean showStats) {
        this.showStats = showStats;
    }

    public boolean isShowDetails() {
        return showDetails;
    }

    public void setShowDetails(boolean showDetails) {
        this.showDetails = showDetails;
    }

    public boolean isShowChanges() {
        return showChanges;
    }

    public void setShowChanges(boolean showChanges) {
        this.showChanges = showChanges;
    }
}

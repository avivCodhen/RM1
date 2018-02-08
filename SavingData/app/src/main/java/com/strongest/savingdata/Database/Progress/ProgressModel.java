package com.strongest.savingdata.Database.Progress;

import java.util.ArrayList;

/**
 * Created by Cohen on 12/26/2017.
 */

public class ProgressModel {

    private ArrayList<ProgressModelNode> modelNodes = new ArrayList<>();
    private int order;
    private ProgressModel nextModel;
    private boolean completed;
    private boolean on;
    private boolean filled;


    public ProgressModel(int order) {
        this.order = order;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public ArrayList<ProgressModelNode> getModelNodes() {
        return modelNodes;
    }

    public void setModelNodes(ArrayList<ProgressModelNode> modelNodes) {
        this.modelNodes = modelNodes;
    }

    public ProgressModel getNextModel() {
        return nextModel;
    }

    public void setNextModel(ProgressModel nextModel) {
        this.nextModel = nextModel;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    public boolean isFilled() {
        return filled;
    }

    public int getOrder() {
        return order;
    }
}

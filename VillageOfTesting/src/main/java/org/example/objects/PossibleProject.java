package org.example.objects;

import org.example.interfaces.ICompleteAction;

public class PossibleProject extends Building {
    private int woodCost;
    private int metalCost;
    private int daysToComplete;
    private ICompleteAction completeAction;

    public PossibleProject(String name) {
        super(name);
    }
    public PossibleProject(String name, int woodCost, int metalCost, int daysToComplete, ICompleteAction completeAction) {
        super(name);
        this.woodCost = woodCost;
        this.metalCost = metalCost;
        this.daysToComplete = daysToComplete;
        this.completeAction = completeAction;
    }

    public Project GetProject() {
        return new Project(getName(), getDaysToComplete(), completeAction);
    }

    public int getWoodCost() {
        return woodCost;
    }
    public void setWoodCost(int woodCost) {
        this.woodCost = woodCost;
    }
    public int getMetalCost() {
        return metalCost;
    }
    public void setMetalCost(int metalCost) {
        this.metalCost = metalCost;
    }
    public int getDaysToComplete() {
        return daysToComplete;
    }
    public void setDaysToComplete(int daysToComplete) {
        this.daysToComplete = daysToComplete;
    }
}

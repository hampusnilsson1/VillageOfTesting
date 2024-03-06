package org.example.objects;

import org.example.interfaces.ICompleteAction;

public class Project extends Building {
    private int daysLeft;
    private ICompleteAction completeAction;

    public Project(String name, int daysLeft, ICompleteAction completeAction) {
        super(name);
        this.daysLeft = daysLeft;
        this.completeAction = completeAction;
    }
    public void Complete() {
        completeAction.UponCompletion();
    }

    public int getDaysLeft() {
        return daysLeft;
    }
    public void setDaysLeft(int daysLeft) {
        this.daysLeft = daysLeft;
    }

    public ICompleteAction getCompleteAction() {
        return completeAction;
    }
    public void setCompleteAction(ICompleteAction completeAction) {
        this.completeAction = completeAction;
    }

    public boolean BuildOn() {
        daysLeft--;
        return daysLeft < 1;
    }
}

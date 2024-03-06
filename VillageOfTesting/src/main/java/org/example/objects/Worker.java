package org.example.objects;

import org.example.interfaces.IOccupationAction;

public class Worker {
    public static int daysUntilStarvation = 5;
    private String name;
    private String occupation;
    private IOccupationAction occupationAction;
    private boolean hungry;
    private boolean alive;
    private int daysHungry;

    public Worker(String name, String occupation, IOccupationAction occupationAction) {
        this.name = name;
        this.occupation = occupation;
        this.occupationAction = occupationAction;
        hungry = true;
        alive = true;
        daysHungry = 0;
    }

    public void DoWork() {
        if (!alive) {
            System.out.println(name + " is not alive and cannot work...");
            return;
        }
        if (!hungry) {
            occupationAction.Work(name);
            hungry = true;
        }
        else {
            daysHungry++;
            if (daysHungry >= daysUntilStarvation) {
                alive = false;
                System.out.println(getName() + " has died of hunger!");
            }
        }
    }

    public void Feed() {
        if (alive) {
            daysHungry = 0;
            hungry = false;
        }
    }

    public String getName() {
        return name;
    }
    public String getOccupation() {
        return occupation;
    }

    public boolean isHungry() {
        return hungry;
    }
    public int getDaysHungry() {
        return daysHungry;
    }
    public boolean isAlive() {
        return alive;
    }
}

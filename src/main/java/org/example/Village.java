package org.example;

import org.example.interfaces.IOccupationAction;
import org.example.objects.Building;
import org.example.objects.PossibleProject;
import org.example.objects.Project;
import org.example.objects.Worker;

import java.util.ArrayList;
import java.util.HashMap;

public class Village {

    private boolean gameOver = false;
    private int food = 0;
    private int wood = 0;
    private int metal = 0;
    private ArrayList<Worker> workers = new ArrayList<>();
    private ArrayList<Building> buildings = new ArrayList<>();
    private ArrayList<Project> projects = new ArrayList<>();
    private final HashMap<String, IOccupationAction> occupationHashMap = new HashMap<>();
    private final HashMap<String, PossibleProject> possibleProjects = new HashMap<>();
    private int metalPerDay = 1;
    private int woodPerDay = 1;
    private int foodPerDay = 5;
    private int maxWorkers = 0;
    private int daysGone = 0;

    public Village() {
        occupationHashMap.put("farmer", name -> AddFood(name));
        occupationHashMap.put("lumberjack", name -> AddWood(name));
        occupationHashMap.put("miner", name -> AddMetal(name));
        occupationHashMap.put("builder", name -> Build(name));

        possibleProjects.put("House", new PossibleProject("House", 5,0,3, () -> NewHouse()));
        possibleProjects.put("Woodmill", new PossibleProject("Woodmill", 5,1,5, () -> NewWoodmill()));
        possibleProjects.put("Quarry", new PossibleProject("Quarry", 3,5,7, () -> NewQuarry()));
        possibleProjects.put("Farm", new PossibleProject("Farm", 5,2,5, () -> NewFarm()));
        possibleProjects.put("Castle", new PossibleProject("Castle", 50,50,50, () -> NewCastle()));

        buildings.add(new Building("House"));
        buildings.add(new Building("House"));
        buildings.add(new Building("House"));
        maxWorkers = 6;
        food = 10;
    }
    public void Day() {
        FeedWorkers();
        boolean someoneAlive = false;
        for (Worker worker: workers) {
            worker.DoWork();
            if (worker.isAlive()) {
                someoneAlive = true;
            }
        }
        daysGone++;
        if (!someoneAlive && workers.size() > 0) {
            System.out.println("Everyone is dead! You lasted " + daysGone + " days!");
            GameOver();
        }
    }

    public void GameOver() {
        gameOver = true;
    }


    public void PrintInfo() {
        if (workers.size() > 0) {
            System.out.println("You have " + workers.size() + " workers. They are: ");
            for (Worker worker : workers) {
                System.out.println(worker.getName() + ", " + worker.getOccupation() + ".");
                if (worker.isHungry() && worker.getDaysHungry() > 0) {
                    System.out.println(worker.getName() + " has been hungry for " + worker.getDaysHungry() + " days!");
                }
            }
        }
        else {
            System.out.println("You have no workers.");
        }
        System.out.println("Your current buildings are: ");
        for (Building building : buildings) {
            System.out.print(building.getName() + " ");
        }
        System.out.println();
        System.out.println("You can have " + maxWorkers + " workers.");
        System.out.println("Your current projects are: ");
        for (Project project : projects) {
            System.out.print(project.getName() + ", " + project.getDaysLeft() + " points left until completion.");
        }
        System.out.println();
        System.out.println("Current Food:  " + food);
        System.out.println("Current Wood:  " + wood);
        System.out.println("Current Metal: " + metal);
        System.out.println("Generating " + foodPerDay + " food per day per worker.");
        System.out.println("Generating " + woodPerDay + " wood per day per worker.");
        System.out.println("Generating " + metalPerDay + " metal per day per worker.");
    }


    public void AddWorker(String name, String occupation) {
        if(occupationHashMap.containsKey(occupation) && !isFull()) {
            IOccupationAction jobInterface = occupationHashMap.get(occupation);
            Worker worker = new Worker(name, occupation, jobInterface);
            workers.add(worker);
            System.out.println(name + " was successfully added.");
            return;
        }
        System.out.println("There is no such job.");
    }

    public void AddProject(String name) {
        if (possibleProjects.containsKey(name)) {
            PossibleProject possibleProject = possibleProjects.get(name);
            if (getWood() >= possibleProject.getWoodCost() &&
                    getMetal() >= possibleProject.getMetalCost()) {
                wood -= possibleProject.getWoodCost();
                metal -= possibleProject.getMetalCost();

                Project newProject = possibleProject.GetProject();
                projects.add(newProject);
                System.out.println(newProject.getName() + " added to the project queue!");
                return;
            }
            System.out.println("Not enough material!");
            return;
        }
        System.out.println("That was not one of the options.");
    }

    public void AddFood(String name) {
        food += foodPerDay;
        System.out.println(name + " gathers " + foodPerDay + " food!");
    }
    public void AddMetal(String name) {
        metal += metalPerDay;
        System.out.println(name + " gathers " + metalPerDay + " metal!");
    }
    public void AddWood(String name) {
        wood += woodPerDay;
        System.out.println(name + " gathers " + woodPerDay + " wood!");
    }
    public void Build(String name) {
        if (projects.size() > 0) {
            Project currentProject = projects.get(0);
            System.out.println(name + " builds on " + currentProject.getName() + "!");
            boolean complete = currentProject.BuildOn();
            if (complete) {
                projects.remove(currentProject);
                buildings.add(new Building(currentProject.getName()));
                System.out.println(currentProject.getName() + " was completed!");
                currentProject.Complete();
            }
        }
        else {
            System.out.println("No buildings for " + name + " to work on!");
        }
    }

    private void FeedWorkers() {
        for (Worker worker: workers) {
            if (food > 0 && worker.isAlive()) {
                worker.Feed();
                System.out.print(worker.getName() + " eats. ");
                food--;
            }
            else {
                if (worker.isAlive()) {
                    System.out.println("No food left for " + worker.getName() + "! " + worker.getDaysHungry() + " days without food! ");
                }
                else {
                    System.out.println(worker.getName() + " is dead...");
                }
            }
        }
        System.out.println();
    }

    private void NewHouse() {
        maxWorkers += 2;
    }
    private void NewFarm() {
        foodPerDay += 5;
    }
    private void NewQuarry() {
        metalPerDay++;
    }
    private void NewWoodmill() {
        woodPerDay++;
    }
    private void NewCastle() {
        System.out.println("Castle complete! It took " + daysGone + " days!");
        GameOver();
    }

    public int getFood() {
        return food;
    }
    public void setFood(int food) {
        this.food = food;
    }
    public int getWood() {
        return wood;
    }
    public void setWood(int wood) {
        this.wood = wood;
    }
    public int getMetal() {
        return metal;
    }
    public void setMetal(int metal) {
        this.metal = metal;
    }
    public ArrayList<Worker> getWorkers() {
        return workers;
    }
    public ArrayList<Building> getBuildings() {
        return buildings;
    }
    public ArrayList<Project> getProjects() {
        return projects;
    }
    public int getDaysGone() {
        return daysGone;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public HashMap<String, PossibleProject> getPossibleProjects() {
        return possibleProjects;
    }

    public boolean isFull() {
        return maxWorkers <= workers.size();
    }
}

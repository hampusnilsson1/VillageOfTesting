package org.example;

import org.example.interfaces.IAction;
import org.example.objects.PossibleProject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class VillageInput {
    DatabaseConnection databaseConnection = new DatabaseConnection();
    Village village = new Village();
    Scanner scanner = new Scanner(System.in);
    private final HashMap<String, IAction> actions = new HashMap<>();
    ArrayList<String> options = new ArrayList<>();


    public VillageInput(Village village, DatabaseConnection databaseConnection,Scanner scanner) {
        this();
        this.village = village;
        this.databaseConnection = databaseConnection;
        this.scanner = scanner;
    }
    public VillageInput() {
        actions.put("1", () -> AddWorker());
        actions.put("2", () -> AddProject());
        actions.put("3", () -> village.Day());
        actions.put("4", () -> Load());
        actions.put("5", () -> Save());
        actions.put("6", () -> village.GameOver());

        options.add("1: Add Worker.");
        options.add("2: Add Project.");
        options.add("3: Proceed to next day.");
        options.add("4: Load saved game.");
        options.add("5: Save progress.");
        options.add("6: Quit.");
    }

    public void Run() {
        System.out.println("Welcome to the Village of Testing!");

        while (!village.isGameOver()) {
            System.out.println("Your village looks like...");

            village.PrintInfo();

            while (true) {
                System.out.println();
                System.out.println("Day " + village.getDaysGone());
                System.out.println("What would you like to do?");
                for (String option: options) {
                    System.out.println(option);
                }

                String choice = scanner.nextLine();
                if (actions.containsKey(choice)) {
                    actions.get(choice).Action();
                    break;
                }
                System.out.println("That's not an option.");
            }
        }
    }
    private void AddWorker() {
        if ( village.isFull()) {
            System.out.println("There is nowhere for the new worker to live! Make more houses!");
            return;
        }

        System.out.println("What will be the worker's name?");
        String name = scanner.nextLine();
        if (name.isEmpty()) {
            System.out.println("Please do write a name.");
            return;
        }
        System.out.println("What's their job? The options are Farmer, Lumberjack, Miner or Builder.");
        String occupation = scanner.nextLine().toLowerCase();
        village.AddWorker(name, occupation);
        System.out.println();
    }
    private void AddProject() {
        System.out.println("Which project? Possible choices are: ");
        for (PossibleProject possibleProject : village.getPossibleProjects().values()) {
            System.out.println(possibleProject.getName() + ": " + possibleProject.getWoodCost() + " wood, " + possibleProject.getMetalCost() + " metal");
        }
        String name = scanner.nextLine();
        village.AddProject(name);
        System.out.println();
    }

    public void Save() {
        System.out.println("What name do you wish to save the village under? Current villages are: ");
        ArrayList<String> villages = databaseConnection.GetTownNames();

        for (String villageName: villages) {
            System.out.print(villageName + " ");
        }
        System.out.println();

        String choice = scanner.nextLine();

        if (villages.contains(choice)) {
            System.out.println("Are you sure you want to overwrite " + choice + "? Write \"y\" for yes. Anything else for no.");
            String yes = scanner.nextLine().toLowerCase();
            if (!yes.equals("y")) {
                System.out.println("Cancelling load.");
                return;
            }
        }

        boolean success = databaseConnection.SaveVillage(village, choice);

        if (success) {
            System.out.println("Village " + choice + " successfully saved.");
        }
        else {
            System.out.println("Error, something went wrong. Could not save.");
        }
    }

    public void Load() {
        System.out.println("Which village would you like to load? The choices are: ");
        ArrayList<String> villages = databaseConnection.GetTownNames();
        for (String villageName: villages) {
            System.out.print(villageName + " ");
        }
        System.out.println();

        String choice = scanner.nextLine();

        if (!villages.contains(choice)) {
            System.out.println("That's not one of the choices.");
            return;
        }

        Village loadedVillage = databaseConnection.LoadVillage(choice);
        if (loadedVillage != null) {
            System.out.println("Village " + choice + " successfully loaded.");
            village = loadedVillage;
        }
        else {
            System.out.println("Load failed.");
        }
    }
}

package org.example;

import java.util.ArrayList;

public class DatabaseConnection {

    // This file may not be changed. You have been warned.
    // Regards, Niklas Cullberg

    public ArrayList<String> GetTownNames() {
        ArrayList<String> list = new ArrayList<>();
        list.add("These");
        list.add("are");
        list.add("placeholders");
        list.add("to");
        list.add("make");
        list.add("sure");
        list.add("it");
        list.add("works");
        return list;
    }

    public Village LoadVillage(String choice) {
        return null;
    }

    public boolean SaveVillage(Village village, String choice) {
        return false;
    }
}

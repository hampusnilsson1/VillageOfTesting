package org.example;

import org.example.objects.Building;
import org.example.objects.Worker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class VillageTest {

    Village village;

    @BeforeEach
    void setupEach(){
        village = new Village();
    }

    @ParameterizedTest
    @CsvSource(value = {"Kelly,farmer","Roger,lumberjack","Jules,builder","Hasse,miner","Lennart,farmer"})
    void addWorker_AddingToList_Added(String name, String occupation) {
        int amountOfWorkers = village.getWorkers().size();

        village.AddWorker(name,occupation);
        Worker addedworker = village.getWorkers().get(village.getWorkers().size()-1);

        assertNotEquals(amountOfWorkers,village.getWorkers().size()); // Not the same worker list size
        assertEquals(name,addedworker.getName()); // The same name as added
        assertEquals(occupation,addedworker.getOccupation()); // The same occupation as added
    }
    @ParameterizedTest
    @CsvSource(value = {"Kelly,hugger","Roger,runner","Jules,driver"})
    void addWorker_WrongOccupation_NotAdded(String name, String occupation) {
        int amountOfWorkers = village.getWorkers().size();

        village.AddWorker(name,occupation);

        assertEquals(amountOfWorkers,village.getWorkers().size()); // Same size as start
    }

    @Test
    void addWorker_Adding2Workers_AddedToList(){
        int expectedWorkersSize = 2;

        village.AddWorker("Jimmy","miner");
        village.AddWorker("Jim","farmer");
        Worker firstWorker = village.getWorkers().get(village.getWorkers().size()-2);
        Worker secondWorker = village.getWorkers().get(village.getWorkers().size()-1);

        assertEquals(expectedWorkersSize,village.getWorkers().size()); // 2 Workers added
        assertEquals("miner",firstWorker.getOccupation()); // The same occupation as added 1
        assertEquals("Jimmy",firstWorker.getName()); // The same name as added 1
        assertEquals("farmer",secondWorker.getOccupation()); // The same occupation as added 2
        assertEquals("Jim",secondWorker.getName()); // The same name as added 2
    }

    @Test
    void addWorker_Adding3Workers_AddedToList(){
        int expectedWorkersSize = 3;

        village.AddWorker("Jimmy","miner");
        village.AddWorker("Jim","farmer");
        village.AddWorker("Jen","builder");
        Worker firstWorker = village.getWorkers().get(village.getWorkers().size()-3);
        Worker secondWorker = village.getWorkers().get(village.getWorkers().size()-2);
        Worker thirdWorker = village.getWorkers().get(village.getWorkers().size()-1);

        assertEquals(expectedWorkersSize,village.getWorkers().size()); // 2 Workers added
        assertEquals("miner",firstWorker.getOccupation()); // The same occupation as added 1
        assertEquals("Jimmy",firstWorker.getName()); // The same name as added 1
        assertEquals("farmer",secondWorker.getOccupation()); // The same occupation as added 2
        assertEquals("Jim",secondWorker.getName()); // The same name as added 2
        assertEquals("builder",thirdWorker.getOccupation()); // The same occupation as added 3
        assertEquals("Jen",thirdWorker.getName()); // The same name as added 3
    }

    @Test
    void addWorker_WhenMoreThanMax_DontAddMore() {
        //Tried every way of changing input stream and using the VillageInput
        // but had to add IsFull() in the if statement to the
        // AddWorker function in the Village class to test it
        int expectedWorkerSize = 6;

        village.AddWorker("Greg","miner");
        village.AddWorker("Filly","builder");
        village.AddWorker("Jenn","lumberjack");
        village.AddWorker("Willy","miner");
        village.AddWorker("Pen","builder");
        village.AddWorker("Groo","lumberjack");
        village.AddWorker("Fred","miner");
        village.AddWorker("Hams","builder");
        village.AddWorker("Kled","lumberjack");

        assertEquals(expectedWorkerSize, village.getWorkers().size());
    }
    @Test
    void PrintTest_WhenHungryWorker_SayBeenHungry1Days()
    {
        //Given
        String expectedHungerOutput = "Ken has been hungry for 1 days!";
        village.AddWorker("Ken","miner");
        village.setFood(0);
        village.Day(); //1 Day gone

        //SaveOutput
        PrintStream originalOut = System.out; // Save Old outputstream.
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();// SETUP Reading log
        System.setOut(new PrintStream(outContent));

        village.PrintInfo();

        //Then
        assertEquals(expectedHungerOutput,outContent.toString().substring(45,76));

        System.setOut(originalOut);
        System.out.println(outContent);
    }

    @Test
    void PrintInfo_WhenNoWorkers_SayNoWorkers()
    {
        String expectedNoWorkerOutput = "You have no workers.";
        //SaveOutput
        PrintStream originalOut = System.out; // Save Old outputstream.
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();// SETUP Reading log
        System.setOut(new PrintStream(outContent));

        village.PrintInfo();

        assertEquals(expectedNoWorkerOutput,outContent.toString().substring(0,20));
        assertEquals(0,village.getWorkers().size());

        System.setOut(originalOut);
        System.out.println(outContent);
    }

    @Test
    void PrintInfo_WhenFunction_ThenPrintAllInfo() { // Prob not necessary to do but felt like doing it :)
        village.AddWorker("Greg","miner");
        village.AddWorker("Filly","builder");
        village.AddWorker("Jenn","lumberjack");
        //Workers print
        String expectedPrint = "You have 3 workers";
        //Buildings Prints
        village.getBuildings().add(new Building("Windmill"));
        village.getBuildings().add(new Building("Quarry"));
        String expectedPrintBuildning = "House House House Windmill Quarry";
        //Projects Prints
        village.setWood(10);
        village.AddProject("House");
        String expectedPrintProjects = "House, 3 points left until completion.";
        //Current Material Prints
        String expectedPrintFood = "Current Food:  10";
        //Current Generating Prints
        String expectedPrintGeneration = "Generating 5 food per day per worker.";



        PrintStream originalOut = System.out; // Save Old outputstream.
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();// SETUP Reading log
        System.setOut(new PrintStream(outContent));
        village.PrintInfo();

        assertEquals(expectedPrint, outContent.toString().substring(0,18)); // Test workers
        assertEquals(expectedPrintBuildning, outContent.toString().substring(112,145));//Test Building
        assertEquals(expectedPrintProjects, outContent.toString().substring(202,240));//Test Project
        assertEquals(expectedPrintFood, outContent.toString().substring(242,259));//Test Material(Food)
        assertEquals(expectedPrintGeneration, outContent.toString().substring(297,334));//Test Generation(Food)
        System.setOut(originalOut);
        System.out.println(outContent);
    }

    @ParameterizedTest
    @CsvSource(value={"4,0","5,1","6,1","10,2"})
    void AddProject_WhenValidProject_SuccessWhenEnoughMaterial(int wood, int expectedSize){
        village.AddWorker("Filly","builder");
        village.setWood(wood);
        // Here i realized that AddProject had to be > material and not >= which i feel like it should be so, i changed it.
        int expectedProjectSize = expectedSize;

        village.AddProject("House");
        village.AddProject("House");
        village.Day();

        assertEquals(expectedProjectSize, village.getProjects().size()); // See if the house have been built
        assertEquals(wood-5*village.getProjects().size(),village.getWood());// See if the wood has been used
    }

    @Test
    void AddProject_WhenNONValidProject_Fail(){
        village.AddWorker("Filly","builder");
        village.setWood(100);
        village.setMetal(100);
        int expectedProjectSize = 0;

        village.AddProject("Shouse");
        village.Day();

        assertEquals(expectedProjectSize, village.getProjects().size()); // See if the house have been built
    }

    @Test
    void Day_WhenNewDay_ThenFeedAndWork(){
        village.AddWorker("Sven","farmer");
        village.AddWorker("Greg","miner");
        village.AddWorker("Filly","builder");
        village.AddWorker("Jenn","lumberjack");
        village.setWood(100);
        village.AddProject("House");
        int expectedFood = 11;
        int expectedWood = 96;
        int expectedMetal = 1;

        village.Day();

        assertEquals(expectedFood, village.getFood());
        assertEquals(expectedWood, village.getWood());
        assertEquals(expectedMetal, village.getMetal());
    }

    @ParameterizedTest
    @CsvSource (value = {"House,95,100,3","Woodmill,95,99,5","Quarry,97,95,7","Farm,95,98,5"})
    void Day_When3Days_BuildingDone(String projectName,int expectedWood,int expectedMetal,int daysToBuild){
        village.AddWorker("Filly","builder");
        village.setFood(1000);
        village.setWood(100);
        village.setMetal(100);
        village.AddProject(projectName);
        int expectedBuildings = 4;

        for (int i = 0; i < daysToBuild;i++)
            village.Day();

        assertEquals(expectedBuildings, village.getBuildings().size());
        assertEquals(expectedWood, village.getWood());
        assertEquals(expectedMetal, village.getMetal());
        assertEquals(projectName,village.getBuildings().get(3).getName());
    }
    @Test
    void Day_When2Days_BuildingNotDone(){
        village.AddWorker("Filly","builder");
        village.setFood(100);
        village.setWood(100);
        village.AddProject("House");
        int expectedBuildings = 3;
        int expectedWood = 95;

        village.Day();
        village.Day();

        assertEquals(expectedBuildings, village.getBuildings().size());
        assertEquals(expectedWood, village.getWood());
    }



    @Test
    void Day_WhenNotEnoughFoodForWorkers_ThenAfter5DaysEndGame(){
        village.setFood(0);

        village.AddWorker("Fred","miner");

        village.Day();
        village.Day();
        village.Day();
        village.Day();
        village.Day();

        assertTrue(village.isGameOver());
    }

    @Test
    void Day_WhenNotEnoughFoodForWorkers_ThenBefore5DaysDontEndGame(){
        village.setFood(0);

        village.AddWorker("Fred","miner");

        village.Day();
        village.Day();
        village.Day();
        village.Day();

        assertFalse(village.isGameOver());
    }

    @ParameterizedTest
    @CsvSource(value={"4,False","5,False","6,False","10,False","100,False"})
    void Day_WhenNoWorkers_ThenSameMaterialAndAlive(int daysPast, boolean gameOverState){

        for (int i = 0; i < daysPast; i++) {
            village.Day();
        }

        assertEquals(gameOverState,village.isGameOver());
    }

    @Test
    void Day_WhenNotEnoughFoodForOneWorker_ThenOnlyOneDeadAndGameNotOver(){
        //Here i realized that the workers arent hungry at the start but still ate
        // and the dayshungry wasnt going up so i put hungry to true at the start
        // if the worker isnt fed the first day its hungry at the start now aswell
        // this seemed wrong but cant be achived in the acutal game but i changed it either way.
        village.setFood(1);
        village.AddWorker("IWasPutHereToStarv","lumberjack");
        village.AddWorker("IWasPutHereToStarvSadFace","miner");
        String expectedDeadOutput = "IWasPutHereToStarvSadFace is dead...";
        String expectedWorkStatusOutput = "IWasPutHereToStarvSadFace is not alive and cannot work...";

        village.Day();
        village.Day();
        village.Day();
        village.Day();
        village.Day();//One worker dies here
        village.setFood(10);
        //Collect output
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        village.Day();//Dead cant eat
        System.setOut(originalOut);
        System.out.println(outContent);

        assertFalse(village.isGameOver());
        assertFalse(village.getWorkers().get(1).isAlive());// Dead
        assertTrue(village.getWorkers().get(0).isAlive());// Alive
        assertEquals(expectedDeadOutput,outContent.toString().substring(25,61));// Checks dead
        assertEquals(expectedWorkStatusOutput,outContent.toString().substring(101,158));//Checks cant work
    }

    @Test
    void FullGame_WhenBuildCastle_ThenWinGame(){
        village.AddWorker("Miner1","miner"); // 6 worker slots
        village.AddWorker("Lumberjack1","lumberjack");
        village.AddWorker("Lumberjack2","lumberjack");
        village.AddWorker("Builder","builder");
        village.AddWorker("FoodCollector1","farmer");
        village.AddWorker("FoodCollector2","farmer");

        for (int i = 0; i < 5; i++) {
            village.Day(); // 5 Days past
        }

        village.AddProject("House");//10 Wood to build these
        village.AddProject("House");

        for (int i = 0; i < 3; i++) {
            village.Day(); // 3 Days past to build the first house
        }
        village.AddWorker("Builder2","builder");//2 more slots for workers
        village.AddWorker("Miner2","miner");

        for (int i = 0; i < 2; i++) {
            village.Day(); // 2 Days past to build the second house
        }
        village.AddWorker("Miner3","miner");//2 more slots for workers
        village.AddWorker("Lumberjack3","lumberjack");

        for (int i = 0; i < 14; i++) {
            village.Day(); // 14 Days past gather resources for Castle
        }
        village.AddProject("Castle");

        for (int i = 0; i < 25; i++) {
            village.Day(); // 25 Days to build the Castle with the 2 builders
        }

        //This should end the game at daysGone 49 with the Day() added together
        assertTrue(village.isGameOver());
        assertEquals(49,village.getDaysGone());
    }
}
package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VillageInputTest {

    VillageInput villageInput;
    DatabaseConnection dbMock;

    @BeforeEach
    void setupEach(){
        dbMock = mock(DatabaseConnection.class);
    }

    @Test
    void Save_WhenSaveGameNoExistingName_ShouldAdd(){

        //Player Input Mock
        String playerInput = "MyFirstVillage\n";
        ByteArrayInputStream newSystemInput = new ByteArrayInputStream(playerInput.getBytes());

        //Given
        Village currentVillage = new Village();
        villageInput = new VillageInput(currentVillage,dbMock,new Scanner(newSystemInput));

        //Mock the database connection
        when(villageInput.databaseConnection.GetTownNames()).thenReturn(new ArrayList<>(List.of("Village1","Village2","Village3","Village4")));
        when(villageInput.databaseConnection.SaveVillage(any(),any())).thenReturn(true);

        //When
        villageInput.Save();

        verify(villageInput.databaseConnection,times(1)).SaveVillage(any(),any());
    }

    @ParameterizedTest
    @CsvSource(value = {"Village1,y,1","Village2,no,0"})
    void Save_WhenSaveGameNameExistsOrNot_ThenOverwriteOrNot(String villageName,String overWriteYorN,int saveVillageFunctionTimes){

        //Player Input Mock
        String playerInput = (villageName+"\n"+overWriteYorN+"\n");
        ByteArrayInputStream newSystemInput = new ByteArrayInputStream(playerInput.getBytes());

        //Given
        Village currentVillage = new Village();
        villageInput = new VillageInput(currentVillage,dbMock,new Scanner(newSystemInput));

        //Mock the database connection
        when(villageInput.databaseConnection.GetTownNames()).thenReturn(new ArrayList<>(List.of("Village1","Village2","Village3","Village4")));
        when(villageInput.databaseConnection.SaveVillage(any(),any())).thenReturn(true);

        //When
        villageInput.Save();

        verify(villageInput.databaseConnection,times(saveVillageFunctionTimes)).SaveVillage(any(),any());
    }

    @Test
    void Save_WhenSaveReturnFalse_ShouldPrintError(){

        //Player Input Mock
        String playerInput = "MyFirstVillage\n";
        ByteArrayInputStream newSystemInput = new ByteArrayInputStream(playerInput.getBytes());

        //Given
        Village currentVillage = new Village();
        villageInput = new VillageInput(currentVillage,dbMock,new Scanner(newSystemInput));
        String expectedErrorOutput = "Error, something went wrong. Could not save.";

        //Mock the database connection
        when(villageInput.databaseConnection.GetTownNames()).thenReturn(new ArrayList<>(List.of("Village1","Village2","Village3","Village4")));
        when(villageInput.databaseConnection.SaveVillage(any(),any())).thenReturn(false);

        //Get Output
        PrintStream originalOut = System.out; // Save Old outputstream.
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();// SETUP Reading log
        System.setOut(new PrintStream(outContent));

        //When
        villageInput.Save();

        verify(villageInput.databaseConnection,times(1)).SaveVillage(any(),any());
        assertEquals(expectedErrorOutput,outContent.toString().substring(111,155));

        System.setOut(originalOut);
        System.out.println(outContent);
    }

    @ParameterizedTest
    @CsvSource(value = {"Village3,1","Village4,1","Village5,0","Village6,0"})
    void Load_WhenVillageNameExists_ThenLoadVillageElseDont(String villageName,int loadVillageFunctionTimes){
        //Player Input Mock
        String playerInput = (villageName+"\n");
        ByteArrayInputStream newSystemInput = new ByteArrayInputStream(playerInput.getBytes());

        //Given
        Village currentVillage = new Village();
        villageInput = new VillageInput(currentVillage,dbMock,new Scanner(newSystemInput));

        //Mock the database connection
        when(villageInput.databaseConnection.GetTownNames()).thenReturn(new ArrayList<>(List.of("Village1","Village2","Village3","Village4")));
        when(villageInput.databaseConnection.LoadVillage(any())).thenReturn(new Village());

        //When
        villageInput.Load();

        verify(villageInput.databaseConnection,times(loadVillageFunctionTimes)).LoadVillage(any());
    }

    @Test
    void Load_WhenVillageIsNull_ThenLoadFail(){ // HÅller på med
        //Player Input Mock
        String playerInput = "Village1\n";
        ByteArrayInputStream newSystemInput = new ByteArrayInputStream(playerInput.getBytes());

        //Given
        Village currentVillage = new Village();
        villageInput = new VillageInput(currentVillage,dbMock,new Scanner(newSystemInput));

        //Mock the database connection
        when(villageInput.databaseConnection.GetTownNames()).thenReturn(new ArrayList<>(List.of("Village1","Village2","Village3","Village4")));
        when(villageInput.databaseConnection.LoadVillage(any())).thenReturn(null);

        //When
        villageInput.Load();

        verify(villageInput.databaseConnection,times(1)).LoadVillage(any());
    }

}
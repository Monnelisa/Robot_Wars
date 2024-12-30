package za.co.wethinkcode.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Scanner;

import java.nio.file.Paths;
import java.nio.file.Files;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import de.vandermeer.asciitable.AsciiTable;
import za.co.wethinkcode.server.utilites.Position;
import static za.co.wethinkcode.server.utilites.Colours.*;

public class ServerManager {

    // Constants for file path and default values
    private static final int DEFAULT_WORLD_X = 200;
    private static final int DEFAULT_WORLD_Y = 200;
    private static final int DEFAULT_VISIBILITY = 10;
    private static final int DEFAULT_REPAIR_TIME = 5;
    private static final int DEFAULT_RELOAD_TIME = 5;
    private static final int DEFAULT_SHIELD_STRENGTH = 10;
    private static final int DEFAULT_MAX_SHOTS = 10;
    private static final int DEFAULT_FIRE_RANGE = 10;


    // Configuration parameters
    static Position world; // The limits of the world
    static int visibility; // Visibility of the robot
    static int repairShieldsTime; // Time taken to repair shields
    static int reloadWeaponsTime; // Time taken to reload weapons
    static int maxShieldStrength; // Maximum strength of a shield
    static int maxShots;
    static int fireRange;
    static boolean usingGui = false;


    // JSON object to hold configuration
    public static JSONObject config = new JSONObject();


    // File path for configuration file
    public static String FILE_PATH = "ServerConfig.json";


    // Method to set up configuration parameters
    public static void setupConfigurationParameters(){
        Scanner scanner = new Scanner(System.in);
        printBLUE("Creating config file......");

        // Check if configuration file exists
        if (!createFile()) {
            if (!getYesOrNoInput(scanner, "Would you like to create a new config file? Enter Yes or No:")){
                printConfig();
                return;
            }
            deleteExistingFile();
        }

        if (getYesOrNoInput(scanner, "Would you like to use the default configuration? Enter Yes or No:")) {
            setDefaultConfiguration();
        } else {
            promptForConfiguration(scanner);
        }
            storeConfiguration();
            writeFile(config);
            printConfig();
    }


    //  set variables to default
    private static void setDefaultConfiguration() {
        world = new Position(DEFAULT_WORLD_X, DEFAULT_WORLD_Y);
        visibility = DEFAULT_VISIBILITY;
        repairShieldsTime = DEFAULT_REPAIR_TIME;
        reloadWeaponsTime = DEFAULT_RELOAD_TIME;
        maxShieldStrength = DEFAULT_SHIELD_STRENGTH;
        maxShots = DEFAULT_MAX_SHOTS;
        fireRange =DEFAULT_FIRE_RANGE;
        usingGui = false;
    }


    //    Prompts user for config inputs
    private static void promptForConfiguration(Scanner scanner) {
        if (getYesOrNoInput(scanner, "Would you like a GUI world? Enter Yes or No:")) {
            world = new Position(480, 480);
            usingGui = true;
        } else {
            world = new Position(getIntInput(scanner, "Enter the World limit X (integer):"),
                    getIntInput(scanner, "Enter the World limit Y (integer):"));
        }

        visibility = getIntInput(scanner, "Enter the visibility (in steps, integer): ");
        repairShieldsTime = getIntInput(scanner, "Enter the time to repair shields (in seconds, integer): ");
        reloadWeaponsTime = getIntInput(scanner, "Enter the time to reload weapons (in seconds, integer): ");
        maxShieldStrength = getIntInput(scanner, "Enter the maximum strength of a shield (in hits, integer): ");
        maxShots = getIntInput(scanner, "Enter the maximum shots (in hits, integer): ");
        fireRange = getIntInput(scanner, "Enter the maximum fire range (in hits, integer): ");
    }


    //  add config to jsonObject
    private static void storeConfiguration() {
        JSONArray limitArray = new JSONArray();
        limitArray.put(world.getX());
        limitArray.put(world.getY());
        config.put("world", limitArray);
        config.put("visibility", visibility);
        config.put("repair", repairShieldsTime);
        config.put("reload", reloadWeaponsTime);
        config.put("shield", maxShieldStrength);
        config.put("shots", maxShots);
        config.put("fireRange", fireRange);
        config.put("gui", usingGui);
    }


    // Method to create configuration file
    public static Boolean createFile() {
        try {
            File configFile = new File(FILE_PATH);
            if (configFile.createNewFile()) {
                printGREEN("File created: " + configFile.getName());
                return true;
            } else if (readFiles() == null){
                printGREEN("File being deleted and New one Created");
                configFile.delete();
                configFile.createNewFile();
                printGREEN("File created: " + configFile.getName());
                return true;
            }else{
                printRED("File already exists!");
                return false;
            }
        } catch (IOException e) {
            printRED("An error occurred."+ e);
            return false;
        }
    }


    // deletes file if exists
    private static void deleteExistingFile() {
        File file = new File(FILE_PATH);
        if (file.delete()) {
            printGREEN("Configuration file deleted successfully.");
            createFile();
        } else {
            printRED("Failed to delete the configuration file.");
        }
    }


    // Method to write configuration to file
    public static void writeFile(JSONObject jsonObj){
        try (FileWriter file = new FileWriter(FILE_PATH)) {
            file.write(jsonObj.toString(4));
            file.flush();
            printGREEN("Config saved!");
        } catch (IOException e) {
            printRED(String.valueOf(e));
        }
    }


    // Method to read configuration from file
    public static JSONObject readFiles() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
            return new JSONObject(content);
        } catch (IOException e) {
            printRED("Config error: Could not read the file.");
        } catch (JSONException e) {
            printRED("Config error: The file is not a valid JSON.");
        }
        return null;
    }


    // Method to print configured parameters
    public static void printConfig(){
        JSONObject jsonConfig = readFiles();
        printLightPurple("GUI: "+jsonConfig.get("gui"));
        AsciiTable configTable = new AsciiTable();
        configTable.getContext().setWidth(30); // Set custom width
        configTable.addRule();
        configTable.addRow("World: ",jsonConfig.get("world"));
        configTable.addRule();
        configTable.addRow("Visibility: ",jsonConfig.get("visibility"));
        configTable.addRule();
        configTable.addRow("Max Shields: ",jsonConfig.get("shield"));
        configTable.addRule();
        configTable.addRow("Max Shots: ",jsonConfig.get("shots"));
        configTable.addRule();
        configTable.addRow("Fire Range: ",jsonConfig.get("fireRange"));
        configTable.addRule();
        configTable.addRow("Reload time: ",jsonConfig.get("reload"));
        configTable.addRule();
        configTable.addRow("Repair time: ",jsonConfig.get("repair"));
        configTable.addRule();
        String rend = configTable.render();
        printLightPurple(rend);
    }


    // standard input/scanner with error handling for int
    private static int getIntInput(Scanner scanner, String prompt) {
        printYELLOW(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                printRED("Invalid input. Please enter an integer value.");
            }
        }
    }


    // standard input/scanner with error handling for yes or no
    private static boolean getYesOrNoInput(Scanner scanner, String prompt) {
        printYELLOW(prompt);
        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("yes")) {
                return true;
            } else if (input.equalsIgnoreCase("no")) {
                return false;
            } else {
                printRED("Enter yes or no please!");
            }
        }
    }
}

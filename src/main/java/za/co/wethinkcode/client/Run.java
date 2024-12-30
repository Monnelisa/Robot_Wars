package za.co.wethinkcode.client;

import java.util.Scanner;
import static java.lang.Integer.parseInt;

import za.co.wethinkcode.client.robot.IRobot;
import za.co.wethinkcode.client.robot.TankRobot;
import za.co.wethinkcode.client.utilites.Helpers;
import za.co.wethinkcode.client.robot.SniperRobot;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.resources.Resources;
import za.co.wethinkcode.client.gui.GUILogic;
import za.co.wethinkcode.client.gui.screens.IngameScreen;
import za.co.wethinkcode.client.robot.FighterRobot;

import static za.co.wethinkcode.client.utilites.Colours.*;

/**
 * This class represents the entry point for starting the client application
 * for Robot Wars. It initializes the game environment, prompts the user
 * for robot configuration details, and manages the client-server interaction.
 */
public class Run {
    private static IRobot robot;
    private static Scanner scanner = new Scanner(System.in);
    private static String ip = "localhost";
    private static String port = "30230";
    private static boolean useGui = false;

    /**
     * Executes the Robot Wars client application.
     *
     * @param args command-line arguments: [0] IP address, [1] port number, [2] optional "-g" flag for GUI mode
     */
    public static void execute(String[] args) {
        printCYAN_BACKGROUND("WELCOME TO ROBOT WARS");

        // Parse command-line arguments for IP and port
        if (args.length > 0) {
            ip = args[0];
            port = args[1];
        }

        // Validate IP and port inputs
        if (Helpers.isValidIP(ip) && Helpers.isInt(port)) {
            printYELLOW("What would you like to call your robot?");
            String robotName = scanner.nextLine();
            Integer robotMake = getIntInput(scanner,"Please enter the digit of the robot model you " +
                    "would like to use:\n" +
                    "1. Fighter\n"+"2. Tank\n"+"3. Sniper");

            // Initialize the selected robot based on user input
            switch (robotMake) {
                case 1:
                    robot = new FighterRobot(robotName);
                    break;
                case 2:
                    robot = new TankRobot(robotName);
                    break;
                case 3:
                    robot = new SniperRobot(robotName);
                    break;
                default:
                    robot = new FighterRobot(robotName);
                    break;
            }

            // Check for GUI mode flag and start GUI if specified
            if (args.length == 3 && "-g".equals(args[2])) {
                startGui();
                useGui = true;
            }

            // Initialize and execute the client with specified parameters
            Client client = new Client(ip, parseInt(port), robot, useGui);
            client.execute();

            // Close the scanner input
            scanner.close();
        }
        else {
            printRED("Please ensure you have provided a valid IP address and port.");
        }
    }

    /**
     * Prompts the user for input and retrieves an integer value.
     *
     * @param scanner the Scanner object used to read user input
     * @param prompt the prompt message to display to the user
     * @return the integer value entered by the user
     */
    private static int getIntInput(Scanner scanner, String prompt) {
        printYELLOW(prompt);
        while (true) {
            try {
                return parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                printRED("Invalid input. Please enter an integer value.");
            }
        }
    }

    /**
     * Initializes and starts the GUI mode for the Robot Wars game.
     * This includes setting up the game environment, loading resources,
     * initializing GUI logic, and starting the game.
     */
    private static void startGui() {
        Game.info().setName("RobotWars");
        Game.info().setDescription("");
        Game.init();
        Game.graphics().setBaseRenderScale(1f);
        Resources.load("game.litidata");
        GUILogic.init();
        Game.screens().add(new IngameScreen());
        Game.world().loadEnvironment("map");
        Game.start();
    }
}

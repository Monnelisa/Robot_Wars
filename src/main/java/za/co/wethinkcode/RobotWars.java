package za.co.wethinkcode;

import java.util.Scanner;

import za.co.wethinkcode.client.Run;

import static za.co.wethinkcode.server.utilites.Colours.*;

/**
 * Main entry point for the RobotWars application.
 * Allows the user to start a server or client instance or quit the application.
 */
public class RobotWars {

    /**
     * Main method to start the RobotWars application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        Scanner userIn =  new Scanner(System.in);
        CheckInput(userIn, "Would you like to start a server or client instance?",args);
        userIn.close();
    }
    /**
     * Prompts the user for input and executes the appropriate command.
     *
     * @param scanner The scanner object for user input.
     * @param prompt The prompt message to display to the user.
     * @param args Command line arguments.
     */
    private static void CheckInput(Scanner scanner, String prompt, String[] args) {
        printGREEN(prompt);
        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("client")) {
                Run.execute(args);
                break;
            } else if (input.equalsIgnoreCase("server")) {
                za.co.wethinkcode.server.Run.execute(args);
                break;
            } else if (input.equalsIgnoreCase("quit")) {
                printRED("Good bye!");
                System.exit(0);
                break;
            } else {
                printRED("Enter client or server please! else  quit");
            }
        }
    }
}

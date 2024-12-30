package za.co.wethinkcode.server.servercommnds;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import static za.co.wethinkcode.server.utilites.Colours.*;

/**
 * Utility class for handling console commands.
 */
public class CommandHandling {

    /**
     * Initiates the console command handling.
     */
    public static void console(){
    // Thread for handling console commands
    Thread consoleThread = new Thread(() -> {
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            while (true) {
                // Read a command from the console
                String command = consoleReader.readLine();
                try{
                    // Create and execute the command
                    Command cmd = Command.create(command);
                    cmd.execute();
                }catch (IllegalArgumentException e){
                    // Handle unsupported command error
                    printRED("Error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            // Handle IO exception
            printRED("Error reading from console: " + e.getMessage());
        } finally {
            try {
                // Close the console reader
                consoleReader.close();
            } catch (IOException e) {
                // Handle closing error
                printRED("Error closing console reader: " + e.getMessage());
            }
        }
    });
        // Start the console handling thread
        consoleThread.start();
    }
}
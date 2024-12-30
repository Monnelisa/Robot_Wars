package za.co.wethinkcode.server;

import za.co.wethinkcode.server.servercommnds.CommandHandling;

/**
 * Entry point for running the server application.
 * This class initializes the server with a specified or default port.
 */
public class Run {

    /**
     * Main method to start the server.
     *
     * @param args command-line arguments specifying the port number
     */
    public static void execute(String[] args) {
        // Define variable for the server port
        int PORT;

        // Check if command-line arguments are provided
        if (args.length==0)
            PORT = 30230;// Default port if no argument is provided
        else
            PORT = Integer.parseInt(args[0]);// Port specified in the command line

        // Setup configuration parameters for the server
        ServerManager.setupConfigurationParameters();

        // Initialize command handling for the server
        CommandHandling.console();

        // Create a new server instance with the specified port
        @SuppressWarnings("unused")
        Server server = new Server(PORT);

    }
}

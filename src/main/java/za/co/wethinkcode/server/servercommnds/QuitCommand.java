package za.co.wethinkcode.server.servercommnds;

import za.co.wethinkcode.server.Server;
import static za.co.wethinkcode.server.utilites.Colours.*;

/**
 * Command to shut down the server and exit the application.
 */
public class QuitCommand extends Command {

    /**
     * Constructs a QuitCommand.
     */
    public QuitCommand() {
        super("Quit");
    }

    /**
     * Executes the quit command, shutting down the server and exiting the application.
     *
     * @return true if the command executed successfully.
     */
    @Override
    public boolean execute() {
        printRED("Shutting down server...");
        Server.setIsClosed(true);
        System.exit(0);
        return true;
    }
}


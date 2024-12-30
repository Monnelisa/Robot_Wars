package za.co.wethinkcode.server.servercommnds;

import za.co.wethinkcode.server.RobotManager;

/**
 * Command to list all robots in the game world and display their details.
 */
public class RobotsCommand extends Command {

    /**
     * Constructs a RobotsCommand.
     */
    public RobotsCommand() {
        super("Robots");
    }


    /**
     * Executes the command, printing the details of all robots in the game world.
     *
     * @return true if the command executed successfully.
     */
    @Override
    public boolean execute() {
        RobotManager.printRobots();
        return true;
    }
}
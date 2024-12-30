package za.co.wethinkcode.server.servercommnds;

import java.util.List;

import za.co.wethinkcode.server.RobotManager;
import za.co.wethinkcode.server.Server;
import za.co.wethinkcode.server.world.Obstacle;

import static za.co.wethinkcode.server.ServerManager.printConfig;
import static za.co.wethinkcode.server.utilites.Colours.*;

/**
 * Command to dump the state of the world and robots.
 */
public class DumpCommand extends Command{

    /**
     * Constructs a DumpCommand.
     */
    public DumpCommand() {
        super("dump");
    }

    /**
     * Executes the command, printing the world and robot states.
     *
     * @return true if the command executed successfully.
     */
    @Override
    public boolean execute() {

        printLightPurple("WORLD_");
        printConfig();
        worldList();

        printLightPurple("\nROBOTS_");
        RobotManager.printRobots();
        return true;
    }

    /**
     * Displays a list of obstacles in the world.
     */
    public static void worldList(){
        List<Obstacle> obstacleList = Server.world.getObstacles();
        printLightPurple("Obstacles in the world:\n");
        if(obstacleList.isEmpty()) {
            printRED("No obstacle in this world");
            return;
        }

        for (Obstacle obs : obstacleList){
            String type = obs.getType();
            int obsTopX = obs.getTopRight().getX();
            int obsTopY = obs.getTopRight().getY();
            int obsBotX = obs.getBottomLeft().getX();
            int obsBotY = obs.getBottomLeft().getY();

            String position = (LIGHT_PURPLE +"["+obsBotX+","+obsBotY+"]"+"to"+"["+obsTopX+","+obsTopY+"]"+RESET);
            String formattedType = getColorForType(type) + type + RESET + " - " + position;
            print("\t" + formattedType);
        }
    }

    /**
     * Determines the color for a given obstacle type.
     *
     * @param type the type of the obstacle.
     * @return the color code for the obstacle type.
     */
    private static String getColorForType(String type) {
        return switch (type.toLowerCase()) {
            case "rock" -> LIGHT_GREY;
            case "pit" -> DARK_GREY;
            case "lake" -> LIGHT_BLUE;
            case "mountain" -> LIGHT_GREEN;
            default -> RESET;
        };
    }
}

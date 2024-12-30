package za.co.wethinkcode.server.robotcommands;

import org.json.JSONObject;
import za.co.wethinkcode.server.Robot;
import za.co.wethinkcode.server.RobotManager;
import za.co.wethinkcode.server.utilites.UpdateResponse;

/**
 * Command to shut down a robot.
 */
public class ShutdownCommand extends Command {

    /**
     * Constructs a ShutdownCommand with the command name "off".
     */
    public ShutdownCommand() {
        super("off");
    }

    /**
     * Executes the shutdown command on the specified robot.
     *
     * @param target The robot to shut down.
     * @return A JSONObject containing the result of the shutdown operation.
     */
    @Override
    public JSONObject execute(Robot target) {
        JSONObject data = new JSONObject();
        try {
            // Remove the robot from the RobotManager
            RobotManager.removeRobot(target.getName());
            // Set the robot's status to shutdown
            target.setStatus(UpdateResponse.SHUTDOWN);
            // Provide a message indicating successful shutdown
            data.put("message", UpdateResponse.SHUTDOWN.getMessage());
        } catch (Exception e) {
            // Handle any exceptions that occur during shutdown
            data.put("message", "Failed to shutdown robot: " + target.getName());
        }
        return data;
    }
}

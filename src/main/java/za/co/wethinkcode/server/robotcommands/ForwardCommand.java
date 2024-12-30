package za.co.wethinkcode.server.robotcommands;

import org.json.JSONObject;

import za.co.wethinkcode.server.Robot;
import za.co.wethinkcode.server.utilites.UpdateResponse;

/**
 * Command to move a robot forward by a specified number of steps.
 */
public class ForwardCommand extends Command {
    /**
     * Constructs a ForwardCommand with arguments.
     *
     * @param argument The arguments for the forward command (number of steps).
     */
    public ForwardCommand(String[] argument) {
        super("forward", argument);
    }

    /**
     * Executes the forward command on the specified robot.
     *
     * @param target The robot to execute the command on.
     * @return A JSONObject containing the result of the forward command.
     */
    @Override
    public JSONObject execute(Robot target) {
        JSONObject data = new JSONObject();
        String[] args = getArgument();
        int steps;
        // If no arguments provided or empty, set failed status
        if (args == null || args.length == 0 || args[0].isEmpty()) {
            target.setStatus(UpdateResponse.FAILED_ARGS);
        } else {
            try {
                steps = Integer.parseInt(args[0]);
                target.updatePosition(steps);
                // If move is successful, return success message
                if (target.getStatus() == UpdateResponse.NORMAL) return data.put("message", "Moved forward " + steps);
            } catch (NumberFormatException e) {
                // If number format exception occurs, set failed status
                target.setStatus(UpdateResponse.FAILED_ARGS);
            }
        }
        // Return the status message, whether successful or failed
        return data.put("message", target.getStatus().getMessage());
    }
}

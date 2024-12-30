package za.co.wethinkcode.server.robotcommands;

import org.json.JSONObject;

import za.co.wethinkcode.server.Robot;
import za.co.wethinkcode.server.utilites.UpdateResponse;

/**
 * Command to move the robot backward by a specified number of steps.
 */
public class BackCommand extends Command {

    /**
     * Constructs a BackCommand with arguments.
     *
     * @param argument The arguments for the back command (number of steps to move back).
     */
    public BackCommand(String[] argument) {
        super("back", argument);
    }

    /**
     * Executes the back command on the specified robot.
     *
     * @param target The robot to execute the command on.
     * @return A JSONObject containing the execution result message.
     */
    @Override
    public JSONObject execute(Robot target) {
        JSONObject data = new JSONObject();
        String[] args = getArgument();
        if (args == null || args.length == 0 || args[0].isEmpty()) {
            target.setStatus(UpdateResponse.FAILED_ARGS);
            return data.put("message", target.getStatus().getMessage());
        } else {
            try {
                int steps = Integer.parseInt(args[0]);
                target.updatePosition(-steps);
                if (target.getStatus() == UpdateResponse.NORMAL) return data.put("message", "Moved forward " + steps);
            } catch (NumberFormatException e) {
                target.setStatus(UpdateResponse.FAILED_ARGS);
                return data.put("message", target.getStatus().getMessage());
            }
        }
        return data.put("message", target.getStatus().getMessage());
    }
}


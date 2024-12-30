package za.co.wethinkcode.server.robotcommands;

import org.json.JSONObject;

import za.co.wethinkcode.server.Robot;
import za.co.wethinkcode.server.utilites.UpdateResponse;

/**
 * Command to turn a robot left or right based on the provided argument.
 */
public class TurnCommand extends Command {

    /**
     * Constructs a TurnCommand with the command name "turn" and optional arguments.
     *
     * @param argument The arguments for the turn command. In this case, it expects ["left"] or ["right"].
     */
    public TurnCommand(String[] argument) {
        super("turn", argument);
    }

    /**
     * Executes the turn command to turn the specified robot left or right.
     *
     * @param target The robot to turn.
     * @return A JSONObject containing a message indicating the result of the turn operation.
     *         If successful, the message indicates the direction turned ("left" or "right").
     *         If unsuccessful due to missing or invalid arguments, it returns an appropriate error message.
     */
    @Override
    public JSONObject execute(Robot target) {
        JSONObject data = new JSONObject();
        String[] args = getArgument();

        if (args == null || args.length == 0 || args[0].isEmpty()) {
            target.setStatus(UpdateResponse.FAILED_ARGS);
        } else {
            String nTurn = args[0];
            if (nTurn.equals("left")) {
                target.updateDirection(false);
            } else if (nTurn.equals("right")) {
                target.updateDirection(true);
            } else {
                target.setStatus(UpdateResponse.FAILED_ARGS);
                return data.put("message", target.getStatus().getMessage());
            }

            if (target.getStatus() == UpdateResponse.NORMAL) {
                return data.put("message", "Turned " + nTurn);
            }
        }
        return data.put("message", target.getStatus().getMessage());
    }
}

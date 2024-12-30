package za.co.wethinkcode.server.robotcommands;

import org.json.JSONObject;
import za.co.wethinkcode.server.Robot;

/**
 * Command to retrieve the current state of a robot.
 */
public class StateCommand extends Command {

    /**
     * Constructs a StateCommand with the command name "state".
     */
    public StateCommand() {
        super("state");
    }

    /**
     * Executes the state command to retrieve the current state of the specified robot.
     *
     * @param target The robot whose state is to be retrieved.
     * @return A JSONObject containing the current state of the robot.
     *         The structure and content of this JSONObject should align with the expected state representation.
     *         In this case, it directly returns the result of `target.getCurrentState()`.
     */
    @Override
    public JSONObject execute(Robot target) {
        return target.getCurrentState();
    }
}

package za.co.wethinkcode.server.robotcommands;

import org.json.JSONObject;

import za.co.wethinkcode.server.Robot;
import za.co.wethinkcode.server.utilites.UpdateResponse;

/**
 * Command to handle errors or unrecognized commands on a robot.
 */
public class ErrorCommand extends Command{

    /**
     * Constructs an ErrorCommand.
     */
    public ErrorCommand() {
        super("error");
    }

    /**
     * Executes the error command on the specified robot.
     *
     * @param target The robot to execute the command on.
     * @return A JSONObject containing the error message.
     */
    @Override
    public JSONObject execute(Robot target) {
        JSONObject data = new JSONObject();
        data.put("messages", UpdateResponse.FAILED_COMMAND.getMessage());
        return data;
    }
}

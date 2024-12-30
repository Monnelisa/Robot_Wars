package za.co.wethinkcode.server.robotcommands;

import org.json.JSONObject;

import za.co.wethinkcode.server.Robot;
import za.co.wethinkcode.server.utilites.UpdateResponse;

/**
 * Command to display help information for a robot.
 */
public class HelpCommand extends Command {

    /**
     * Constructs a HelpCommand.
     */
    public HelpCommand() {
        super("help");
    }

    /**
     * Executes the help command.
     *
     * @param target The robot to execute the command on (not used in this command).
     * @return A JSONObject containing the help message.
     */
    @Override
    public JSONObject execute(Robot target) {
        JSONObject data = new JSONObject();
        // Retrieve the help message from UpdateResponse and put it in the JSON data
        data.put("message",UpdateResponse.HELP.getMessage());
        return data;
    }
}

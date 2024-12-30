package za.co.wethinkcode.server.robotcommands;

import org.json.JSONArray;
import org.json.JSONObject;

import za.co.wethinkcode.server.Robot;
import za.co.wethinkcode.server.RobotManager;
import za.co.wethinkcode.server.ServerManager;
import za.co.wethinkcode.server.utilites.UpdateResponse;


public class LaunchCommand extends Command {

    /**
     * Constructs a LaunchCommand with arguments.
     *
     * @param argument The arguments for launching the robot.
     */
    public LaunchCommand(String[] argument) {
        super("launch", argument);
    }

    /**
     * Executes the launch command to add and configure a new robot.
     *
     * @param target The robot to be launched.
     * @return A JSONObject containing the status and initial configuration of the launched robot.
     */
    @Override
    public JSONObject execute(Robot target) {
        JSONObject data = new JSONObject();

        // Attempt to add the robot to the manager and handle status accordingly
        UpdateResponse status =  RobotManager.addRobot(target);
        if (!status.equals(UpdateResponse.SUCCESS)){
            target.setStatus(status);
            data.put("message", target.getStatus().getMessage());
            return data;
            }

        String[] launchArgs = this.getArgument();

        try {
            if (launchArgs[0].equalsIgnoreCase("default")){
                // Set default configuration from server config
                JSONObject config = ServerManager.readFiles();
                target.setKind(launchArgs[0]);
                int shields = (int) config.get("shots");
                int shots = (int) config.get("shields");

                //set shields and shots
                target.setShields(shields);
                target.setShots(shots);

                //set shields and shots won't be changed
                target.setMAX_SHIELDS(shields);
                target.setMAX_SHOTS(shots);

            }else if (launchArgs.length < 3) {
                // Not enough arguments provided
                target.setStatus(UpdateResponse.FAILED_ARGS);
                data.put("message",target.getStatus().getMessage());
                return data;

            }else{
                // Set custom configuration from arguments
                target.setKind(launchArgs[0]);
                int shields = Integer.parseInt(launchArgs[1]);
                int shots = Integer.parseInt(launchArgs[2]);

                //set shields and shots
                target.setShields(shields);
                target.setShots(shots);

                //set shields and shots won't be changed
                target.setMAX_SHIELDS(shields);
                target.setMAX_SHOTS(shots);
            }
        } catch (NumberFormatException e) {
            // Error parsing integer arguments
            target.setStatus(UpdateResponse.FAILED_ARGS);
            data.put("message",target.getStatus().getMessage());
            return data;

        }
        // If all setup is successful, set robot status to READY
        target.setStatus(UpdateResponse.READY);

        // Prepare additional data to return in the JSON response
        Integer[] positionArray = {target.getCurrentPosition().getX(), target.getCurrentPosition().getY()};
        JSONArray position = new JSONArray(positionArray);

        data.put("message", target.getStatus().getMessage());
        data.put("position", position);
        data.put("visibility", 5);
        data.put("reload", target.getReloadTime());
        data.put("repair", target.getRepairTime());
        data.put("shields",target.getShields());
        data.put("shots", target.getShots());
        data.put("fireDistance", target.getFiringRange());
        return data;
    }
}

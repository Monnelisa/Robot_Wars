package za.co.wethinkcode.server.robotcommands;

import org.json.JSONArray;
import org.json.JSONObject;

import za.co.wethinkcode.server.Robot;

import za.co.wethinkcode.server.world.World;
import za.co.wethinkcode.server.RobotManager;
import za.co.wethinkcode.server.world.Obstacle;
import za.co.wethinkcode.server.utilites.Position;

/**
 * Command to scan the environment in four cardinal directions and report findings.
 */
public class LookCommand extends Command {
    private int LOOK_RANGE; // Define the look range

    /**
     * Constructs a LookCommand with the command name "look".
     */
    public LookCommand() {
        super("look");
    }

    /**
     * Executes the look command to scan the environment and gather information.
     *
     * @param target The robot executing the command.
     * @return A JSONObject containing information about objects found in each direction.
     */
    @Override
    public JSONObject execute(Robot target) {
        World world = target.getWorld();
        LOOK_RANGE = world.getVisibility();
        Position currentPosition = target.getCurrentPosition();

        JSONArray objects = new JSONArray();

        // Look in each direction and gather artifacts
        lookInDirection(world, currentPosition, 0, 1, "NORTH", objects); // Look North
        lookInDirection(world, currentPosition, 0, -1, "SOUTH", objects); // Look South
        lookInDirection(world, currentPosition, 1, 0, "EAST", objects); // Look East
        lookInDirection(world, currentPosition, -1, 0, "WEST", objects); // Look West

        JSONObject data = new JSONObject();
        data.put("objects", objects);
        return data;
    }

    /**
     * Scans in a specific direction and adds detected objects to the JSONArray.
     *
     * @param world          The world object to scan.
     * @param currentPosition The current position of the robot.
     * @param dx         The change in X direction (movement in X axis).
     * @param dy         The change in Y direction (movement in Y axis).
     * @param direction      The direction being scanned ("NORTH", "SOUTH", "EAST", "WEST").
     * @param objects        The JSONArray to which detected objects are added.
     */
    private void lookInDirection(World world, Position currentPosition, int dx, int dy, String direction, JSONArray objects) {
        Obstacle alreadyReportedObstacle = null;
        for (int i = 1; i <= LOOK_RANGE; i++) {
            Position newPos = new Position(currentPosition.getX() + i * dx, currentPosition.getY() + i * dy);
//            check edge
            if (!world.isValidCoordinate(newPos)) {
                JSONObject edge = new JSONObject();
                edge.put("direction", direction);
                edge.put("type", "edge");
                edge.put("distance", i);
                objects.put(edge);
                return;
            }
//            check obs
            Obstacle foundObstacle = world.isObstacleAt(newPos);
            if (foundObstacle != null && !foundObstacle.getType().equals("pit")) {
                if (alreadyReportedObstacle == null || !alreadyReportedObstacle.equals(foundObstacle)) {
                    alreadyReportedObstacle = foundObstacle;
                    JSONObject obstacle = new JSONObject();
                    obstacle.put("direction", direction);
                    String objectType = foundObstacle.getType();
                    obstacle.put("type", objectType);
                    obstacle.put("distance", i);
                    objects.put(obstacle);
                    if (objectType.equals("mountain")) return;
                }
            }
//            check robot
            String robotName = RobotManager.isRobotAtPosition(newPos);
            if (!robotName.isEmpty()) {
                JSONObject robotInfo = new JSONObject();
                robotInfo.put("direction", direction);
                robotInfo.put("type", "robot");
                robotInfo.put("distance", i);
                objects.put(robotInfo);
            }
        }
    }
}

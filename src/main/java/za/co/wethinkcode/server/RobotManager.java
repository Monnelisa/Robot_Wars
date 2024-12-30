package za.co.wethinkcode.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.vandermeer.asciitable.AsciiTable;
import org.json.JSONArray;
import org.json.JSONObject;

import za.co.wethinkcode.server.utilites.Direction;
import za.co.wethinkcode.server.utilites.Position;
import za.co.wethinkcode.server.utilites.UpdateResponse;

import static za.co.wethinkcode.server.utilites.Colours.*;
import static za.co.wethinkcode.server.utilites.Colours.RESET;

/**
 * Manages the robots in the server, including adding, removing, and querying robots.
 */
public class RobotManager {
    public static int noAllowedPlayers = 4;
    private static final Map<String, Robot> robots = new ConcurrentHashMap<>();

    /**
     * Adds a robot to the manager.
     *
     * @param robot the robot to add
     * @return the result of the add operation
     */
    public static UpdateResponse addRobot(Robot robot) {
        if(isRobotNameAlreadyExists(robot.getName())){
            return UpdateResponse.FAILED_NAME_TAKEN;}
        else if(robots.size() == noAllowedPlayers){
            return UpdateResponse.FAILED_WORLD_FILLED;}

        robots.put(robot.getName(), robot);
        return UpdateResponse.SUCCESS;
    }

    /**
     * Gets a robot by name.
     *
     * @param name the name of the robot
     * @return the robot with the specified name, or null if not found
     */
    public static Robot getRobot(String name) {
        return robots.get(name);
    }

    /**
     * Checks if a robot name already exists.
     *
     * @param name the name to check
     * @return true if the name already exists, false otherwise
     */
    public static boolean isRobotNameAlreadyExists(String name) {
        return robots.containsKey(name);
    }

    /**
     * Removes a robot by name.
     *
     * @param name the name of the robot to remove
     */
    public static void removeRobot(String name) {
        robots.remove(name);
    }

    /**
     * Lists all robots and their states.
     *
     * @return a JSON array containing information about all robots
     */
    public static JSONArray listAllRobots() {
        JSONArray robotList = new JSONArray();
        for (Map.Entry<String, Robot> entry : robots.entrySet()) {
            JSONObject robotInfo = new JSONObject();
            Robot robot = entry.getValue();
            robotInfo.put("robot", entry.getKey());
            if (robot.getCurrentPosition() != null){
                JSONObject state = new JSONObject();
                Integer[] positionArray = {robot.getCurrentPosition().getX(), robot.getCurrentPosition().getY()};
                JSONArray position = new JSONArray(positionArray);
                state.put("position", position);
                state.put("direction", robot.getCurrentDirection());
                state.put("shields", robot.getShields());
                state.put("shots", robot.getShots());
                state.put("status", robot.getStatus().toString());

                robotInfo.put("state", state);
                robotList.put(robotInfo);
            }
        }
        return robotList;
    }

    /**
     * Displays a list of obstacles in the world.
     */
    public static void printRobots(){
        JSONArray robots = listAllRobots();
        if(robots.isEmpty()) printRED("No robots in this world");
        for (int i = 0; i < robots.length(); i++) {
            String color =  chooseRandomColor();
            JSONObject robot = robots.getJSONObject(i);
            String name = robot.getString("robot");
            JSONObject state = robot.getJSONObject("state");
            int shields = state.getInt("shields");
            JSONArray positionArray = state.getJSONArray("position");
            Position position = new Position(positionArray.getInt(0),positionArray.getInt(1));
            Direction direction = (Direction) state.get("direction");
            int shots = state.getInt("shots");
            String status = state.getString("status");

            AsciiTable robotsTable = new AsciiTable();
            robotsTable.getContext().setWidth(30); // Set custom width
            robotsTable.addRule();
            robotsTable.addRow("Robot Name: ",name);
            robotsTable.addRule();
            robotsTable.addRow("Position: ",position.toString());
            robotsTable.addRule();
            robotsTable.addRow("Direction: ",direction);
            robotsTable.addRule();
            robotsTable.addRow("Shields: ",shields);
            robotsTable.addRule();
            robotsTable.addRow("Shot: ",shots);
            robotsTable.addRule();
            robotsTable.addRow("Status: ",status);
            robotsTable.addRule();
            String rend = robotsTable.render();
            print(color+rend+RESET);
        }
    }

    /**
     * Checks if a robot is at a specified position.
     *
     * @param position the position to check
     * @return the name of the robot at the position, or an empty string if no robot is found
     */
    public static String isRobotAtPosition(Position position) {
        for (Map.Entry<String, Robot> robot: robots.entrySet()) {
            Robot iterRobot = robot.getValue();
            if (position.equals(iterRobot.getCurrentPosition())) {
                return robot.getKey();
            }
        }
        return "";
    }

    /**
     * Gets the robot at the specified position.
     *
     * @param name the name of the robot
     * @return the robot at the specified position, or null if not found
     */
    public static Robot robotAtPosition(String name) {
        if (robots.containsKey(name)) {
            return robots.get(name);
        }
        return null;
    }

    /**
     * Checks if there is a robot at a specified position.
     *
     * @param position the position to check
     * @return true if there is a robot at the position, false otherwise
     */
    public static boolean booleanRobotAtPosition(Position position) {
        for (Map.Entry<String, Robot> robot: robots.entrySet()) {
            Robot iterRobot = robot.getValue();
            if (position.equals(iterRobot.getCurrentPosition())) {
                return true;
            }
        }
        return false;
    }
}

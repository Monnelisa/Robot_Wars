package za.co.wethinkcode.client.robot;

import java.util.List;

import org.json.JSONObject;

import za.co.wethinkcode.client.utilites.Direction;
import za.co.wethinkcode.client.utilites.Position;

public interface IRobot {


    /**
     * Return the name of the robot.
     * @return robot name
     */
    String getName();

    /**
     * set the name of the robot
     * @param name String, new name for robot
     */
    void setName(String name);

    /**
     * Set the latest command and arguements after verifying command and args
     * are valid.
     * @param userInput string, raw unverified userInput
     * @return true if allowed command else false
     */
    boolean setCommandAndArgs(String userInput);

    /**
     * returns latest verified and stored command
     * @return command string
     */
    String getCommand();

    /**
     * returns latest list of verified arguements
     * @return list of arguements
     */
    List<String> getArgs();

    /**
     * Generates a JSONObject from robot variables and returns the object to send 
     * as a request to the server,
     * @return JSONObject, formatted command and args as expected by the server
     */
    JSONObject generateRequest();

    /**
     * changes the robots' current position to the passed position
     * @param newPosition, position provided by server in response
     * @return nothing
     */
    void setPosition(Position newPosition);


    /**
     * returns the robots' current position
     * @return  Position, current robot position
     */
    Position getPosition();

    /**
     * changes the robots' remaining shields to the passed position
     * @param shields, remaining shields provided by server response
     * @return nothing
     */
    void setShields(Integer shields);

    /**
     * returns robots' current remaining shields
     * @return Integer, current shields robot has left
     */
    Integer getShields();

    /**
     * changes the robots' remaining shots to the passed position
     * @param shots, remaining shots provided by server response
     * @return nothing
     */
    void setShots(Integer shots);

    /**
     * returns robots' current remaining shots
     * @return Integer, current shots robot has left
     */
    Integer getShots();

    /**
     * sets the robots' status as provided by a server response
     *
     * @param status, current robot status as provided by server response
     */
    default void setStatus(String status) {

    }

    /**
     * returns the robots' current status
     * @return String, current robot status
     */
    String getStatus();

    /**
     * sets the current robot direction to the direction provided by the server response
     * @param newDirection, Direction, robots' current direction according to the server.
     */
    void setDirection(Direction newDirection);

    /**
     * returns the robots' current direction
     * @return Direction, current robot direction
     */
    Direction getDirection();

    /**
     * sets the robots' maximum visibility as received from the launch response
     * @param visibility, Integer, maximum visibility
     */
    void setVisiblity(Integer visibility);

    /**
     * returns the maximum visibility the robot can see too
     * @return Integer, maximum visibility
     */
    Integer getVisibility();

    /**
     * sets the robots' time to reload as recieved from the launch response
     * @param reloadTime Integer, how long it takes for the robot to reload in seconds
     */
    void setReloadTime(Integer reloadTime);

    /**
     * returns the time for a reload to take place
     * @return Integer, how long a reload takes place for in seconds
     */
    Integer getReloadTime();

    /**
     * sets the robots' time to repair as received from the launch response
     * @param repairTime, Integer, how long it takes for the robot to repair in seconds
     */
    void setRepairTime(Integer repairTime);

    /**
     * returns the time for a repair to take place
     * @return Integer, how long a repair takes place for in seconds
     */
    Integer getRepairTime();

    /**
     * sets the maximum shields that a robot can repair up too
     * @param maxShields, Integer, maximum shields a robot can have
     */
    void setMaxShields(Integer maxShields);

    /**
     * returns the maximum shields a robot can repair up too
     * @return Integer, max shields a robot can repair up too
     */
    Integer getMaxShields();

    /**
     * Sets the firing range of the robot.
     *
     * @param range the firing range to set
     */
    public void setFiringRange(Integer range);

    /**
     * Retrieves the firing range of the robot.
     *
     * @return the firing range of the robot
     */
    public Integer getFiringRange();

}

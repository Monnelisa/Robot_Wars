package za.co.wethinkcode.client.robot;

import java.util.Arrays;
import java.util.List;
import java.util.Collections;

import org.json.JSONObject;
import org.json.JSONArray;

import za.co.wethinkcode.client.utilites.Direction;
import za.co.wethinkcode.client.utilites.Helpers;
import za.co.wethinkcode.client.utilites.Position;

public abstract class AbstractRobot implements IRobot{
    protected String robotName;
    protected String robotMake;
    protected Position currentPosition;
    protected Direction currentDirection;
    protected Integer remainingShields;
    protected Integer remainingShots;
    protected String status = "NORMAL";
    protected String command;
    protected List<String> arguments;
    protected Integer visibility;
    protected Integer reloadTime;
    protected Integer repairTime;
    protected Integer maxShields;
    protected Integer firingRange;


    public AbstractRobot(String name, String make, int shields, int shots) {
        this.robotName = name;
        this.robotMake = make;
        this.remainingShields = shields;
        this.remainingShots = shots;
    }

    public String getName() {
        return this.robotName;
    }

    public void setName(String name) {
        this.robotName = name;
    }

    public boolean setCommandAndArgs(String userInput) {
        String[] commandArgument = userInput.split(" ");
        if(Helpers.isValidCommand(commandArgument[0])) {
            if (commandArgument.length > 1) {
                if(Helpers.isActionCommand(commandArgument[0]) && Helpers.isInt(commandArgument[1])){
                    this.command = commandArgument[0];
                    String[] argArray = commandArgument[1].split(" ");
                    this.arguments = Arrays.asList(argArray);
                    return true;
                }
                if("turn".equals(commandArgument[0]) && "left".equals(commandArgument[1])
                                                        || "right".equals(commandArgument[1])) {
                    this.command = commandArgument[0];
                    String[] argArray = commandArgument[1].split(" ");
                    this.arguments = Arrays.asList(argArray);
                    return true;
                }
            }
            else {
                this.command = commandArgument[0];
                if ("launch".equals(this.command)) {
                    String[] launchArgs = {
                        this.robotMake,
                        this.remainingShields.toString(),
                        this.remainingShots.toString()
                    };
                    this.arguments = Arrays.asList(launchArgs);
                } else {
                    this.arguments = Collections.emptyList();
                }
                return true;
            }
        } return false;
    }

    public JSONObject generateRequest() {
        JSONObject request = new JSONObject();
        request.put("robot", this.robotName);
        request.put("command", this.command);
        JSONArray arguments = new JSONArray(this.arguments);
        request.put("arguments", arguments);
        return request;
    }

    public String getCommand() {
        return this.command;
    }

    public List<String> getArgs() {
        return this.arguments;
    }

    public void setPosition(Position newPosition) {
        this.currentPosition = newPosition;
    }

    public Position getPosition() {
        return this.currentPosition;
    }

    public void setDirection(Direction newDirection) {
        this.currentDirection = newDirection;
    }

    public Direction getDirection() {
        return this.currentDirection;
    }

    public void setShields(Integer shields) {
        this.remainingShields = shields;
    }

    public Integer getShields() {
        return this.remainingShields;
    }

    public void setShots(Integer shots) {
        this.remainingShots = shots;
    }

    public Integer getShots() {
        return this.remainingShots;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public void setVisiblity(Integer visibility) {
        this.visibility = visibility;
    }

    public Integer getVisibility() {
        return this.visibility;
    }

    public void setReloadTime(Integer reloadTime) {
        this.reloadTime = reloadTime;
    }

    public Integer getReloadTime() {
        return this.reloadTime;
    }

    public void setRepairTime(Integer repairTime) {
        this.repairTime = repairTime;
    }

    public Integer getRepairTime() {
        return this.repairTime;
    }

    public void setMaxShields(Integer maxShields) {
        this.maxShields = maxShields;
    }

    public Integer getMaxShields() {
        return this.maxShields;
    }

    public void setFiringRange(Integer range) {
        this.firingRange = range;
    }

    public Integer getFiringRange() {
        return this.firingRange;
    }
}

package za.co.wethinkcode.server;

import java.net.Socket;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import za.co.wethinkcode.server.world.World;
import za.co.wethinkcode.server.utilites.Position;
import za.co.wethinkcode.server.utilites.Direction;
import za.co.wethinkcode.server.robotcommands.Command;
import za.co.wethinkcode.server.utilites.UpdateResponse;

/**
 * Represents a robot in the server, handling its state and interactions.
 */

public class Robot {
    private String name;
    private Position currentPosition;
    private Direction currentDirection;
    private UpdateResponse status;
    private final World world;
    private String kind;
    private int MAX_SHIELDS;
    private int MAX_SHOTS;
    private int shields;
    private int shots;
    private int firingRange;
    private Thread robotThread;
    private Object clientConnection;


    /**
     * Constructs a new Robot with the given name and world.
     *
     * @param name  the name of the robot
     * @param world the world in which the robot exists
     */
    public Robot(String name, World world) {
        this.world = world;
        this.name = name;
        this.status = UpdateResponse.READY;
        this.firingRange = world.getFireRange();
        this.currentPosition = world.placeRobot();
        this.currentDirection = Direction.NORTH; // Default initial direction
    }

    public synchronized String getName() {
        return this.name;
    }

    public synchronized void setName(String name) {
        this.name = name;
    }

    public synchronized String getKind() {
        return this.kind;
    }

    public synchronized void setKind(String kind) {
        this.kind = kind;
    }

    public synchronized Direction getCurrentDirection() {
        return this.currentDirection;
    }

    public synchronized Position getCurrentPosition() {
        return this.currentPosition;
    }

    public synchronized UpdateResponse getStatus() {
        return this.status;
    }

    public synchronized void setStatus(UpdateResponse status) {
        this.status = status;
    }

    public synchronized int getShields() {
        return this.shields;
    }

    public synchronized int getShots() {
        return this.shots;
    }

    public int getReloadTime() {
        return world.getReloadTime();
    }

    public int getRepairTime() {
        return world.getRepairTime();
    }

    public synchronized void setShields(int shields) {
        this.shields = shields;
    }

    public synchronized void setShots(int shots) {
        this.shots = shots;
    }

    public int getMAX_SHIELDS() {
        return this.MAX_SHIELDS;
    }

    public int getMAX_SHOTS() {
        return this.MAX_SHOTS;
    }

    public void setMAX_SHIELDS(int shields) {
        this.MAX_SHIELDS = shields;
    }

    public void setMAX_SHOTS(int shots) {
        this.MAX_SHOTS = shots;
    }

    public void setFiringRange(int firingRange) {
        this.firingRange = firingRange;
    }

    public int getFiringRange() {
        return this.firingRange;
    }

    /**
     * Updates the position of the robot based on the number of steps and the current direction.
     *
     * @param nrSteps the number of steps to move
     */
    public synchronized void updatePosition(int nrSteps) {
        if (this.status == UpdateResponse.DEAD) {
            this.status = UpdateResponse.FAILED;
            return;
        }

        int newX = this.currentPosition.getX();
        int newY = this.currentPosition.getY();

        if (Direction.NORTH.equals(this.currentDirection)) {
            newY += nrSteps;
        } else if (Direction.EAST.equals(this.currentDirection)) {
            newX += nrSteps;
        } else if (Direction.WEST.equals(this.currentDirection)) {
            newX -= nrSteps;
        } else if (Direction.SOUTH.equals(this.currentDirection)) {
            newY -= nrSteps;
        }

        Position newPosition = new Position(newX, newY);

        if (!world.isValidCoordinate(newPosition)) {
            this.status = UpdateResponse.FAILED_OUTSIDE_WORLD;
            this.currentPosition = trimPosition(newPosition);
            return;
        }

        Position lastValidPosition = world.lastValidPosition(currentPosition, newPosition);

        this.currentPosition = lastValidPosition;
        if (world.isPositionBlocked(lastValidPosition)) {
            if (world.isObstacleAt(lastValidPosition).getType().equals("pit")){
                this.status = UpdateResponse.PITDEAD;
                return;
            }
        }
        if (!lastValidPosition.equals(newPosition)) {
            this.status = UpdateResponse.FAILED_OBSTRUCTED;
            return;
        }
        
        this.status = UpdateResponse.NORMAL;
    }

    public synchronized void setPosition(Position initialPosition) {
        this.currentPosition = initialPosition;
    }

    /**
     * Updates the direction of the robot.
     *
     * @param turnRight whether to turn right (true) or left (false)
     */
    public synchronized void updateDirection(boolean turnRight) {
        if (this.status == UpdateResponse.DEAD) {
            this.status = UpdateResponse.FAILED;
            return;
        }

        Direction currentDirect = this.currentDirection;
        Direction newDirection;

        if (turnRight) {
            if (currentDirect.equals(Direction.NORTH)) {
                newDirection = Direction.EAST;
            } else if (currentDirect.equals(Direction.EAST)) {
                newDirection = Direction.SOUTH;
            } else if (currentDirect.equals(Direction.SOUTH)) {
                newDirection = Direction.WEST;
            } else {
                newDirection = Direction.NORTH;
            }
        } else {
            if (currentDirect.equals(Direction.NORTH)) {
                newDirection = Direction.WEST;
            } else if (currentDirect.equals(Direction.WEST)) {
                newDirection = Direction.SOUTH;
            } else if (currentDirect.equals(Direction.SOUTH)) {
                newDirection = Direction.EAST;
            } else {
                newDirection = Direction.NORTH;
            }
        }
        this.currentDirection = newDirection;
        this.status = UpdateResponse.NORMAL;
    }

    /**
     * Handles a command given to the robot.
     *
     * @param command the command to handle
     * @return a JSON object representing the result of the command
     */
    public synchronized JSONObject handleCommand(Command command) {
        if (this.status == UpdateResponse.DEAD && !command.getName().equalsIgnoreCase("normal")) {
            JSONObject data = new JSONObject();
            data.put("message", "The robot is dead and cannot execute commands.");
            return data;
        }
        return command.execute(this);
    }

    public World getWorld() {
        return this.world;
    }

    /**
     * Inflicts damage on the robot, reducing its shields.
     */
    public synchronized void takeDamage(int dmg) {
        if (shields > 0) {
            shields -= dmg;
        }
        if (shields < 0) {
            this.status = UpdateResponse.DEAD;
        }
    }

    /**
     * Resets the shields of the robot to the maximum value.
     */
    public synchronized void resetShields() {
        this.shields = this.MAX_SHIELDS;
    }

    /**
     * Resets the shots of the robot to the maximum value.
     */
    public synchronized void resetShots() {
        this.shots = this.MAX_SHOTS;
    }

    /**
     * Gets the current state of the robot.
     *
     * @return a JSON object representing the current state of the robot
     */
    public synchronized JSONObject getCurrentState() {
        JSONObject state = new JSONObject();
        Integer[] positionArray = {currentPosition.getX(), currentPosition.getY()};
        JSONArray position = new JSONArray(positionArray);
        state.put("position", position);
        state.put("direction", currentDirection);
        state.put("shields", shields);
        state.put("shots", shots);
        state.put("status", status);
        return state;
    }

    /**
     * Trims the position of the robot to ensure it is within the world boundaries.
     *
     * @param position the position to trim
     * @return the trimmed position
     */
    private Position trimPosition(Position position) {
        int x = position.getX();
        int y = position.getY();

        if (x > world.getTopRight().getX()) {
            x = world.getTopRight().getX();
        } else if (x < world.getBottomLeft().getX()) {
            x = world.getBottomLeft().getX();
        } else if (y > world.getTopRight().getY()) {
            y = world.getTopRight().getY();
        } else if (y < world.getBottomLeft().getY()) {
            y = world.getBottomLeft().getY();
        }
        return new Position(x, y);
    }
    public synchronized void handleRobotDeath() {
        this.status = UpdateResponse.DEAD;
        RobotManager.removeRobot(this.name);
        if (robotThread != null && robotThread.isAlive()) {
            robotThread.interrupt(); // Assuming interrupting is safe for your threads
        }
        disconnectClient();
    }
    private synchronized void disconnectClient() {
        try {
            if (clientConnection instanceof Socket) {
                Socket socket = (Socket) clientConnection;
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setRobotThread(Thread thread) {
        this.robotThread = thread;
    }
    public void setClientConnection(Object connection) {
        this.clientConnection = connection;
    }

}

package za.co.wethinkcode.server.robotcommands;

import org.json.JSONObject;
import za.co.wethinkcode.server.Robot;
import za.co.wethinkcode.server.world.World;
import za.co.wethinkcode.server.RobotManager;
import za.co.wethinkcode.server.utilites.Position;
import za.co.wethinkcode.server.utilites.Direction;
import za.co.wethinkcode.server.utilites.UpdateResponse;

/**
 * Command to fire shots in a specified direction from the robot.
 */
public class FireCommand extends Command {

    /**
     * Constructs a FireCommand.
     */
    public FireCommand() {
        super("fire");
    }

    /**
     * Executes the fire command on the specified robot.
     *
     * @param target The robot to execute the command on.
     * @return A JSONObject containing the result of the fire command.
     */
    @Override
    public JSONObject execute(Robot target) {
        JSONObject data = new JSONObject();

        Position currentPosition = target.getCurrentPosition();
        Direction currentDirection = target.getCurrentDirection();
        int firingRange = target.getFiringRange();
        int shotsAvailable = target.getShots();

        if (shotsAvailable <= 0) {
            target.setStatus(UpdateResponse.MISS);
            return data.put("message", "No shots available.");
        }

        boolean hitDetected = false;

        for (int step = 1; step <= firingRange; step++) {
            Position shotPosition = currentDirection.move(currentPosition, step);

            // Check if there's an obstacle in the path
            if (isObstacleInPath(target.getWorld(), shotPosition)) {
                // Shooting an obstacle has no effect, continue checking next steps
                continue;
            }

            // Check for hits at the shot position
            Robot hitRobot = checkForHit(shotPosition);
            if (hitRobot != null) {
                if (hitRobot.getShields() <= 1) {
                    hitRobot.handleRobotDeath();
                    target.setStatus(UpdateResponse.KILL);
                    int shotsFired = calculateShotsFired(step);
                    target.setShots(Math.max(shotsAvailable - shotsFired, 0));
                    return data.put("message", UpdateResponse.KILL.getMessage());
                }

                int shotsFired = calculateShotsFired(step);
                target.setShots(Math.max(shotsAvailable - shotsFired, 0));

                hitRobot.setStatus(UpdateResponse.GOTHIT);
                hitRobot.takeDamage(shotsFired);
                target.setStatus(UpdateResponse.HIT);


                data.put("message", UpdateResponse.HIT.getMessage());
                data.put("distance", step);
                data.put("robot", hitRobot.getName());
                data.put("state", hitRobot.getCurrentState());

                hitDetected = true;
                break;
            }
        }

        if (!hitDetected) {
            target.setShots(shotsAvailable - 1);
            target.setStatus(UpdateResponse.MISS);
            data.put("message", UpdateResponse.MISS.getMessage());
        }

        return data;
    }

    /**
     * Calculates the number of shots fired based on the number of steps.
     *
     * @param steps The number of steps from the robot to the target.
     * @return The number of shots fired.
     */
    private int calculateShotsFired(int steps) {
        return switch (steps) {
            case 1 -> 5;
            case 2 -> 4;
            case 3 -> 3;
            case 4 -> 2;
            case 5 -> 1;
            default -> 0;
        };
    }

    /**
     * Checks if there's a robot at the specified position.
     *
     * @param position The position to check for a hit.
     * @return The robot at the position if detected, null otherwise.
     */
    private Robot checkForHit(Position position) {
        String robotAtPosition = RobotManager.isRobotAtPosition(position);
        return RobotManager.robotAtPosition(robotAtPosition);
    }

    /**
     * Checks if there's an obstacle in the path at the specified position.
     *
     * @param world The world object containing obstacles.
     * @param position The position to check for obstacles.
     * @return true if there's an obstacle, false otherwise.
     */
    private boolean isObstacleInPath(World world, Position position) {
        return world.isPositionBlocked(position);
    }
}

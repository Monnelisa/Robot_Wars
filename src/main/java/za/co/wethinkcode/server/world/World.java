package za.co.wethinkcode.server.world;

import java.util.List;
import java.util.Random;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import za.co.wethinkcode.server.RobotManager;
import za.co.wethinkcode.server.utilites.Position;

/**
 * Represents the game world where robots are placed and obstacles are generated.
 */
public class World{
    private final Random random;
    private final Position bottomLeft = new Position(0, 0);
    private Position topRight = new Position(200, 400);
    private List<Obstacle> obstacles = new ArrayList<Obstacle>();
    private final int reloadTime;
    private final int repairTime;
    private final int visibility;
    private final int fireRange;

    /**
     * Constructs a new World instance.
     */
    public World(Position topRight, Boolean usingGui,int reloadTime,int repairTime,int visibility ,int fireRange) {
        this.topRight = topRight;
        this.obstacles = new ArrayList<>();
        this.random = new Random();
        this.reloadTime = reloadTime;
        this.repairTime = repairTime;
        this.visibility = visibility;
        this.fireRange = fireRange;

        if (usingGui) this.obstacles = generateGUIObstacles();
        else generateObstacles(10);
    }

    /**
     * Constructor for world with defaults
     */
    public World() {
        this.random = new Random();
        this.reloadTime = 5;
        this.repairTime = 15;
        this.visibility = 5;
        this.fireRange = 5;
        generateObstacles(1);
    }

    public Position getBottomLeft() {
        return this.bottomLeft;
    }

    public Position getTopRight() {
        return this.topRight;
    }

    public List<Obstacle> getObstacles() {
        return this.obstacles;
    }

    public int getReloadTime() {
        return this.reloadTime;
    }

    public int getRepairTime() {
        return this.repairTime;
    }

    public int getVisibility() {
        return this.visibility;
    }

    public int getFireRange() {
        return fireRange;
    }
    /**
     * Checks if a given position is a valid coordinate within the world boundaries.
     *
     * @param newPosition the position to check
     * @return true if the position is valid, false otherwise
     */
    public boolean isValidCoordinate(Position newPosition) {
        return newPosition.isIn(bottomLeft, topRight);
    }

    /**
     * Checks if an obstacle is present at a given position.
     *
     * @param position the position to check
     * @return the obstacle at the position, or null if no obstacle is present
     */
    public Obstacle isObstacleAt(Position position) {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.isPositionBlocked(position)) {
                return obstacle;
            }
        }
        return null;
    }

    /**
     * Determines the last valid position between two positions to avoid obstacles.
     *
     * @param a the starting position
     * @param b the destination position
     * @return the last valid position before encountering an obstacle
     */
    public Position lastValidPosition(Position a, Position b) {
        boolean blocked = false;
        boolean inPit = false;
        int aX = a.getX();
        int bX = b.getX();
        int aY = a.getY();
        int bY = b.getY();

        if (aY < bY) {
            while (aY != bY && !blocked) {
                if (inPit) break;
                Position newPos = new Position(aX, aY + 1);
                Obstacle obstacle = isObstacleAt(newPos);
                blocked = (obstacle != null ||
                RobotManager.booleanRobotAtPosition(newPos));
                if (obstacle != null) {
                    if (obstacle.getType().equals("pit")) {
                        aY++;
                        inPit = true;
                    }
                }
                if (!blocked) aY++;
            }
        } else if (aY > bY) {
            while (aY != bY && !blocked) {
                if (inPit) break;
                Position newPos = new Position(aX, aY - 1);
                Obstacle obstacle = isObstacleAt(newPos);
                blocked = (obstacle != null ||
                RobotManager.booleanRobotAtPosition(newPos));
                if (obstacle != null) {
                    if (obstacle.getType().equals("pit")) {
                        aY--;
                        inPit = true;
                    }
                }
                if (!blocked) aY--;
            }
        } else if (aX < bX) {
            while (aX != bX && !blocked) {
                if (inPit) break;
                Position newPos = new Position(aX + 1, aY);
                Obstacle obstacle = isObstacleAt(newPos);
                blocked = (obstacle != null ||
                RobotManager.booleanRobotAtPosition(newPos));
                if (obstacle != null) {
                    if (obstacle.getType().equals("pit")) {
                        aX++;
                        inPit = true;
                    }
                }
                if (!blocked) aX++;
            }
        } else if (aX > bX) {
            while (aX != bX && !blocked) {
                if (inPit) break;
                Position newPos = new Position(aX - 1, aY);
                Obstacle obstacle = isObstacleAt(newPos);
                blocked = (obstacle != null ||
                RobotManager.booleanRobotAtPosition(newPos));
                if (obstacle != null) {
                    if (obstacle.getType().equals("pit")) {
                        aX--;
                        inPit = true;
                    }
                }
                if (!blocked) aX--;
            }
        }

        return new Position(aX, aY);
    }

    /**
     * Places a robot in a random position within the world boundaries.
     *
     * @return the position where the robot is placed
     */
    public Position placeRobot() {
        int x, y;

        do {
            x = random.nextInt(topRight.getX() + 1);
            y = random.nextInt(topRight.getY() + 1);
        } while (isPositionBlocked(new Position(x, y)) ||
                    RobotManager.booleanRobotAtPosition(new Position(x,y)));
            // Ensure the position is not blocked by an obstacle or robot

        return new Position(x, y);
    }

    /**
     * Checks if a position is blocked by an obstacle.
     *
     * @param position the position to check
     * @return true if the position is blocked, false otherwise
     */
    public boolean isPositionBlocked(Position position) {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.isPositionBlocked(position)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Generates a specified number of obstacles within the world.
     *
     * @param numberOfObstacles the number of obstacles to generate
     */
    public void generateObstacles(int numberOfObstacles) {
        for (int i = 0; i < numberOfObstacles; i++) {
            Obstacle obstacle = createRandomObstacle();
            if (obstacles.size() == 0) {
                obstacles.add(obstacle);
            } else if (obstacle != null && !isObstacleOverlapping(obstacle)) {
                obstacles.add(obstacle);
            } else {
                i--; // Retry if obstacle is null or overlapping
            }
        }
    }

    private Obstacle  createRandomObstacle() {
        int x = random.nextInt(topRight.getX());
        int y = random.nextInt(topRight.getY());
        int size = random.nextInt(5,10) + 1; // Random size between 1 and 5

        return switch (random.nextInt(4)) {
            case 0 -> new Mountain(x, y, 20);
            case 1 -> new Lake(x, y, 40);
            case 2 -> new BottomlessPit(x, y,size);
            case 3 -> new BasicObstacle(x, y,size);
            default -> null; // Should never happen
        };
    }

    private boolean isObstacleOverlapping(Obstacle newObstacle) {
        for (Obstacle obstacle : obstacles) {
            for (Position position : newObstacle.blockedPositions) {
                if (obstacle.isPositionBlocked(position)) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<Obstacle> generateGUIObstacles() {
        List<Obstacle> obstacles = new ArrayList<Obstacle>();
        try (InputStream inputStream = getClass().getResourceAsStream("/maps/obstacles.csv");
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader reader = new BufferedReader(inputStreamReader)){
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if ("type".equals(values[0])) continue;
                Integer x = Integer.parseInt(values[1]);
                Integer y = (480 - (Integer.parseInt(values[2])));
                if ("m".equals(values[0])) {
                    obstacles.add(new Mountain(x, y, 28));
                } else if ("l".equals(values[0])) {
                    obstacles.add(new Lake(x, y, 28));
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            return Collections.emptyList();
        }

        return obstacles;
    }
}

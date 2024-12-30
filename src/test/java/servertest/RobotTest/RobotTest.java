package servertest.RobotTest;

import org.junit.jupiter.api.Test;
import za.co.wethinkcode.server.Robot;
import za.co.wethinkcode.server.utilites.Direction;
import za.co.wethinkcode.server.utilites.Position;
import za.co.wethinkcode.server.world.World;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RobotTest {

    @Test
    public void testUpdatePosition_MoveForward() {
        // Create a mock World
        World mockWorld = new World();
        Position initialPosition = new Position(0, 0);
        Robot robot = new Robot("TestRobot", mockWorld);
        robot.setPosition(initialPosition);

        // Move the robot forward by 5 steps
        robot.updatePosition(5);

        // Expected position after moving forward by 5 steps from the initial position (0, 0)
        Position expectedPosition = new Position(0, 5);

        // Verify that the robot's position is updated correctly
        assertEquals(expectedPosition, robot.getCurrentPosition());
    }

    @Test
    public void testUpdateDirection_TurnLeft() {
        // Create a mock World
        World mockWorld = new World();
        Robot robot = new Robot("TestRobot", mockWorld);

        // Turn the robot left
        robot.updateDirection(false);

        // Expected direction after turning left from NORTH is WEST
        Direction expectedDirection = Direction.WEST;

        // Verify that the robot's direction is updated correctly
        assertEquals(expectedDirection, robot.getCurrentDirection());
    }

    @Test
    public void testUpdateDirection_TurnRight() {
        // Create a mock World
        World mockWorld = new World();
        Robot robot = new Robot("TestRobot", mockWorld);

        // Turn the robot right
        robot.updateDirection(true);

        // Expected direction after turning right from NORTH is EAST
        Direction expectedDirection = Direction.EAST;

        // Verify that the robot's direction is updated correctly
        assertEquals(expectedDirection, robot.getCurrentDirection());
    }

    @Test
    public void testTakeDamage() {
        // Create a mock World
        World mockWorld = new World();
        Robot robot = new Robot("TestRobot", mockWorld);
        robot.setShields(3); // Initial shields

        // Robot takes damage
        robot.takeDamage(1);

        // Expected shields after taking damage once
        int expectedShields = 2;

        // Verify that the robot's shields are updated correctly
        assertEquals(expectedShields, robot.getShields());
    }
}


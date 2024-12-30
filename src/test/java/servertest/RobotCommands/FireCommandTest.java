package servertest.RobotCommands;


import org.junit.jupiter.api.Test;
import za.co.wethinkcode.server.Robot;
import za.co.wethinkcode.server.world.World;
import za.co.wethinkcode.server.robotcommands.FireCommand;


public class FireCommandTest {
    public FireCommand fireCommand;
    private Robot robot;
    private World world;

    @Test
    public void setUp() {
        fireCommand = new FireCommand();
        world = new World();
        robot = new Robot("testRobot", world);
        robot.getCurrentPosition();
        robot.getCurrentDirection();
        robot.setShots(5);  // Set initial number of shots
    }
}
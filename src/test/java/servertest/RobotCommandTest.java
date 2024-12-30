package servertest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.server.Robot;
import za.co.wethinkcode.server.robotcommands.*;
import za.co.wethinkcode.server.world.World;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONObject;

public class RobotCommandTest {

    private Robot mockRobot;

    @BeforeEach
    public void setUp() {
        mockRobot = mock(Robot.class);
    }

    private Robot mock(Class<Robot> robotClass) {
        return null;
    }

    @Test
    public void testCreateLaunchCommand() {
        String[] args = {"robot1"};
        Command command = Command.create("launch", args);
        assertInstanceOf(LaunchCommand.class, command);
        assertEquals("launch", command.getName());
        assertEquals(args, command.getArgument());
    }

    @Test
    public void testCreateShutdownCommand() {
        Command command = Command.create("off", new String[]{});
        assertInstanceOf(ShutdownCommand.class, command);
        assertEquals("off", command.getName());
    }

    @Test
    public void testCreateHelpCommand() {
        Command command = Command.create("help", new String[]{});
        assertInstanceOf(HelpCommand.class, command);
        assertEquals("help", command.getName());
    }

    @Test
    public void testCreateForwardCommand() {
        String[] args = {"10"};
        Command command = Command.create("forward", args);
        assertInstanceOf(ForwardCommand.class, command);
        assertEquals("forward", command.getName());
        assertEquals(args, command.getArgument());
    }

    @Test
    public void testCreateBackCommand() {
        String[] args = {"10"};
        Command command = Command.create("back", args);
        assertInstanceOf(BackCommand.class, command);
        assertEquals("back", command.getName());
        assertEquals(args, command.getArgument());
    }

    @Test
    public void testCreateTurnCommand() {
        String[] args = {"left"};
        Command command = Command.create("turn", args);
        assertInstanceOf(TurnCommand.class, command);
        assertEquals("turn", command.getName());
        assertEquals(args, command.getArgument());
    }

    @Test
    public void testCreateLookCommand() {
        Command command = Command.create("look", new String[]{});
        assertInstanceOf(LookCommand.class, command);
        assertEquals("look", command.getName());
    }

    @Test
    public void testCreateFireCommand() {
        Command command = Command.create("fire", new String[]{});
        assertInstanceOf(FireCommand.class, command);
        assertEquals("fire", command.getName());
    }

    @Test
    public void testCreateReloadCommand() {
        Command command = Command.create("reload", new String[]{});
        assertInstanceOf(ReloadCommand.class, command);
        assertEquals("reload", command.getName());
    }

    @Test
    public void testCreateRepairCommand() {
        Command command = Command.create("repair", new String[]{});
        assertInstanceOf(RepairCommand.class, command);
        assertEquals("repair", command.getName());
    }

    @Test
    public void repairCommandTest(){
        // Testing repair command
        Robot robot = new Robot("TestBot", new World());
        robot.setShields(3); // Simulate damage

        RepairCommand repairCommand = new RepairCommand();
        JSONObject response = repairCommand.execute(robot);
        System.out.println(response.getString("message")); // Should print "Repairing in progress. Please wait..."
    }

    @Test
    public void testCreateStateCommand() {
        Command command = Command.create("state", new String[]{});
        assertInstanceOf(StateCommand.class, command);
        assertEquals("state", command.getName());
    }

    @Test
    public void testCreateErrorCommand() {
        Command command = Command.create("unknown", new String[]{});
        assertInstanceOf(ErrorCommand.class, command);
        assertEquals("error", command.getName());
    }
}

package servertest.RobotTest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.server.RobotManager;
import za.co.wethinkcode.server.utilites.Direction;
import za.co.wethinkcode.server.utilites.Position;
import za.co.wethinkcode.server.utilites.UpdateResponse;
import za.co.wethinkcode.server.Robot;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RobotManagerTest {

    private Robot mockRobot;
    private Position mockPosition;

    @BeforeEach
    public void setUp() {
        mockRobot = mock(Robot.class);
        mockPosition = mock(Position.class);
        RobotManager.removeRobot("testRobot"); // Clear any existing state
    }

    @Test
    public void testAddRobot() {
        when(mockRobot.getName()).thenReturn("testRobot");

        UpdateResponse response = RobotManager.addRobot(mockRobot);
        assertEquals(UpdateResponse.SUCCESS, response);
        assertEquals(mockRobot, RobotManager.getRobot("testRobot"));

        // Test adding robot with existing name
        response = RobotManager.addRobot(mockRobot);
        assertEquals(UpdateResponse.FAILED_NAME_TAKEN, response);

        // Test adding robot when world is full
        for (int i = 1; i < RobotManager.noAllowedPlayers; i++) {
            Robot additionalMockRobot = mock(Robot.class);
            when(additionalMockRobot.getName()).thenReturn("robot" + i);
            RobotManager.addRobot(additionalMockRobot);
        }
        Robot anotherMockRobot = mock(Robot.class);
        when(anotherMockRobot.getName()).thenReturn("extraRobot");
        response = RobotManager.addRobot(anotherMockRobot);
        assertEquals(UpdateResponse.FAILED_WORLD_FILLED, response);
    }

    @Test
    public void testGetRobot() {
        when(mockRobot.getName()).thenReturn("testRobot");
        RobotManager.addRobot(mockRobot);

        Robot retrievedRobot = RobotManager.getRobot("testRobot");
        assertEquals(mockRobot, retrievedRobot);
    }

    @Test
    public void testIsRobotNameAlreadyExists() {
        when(mockRobot.getName()).thenReturn("testRobot");
        RobotManager.addRobot(mockRobot);

        assertTrue(RobotManager.isRobotNameAlreadyExists("testRobot"));
        assertFalse(RobotManager.isRobotNameAlreadyExists("nonExistentRobot"));
    }

    @Test
    public void testRemoveRobot() {
        when(mockRobot.getName()).thenReturn("testRobot");
        RobotManager.addRobot(mockRobot);

        RobotManager.removeRobot("testRobot");
        assertNull(RobotManager.getRobot("testRobot"));
    }

    @Test
    public void testListAllRobots() {
        // Create mock instances
        Robot mockRobot1 = mock(Robot.class);
        Position mockPosition1 = mock(Position.class);

        // Mocking the robot object
        when(mockRobot1.getName()).thenReturn("testRobot");
        when(mockRobot1.getCurrentPosition()).thenReturn(mockPosition1);
        when(mockPosition1.getX()).thenReturn(0);
        when(mockPosition1.getY()).thenReturn(0);
        when(mockRobot1.getCurrentDirection()).thenReturn(Direction.NORTH);
        when(mockRobot1.getShields()).thenReturn(100);
        when(mockRobot1.getShots()).thenReturn(10);
        when(mockRobot1.getStatus()).thenReturn(UpdateResponse.READY);

        // Add the robot to the manager
        RobotManager.addRobot(mockRobot1);

        // List all robots
        JSONArray robotList = RobotManager.listAllRobots();

        // Assertions
        assertEquals(1, robotList.length());
        JSONObject robotInfo = robotList.getJSONObject(0);
        assertEquals("testRobot", robotInfo.getString("robot"));

        JSONObject state = robotInfo.getJSONObject("state");
        assertEquals(Direction.NORTH.toString(), state.get("direction").toString());
        assertEquals(100, state.getInt("shields"));
        assertEquals(10, state.getInt("shots"));
        assertEquals("READY", state.getString("status"));

        JSONArray position = state.getJSONArray("position");
        assertEquals(0, position.getInt(0));
        assertEquals(0, position.getInt(0));
    }

    @Test
    public void testIsRobotAtPosition() {
        // Create mock instances
        Robot mockRobot = mock(Robot.class);
        Position mockPosition = mock(Position.class);

        // Mocking the robot object
        when(mockRobot.getName()).thenReturn("testRobot");
        when(mockRobot.getCurrentPosition()).thenReturn(mockPosition);

        // Add the robot to the manager
        RobotManager.addRobot(mockRobot);

        // Test the method with the position of the robot
        assertEquals("testRobot", RobotManager.isRobotAtPosition(mockPosition));

        // Test the method with a different position
        Position anotherPosition = mock(Position.class);
        assertEquals("", RobotManager.isRobotAtPosition(anotherPosition));
    }

    @Test
    public void testRobotAtPosition() {
        when(mockRobot.getName()).thenReturn("testRobot");

        RobotManager.addRobot(mockRobot);
        assertEquals(mockRobot, RobotManager.robotAtPosition("testRobot"));

        assertNull(RobotManager.robotAtPosition("nonExistentRobot"));
    }
}
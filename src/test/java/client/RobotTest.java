package client;

import za.co.wethinkcode.client.robot.FighterRobot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.*;

import java.util.Collections;

public class RobotTest {

    @Test
    public void testName() {
        FighterRobot testRobot = new FighterRobot("testee");
        assertEquals(testRobot.getName(), "testee");
    }

    @Test
    public void testJustCommand() {
        FighterRobot testRobot = new FighterRobot("testee");
        assertTrue(testRobot.setCommandAndArgs("look"));
        assertEquals("look", testRobot.getCommand());
        assertEquals(Collections.emptyList(), testRobot.getArgs());
    }

    @Test
    public void testCommandAndArgSetting() {
        FighterRobot testRobot = new FighterRobot("testee");
        assertTrue(testRobot.setCommandAndArgs("forward 10"));
        assertEquals("forward", testRobot.getCommand());
        assertEquals("10", testRobot.getArgs().get(0));
    }

    @Test
    public void testIncorrectCommand() {
        FighterRobot testRobot = new FighterRobot("testee");
        assertTrue(testRobot.setCommandAndArgs("forward 10"));
        assertFalse(testRobot.setCommandAndArgs("ping pong"));
        assertEquals("forward", testRobot.getCommand());
        assertEquals("10", testRobot.getArgs().get(0));
    }

    @Test
    public void testIncorrectArgs() {
        FighterRobot testRobot = new FighterRobot("testee");
        assertTrue(testRobot.setCommandAndArgs("forward 10"));
        assertFalse(testRobot.setCommandAndArgs("back throwit"));
        assertEquals("forward", testRobot.getCommand());
        assertEquals("10", testRobot.getArgs().get(0));
    }
}

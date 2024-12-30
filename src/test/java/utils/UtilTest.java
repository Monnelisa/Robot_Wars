package utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.*;

import za.co.wethinkcode.client.utilites.Helpers;
import za.co.wethinkcode.server.utilites.UpdateResponse;

public class UtilTest {
    
    @Test
    public void testCommandCheck() {
        assertTrue(Helpers.isValidCommand("forward"));
        assertTrue(Helpers.isValidCommand("fire"));
        assertTrue(Helpers.isValidCommand("rEpAir"));
        assertFalse(Helpers.isValidCommand("fail"));
        assertFalse(Helpers.isValidCommand("hElLo"));
    }
    @Test
    public void testGetMessage() {
        UpdateResponse response = UpdateResponse.READY;
        response.setMessage("Ready");
        assertEquals("Ready", UpdateResponse.READY.getMessage());
        assertEquals("Shutting down...", UpdateResponse.SHUTDOWN.getMessage());
        assertEquals("Fired bullet", UpdateResponse.FIRE.getMessage());
        assertEquals("look", UpdateResponse.LOOK.getMessage());
        assertEquals("Reloaded", UpdateResponse.RELOAD.getMessage());
        assertEquals("Repaired", UpdateResponse.REPAIRED.getMessage());
        assertEquals("Done", UpdateResponse.SUCCESS.getMessage());
        assertEquals("failed", UpdateResponse.FAILED.getMessage());
        assertEquals("Unsupported command", UpdateResponse.FAILED_COMMAND.getMessage());
        assertEquals("Could not parse arguments", UpdateResponse.FAILED_ARGS.getMessage());
        assertEquals("You are at the edge", UpdateResponse.FAILED_OUTSIDE_WORLD.getMessage());
        assertEquals("Obstructed", UpdateResponse.FAILED_OBSTRUCTED.getMessage());
        assertEquals("Too many of you in this world", UpdateResponse.FAILED_NAME_TAKEN.getMessage());
        assertEquals("No more space in this world", UpdateResponse.FAILED_WORLD_FILLED.getMessage());
        assertEquals(
                "commands:\n" +
                        "OFF     - Shut down robot\n" +
                        "HELP    - provide information about commands\n" +
                        "FORWARD - move forward by specified number of steps, e.g. 'FORWARD 10'\n" +
                        "BACK    - move backwards by specified number of steps, e.g. 'BACK 10'\n" +
                        "LEFT    - turn left\n" +
                        "RIGHT   - turn right\n"+
                        "FIRE    - shoot your shot\n"+
                        "LOOK    - Look around your robot\n"+
                        "STATE   - display what state your robot is in\n"+
                        "REPAIR  - repair your robot\n"+
                        "RELOAD  - reload your shots\n"
                        ,
                    UpdateResponse.HELP.getMessage()
        );
    }

    @Test
    public void testSetMessage() {
        UpdateResponse response = UpdateResponse.READY;
        response.setMessage("New message");
        assertEquals("New message", response.getMessage());
    }
}


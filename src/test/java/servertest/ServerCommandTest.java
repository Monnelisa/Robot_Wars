package servertest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import za.co.wethinkcode.server.servercommnds.*;

public class ServerCommandTest {
        @Test
        public void testCreateQuitCommand() {
            Command command = Command.create("quit");
            assertInstanceOf(QuitCommand.class, command);
            assertEquals("quit", command.getName());
        }

        @Test
        public void testCreateRobotsCommand() {
            Command command = Command.create("robots");
            assertInstanceOf(RobotsCommand.class, command);
            assertEquals("robots", command.getName());
        }

        @Test
        public void testCreateDumpCommand() {
            Command command = Command.create("dump");
            assertInstanceOf(DumpCommand.class, command);
            assertEquals("dump", command.getName());
        }

        @Test
        public void testExecuteRobotsCommand() {
            Command command = new RobotsCommand();
            assertTrue(command.execute());
        }

}


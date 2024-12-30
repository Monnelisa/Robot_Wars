package servertest.world;

import za.co.wethinkcode.server.utilites.Position;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import za.co.wethinkcode.server.world.BasicObstacle;
import za.co.wethinkcode.server.world.Obstacle;

public class ObstacleTest {
    Obstacle obstacle;

    @BeforeEach
    public void setUp() {
        obstacle = new BasicObstacle(5, 5, 5);
    }

   @Test
   public void testGetTopLeft() {
       Position bottomLeft = new Position(5, 5);
       assertEquals(bottomLeft, obstacle.getBottomLeft());
   }

   @Test
   public void testBlockedPosition() {
       Position testPostion = new Position(7, 7);
       assertTrue(obstacle.isPositionBlocked(testPostion));
       testPostion = new Position(15, 15);
       assertFalse(obstacle.isPositionBlocked(testPostion));
   }
}

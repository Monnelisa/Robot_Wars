package servertest.world;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import za.co.wethinkcode.server.utilites.Position;

public class PositionTest {
    Position testPosition;

    @BeforeEach
    public void setUp() {
        testPosition = new Position(5, 5);
    }

   @Test
   public void testGetX() {
       assertEquals(5, testPosition.getX());
   }

   @Test
   public void testGetY() {
       assertEquals(5, testPosition.getY());
   }
}

package za.co.wethinkcode.server.world;

import java.util.HashSet;
import java.util.Objects;

import za.co.wethinkcode.server.utilites.Position;

/**
 * Represents an abstract class for obstacles in the game world.
 */
public abstract class Obstacle {
    protected Position bottomLeft;
    protected Position topRight;
    protected int size;
    protected String type;
    protected HashSet<Position> blockedPositions = new HashSet<Position>();

    /**
     * Constructs an obstacle with the specified parameters.
     *
     * @param bottomLeft The position of the bottom-left corner of the obstacle.
     * @param topRight   The position of the top-right corner of the obstacle.
     */
    public Obstacle(Position bottomLeft, Position topRight) {
        this.bottomLeft = bottomLeft;
        this.topRight = topRight;
        calcBlockedPositions();
    }

    public Position getBottomLeft() {
        return this.bottomLeft;
    }

    public Position getTopRight() {
        return this.topRight;
    }

    /**
     * Abstract method to calculate the blocked positions by the obstacle.
     */
    protected abstract void calcBlockedPositions();

    /**
     * Gets the type of the obstacle.
     *
     * @return The type of the obstacle.
     */
    public String getType() {
        return type;
    }

    /**
     * Checks if a given position is blocked by the obstacle.
     *
     * @param newPosition The position to check.
     * @return True if the position is blocked, false otherwise.
     */
    public boolean isPositionBlocked(Position newPosition) {
        int x1 =  getBottomLeft().getX();
        int y1 = getBottomLeft().getY();
        int x2 = getTopRight().getX();
        int y2 = getTopRight().getY();

        int x = newPosition.getX();
        int y = newPosition.getY();

        return x >= x1 && x <= x2 && y >= y1 && y <= y2;
    }

    /**
     * Overrides the equals method to compare two obstacles based on their positions.
     *
     * @param o The object to compare.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Obstacle obstacle = (Obstacle) o;

        if (bottomLeft != obstacle.bottomLeft) return false;
        return topRight == obstacle.topRight;
    }

    /**
     * Overrides the hashCode method to generate a hash code based on the obstacle's positions.
     *
     * @return The hash code of the obstacle.
     */
    @Override
    public int hashCode() {
        return Objects.hash(bottomLeft, topRight);
    }
}

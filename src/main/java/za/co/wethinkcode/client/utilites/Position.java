package za.co.wethinkcode.client.utilites;

import java.util.Objects;

public class Position {
    private final int x;
    private final int y;

/**
     * Constructs a new Position object with the specified x and y coordinates.
     *
     * @param x the x-coordinate of the position
     * @param y the y-coordinate of the position
     */
    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the x-coordinate of the position.
     *
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of the position.
     *
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param o the object to compare to
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (x != position.x) return false;
        return y == position.y;
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Returns a string representation of the position.
     *
     * @return a string representation of the position
     */
    @Override
    public String toString() {
        return "[" + this.getX() + "," + this.getY() + "]";
    }

    /**
     * Checks if this position is within the specified bottom-left and top-right positions,
     * forming a rectangular region.
     *
     * @param bottomLeft the bottom-left position of the region
     * @param topRight   the top-right position of the region
     * @return true if this position is within the specified region, false otherwise
     */
    public boolean isIn(Position bottomLeft, Position topRight) {
        boolean withinTop = this.y <= topRight.getY();
        boolean withinBottom = this.y >= bottomLeft.getY();
        boolean withinLeft = this.x >= bottomLeft.getX();
        boolean withinRight = this.x <= topRight.getX();
        return withinTop && withinBottom && withinLeft && withinRight;
    }
    
}


package za.co.wethinkcode.server.utilites;

import java.util.Objects;

public class Position {
    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (x != position.x) return false;
        return y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "[" + this.getX() + "," + this.getY() + "]";
    }

    public boolean isIn(Position bottomLeft, Position topRight) {
        boolean withinTop = this.y <= topRight.getY();
        boolean withinBottom = this.y >= bottomLeft.getY();
        boolean withinLeft = this.x >= bottomLeft.getX();
        boolean withinRight = this.x <= topRight.getX();
        return withinTop && withinBottom && withinLeft && withinRight;
    }
}

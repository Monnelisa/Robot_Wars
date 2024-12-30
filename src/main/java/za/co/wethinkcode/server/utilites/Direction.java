package za.co.wethinkcode.server.utilites;

public enum Direction {
    NORTH,
    SOUTH,
    EAST,
    WEST;

    @Override
    public String toString() {
        switch (this) {
            case NORTH:
                return "North";
            case SOUTH:
                return "South";
            case EAST:
                return "East";
            case WEST:
                return "West";
            default:
                return "";
        }
    }

    public Position move(Position position, int steps) {
        switch (this) {
            case NORTH:
                return new Position(position.getX(), position.getY() + steps);
            case SOUTH:
                return new Position(position.getX(), position.getY() - steps);
            case EAST:
                return new Position(position.getX() + steps, position.getY());
            case WEST:
                return new Position(position.getX() - steps, position.getY());
            default:
                throw new IllegalArgumentException("Invalid direction: " + this);
        }
    }

    Integer getX() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getX'");
    }
}

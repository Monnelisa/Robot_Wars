package za.co.wethinkcode.server.world;

import za.co.wethinkcode.server.utilites.Position;

public class Lake extends Obstacle {
    /**
     * Constructs a lake with specified bottom-left position and size.
     * The rectangle's width is set to the size and the height can be a fixed value or derived.
     *
     * @param x    The x-coordinate of the bottom-left corner of the mountain.
     * @param y    The y-coordinate of the bottom-left corner of the mountain.
     * @param size The size of the mountain (used for width).
     */
    public Lake(int x, int y, int size) {
        super(new Position(x, y), new Position(x + size, y + size));
        this.size = size;
        calcBlockedPositions();
    }

    public String getType() {
        return "lake";
    }

    /**
     * Calculates and stores all blocked positions within the mountain's rectangular area.
     */
    @Override
    protected void calcBlockedPositions() {
        int minX = Math.min(this.getBottomLeft().getX(), this.getTopRight().getX());
        int minY = Math.min(this.getBottomLeft().getY(), this.getTopRight().getY());
        int maxX = Math.max(this.getBottomLeft().getX(), this.getTopRight().getX());
        int maxY = Math.max(this.getBottomLeft().getY(), this.getTopRight().getY());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                this.blockedPositions.add(new Position(x, y));
            }
        }
    }
}

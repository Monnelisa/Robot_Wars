package za.co.wethinkcode.client.gui;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.EntityInfo;
import de.gurkenlabs.litiengine.entities.MovementInfo;
import de.gurkenlabs.litiengine.graphics.RenderType;
import za.co.wethinkcode.client.utilites.Position;

@EntityInfo(width = 8, height = 8, renderType = RenderType.OVERLAY) 
@MovementInfo(velocity = 0)
public class Shot extends Creature implements IUpdateable{
    private static Shot instance;
    private Integer targetX;
    private Integer targetY;

    /**
     * instance ensures that we only ever allow one shot to appear in the game,
     * generating it if it doesn't exist or returning the one that already exists 
     * @return Shot, newly or previously created projectile
     */
    public static Shot instance(Position shooterPosition, Position targetPosition) { 
        if (instance == null) { 
          instance = new Shot(shooterPosition, targetPosition); 
        } 
    
        return instance; 
      } 
    
    /**
     * shot constructor, setting the target position, adding it to the gui and setting its'
     * initial location
     * @param shooterPosition, current player position
     * @param targetPosition, where the shot will land
     */
    public Shot(Position shooterPosition, Position targetPosition) {
        super("projectile");
        this.targetX = targetPosition.getX();
        this.targetY = 480 - targetPosition.getY();
        Game.world().environment().add(this);
        this.setLocation(shooterPosition.getX(), 480 - shooterPosition.getY());
    }

    /**
     * firstly an if to remove the projectile from screen if it has reached its' desired position, 
     * otherwise it progressively moves towards its' target.
     * The GUI module returns position as a double so we convert the position into ints
     * first for comparison 
     */
    @Override
    public void update() {
        if ((int)(Math.round(this.getX())) == targetX &&
            (int)(Math.round(this.getY())) == targetY) {
                Game.world().environment().remove(this);
                instance = null;
            }
        Game.physics().move(this, targetX, targetY, 5);
    }
}

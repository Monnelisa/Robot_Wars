package za.co.wethinkcode.client.gui;

import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.EntityInfo;
import de.gurkenlabs.litiengine.entities.MovementInfo;


@EntityInfo(width = 18, height = 18) 
@MovementInfo(velocity = 70)
public class GUIEnemy extends Creature implements IUpdateable {
    private static GUIEnemy instance;
    /**
     * instance ensures that we only ever allow one enemy to appear in the game,
     * generating it if it doesn't exist or returning the one that already exists 
     * @return GUIEnemy, newly or previously created enemy
     */
    public static GUIEnemy instance() { 
        if (instance == null) { 
          instance = new GUIEnemy(); 
        } 
    
        return instance; 
      } 

      private GUIEnemy() { 
        super("enemy");
      }
      /**
       * We are using the default update cycle of the game for this entity
       */
      @Override
      public void update() {

      };
}

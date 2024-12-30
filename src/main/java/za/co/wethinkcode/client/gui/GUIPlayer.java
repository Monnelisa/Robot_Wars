package za.co.wethinkcode.client.gui;

import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.EntityInfo;
import de.gurkenlabs.litiengine.entities.MovementInfo;


@EntityInfo(width = 18, height = 18) 
@MovementInfo(velocity = 70)
public class GUIPlayer extends Creature implements IUpdateable {
    private static GUIPlayer instance;

    /**
     * instance ensures that we only ever allow one player to appear in the game,
     * generating it if it doesn't exist or returning the one that already exists 
     * @return GUIPlayer, newly or previously created player
     */
    public static GUIPlayer instance() { 
        if (instance == null) { 
          instance = new GUIPlayer(); 
        } 
    
        return instance; 
      } 

      private GUIPlayer() { 
        super("robot");
      }

      /**
       * We are using the default update cycle of the game for this entity
       */
      @Override
      public void update() {

      };
}

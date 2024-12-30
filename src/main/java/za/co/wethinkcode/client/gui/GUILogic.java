package za.co.wethinkcode.client.gui;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.Spawnpoint;
import de.gurkenlabs.litiengine.graphics.Camera;
import za.co.wethinkcode.client.robot.IRobot;
import za.co.wethinkcode.client.utilites.Position;

public class GUILogic {
    /**
     * called to set the initial state of our GUI
     */
    public static void init() {
        Camera camera = new Camera();
        camera.setFocus(240, 240);
        camera.setZoom(3f, 0);
        Game.world().setCamera(camera);
        Game.world().setGravity(0);
    }

    /**
     * spawns our newly launched robot onto the GUI after getting its' spawn position
     * 
     * @param robot, the robot that was launched in order to get its' details
     */
    public static void launch(IRobot robot) {
        Position startingPosition = robot.getPosition();
        Spawnpoint spawnpoint = new Spawnpoint(startingPosition.getX(), (480 - startingPosition.getY()));
        spawnpoint.spawn(GUIPlayer.instance());
    }

    /**
     * takes a position and sets the robots' location to that position after reversing
     * the y that the server responded with
     * 
     * @param position, the position we would like our robot to be at next
     */
    public static void makeMovement(Position position) {
        int moveX = position.getX();
        int moveY = (480 - position.getY());
        GUIPlayer.instance().setLocation(moveX, moveY);
    }

    /**
    * Spawns an enemy at the specified position on the GUI.
    *
    * @param enemyPosition the position where the enemy should be spawned
    */
    public static void showEnemy(Position enemyPosition) {
        if (enemyPosition != null) {
            Spawnpoint spawnPoint = new Spawnpoint(enemyPosition.getX(), (480 - enemyPosition.getY()));
            spawnPoint.spawn(GUIEnemy.instance());
        }
    }

    /**
     * creates a shot entity and provides it with the current player location and the 
     * desired target location
     * @param shooterPosition, current player position
     * @param targetPosition, where the shot will land
     */
    public static void fire(Position shooterPosition, Position targetPosition) {
        Shot.instance(shooterPosition, targetPosition);
    }
}

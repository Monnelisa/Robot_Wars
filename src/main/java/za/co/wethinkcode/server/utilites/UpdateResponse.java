package za.co.wethinkcode.server.utilites;

import java.util.Set;
import java.util.EnumSet;

public enum UpdateResponse {
    READY ("Ready"),
    SHUTDOWN("Shutting down..."),
    FIRE("Fired bullet"),
    LOOK("look"),
    RELOADING("Reloading"),
    RELOAD("Reloaded"),
    REPAIRING("Repairing"),
    REPAIRED("Repaired"),
    SUCCESS("Done"),
    FAILED("failed"),
    FAILED_COMMAND("Unsupported command"),
    FAILED_ARGS ("Could not parse arguments"),
    FAILED_OUTSIDE_WORLD ("You are at the edge"),
    FAILED_OBSTRUCTED ("Obstructed"),
    FAILED_NAME_TAKEN ("Too many of you in this world"),
    FAILED_WORLD_FILLED ("No more space in this world"),
    HIT("Hit confirmed"),
    GOTHIT("WAS HIT"),
    KILL("Robot killed"),
    MISS("Shot missed"),
    DEAD("The robot is dead and is no longer in the world."),
    PITDEAD("You have fallen into a tumbolia and will never be seen again!"),
    NORMAL("The robot is waiting for the next command."),
    HELP (
            """
                    commands:
                    OFF     - Shut down robot
                    HELP    - provide information about commands
                    FORWARD - move forward by specified number of steps, e.g. 'FORWARD 10'
                    BACK    - move backwards by specified number of steps, e.g. 'BACK 10'
                    LEFT    - turn left
                    RIGHT   - turn right
                    FIRE    - shoot your shot
                    LOOK    - Look around your robot
                    STATE   - display what state your robot is in
                    REPAIR  - repair your robot
                    RELOAD  - reload your shots
                    """
    );
    public static final Set<UpdateResponse> FAILURE_STATUSES = EnumSet.of(
            UpdateResponse.FAILED,
            UpdateResponse.FAILED_COMMAND,
            UpdateResponse.FAILED_ARGS,
            UpdateResponse.FAILED_OUTSIDE_WORLD,
            UpdateResponse.FAILED_OBSTRUCTED,
            UpdateResponse.FAILED_NAME_TAKEN,
            UpdateResponse.FAILED_WORLD_FILLED,
            UpdateResponse.DEAD
    );

    private String message;

    UpdateResponse(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }
}

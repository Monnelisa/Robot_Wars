package za.co.wethinkcode.server.robotcommands;

import org.json.JSONObject;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import za.co.wethinkcode.server.Robot;
import za.co.wethinkcode.server.utilites.UpdateResponse;
import static za.co.wethinkcode.server.utilites.Colours.*;

/**
 * Command to initiate reloading of a robot's ammunition asynchronously.
 */
public class ReloadCommand extends Command {

    // Constants for conversion and logger initialization
    private static final int RELOAD_DELAY_MULTIPLIER = 1000;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    /**
     * Constructs a ReloadCommand with the command name "reload".
     */
    public ReloadCommand() {
        super("reload");
    }

    /**
     * Executes the reload command on the specified robot.
     *
     * @param target The robot to reload.
     * @return A JSONObject containing the result of the reload operation.
     */
    @Override
    public JSONObject execute(Robot target) {
        JSONObject data = new JSONObject();

        // Synchronize on the target to ensure thread safety
        synchronized (target) {
            // Check if the robot's ammo is below the maximum capacity
            if (target.getShots() < target.getMAX_SHOTS()) {
                // Set the robot's status to reloading
                target.setStatus(UpdateResponse.RELOADING);
                try {
                    // Schedule the reload task to execute after the reload time
                    scheduler.schedule(() -> {
                        resetShots(target);
                        target.setStatus(UpdateResponse.NORMAL);
                    }, target.getReloadTime() * RELOAD_DELAY_MULTIPLIER, TimeUnit.MILLISECONDS);

                    // Inform that reloading is in progress
                    data.put("message", "Reloading in progress.");
                } catch (Exception e) {
                    // Log and handle any scheduling errors
                    printRED("Failed to schedule reload task");
                    target.setStatus(UpdateResponse.FAILED);
                    data.put("message", "Failed to start reloading.");
                }
            } else {
                // If the robot already has full ammo, update the status accordingly
                target.setStatus(UpdateResponse.NORMAL);
                data.put("message", "Already at full ammo.");
            }
        }
        return data;
    }

    /**
     * Resets the robot's shots to the maximum value.
     *
     * @param target The robot whose shots are to be reset.
     */
    private void resetShots(Robot target) {
        synchronized (target) {
            target.resetShots();
            target.setStatus(UpdateResponse.NORMAL);
            // Use logging instead of print statements
            printGREEN("Shots have been reset to maximum for robot: " + target.getName());
        }
    }

    /**
     * Shuts down the scheduler used for reloading gracefully.
     * This method should be called when the ReloadCommand is no longer needed.
     */
    public void shutdownScheduler() {
        scheduler.shutdown();
        try {
            // Await termination of the scheduler with a timeout
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                // Force shutdown if it doesn't terminate in time
                scheduler.shutdownNow();
                if (!scheduler.awaitTermination(60, TimeUnit.SECONDS))
                    printRED("Failed to schedule reload task");
            }
        } catch (InterruptedException e) {
            // Handle interrupted exceptions and force shutdown
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}

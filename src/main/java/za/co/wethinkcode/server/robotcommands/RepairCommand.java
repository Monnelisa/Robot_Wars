package za.co.wethinkcode.server.robotcommands;

import org.json.JSONObject;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import za.co.wethinkcode.server.Robot;
import za.co.wethinkcode.server.utilites.UpdateResponse;
import static za.co.wethinkcode.server.utilites.Colours.*;

/**
 * Represents a command to repair the shields of a robot asynchronously.
 */
public class RepairCommand extends Command {

    private static final int REPAIR_DELAY_MULTIPLIER = 1000;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    /**
     * Constructs a RepairCommand object.
     */
    public RepairCommand() {
        super("repair");
    }

    /**
     * Executes the repair command on the specified robot.
     *
     * @param target The robot to repair.
     * @return A JSONObject containing the result of the repair operation.
     */
    @Override
    public JSONObject execute(Robot target) {

        JSONObject data = new JSONObject();

        synchronized (target) {
            if (target.getShields() < target.getMAX_SHIELDS()) {
                target.setStatus(UpdateResponse.REPAIRING);
                try {
                    // Schedule a task to reset shields after a delay
                    scheduler.schedule(() -> {
                        resetShields(target);
                        target.setStatus(UpdateResponse.REPAIRED);
                    }, target.getRepairTime() * REPAIR_DELAY_MULTIPLIER, TimeUnit.MILLISECONDS);

                    data.put("message", UpdateResponse.REPAIRING.getMessage());
                } catch (Exception e) {
                    printRED("Failed to schedule repair task");
                    target.setStatus(UpdateResponse.FAILED);
                    data.put("message", "Failed to start repair.");
                }
            } else {
                target.setStatus(UpdateResponse.NORMAL);
                data.put("message", "Already at full shields.");
            }
        }
        return data;
    }

    /**
     * Resets the shields of the specified robot to maximum.
     *
     * @param target The robot to reset shields for.
     */
    private void resetShields(Robot target) {
        synchronized (target) {
            target.setShields(target.getMAX_SHIELDS());
            target.setStatus(UpdateResponse.NORMAL);
            printGREEN("Shields have been reset to maximum for robot: " + target.getName() + "\n 'Status Changed' ");
        }
    }

    /**
     * Shuts down the scheduler used for scheduling repair tasks.
     */
    public void shutdownScheduler() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
                if (!scheduler.awaitTermination(60, TimeUnit.SECONDS))
                    printRED("Scheduler did not terminate");
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}

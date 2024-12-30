package za.co.wethinkcode.server.servercommnds;

import static za.co.wethinkcode.server.utilites.Colours.*;

/**
 * Abstract base class for commands executed by the server.
 */
public abstract class Command {

    private final String name;

    /**
     * Executes the command.
     *
     * @return true if the command executes successfully, false otherwise.
     */
    public abstract boolean execute();

    /**
     * Constructs a new Command with the specified name.
     *
     * @param name the name of the command
     */
    public Command(String name){
        this.name = name.trim().toLowerCase();
    }

    /**
     * Creates a new Command instance based on the instruction.
     *
     * @param instruction the instruction string containing the command
     * @return a Command instance corresponding to the instruction
     * @throws IllegalArgumentException if the command is unsupported
     */
    public static Command create(String instruction) {
        String[] args = instruction.toLowerCase().trim().split(" ");
        return switch (args[0]) {
            case "quit" -> new QuitCommand();
            case "robots" -> new RobotsCommand();
            case "dump" -> new DumpCommand();
            default -> throw new IllegalArgumentException(RED+"Unsupported command: " + instruction+RESET);
        };
    }

    public String getName() {
           return this.name;
    }
}


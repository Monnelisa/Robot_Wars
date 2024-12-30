package za.co.wethinkcode.server.robotcommands;

 import org.json.JSONObject;

 import za.co.wethinkcode.server.Robot;

 /**
 * Abstract base class for robot commands.
 */
public abstract class Command {
    private final String name;
    private String[] arg;

    /**
      * Executes the command on the target robot.
      *
      * @param target the robot on which the command is executed
      * @return a JSON object representing the result of the command
      */
    public abstract JSONObject execute(Robot target);

     /**
      * Constructs a new Command with the specified name.
      *
      * @param name the name of the command
      */
    public Command(String name){
        this.name = name.trim().toLowerCase();
    }

     /**
      * Constructs a new Command with the specified name and arguments.
      *
      * @param name the name of the command
      * @param argument the arguments for the command
      */
     public Command(String name, String[] argument) {
        this(name);
        this.arg = argument;
    }

     /**
      * Gets the name of the command.
      *
      * @return the name of the command
      */
    public String getName() {                                                                           //<2>
        return name;
    }

     /**
      * Gets the arguments for the command.
      *
      * @return the arguments for the command
      */
    public String[] getArgument() {
        return this.arg;
    }

     /**
      * Creates a new Command instance based on the instruction and arguments.
      *
      * @param instruction the instruction string representing the command
      * @param args the arguments for the command
      * @return a Command instance corresponding to the instruction
      */
    public static Command create(String instruction,String[] args) {
        return switch (instruction) {
            case "launch" -> new LaunchCommand(args);
            case "off", "quit" -> new ShutdownCommand();
            case "help" -> new HelpCommand();
            case "forward" -> new ForwardCommand(args);
            case "back" -> new BackCommand(args);
            case "turn" -> new TurnCommand(args);
            case "look" -> new LookCommand();
            case "fire" -> new FireCommand();
            case "reload" -> new ReloadCommand();
            case "repair" -> new RepairCommand();
            case "state" -> new StateCommand();
            default -> new ErrorCommand();
        };
    }
}


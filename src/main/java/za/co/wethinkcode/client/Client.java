package za.co.wethinkcode.client;

import za.co.wethinkcode.client.gui.GUILogic;
import java.io.*;
import java.net.*;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import de.vandermeer.asciitable.AsciiTable;
import de.gurkenlabs.litiengine.Game;
import za.co.wethinkcode.client.robot.*;
import za.co.wethinkcode.client.utilites.Position;
import za.co.wethinkcode.client.utilites.Direction;
import static za.co.wethinkcode.client.utilites.Colours.*;

/**
 * Represents a client that connects to a server and interacts using commands.
 * Uses a robot interface to execute commands and manage state.
 */
public class Client {
    private String address;
    private int port;
    private IRobot robot;
    private Scanner terminalIn = new Scanner(System.in);
    private String strResponse;
    private boolean useGui = false;
    private boolean launched = false;
    private boolean badName = false;


    /**
     * Constructs a new Client with the specified address, port, robot instance, and GUI usage flag.
     *
     * @param address The IP address or hostname of the server.
     * @param port The port number to connect to on the server.
     * @param robot The robot instance that will execute commands.
     * @param useGui Flag indicating whether to use GUI for interaction.
     */
    public Client(String address, int port, IRobot robot, Boolean useGui) {
        this.address = address;
        this.port = port;
        this.robot = robot;
        this.useGui = useGui;
    }
    
    /**
     * Attempts to connect to the server, executes commands, and handles server responses.
     * Uses a socket connection and streams for communication.
     */
    public void execute() {
        // Display a message indicating attempt to connect to the specified address and port
        printBLUE(String.format("Attempting to connect to %s on port %d...", address, port));
        
        try (
            // Attempt to create necessary resources within try-with-resources
            Socket socket = new Socket(address, port);
            PrintStream out = new PrintStream(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            // Connection successful, notify user
            printGREEN("Connection Successful!");
            printOutput("At your service!");
            
            // Begin interaction with the server
            forceLaunch(out, in);
            String input = "";
            
            // Continue interacting until "quit" command is received or program is terminated
            while (!input.contains("quit") && launched) {
                
                // Prompt user for input
                printOutput("What would you like me to do next?");
                input = terminalIn.nextLine().trim().toLowerCase();
                
                if (input.contains("quit")) continue; // Skip processing if input is "quit"
                
                // Process user command
                if (robot.setCommandAndArgs(input)) {
                    // Send command to server
                    out.println(robot.generateRequest());
                    out.flush();
                    
                    // Receive and process server response
                    strResponse = in.readLine();
                    
                    if (strResponse != null) {
                        // Parse response as JSON
                        JSONObject response = new JSONObject(strResponse);
                        JSONObject state = response.getJSONObject("state");
                        JSONObject data = response.getJSONObject("data");
                        String status = state.getString("status");
                        
                        // Handle specific server statuses
                        if (status.equalsIgnoreCase("dead")||
                            status.equalsIgnoreCase("pitdead")) {
                            printOutput(data.getString("message"));
                            break; // Break loop if robot is dead
                        }
                        
                        // Handle general response
                        printOutput(handleResponse(response));
                    } else {
                        // Server closed the connection
                        printOutput("Server closed the connection.");
                        break; // Break loop if server closes connection
                    }
                } else {
                    // Unsupported command
                    printOutput(String.format("Unsupported Command - %s", input));
                }
            }
            
            // Notify end of interaction
            printOutput("Bye!");
            
            // Send a quit command to the server
            JSONObject quitResponse = new JSONObject();
            quitResponse.put("robot", robot.getName());
            quitResponse.put("command", "quit");
            quitResponse.put("arguments", new JSONArray());
            out.println(quitResponse);
            
            // Close resources
            out.close();
            in.close();
            socket.close();
            terminalIn.close(); // Assuming terminalIn is a Scanner or similar
            Game.exit(); // Exit the game
            
        } catch (IOException e) {
            // Handle IOException (e.g., server offline)
            System.out.println("Server is offline.");
        }
    }

    /**
    * Forces the launch process by interacting with the server.
    * Prompts the user to confirm readiness and handles initial launch requests.
    *
    * @param out PrintStream connected to the server's output stream.
    * @param in BufferedReader connected to the server's input stream.
    */
    private void forceLaunch(PrintStream out, BufferedReader in) {
        String input = "";
        boolean looped = false;
        boolean shouldQuit = false;

        while (!input.contains("yes") || !input.contains("y")) {
            if (input.contains("quit")) {
                shouldQuit = true;
                break;
            }
            if (badName) {
                printOutput("Please enter a different name.");
                robot.setName(terminalIn.nextLine().trim());
            }
            badName = false;
            if (looped)
                printOutput("We will need to launch before any"
                        + " other action can be taken.");
            looped = true;
            printOutput("Are you ready to launch?");
            input = terminalIn.nextLine().trim().toLowerCase();
        }
        if (!shouldQuit) {
            try {
                if (robot.setCommandAndArgs("launch")) {
                    out.println(robot.generateRequest());
                    out.flush();
                    strResponse = in.readLine();
                    JSONObject response = new JSONObject(strResponse);
                    printOutput(handleResponse(response));
                    if (badName)
                        forceLaunch(out, in);
                    launched = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
    * Handles the server response and updates the robot's state based on the received data.
    *
    * @param response JSONObject containing the server response data.
    * @return A message string representing the server action taken.
    */
    private String handleResponse(JSONObject response) {
        String message;
        JSONObject data = response.getJSONObject("data");
        JSONObject state = response.getJSONObject("state");
        boolean useState = true;
        switch (robot.getCommand()) {
            case "launch":
                message = data.getString("message");
                if ("OK".equals(response.getString("result"))) {
                    handleLaunchData(data);
                } else {
                    if ("Too many of you in this world".equals(message)) {
                        badName = true;
                        useState = false;
                    }
                }
                break;
            case "state":
                message = state.getString("status") + "\n" + printASCIIState(state);
                break;
            case "look":
                message = handleLookData(data);
                break;
            default:
                message = data.getString("message");
                break;
        }

        if (useState) {
            robot.setPosition(new Position(state.getJSONArray("position").getInt(0),
                    state.getJSONArray("position").getInt(1)));
            robot.setDirection(state.getEnum(Direction.class, "direction"));
            robot.setShields(state.getInt("shields"));
            robot.setShots(state.getInt("shots"));
            robot.setStatus(state.getString("status"));
        }

        if (useGui && useState) {
            switch (robot.getCommand()) {
                case "launch":
                    GUILogic.launch(robot);
                    break;
                case "forward":
                    GUILogic.makeMovement(robot.getPosition());
                    break;
                case "back":
                    GUILogic.makeMovement(robot.getPosition());
                    break;
                case "fire":
                    if ("Hit confirmed".equals(message)) {
                        JSONArray targetPosition = data.getJSONObject("state").getJSONArray("position");
                        Integer targetX = targetPosition.getInt(0);
                        Integer targetY = targetPosition.getInt(1);
                        Integer distance = data.getInt("distance");
                        handleGUIFire(targetX, targetY, distance);
                    } else if ("Shot missed".equals(message)) {
                        handleGUIFire(-1, -1, robot.getFiringRange());
                    }
                default:
                    break;
            }
        }

        return message;
    }

    /**
    *Prints a formatted message using the robot's name and current position (if available).
    *
    * @param message The message to print.
    */
    private void printOutput(String message) {
        String output;
        if (robot.getPosition() == null) {
            output = String.format("%s: %s", robot.getName(), message);
        } else {
            output = String.format("%s: %s - %s", robot.getName(),
                    robot.getPosition().toString(),
                    message);
        }
        printBLUE(output);
    }

    /**
    * Handles the data received during the 'launch' command and updates the robot's attributes.
    *
    * @param data JSONObject containing launch-related data.
    */
    private void handleLaunchData(JSONObject data) {
        robot.setVisiblity(data.getInt("visibility"));
        robot.setReloadTime(data.getInt("reload"));
        robot.setRepairTime(data.getInt("repair"));
        robot.setMaxShields(data.getInt("shields"));
        robot.setFiringRange(data.getInt("fireDistance"));
    }

    /**
    * Handles the data received during the 'look' command and formats the output message.
    *
    * @param data JSONObject containing look-related data.
    * @return A formatted string describing objects in the robot's vicinity.
    */
    private String handleLookData(JSONObject data) {
        String returnString = "";
        JSONArray objects = data.getJSONArray("objects");
        if (objects.length() > 0) {
            for (int i = 0; i < objects.length(); i++) {
                if (i > 0) {
                    returnString += "\n";
                }
                JSONObject obstacle = objects.getJSONObject(i);
                String direction = obstacle.getString("direction").toLowerCase();
                String type = obstacle.getString("type").toLowerCase();
                if ("robot".equals(type) && useGui)
                    spawnGUIRobot(obstacle);
                Integer steps = obstacle.getInt("distance");
                if (steps == 1) {
                    returnString += String.format("There is a %s %d step away from you to the %s",
                            type, steps, direction);
                } else {
                    returnString += String.format("There is a %s %d steps away from you to the %s",
                            type, steps, direction);
                }
            }
            return returnString;
        } else {
            return "No object found in the vicinity";
        }
    }

    /**
    * Prints the robot's current state in an ASCII table format.
    *
    * @param state JSONObject containing state-related data.
    * @return A formatted ASCII table representing the robot's state.
    */
    private String printASCIIState(JSONObject state) {
        AsciiTable table = new AsciiTable();
        table.getContext().setWidth(30);
        table.addRule();
        table.addRow("Direction: ", state.getString("direction"));
        table.addRule();
        table.addRow("Shields: ", state.getInt("shields"));
        table.addRule();
        table.addRow("Shots: ", state.getInt("shots"));
        table.addRule();
        table.addRow("status: ", state.getString("status"));
        table.addRule();
        return table.render();
    }

    /**
    * Spawns a GUI representation of an enemy robot based on received data.
    *
    * @param robotInfo JSONObject containing information about the enemy robot.
    */
    private void spawnGUIRobot(JSONObject robotInfo) {
        Direction currentPlayerDirection = robot.getDirection();
        Integer distance = robotInfo.getInt("distance");
        Position playerPosition = robot.getPosition();
        Position enemyPosition = getNewPosition(currentPlayerDirection, playerPosition.getX(), playerPosition.getY(),
                distance);

        GUILogic.showEnemy(enemyPosition);
    }

    /**
    * Handles GUI logic for firing actions, including visual representation of firing events.
    *
    * @param targetX X-coordinate of the target position (or -1 if not applicable).
    * @param targetY Y-coordinate of the target position (or -1 if not applicable).
    * @param distance Distance of the shot fired.
    */
    private void handleGUIFire(Integer targetX, Integer targetY, Integer distance) {
        Position shooterPosition = robot.getPosition();
        Position targetPosition;
        if (targetX > -1 && targetY > -1) {
            targetPosition = new Position(targetX, targetY);
        } else {
            targetPosition = getNewPosition(robot.getDirection(), shooterPosition.getX(), shooterPosition.getY(),
                    distance);
        }
        GUILogic.fire(shooterPosition, targetPosition);
    }

    /**
    * Calculates and returns a new position based on the current direction and distance.
    *
    * @param direction Direction of movement.
    * @param initialX Initial X-coordinate.
    * @param initialY Initial Y-coordinate.
    * @param distance Distance to move.
    * @return A new Position object representing the updated position.
    */
    private Position getNewPosition(Direction direction, Integer initalX, Integer initalY, Integer distance) {
        switch (direction) {
            case NORTH:
                return new Position(initalX, initalY + distance);
            case EAST:
                return new Position(initalX + distance, initalY);
            case SOUTH:
                return new Position(initalX, initalY - distance);
            case WEST:
                return new Position(initalX - distance, initalY);
            default:
                return new Position(0, 0);
        }
    }

}
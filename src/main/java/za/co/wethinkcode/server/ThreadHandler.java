package za.co.wethinkcode.server;

import java.io.*;
import java.net.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import za.co.wethinkcode.server.world.World;
import za.co.wethinkcode.server.utilites.UpdateResponse;
import static za.co.wethinkcode.server.utilites.Colours.*;
import za.co.wethinkcode.server.robotcommands.Command;

/**
 * Handles client connections and processes their requests in a separate thread.
 * Each client is associated with a specific robot in the world.
 */
public class ThreadHandler implements Runnable{

    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private final Robot robot;

    /**
     * Constructs a new ThreadHandler.
     *
     * @param socket the client socket
     * @param world the world in which the robot operates
     * @throws IOException if an I/O error occurs
     */
    public ThreadHandler(Socket socket, World world)throws IOException {
        this.socket = socket;
        int portSocket = socket.getPort();
        this.robot = new Robot("BOT:"+portSocket, world);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    /**
     * Runs the thread, handling client requests.
     */
    @Override
    public void run() {
        try {
            handleClient();
        } catch (IOException e) {
           printRED("Client Connection was Interrupted!! -- " + robot.getName());
        } finally {
            cleanUp();
            RobotManager.removeRobot(robot.getName());
        }
    }

    /**
     * Handles client requests in a loop until the client disconnects or an error occurs.
     *
     * @throws IOException if an I/O error occurs
     */
    private void handleClient() throws IOException {
        String request;
        boolean shouldContinue = true;

        while (shouldContinue) {
            request = in.readLine();

            if (request == null) {
                break;
            }

            JSONObject responseObject;
            try {
                responseObject = new JSONObject(request);
                printPURPLE("Received from " + robot.getName() + ":\n" +
                        "  Command: " + responseObject.get("command") + "\n" +
                        "  Arguments: " + responseObject.get("arguments"));
            } catch (JSONException e) {
                printRED("Invalid JSON received: " + request);
                shouldContinue = false;
                continue;
            }

            JSONObject response = processRequest(robot, responseObject);
            printCYAN("Response to " + robot.getName() + ":\n" +
                    "  Result: " + response.get("result")+"\n"+
                    "  Status: "+ robot.getStatus());

            // Send response back to client
            out.println(response);
            out.flush();

            if (robot.getStatus() == UpdateResponse.SHUTDOWN||
                robot.getStatus() == UpdateResponse.DEAD||
                robot.getStatus() == UpdateResponse.PITDEAD) {
                shouldContinue = false;
            }
        }
        printRED("Client disconnected: " + robot.getName());
        RobotManager.removeRobot(robot.getName());
        cleanUp();
    }

    /**
     * Processes the client's request and generates a response.
     *
     * @param target the robot targeted by the request
     * @param request the client's request as a JSON object
     * @return the response as a JSON object
     */
    private JSONObject processRequest(Robot target, JSONObject request) {
        JSONObject response = new JSONObject();
        if (target.getStatus() == UpdateResponse.DEAD || target.getStatus() == UpdateResponse.PITDEAD) return createDeadResponse(target);

        try {
            String robotName = request.getString("robot");
            String userCommand = request.getString("command");
            JSONArray userArguments = request.getJSONArray("arguments");
            String[] arguments = new String[userArguments.length()];

            for (int i = 0; i < userArguments.length(); i++) {
                arguments[i] = userArguments.getString(i);
            }

            target.setName(robotName);

            if (isRobotBusy(target)
                    &&!userCommand.equalsIgnoreCase("look")
                    &&!userCommand.equalsIgnoreCase("state")) {
                return createBusyResponse(target);
            }

            Command command = Command.create(userCommand, arguments);
            JSONObject data = target.handleCommand(command);

            if (isCommandSuccessful(target)) {
                response.put("result", "OK");
            } else {
                response.put("result", "ERROR");
                target.setStatus(UpdateResponse.NORMAL);
            }


            response.put("data", data);
            response.put("state", CreateStateResponse(target));

        } catch (JSONException e) {
            printRED("JSON parsing error: " + e.getMessage());
            response.put("result", "ERROR");
            response.put("data", new JSONObject().put("message", target.getStatus().getMessage()));
        }
        return response;
    }

    /**
     * Creates a response indicating the robot is busy.
     *
     * @param target the robot
     * @return the response as a JSON object
     */
    private JSONObject createBusyResponse(Robot target) {
        JSONObject data = new JSONObject();
        data.put("message", "Robot is currently busy and cannot perform the task!");
        JSONObject response = new JSONObject();
        response.put("result", "ERROR");
        response.put("data", data);
        response.put("state", CreateStateResponse(target));
        return response;
    }

    /**
     * Creates a response indicating the robot is dead.
     *
     * @param target the robot
     * @return the response as a JSON object
     */
    private JSONObject createDeadResponse(Robot target) {
        JSONObject data = new JSONObject();
        if (target.getStatus() == UpdateResponse.DEAD) {
            data.put("message", "Robot is dead!");
        } else if (target.getStatus() == UpdateResponse.PITDEAD) {
            target.setStatus(UpdateResponse.DEAD);
            data.put("message", UpdateResponse.PITDEAD.getMessage());
        }
        JSONObject response = new JSONObject();
        response.put("result", "ERROR");
        response.put("data", data);
        response.put("state", CreateStateResponse(target));
        return response;
    }

    /**
     * Creates a JSON object representing the current state of the robot.
     *
     * @param target the robot
     * @return the current state as a JSON object
     */
    private JSONObject CreateStateResponse(Robot target){
        return target.getCurrentState();
    }

    /**
     * Checks if the robot is busy.
     *
     * @param robot the robot
     * @return true if the robot is busy, false otherwise
     */
    private boolean isRobotBusy(Robot robot) {
        UpdateResponse status = robot.getStatus();
        return status == UpdateResponse.REPAIRING || status == UpdateResponse.RELOADING;
    }

    /**
     * Checks if the command was successful.
     *
     * @param robot the robot
     * @return true if the command was successful, false otherwise
     */
    private boolean isCommandSuccessful(Robot robot) {
        return !UpdateResponse.FAILURE_STATUSES.contains(robot.getStatus());
    }

    /**
     * Cleans up resources by closing the socket, input, and output streams.
     */
    private void cleanUp() {
        try {
            synchronized (Server.clientSockets) {
                Server.clientSockets.remove(socket);
            }
            if (socket != null) {
                socket.close();
            }
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            printRED("Error closing resources: " + e.getMessage());
        }
    }
}


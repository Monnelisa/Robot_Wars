package za.co.wethinkcode.server;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.net.Socket;
import java.net.ServerSocket;

import java.io.PrintWriter;
import java.io.IOException;

import za.co.wethinkcode.server.utilites.Position;
import za.co.wethinkcode.server.world.World;
import static za.co.wethinkcode.server.utilites.Colours.*;

/**
 * Server class to handle multiple client connections.
 * It listens on a specified port and spawns a new thread for each client connection.
 */
public class Server {

    private ServerSocket serverSocket = null;
    public static final List<Socket> clientSockets = new ArrayList<>();
    private static boolean isClosed = false;
    public static String FILE_PATH = "ServerConfig.json";
    public static World world;

    /**
     * Constructs a new Server and starts listening on the specified port.
     *
     * @param port the port number on which the server will listen for connections
     */
    public Server(int port) {
        startWorld();
        printCommands();
        // Server socket setup
        try{
            serverSocket = new ServerSocket(port);

            printGREEN("Server started.");
            printGREEN("IP Address: " + findIP());
            printGREEN("Listening on port " + port);

            while (!isClosed) {
                if (clientSockets.size() !=4)
                    printBLUE("Waiting for clients ...");

                // Accept a new client connection
                Socket clientSocket = serverSocket.accept();
                printGREEN("Client connected: " + clientSocket.getInetAddress());

                //adding client sockets to list
                clientSockets.add(clientSocket);

                // Create a new thread to handle the client connection
                ThreadHandler clientHandler = new ThreadHandler(clientSocket, world);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            printRED(String.valueOf(e));
        } finally {
            stop();
        }
        }

    /**
     * Stops the server and disconnects all clients.
     */
    public void stop() {
        try {
            // Send termination signal to all clients
            for (Socket clientSocket : clientSockets) {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println("Server is shutting down. Goodbye!");
                clientSocket.close();
            }

            // Stop accepting new client connections
            serverSocket.close();
            clientSockets.clear();
            printRED("Server stopped.");
        } catch (IOException e) {
            printRED(String.valueOf(e));
        }
    }

    /**
     * Sets the server's closed status.
     *
     * @param closed the new closed status
     */
    public static void setIsClosed(boolean closed){isClosed = closed;}

    private static void startWorld() {
        JSONObject config = ServerManager.readFiles();
        if (config != null) {
            JSONArray worldLimits = config.getJSONArray("world");
            Boolean usingGui = config.getBoolean("gui");
            Position worldPos = new Position(worldLimits.getInt(0), worldLimits.getInt(1));
            int reloadTime = (int) config.get("reload");
            int repairTime = (int) config.get("repair");
            int visibility = (int) config.get("visibility");
            int fireRange = (int) config.get("fireRange");
            world = new World(worldPos, usingGui,reloadTime,repairTime,visibility,fireRange);
        }
    }

    private void printCommands(){
        String commands = """
                
                Server Commands:
                Robots  - Shows all robots in world
                Dump    - Shows all info about world
                Quit    - Kills server and client connections
                """;
        printGREEN(commands);
    }


    /**
    * Finds and returns the non-loopback IPv4 address of the current machine.
    * Returns "127.0.1.1" if no suitable address is found or an exception occurs.
    *
    * @return the IPv4 address of the current machine, or "127.0.1.1" on failure.
    */
    private String findIP() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (isIPv4) 
                            return sAddr;
                    }
                }
            }
            return "127.0.1.1";
        } catch (Exception ignored) { }
        return "127.0.1.1";
    }
}


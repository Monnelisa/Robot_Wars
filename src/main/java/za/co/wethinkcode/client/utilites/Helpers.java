package za.co.wethinkcode.client.utilites;

import java.util.Set;

import static java.lang.Integer.parseInt;

public class Helpers {

    /**
     * Checks if the given command is a valid command.
     *
     * @param cmd the command to check
     * @return true if the command is valid, false otherwise
     */
    public static boolean isValidCommand(String cmd) {
        Set<String> validCommands = Set.of(
                "forward",
                "back",
                "turn",
                "look",
                "fire",
                "reload",
                "repair",
                "state",
                "launch",
                "help"
        );
        return validCommands.contains(cmd.toLowerCase());
    }

    /**
     * Checks if the given command is an action command.
     *
     * @param cmd the command to check
     * @return true if the command is an action command, false otherwise
     */
    public static boolean isActionCommand(String cmd) {
        Set<String> validCommands = Set.of(
                "forward",
                "back"
        );
        return validCommands.contains(cmd.toLowerCase());
    }

    /**
     * Checks if the given argument is an integer.
     *
     * @param arg the argument to check
     * @return true if the argument is an integer, false otherwise
     */
    public static boolean isInt(String arg) {
        try {
            @SuppressWarnings("unused")
            int test = parseInt(arg);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the given string is a valid IP address.
     *
     * @param ip the IP address to check
     * @return true if the IP address is valid, false otherwise
     */
    public static boolean isValidIP(String ip) {
        String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";

        if ("localhost".equals(ip)) return true;

        return ip.matches(PATTERN);
    }
}

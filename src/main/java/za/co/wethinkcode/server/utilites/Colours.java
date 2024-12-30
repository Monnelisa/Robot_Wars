package za.co.wethinkcode.server.utilites;

import java.util.Random;

public class Colours {
    // ANSI escape codes for basic text colors
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    public static final String DARK_GREY ="\u001B[90m";
    public static final String LIGHT_RED ="\u001B[91m";
    public static final String LIGHT_GREEN ="\u001B[92m";
    public static final String LIGHT_BLUE ="\u001B[94m";
    public static final String LIGHT_PURPLE ="\u001B[95m";
    public static final String LIGHT_CYAN ="\u001B[96m";
    public static final String LIGHT_GREY ="\u001B[97m";

    // ANSI escape codes for basic background colors
    public static final String BLACK_BACKGROUND = "\u001B[40m";
    public static final String RED_BACKGROUND = "\u001B[41m";
    public static final String GREEN_BACKGROUND = "\u001B[42m";
    public static final String YELLOW_BACKGROUND = "\u001B[43m";
    public static final String BLUE_BACKGROUND = "\u001B[44m";
    public static final String PURPLE_BACKGROUND = "\u001B[45m";
    public static final String CYAN_BACKGROUND = "\u001B[46m";
    public static final String WHITE_BACKGROUND = "\u001B[47m";

    // ANSI escape code to reset to default color
    public static final String RESET = "\u001B[0m";

    public static void printRED(String msg){
        System.out.println(RED+msg+RESET);
    }
    public static void printGREEN(String msg){
        System.out.println(GREEN+msg+RESET);
    }
    public static void printBLUE(String msg){
        System.out.println(BLUE+msg+RESET);
    }
    public static void printYELLOW(String msg){
        System.out.println(YELLOW+msg+RESET);
    }
    public static void printCYAN(String msg){
        System.out.println(CYAN+msg+RESET);
    }
    public static void printPURPLE(String msg){
        System.out.println(PURPLE+msg+RESET);
    }
    public static void printLightPurple(String msg){
        System.out.println(LIGHT_PURPLE+msg+RESET);
    }

    private static final String[] COLORS = {
            BLACK, RED, GREEN, YELLOW, BLUE, PURPLE, CYAN, WHITE,
            DARK_GREY, LIGHT_RED, LIGHT_GREEN, LIGHT_BLUE, LIGHT_PURPLE, LIGHT_CYAN, LIGHT_GREY
    };

    // Method to choose a random color
    public static String chooseRandomColor() {
        Random random = new Random();
        int index = random.nextInt(COLORS.length);
        return COLORS[index];
    }

    public static void print(String msg){
        System.out.println(msg);
    }
}




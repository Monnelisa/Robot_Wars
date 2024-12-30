package za.co.wethinkcode.client.utilites;

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
    public static void printCYAN_BACKGROUND(String msg){
        System.out.println(BLACK+CYAN_BACKGROUND+msg+RESET);
    }

    // Method to generate ANSI escape code for 256-color text
    public static String color256(int colorCode) {
        return "\u001B[38;5;" + colorCode + "m";
    }

    // Method to generate ANSI escape code for 256-color background
    public static String background256(int colorCode) {
        return "\u001B[48;5;" + colorCode + "m";
    }

}




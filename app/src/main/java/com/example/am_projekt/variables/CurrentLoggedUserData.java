package com.example.am_projekt.variables;

public class CurrentLoggedUserData {
    private static String currentLoggedUsername = "NOT_LOGGED";
    private static String currentServerIP = "192.168.0.101";

    public static String getCurrentLoggedUsername() {
        return currentLoggedUsername;
    }

    public static void setCurrentLoggedUsername(String currentLoggedUsername) {
        CurrentLoggedUserData.currentLoggedUsername = currentLoggedUsername;
    }

    public static String getCurrentServerIP() {
        return currentServerIP;
    }

    public static void setCurrentServerIP(String currentServerIP) {
        CurrentLoggedUserData.currentServerIP = currentServerIP;
    }
}

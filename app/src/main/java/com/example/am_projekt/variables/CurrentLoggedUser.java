package com.example.am_projekt.variables;

public class CurrentLoggedUser {
    private static String currentLoggedUsername = "NOT_LOGGED";

    public static String getCurrentLoggedUsername() {
        return currentLoggedUsername;
    }

    public static void setCurrentLoggedUsername(String currentLoggedUsername) {
        CurrentLoggedUser.currentLoggedUsername = currentLoggedUsername;
    }
}

package de.swt.bibliothek.util;

public class Validation {

    public static boolean isValidBenutzerId(String userId) {
        if (userId == null || userId.isEmpty()) return false;
        try {
            Integer.parseInt(userId);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean isValidPassword(String password) {
        return password != null && !password.isEmpty();
    }
}

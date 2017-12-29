package de.swt.bibliothek.util;

public class Validation {

    public static boolean isValidId(String id) {
        if (id == null || id.isEmpty()) return false;
        try {
            Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean isValidPassword(String password) {
        return password != null && !password.isEmpty();
    }
}

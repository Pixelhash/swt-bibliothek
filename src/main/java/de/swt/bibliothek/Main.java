package de.swt.bibliothek;

import org.mindrot.jbcrypt.BCrypt;

import java.io.Console;
import java.sql.SQLException;

/**
 * Application's Main class
 */
public class Main {

    /**
     * Main method.
     *
     * @param args program arguments (just '-p' is used here).
     * @throws SQLException thrown, if connection to MySQL database isn't successful.
     */
    public static void main(String[] args) throws SQLException {
        /*
            Password hash generation for admin user to insert users directly into the database
         */
        if (args.length == 1 && args[0].equals("-p")) {
            Console console = System.console();
            if (console == null) {
                throw new RuntimeException("Couldn't get 'Console' instance!");
            }
            console.printf("--- SWT-Bibliothekssoftware Password Creation Tool ---%n%n");
            console.printf("Note: The typed passwords are invisible!%n%n");
            char passwordArray[] = console.readPassword("Enter the password: ");
            char confirmPasswordArray[] = console.readPassword("Retype the password: ");
            String password = new String(passwordArray);
            String confirmPassword = new String(confirmPasswordArray);

            if (!password.equals(confirmPassword)) {
                console.printf("%nPasswords do not match. Try again!%n");
            } else {
                String hash = BCrypt.hashpw(password, BCrypt.gensalt());
                console.printf("%nGenerated hash: %s%n", hash);
            }
        } else {
            /*
                Start the application
             */
            new Application();
        }
    }

}

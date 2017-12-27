package de.swt.bibliothek;

import org.mindrot.jbcrypt.BCrypt;

import java.io.Console;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        // Password hash generation for admin user to insert users into database
        if (args.length == 1 && args[0].equals("-p")) {
            Console console = System.console();
            if (console == null) {
                System.out.println("Couldn't get Console instance");
                System.exit(1);
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
            // Start the application
            new Application();
        }
    }

}

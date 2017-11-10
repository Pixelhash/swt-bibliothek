package de.swt.bibliothek.config;

import de.swt.bibliothek.Application;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationConfig {
    private static ApplicationConfig ourInstance = new ApplicationConfig();

    public static ApplicationConfig getInstance() {
        return ourInstance;
    }

    private Properties properties = null;
    private InputStream in = null;

    private ApplicationConfig() {

    }

    public boolean load() {
        properties = new Properties();
        try {
            in = new FileInputStream("application.properties");
            properties.load(in);
        } catch (IOException ex) {
            Application.LOGGER.error("Unable to open 'application.properties' file!");
            ex.printStackTrace();
            return false;
        } finally {
          if (in != null) {
              try {
                  in.close();
              } catch (IOException ex) {
                  Application.LOGGER.error("Unable to close input stream of config!");
                  ex.printStackTrace();
              }
          }
        }
        return true;
    }
}

package de.swt.bibliothek;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import de.swt.bibliothek.config.ApplicationConfig;
import de.swt.bibliothek.controller.SearchController;
import de.swt.bibliothek.dao.*;
import de.swt.bibliothek.model.*;
import de.swt.bibliothek.util.Filters;
import de.swt.bibliothek.util.Path;
import de.swt.bibliothek.util.ViewUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class Application {

    public static Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static KategorieDao kategorieDao;
    public static BuchDao buchDao;
    public static BuchAutorDao buchAutorDao;
    public static BuchExemplarDao buchExemplarDao;
    public static AdresseDao adresseDao;
    public static BenutzerDao benutzerDao;
    public static VerlagDao verlagDao;
    public static AutorDao autorDao;

    // Order: host -> port -> name -> user -> password
    private static String DATABASE_URL = "jdbc:mysql://%s:%s/%s?user=%s&password=%s&useSSL=false";

    public static void main(String[] args) throws SQLException {
        LOGGER.info("Loading config...");
        if (!ApplicationConfig.getInstance().load()) {
            LOGGER.error("Config file is missing! Exiting...");
            System.exit(1);
        }
        LOGGER.info("Config successfully loaded!");

        ApplicationConfig config = ApplicationConfig.getInstance();
        JdbcConnectionSource connectionSource = new JdbcConnectionSource(String.format(DATABASE_URL,
                config.get(ApplicationConfig.DATABASE_HOST_KEY),
                config.get(ApplicationConfig.DATABASE_PORT_KEY),
                config.get(ApplicationConfig.DATABASE_NAME_KEY),
                config.get(ApplicationConfig.DATABASE_USER_KEY),
                config.get(ApplicationConfig.DATABASE_PASSWORD_KEY)
        ));

        if (Boolean.valueOf((String) config.get(ApplicationConfig.DATABASE_CREATE_TABLES))) {
            setupSchema(connectionSource);
        }
        if (Boolean.valueOf((String) config.get(ApplicationConfig.DATABASE_INSERT_DUMMY_DATA))) {
            setupDummyEnities();
        }
        createDaos(connectionSource);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                LOGGER.info("Closing database connection...");
                connectionSource.close();
                LOGGER.info("Database successfully closed.");
            } catch(IOException e) {
                LOGGER.error("Unable to close database!");
                e.printStackTrace();
            }
        }));

        ipAddress((String) config.get(ApplicationConfig.HOST_KEY));
        port(Integer.valueOf((String) config.get(ApplicationConfig.PORT_KEY)));

        staticFileLocation("/public");

        before("*", Filters.addTrailingSlashes);
        before("*", Filters.addGzipHeader);

        get(Path.Web.INDEX_SEARCH, SearchController.getBookSearch);
        post(Path.Web.INDEX_SEARCH, SearchController.postBookSearch);

        get("*", ViewUtil.notFound);
    }

    private static void setupSchema(JdbcConnectionSource connectionSource) throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, Kategorie.class);
        TableUtils.createTableIfNotExists(connectionSource, Buch.class);
        TableUtils.createTableIfNotExists(connectionSource, BuchAutor.class);
        TableUtils.createTableIfNotExists(connectionSource, BuchExemplar.class);
        TableUtils.createTableIfNotExists(connectionSource, Adresse.class);
        TableUtils.createTableIfNotExists(connectionSource, Benutzer.class);
        TableUtils.createTableIfNotExists(connectionSource, Verlag.class);
        TableUtils.createTableIfNotExists(connectionSource, Autor.class);
    }

    private static void createDaos(JdbcConnectionSource connectionSource) {
        kategorieDao = new KategorieDao(connectionSource, Kategorie.class);
        buchDao = new BuchDao(connectionSource, Buch.class);
        buchAutorDao = new BuchAutorDao(connectionSource, BuchAutor.class);
        buchExemplarDao = new BuchExemplarDao(connectionSource, BuchExemplar.class);
        adresseDao = new AdresseDao(connectionSource, Adresse.class);
        benutzerDao = new BenutzerDao(connectionSource, Benutzer.class);
        verlagDao = new VerlagDao(connectionSource, Verlag.class);
        autorDao = new AutorDao(connectionSource, Autor.class);
    }

    private static void setupDummyEnities() throws SQLException {
        // TODO
    }
}
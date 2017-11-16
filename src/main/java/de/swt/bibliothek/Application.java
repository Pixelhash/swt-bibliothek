package de.swt.bibliothek;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import de.swt.bibliothek.config.ApplicationConfig;
import de.swt.bibliothek.controller.SearchController;
import de.swt.bibliothek.dao.BuchAutorDao;
import de.swt.bibliothek.dao.BuchDao;
import de.swt.bibliothek.dao.BuchExemplarDao;
import de.swt.bibliothek.dao.KategorieDao;
import de.swt.bibliothek.model.Buch;
import de.swt.bibliothek.model.BuchAutor;
import de.swt.bibliothek.model.BuchExemplar;
import de.swt.bibliothek.model.Kategorie;
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

    // Reihenfolge: host -> port -> datenbank -> benutzer -> passwort
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

        setupSchema(connectionSource);
        setupDummyEnities();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    connectionSource.close(); // TODO: Check if this is actually called.
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        });

        ipAddress((String) config.get(ApplicationConfig.HOST_KEY));
        port(Integer.valueOf((String) config.get(ApplicationConfig.PORT_KEY)));

        staticFileLocation("/public");

        before("*", Filters.addTrailingSlashes);
        before("*", Filters.addGzipHeader);

        get(Path.Web.INDEX_SEARCH, SearchController.getBookSearch);
        post(Path.Web.INDEX_SEARCH, SearchController.postBookSearch);

        get("*", ViewUtil.notFound);
    }

    private static String render(Map<String, Object> model, String templatePath) {
        return new VelocityTemplateEngine().render(new ModelAndView(model, templatePath));
    }

    private static void setupSchema(JdbcConnectionSource connectionSource) throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, Kategorie.class);
        TableUtils.createTableIfNotExists(connectionSource, Buch.class);

        kategorieDao = new KategorieDao(connectionSource, Kategorie.class);
        buchDao = new BuchDao(connectionSource, Buch.class);
        buchAutorDao = new BuchAutorDao(connectionSource, BuchAutor.class);
        buchExemplarDao = new BuchExemplarDao(connectionSource, BuchExemplar.class);
    }

    private static void setupDummyEnities() throws SQLException {
        // TODO
    }
}
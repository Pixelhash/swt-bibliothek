package de.swt.bibliothek;

import com.bugsnag.Bugsnag;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import de.swt.bibliothek.config.ApplicationConfig;
import de.swt.bibliothek.config.ConfigProvider;
import de.swt.bibliothek.config.DatabaseConfig;
import de.swt.bibliothek.config.ErrorReportingConfig;
import de.swt.bibliothek.controller.BenutzerController;
import de.swt.bibliothek.controller.SearchController;
import de.swt.bibliothek.dao.*;
import de.swt.bibliothek.model.*;
import de.swt.bibliothek.util.Filters;
import de.swt.bibliothek.util.Path;
import de.swt.bibliothek.util.ViewUtil;
import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.source.ConfigurationSource;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Console;
import java.io.IOException;
import java.sql.SQLException;

import static spark.Spark.*;

public class Application {

    // Order: host -> port -> name -> user -> password
    private static final String DATABASE_URL = "jdbc:mysql://%s:%s/%s?user=%s&password=%s&useSSL=false&autoReconnect=true";

    // Logging instance
    public static Logger LOGGER = LoggerFactory.getLogger(Application.class);

    // Dao instances
    public static KategorieDao kategorieDao;
    public static BuchDao buchDao;
    public static BuchAutorDao buchAutorDao;
    public static BuchExemplarDao buchExemplarDao;
    public static AdresseDao adresseDao;
    public static BenutzerDao benutzerDao;
    public static VerlagDao verlagDao;
    public static AutorDao autorDao;

    // Database source
    private JdbcConnectionSource connectionSource;

    // Config instances
    private DatabaseConfig databaseConfig;
    private ApplicationConfig applicationConfig;
    private ErrorReportingConfig errorReportingConfig;

    public Application() throws SQLException {
        LOGGER.info("Starting application...");
        this.loadConfig();

        if (errorReportingConfig.enabled()) {
            this.startErrorReporting();
        }

        this.setupDatabaseConnection();
        this.createDaos(connectionSource);
        this.addShutdownHook();
        this.setupSpark();
    }

    @Deprecated
    private void setupSchema(JdbcConnectionSource connectionSource) throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, Kategorie.class);
        TableUtils.createTableIfNotExists(connectionSource, Buch.class);
        TableUtils.createTableIfNotExists(connectionSource, BuchAutor.class);
        TableUtils.createTableIfNotExists(connectionSource, BuchExemplar.class);
        TableUtils.createTableIfNotExists(connectionSource, Adresse.class);
        TableUtils.createTableIfNotExists(connectionSource, Benutzer.class);
        TableUtils.createTableIfNotExists(connectionSource, Verlag.class);
        TableUtils.createTableIfNotExists(connectionSource, Autor.class);
    }

    @Deprecated
    private void setupDummyEnities() throws SQLException {
        // TODO
    }

    private void setupSpark() {
        ipAddress(applicationConfig.host());
        port(applicationConfig.port());

        staticFileLocation("/public");

        before("*", Filters.addTrailingSlashes);
        before("*", Filters.addGzipHeader);
        before("*", Filters.addBasicCsrfToken);
        before("*", Filters.addBasicCsrfProtection);

        // Kunden-Search
        get(Path.Web.INDEX_SEARCH, SearchController.getKundenSearch);
        post(Path.Web.INDEX_SEARCH, SearchController.postKundenSearch);

        // Login
        get(Path.Web.LOGIN, BenutzerController.getLogin);
        post(Path.Web.LOGIN, BenutzerController.postLogin);

        // Dashboard
        get(Path.Web.DASHBOARD, BenutzerController.getUebersicht);

        get("*", ViewUtil.notFound);
    }

    private void addShutdownHook() {
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
    }

    private void createDaos(JdbcConnectionSource connectionSource) {
        kategorieDao = new KategorieDao(connectionSource, Kategorie.class);
        buchDao = new BuchDao(connectionSource, Buch.class);
        buchAutorDao = new BuchAutorDao(connectionSource, BuchAutor.class);
        buchExemplarDao = new BuchExemplarDao(connectionSource, BuchExemplar.class);
        adresseDao = new AdresseDao(connectionSource, Adresse.class);
        benutzerDao = new BenutzerDao(connectionSource, Benutzer.class);
        verlagDao = new VerlagDao(connectionSource, Verlag.class);
        autorDao = new AutorDao(connectionSource, Autor.class);
    }

    private void setupDatabaseConnection() throws SQLException {
        connectionSource = new JdbcConnectionSource(String.format(DATABASE_URL,
                databaseConfig.host(),
                databaseConfig.port(),
                databaseConfig.name(),
                databaseConfig.user(),
                databaseConfig.password()
        ));
    }

    private void loadConfig() {
        LOGGER.info("Loading config...");
        ConfigurationProvider configurationProvider = ConfigProvider.configurationProvider();
        try {
            databaseConfig = configurationProvider.bind("database", DatabaseConfig.class);
            applicationConfig = configurationProvider.bind("application", ApplicationConfig.class);
            errorReportingConfig = configurationProvider.bind("errorReporting", ErrorReportingConfig.class);
        } catch (IllegalStateException e) {
            LOGGER.error("Config is missing or invalid. Exiting...");
            System.exit(1);
        }
        LOGGER.info("Config successfully loaded!");
    }

    private void startErrorReporting() {
        LOGGER.info("Starting error reporting via 'Bugsnag'...");
        new Bugsnag(this.errorReportingConfig.apiKey());
    }
}
package de.swt.bibliothek;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import de.swt.bibliothek.config.ApplicationConfig;
import de.swt.bibliothek.config.ConfigProvider;
import de.swt.bibliothek.config.DatabaseConfig;
import de.swt.bibliothek.controller.BenutzerController;
import de.swt.bibliothek.controller.BuchController;
import de.swt.bibliothek.controller.BuchExemplarController;
import de.swt.bibliothek.controller.SearchController;
import de.swt.bibliothek.dao.*;
import de.swt.bibliothek.database.AbstractDatabase;
import de.swt.bibliothek.database.MysqlDatabase;
import de.swt.bibliothek.database.SqliteDatabase;
import de.swt.bibliothek.model.*;
import de.swt.bibliothek.util.Filters;
import de.swt.bibliothek.util.Path;
import de.swt.bibliothek.util.ViewUtil;
import org.cfg4j.provider.ConfigurationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

import static spark.Spark.*;

/**
 * Entry point for the application.
 * Connects to the database, sets all Daos and registers all available HTTP routes.
 */
public class Application {

    private static final String MYSQL_DATABASE_TYPE = "mysql";
    private static final String SQLITE_DATABASE_TYPE = "sqlite";

    // Logging instance
    public static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    // Dao instances
    private static KategorieDao kategorieDao;
    private static BuchDao buchDao;
    private static BuchAutorDao buchAutorDao;
    private static BuchExemplarDao buchExemplarDao;
    private static AdresseDao adresseDao;
    private static BenutzerDao benutzerDao;
    private static VerlagDao verlagDao;
    private static AutorDao autorDao;

    // Database
    private AbstractDatabase database;

    // Config instances
    private DatabaseConfig databaseConfig;
    private ApplicationConfig applicationConfig;

    /**
     * Starts the application. See {@link Application}.
     *
     * @throws SQLException see {@link Main#main(String[])}.
     */
    public Application() throws SQLException {
        LOGGER.info("Starting application...");
        this.loadConfig();
        this.setupDatabaseConnection();
        this.createDaos(database.getConnectionSource());
        this.addShutdownHook();
        this.setupSpark();
    }

    /**
     * Sets IP and port of the application, registers all HTTP routes
     * and starts the server.
     */
    private void setupSpark() {
        LOGGER.info("Setting routes and filters...");
        ipAddress(applicationConfig.host());
        port(applicationConfig.port());

        staticFileLocation("/public");

        /*
            The order of the following filters is important!
            CSRF token things have to be the last ones!
         */
        before("*", Filters.addTrailingSlashes);
        before("*", Filters.addGzipHeader);
        before(Path.Web.DASHBOARD, Filters.addLoginCheck); // Protect dashboard from logged out users
        before(Path.Web.DASHBOARD, Filters.refreshSessionUser); // Refresh the 'user' session object
        before("/employee/*", Filters.addLoginCheck); // Protect employee actions from logged out users
        before("/employee/*", Filters.addEmployeeCheck); // Protect employee actions from customers
        before(Path.Web.LOGIN, Filters.redirectIfLoggedIn); // Redirect to dashboard if already logged in
        before("*", Filters.addBasicCsrfProtection);
        before("*", Filters.addBasicCsrfToken);

        // Kunden-Search
        get(Path.Web.INDEX_SEARCH, SearchController.getKundenSearch);
        post(Path.Web.INDEX_SEARCH, SearchController.postKundenSearch);

        // Login
        get(Path.Web.LOGIN, BenutzerController.getLogin);
        post(Path.Web.LOGIN, BenutzerController.postLogin);

        // Logout
        post(Path.Web.LOGOUT, BenutzerController.postLogout);

        // Dashboard
        get(Path.Web.DASHBOARD, BenutzerController.getUebersicht);

        // Borrow book
        get(Path.Web.LEND, BuchExemplarController.getAusleihen);
        post(Path.Web.LEND, BuchExemplarController.postAusleihen);

        // Return book
        get(Path.Web.RETURN, BuchExemplarController.getRueckgabe);
        post(Path.Web.RETURN, BuchExemplarController.postRueckgabe);

        // Book search
        get(Path.Web.BOOK_SEARCH, SearchController.getBookSearch);
        post(Path.Web.BOOK_SEARCH, SearchController.postBookSearch);
        delete(Path.Web.BOOK_DELETE, BuchController.postLoeschen);

        //get("*", ViewUtil.notFound); Using this version shows no logging message
        notFound(ViewUtil.notFound);
        internalServerError(ViewUtil.internalServerError);
    }

    /**
     * Adds a shutdown hook, which closes the database connection on shutdown.
     */
    private void addShutdownHook() {
        LOGGER.info("Adding shutdown hook...");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                LOGGER.info("Closing database connection...");
                database.getConnectionSource().close();
                LOGGER.info("Database successfully closed.");
            } catch (IOException e) {
                LOGGER.error("Unable to close database!");
                e.printStackTrace();
            }
        }));
    }

    /**
     * Initialises all Dao-objects.
     *
     * @param connectionSource here a MySQL JDBC connection source.
     */
    private void createDaos(JdbcConnectionSource connectionSource) {
        LOGGER.info("Initialising Dao-objects...");
        kategorieDao = new KategorieDao(connectionSource, Kategorie.class);
        buchDao = new BuchDao(connectionSource, Buch.class);
        buchAutorDao = new BuchAutorDao(connectionSource, BuchAutor.class);
        buchExemplarDao = new BuchExemplarDao(connectionSource, BuchExemplar.class);
        adresseDao = new AdresseDao(connectionSource, Adresse.class);
        benutzerDao = new BenutzerDao(connectionSource, Benutzer.class);
        verlagDao = new VerlagDao(connectionSource, Verlag.class);
        autorDao = new AutorDao(connectionSource, Autor.class);
    }

    private static void setupSchema(JdbcConnectionSource connectionSource) throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, Kategorie.class);
        TableUtils.createTableIfNotExists(connectionSource, Kategorie.class);
        TableUtils.createTableIfNotExists(connectionSource, Buch.class);
        TableUtils.createTableIfNotExists(connectionSource, Buch.class);
        TableUtils.createTableIfNotExists(connectionSource, BuchAutor.class);
        TableUtils.createTableIfNotExists(connectionSource, BuchExemplar.class);
        TableUtils.createTableIfNotExists(connectionSource, Adresse.class);
        TableUtils.createTableIfNotExists(connectionSource, Benutzer.class);
        TableUtils.createTableIfNotExists(connectionSource, Verlag.class);
        TableUtils.createTableIfNotExists(connectionSource, Autor.class);
    }

    /**
     * Initialises the connection source and connects to the database.
     *
     * @throws SQLException thrown, if database connection couldn't be established.
     */
    private void setupDatabaseConnection() throws SQLException {
        String databaseType = databaseConfig.type();

        if (databaseType == null || (!databaseType.equals(MYSQL_DATABASE_TYPE) && !databaseType.equals(SQLITE_DATABASE_TYPE))) {
            throw new RuntimeException(String.format("Unknown database type '%s'!", databaseType));
        }
        LOGGER.info(String.format("Connecting to %s database...", databaseType));

        if (databaseType.equals(SQLITE_DATABASE_TYPE)) {
            database = new SqliteDatabase(SqliteDatabase.createConnectionString(databaseConfig.dbPath()));
            setupSchema(database.getConnectionSource());
        } else if (databaseType.equals(MYSQL_DATABASE_TYPE)) {
            database = new MysqlDatabase(MysqlDatabase.createConnectionString(
                databaseConfig.host(),
                databaseConfig.port(),
                databaseConfig.name(),
                databaseConfig.user(),
                databaseConfig.password()
            ));
        }
    }

    private void loadConfig() {
        LOGGER.info("Loading config...");
        ConfigurationProvider configurationProvider = ConfigProvider.configurationProvider();
        try {
            databaseConfig = configurationProvider.bind("database", DatabaseConfig.class);
            applicationConfig = configurationProvider.bind("application", ApplicationConfig.class);
        } catch (IllegalStateException e) {
            throw new RuntimeException("Config is missing or invalid.");
        }
    }

    public static BenutzerDao getBenutzerDao() {
        return benutzerDao;
    }

    public static KategorieDao getKategorieDao() {
        return kategorieDao;
    }

    public static BuchDao getBuchDao() {
        return buchDao;
    }

    public static BuchAutorDao getBuchAutorDao() {
        return buchAutorDao;
    }

    public static BuchExemplarDao getBuchExemplarDao() {
        return buchExemplarDao;
    }

    public static AdresseDao getAdresseDao() {
        return adresseDao;
    }

    public static VerlagDao getVerlagDao() {
        return verlagDao;
    }

    public static AutorDao getAutorDao() {
        return autorDao;
    }
}

package de.swt.bibliothek;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import de.swt.bibliothek.config.ApplicationConfig;
import de.swt.bibliothek.controller.SearchController;
import de.swt.bibliothek.dao.BuchDao;
import de.swt.bibliothek.dao.KategorieDao;
import de.swt.bibliothek.model.Buch;
import de.swt.bibliothek.model.Kategorie;
import de.swt.bibliothek.util.Filters;
import de.swt.bibliothek.util.Path;
import de.swt.bibliothek.util.ViewUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class Application {

    public static Logger LOGGER = LoggerFactory.getLogger(Application.class);
    public static KategorieDao kategorieDao;
    public static BuchDao buchDao;

    // Reihenfolge: host -> datenbank -> benutzer -> passwort
    private static String DATABASE_URL = "jdbc:mysql://%s/%s?user=%s&password=%s&useSSL=false";

    public static void main(String[] args) {
        LOGGER.info("Loading config...");
        if (!ApplicationConfig.getInstance().load()) {
            LOGGER.error("Config file is missing! Exiting...");
            System.exit(1);
        }
        LOGGER.info("Config successfully loaded!");

        ApplicationConfig config = ApplicationConfig.getInstance();
        try {
            JdbcConnectionSource connectionSource = new JdbcConnectionSource(String.format(DATABASE_URL,
                    config.get(ApplicationConfig.DATABASE_HOST_KEY),
                    config.get(ApplicationConfig.DATABASE_NAME_KEY),
                    config.get(ApplicationConfig.DATABASE_USER_KEY),
                    config.get(ApplicationConfig.DATABASE_PASSWORD_KEY)
            ));

            kategorieDao = new KategorieDao(connectionSource, Kategorie.class);
            buchDao = new BuchDao(connectionSource, Buch.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

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
}
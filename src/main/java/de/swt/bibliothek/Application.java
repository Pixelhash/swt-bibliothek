package de.swt.bibliothek;

import de.swt.bibliothek.config.ApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class Application {

    public static Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        LOGGER.info("Loading config...");
        if (!ApplicationConfig.getInstance().load()) {
            LOGGER.error("Config file is missing! Exiting...");
            System.exit(1);
        }
        LOGGER.info("Config successfully loaded!");


        staticFileLocation("/public");

        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("title", "Bibliothek | Suche");
            return render(model, "velocity/index.vm");
        });

    }

    private static String render(Map<String, Object> model, String templatePath) {
        return new VelocityTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}
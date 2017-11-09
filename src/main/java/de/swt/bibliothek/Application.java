package de.swt.bibliothek;

import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class Application {
    public static void main(String[] args) {

        staticFileLocation("/public");

        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("message", "Hello World!");
            model.put("title", "Index");
            return render(model, "velocity/index.vm");
        });

    }

    private static String render(Map<String, Object> model, String templatePath) {
        return new VelocityTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}
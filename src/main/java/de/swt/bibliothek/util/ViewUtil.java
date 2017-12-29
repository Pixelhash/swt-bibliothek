package de.swt.bibliothek.util;

import de.swt.bibliothek.Application;
import org.apache.velocity.app.VelocityEngine;
import org.eclipse.jetty.http.HttpStatus;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.velocity.VelocityTemplateEngine;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ViewUtil {

    public static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);

    public static String render(Request req, Map<String, Object> model, String templatePath) {
        model.put("msg", new MessageBundle());
        model.put("session", req.session());
        model.put("Application", Application.class); // Access to Dao objects
        model.put("WebPath", Path.Web.class); // Access application URLs from templates
        model.put("Math", Math.class); // To be able to calculate in view
        return strictVelocityEngine().render(new ModelAndView(model, templatePath));
    }

    public static String returnError(Request req, Map<String, Object> model, String templatePath, String title, String errorMsg) {
        model.put("title", title);
        model.put("error", true);
        model.put("errorMsg", errorMsg);
        return ViewUtil.render(req, model, templatePath);
    }

    public static String returnSuccess(Request req, Map<String, Object> model, String templatePath, String title, String successMsg) {
        model.put("title", title);
        model.put("success", true);
        model.put("successMsg", successMsg);
        return ViewUtil.render(req, model, templatePath);
    }

    public static Route notAcceptable = (Request req, Response res) -> {
        res.status(HttpStatus.NOT_ACCEPTABLE_406);
        return new MessageBundle().get("ERROR_406_NOT_ACCEPTABLE");
    };

    public static Route notFound = (Request req, Response res) -> {
        Map<String, Object> model = new HashMap<>();
        model.put("title", "Fehler | Bibliothek");
        res.status(HttpStatus.NOT_FOUND_404);
        return render(req, model, Path.Template.NOT_FOUND);
    };

    public static Route internalServerError = (Request req, Response res) -> {
        Map<String, Object> model = new HashMap<>();
        model.put("title", "Fehler | Bibliothek");
        res.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
        return render(req, model, Path.Template.INTERNAL_SERVER_ERROR);
    };

    private static VelocityTemplateEngine strictVelocityEngine() {
        VelocityEngine configuredEngine = new VelocityEngine();
        configuredEngine.setProperty("runtime.references.strict", true);
        configuredEngine.setProperty("resource.loader", "class");
        configuredEngine.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        return new VelocityTemplateEngine(configuredEngine);
    }
}

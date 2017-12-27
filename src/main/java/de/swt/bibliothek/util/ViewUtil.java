package de.swt.bibliothek.util;

import org.apache.velocity.app.VelocityEngine;
import org.eclipse.jetty.http.HttpStatus;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.velocity.VelocityTemplateEngine;
import java.util.HashMap;
import java.util.Map;

public class ViewUtil {

    public static String render(Request req, Map<String, Object> model, String templatePath) {
        model.put("msg", new MessageBundle());
        model.put("session", req.session());
        //model.put("currentUser", getSessionCurrentUser(req));
        model.put("WebPath", Path.Web.class); // Access application URLs from templates
        return strictVelocityEngine().render(new ModelAndView(model, templatePath));
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

    private static VelocityTemplateEngine strictVelocityEngine() {
        VelocityEngine configuredEngine = new VelocityEngine();
        configuredEngine.setProperty("runtime.references.strict", true);
        configuredEngine.setProperty("resource.loader", "class");
        configuredEngine.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        return new VelocityTemplateEngine(configuredEngine);
    }
}

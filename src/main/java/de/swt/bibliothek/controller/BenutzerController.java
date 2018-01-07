package de.swt.bibliothek.controller;

import de.swt.bibliothek.Application;
import de.swt.bibliothek.model.Benutzer;
import de.swt.bibliothek.util.*;
import org.mindrot.jbcrypt.BCrypt;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

public class BenutzerController {

    public static Route getLogin = (Request req, Response res) -> {
        Map<String, Object> model = new HashMap<>();
        model.put("title", "Login | Bibliothek");
        return ViewUtil.render(req, model, Path.Template.LOGIN);
    };

    public static Route postLogin = (Request req, Response res) -> {
        Map<String, Object> model = new HashMap<>();
        String userId = RequestUtil.getBenutzerId(req);
        String password = RequestUtil.getBenutzerPassword(req);

        if (Validation.isValidId(userId) && Validation.isValidPassword(password)) {
            // Input fields are valid
            model.put("title", "Übersicht | Bibliothek");
            Benutzer benutzer = Application.getBenutzerDao().getBenutzer(Integer.parseInt(userId));
            if (benutzer != null) {
                // User exists
                if (BCrypt.checkpw(password, benutzer.getPasswort())) {
                    // Password is correct
                    req.session().attribute("user", benutzer);
                    res.redirect(Path.Web.DASHBOARD);
                    return null;
                }
            }
        }
        Application.LOGGER.warn("Invalid login!");
        // Invalid or missing login parameters
        model.put("title", "Login | Bibliothek");
        model.put("error", true);
        model.put("errorMsg", new MessageBundle().get("ERROR_INVALID_LOGIN"));
        return ViewUtil.render(req, model, Path.Template.LOGIN);
    };

    public static Route getUebersicht = (Request req, Response res) -> {
        Map<String, Object> model = new HashMap<>();
        Benutzer benutzer = req.session().attribute("user");
        String userMessage = String.format(new MessageBundle().get("DASHBOARD_SUBTITLE"),
                benutzer.getFullName());

        model.put("title", "Übersicht | Bibliothek");
        model.put("userMsg", userMessage);
        return ViewUtil.render(req, model, Path.Template.CUSTOMER_DASHBOARD);
    };

    public static Route postLogout = (Request req, Response res) -> {
        if (req.session().attribute("user") != null) {
            req.session().removeAttribute("user");
        }
        res.redirect(Path.Web.LOGIN);
        return null;
    };


}

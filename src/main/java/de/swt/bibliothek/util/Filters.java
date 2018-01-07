package de.swt.bibliothek.util;

import de.swt.bibliothek.Application;
import de.swt.bibliothek.model.Benutzer;
import org.eclipse.jetty.http.HttpStatus;
import spark.Filter;
import spark.Request;
import spark.Response;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static spark.Spark.halt;

public class Filters {

    public static final String CSRF_TOKEN = "csrf_token";

    public static Filter addTrailingSlashes = (Request req, Response res) -> {
        if (!req.pathInfo().endsWith("/")) {
            res.redirect(req.pathInfo() + "/");
        }
    };

    public static Filter addGzipHeader = (Request req, Response res) -> {
        res.header("Content-Encoding", "gzip");
    };

    public static Filter addBasicCsrfToken = (Request req, Response res) -> {
        if (req.requestMethod().equals("GET") && req.session().attribute(CSRF_TOKEN) == null) {
            req.session().attribute(CSRF_TOKEN, genCsrfToken());
        }
    };

    public static Filter addBasicCsrfProtection = (Request req, Response res) -> {
        List<String> safeMethods = Arrays.asList("GET", "OPTIONS", "HEAD");
        if (!safeMethods.contains(req.requestMethod())) {
            String requestToken = null;
            String sessionToken = req.session().attribute(CSRF_TOKEN);
            if (!req.contentType().equalsIgnoreCase("application/json")) {
                requestToken = RequestUtil.getCsrfToken(req);
            } else {
                requestToken = Optional.of(req.headers("X-CSRF-TOKEN")).orElse("");
            }
            //Application.LOGGER.info(String.format("Request token: %s; Session token: %s", requestToken, sessionToken));
            if (requestToken == null || sessionToken == null || !requestToken.equals(sessionToken)) {
                Application.LOGGER.warn("Invalid CSRF token!");
                halt(403, "Invalid session. Please reload the page!");
            }
        }
    };

    public static Filter addLoginCheck = (Request req, Response res) -> {
        if (req.session().attribute("user") == null) {
            res.redirect(Path.Web.LOGIN);
            halt();
        }
    };

    public static Filter redirectIfLoggedIn = (Request req, Response res) -> {
        if (req.session().attribute("user") != null) {
            res.redirect(Path.Web.DASHBOARD);
            halt();
        }
    };

    public static Filter refreshSessionUser = (Request req, Response res) -> {
        if (req.session().attribute("user") != null) {
            Application.getBenutzerDao().getRawDao().refresh(req.session().attribute("user"));
        }
    };

    public static Filter addEmployeeCheck = (Request req, Response res) -> {
        Benutzer benutzer = req.session().attribute("user");
        if (benutzer != null && benutzer.getRolle() == Benutzer.Rolle.KUNDE) {
            halt(HttpStatus.FORBIDDEN_403, "You are not allowed to browse this page!");
        }
    };

    private static String genCsrfToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}

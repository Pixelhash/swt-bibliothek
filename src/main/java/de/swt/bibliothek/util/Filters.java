package de.swt.bibliothek.util;

import de.swt.bibliothek.Application;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Session;

import java.util.*;

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
        if (req.session().attribute(CSRF_TOKEN) == null) {
            req.session().attribute(CSRF_TOKEN, genCsrfToken());
        }
    };

    public static Filter addBasicCsrfProtection = (Request req, Response res) -> {
        List<String> safeMethods = Arrays.asList("GET", "OPTIONS", "HEAD");
        if (!safeMethods.contains(req.requestMethod())) {
            String requestToken = RequestUtil.getCsrfToken(req);
            String sessionToken = req.session().attribute(CSRF_TOKEN);
            //Application.LOGGER.info(String.format("Request token: %s; Session token: %s", requestToken, sessionToken));
            if (requestToken == null || sessionToken == null || !requestToken.equals(sessionToken)) {
                Application.LOGGER.warn("Invalid CSRF token!");
                halt(403, "Invalid token!");
            }
        }
    };

    private static String genCsrfToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}

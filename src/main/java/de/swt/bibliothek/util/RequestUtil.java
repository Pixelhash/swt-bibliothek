package de.swt.bibliothek.util;

import spark.Request;

public class RequestUtil {

    public static String getQuerySearch(Request req) {
        return req.queryParams("query");
    }

    public static String getCsrfToken(Request req) {
        return req.queryParams(Filters.CSRF_TOKEN);
    }

    public static String getBenutzerId(Request req) {
        return req.queryParams("user_id");
    }

    public static String getBenutzerPassword(Request req) {
        return req.queryParams("password");
    }

    public static String getBuchExemplarId(Request req) {
        return req.queryParams("bookexemplar_id");
    }

}

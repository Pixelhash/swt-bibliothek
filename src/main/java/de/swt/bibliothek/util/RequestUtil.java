package de.swt.bibliothek.util;

import spark.Request;

public class RequestUtil {

    public static String getQuerySearch(Request req) {
        return req.queryParams("query");
    }

    public static String getCsrfToken(Request req) {
        return req.queryParams(Filters.CSRF_TOKEN);
    }

}

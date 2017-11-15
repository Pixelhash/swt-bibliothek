package de.swt.bibliothek.util;

import spark.Request;

public class RequestUtil {

    public static String getQuerySearch(Request request) {
        return request.queryParams("query");
    }

}

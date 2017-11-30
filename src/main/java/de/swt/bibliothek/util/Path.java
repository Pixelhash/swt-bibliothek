package de.swt.bibliothek.util;

public class Path {

    public static class Web {
        public static final String INDEX_SEARCH = "/";

        public static String getIndexSearch() {
            return INDEX_SEARCH;
        }
    }

    public static class Template {
        public static final String INDEX_SEARCH = "/velocity/pages/index.vm";
        public static final String INDEX_RESULTS = "/velocity/pages/search_results.vm";
        public static final String NOT_FOUND = "/velocity/error/404.vm";
    }

}

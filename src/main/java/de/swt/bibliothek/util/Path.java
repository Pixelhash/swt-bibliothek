package de.swt.bibliothek.util;

public class Path {

    public static class Web {
        public static final String INDEX_SEARCH = "/";
        public static final String BOOK_SEARCH = "/employee/search/book/";
        public static final String BOOK_DELETE = "/employee/book/:id/";

        public static final String LOGIN = "/login/";
        public static final String LOGOUT = "/logout/";
        public static final String DASHBOARD = "/dashboard/";
        public static final String LEND = "/employee/borrow/";
        public static final String RETURN = "/employee/return/";

        public static String getIndexSearch() {
            return INDEX_SEARCH;
        }

        public static String getBookSearch() {
            return BOOK_SEARCH;
        }

        public static String getLogin() {
            return LOGIN;
        }

        public static String getLogout() {
            return LOGOUT;
        }

        public static String getDashboard() {
            return DASHBOARD;
        }

        public static String getLend() {
            return LEND;
        }

        public static String getReturn() {
            return RETURN;
        }
    }

    public static class Template {
        public static final String INDEX_SEARCH = "/velocity/pages/index.vm";
        public static final String INDEX_RESULTS = "/velocity/pages/search_results.vm";
        public static final String BOOK_SEARCH = "/velocity/pages/advanced_search/book_search.vm";
        public static final String BOOK_RESULTS = "/velocity/pages/advanced_search/book_results.vm";
        public static final String NOT_FOUND = "/velocity/error/404.vm";
        public static final String INTERNAL_SERVER_ERROR = "/velocity/error/500.vm";
        public static final String LOGIN = "/velocity/pages/login.vm";
        public static final String CUSTOMER_DASHBOARD = "/velocity/pages/dashboard.vm";
        public static final String LEND = "/velocity/pages/borrow.vm";
        public static final String RETURN = "/velocity/pages/return.vm";
    }

}

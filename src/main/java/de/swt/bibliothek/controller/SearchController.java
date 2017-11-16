package de.swt.bibliothek.controller;

import com.j256.ormlite.stmt.QueryBuilder;
import de.swt.bibliothek.Application;
import de.swt.bibliothek.model.Buch;
import de.swt.bibliothek.util.MessageBundle;
import de.swt.bibliothek.util.Path;
import de.swt.bibliothek.util.RequestUtil;
import de.swt.bibliothek.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchController {

    public static Route getBookSearch = (Request req, Response res) -> {
        Map<String, Object> model = new HashMap<>();
        model.put("title", "Bibliothek | Suche");
        return ViewUtil.render(req, model, Path.Template.INDEX_SEARCH);
    };

    public static Route postBookSearch = (Request req, Response res) -> {
        Map<String, Object> model = new HashMap<>();
        model.put("title", "Bibliothek | Ergebnisse");
        String searchQuery = RequestUtil.getQuerySearch(req);

        if (searchQuery.isEmpty()) {
            model.put("error", true);
            model.put("errorMsg", new MessageBundle().get("ERROR_MISSING_FIELDS"));
            return ViewUtil.render(req, model, Path.Template.INDEX_SEARCH);
        } else if (searchQuery.length() < 3) {
            model.put("error", true);
            model.put("errorMsg", new MessageBundle().get("ERROR_SHORT_QUERY"));
            return ViewUtil.render(req, model, Path.Template.INDEX_SEARCH);
        }

        model.put("query", searchQuery);
        List<Buch> results = Application.buchDao.getSearchedBooks(searchQuery);

        if (results.isEmpty()) {
            model.put("error", true);
            model.put("errorMsg", new MessageBundle().get("ERROR_NO_RESULTS"));
            return ViewUtil.render(req, model, Path.Template.INDEX_SEARCH);
        }

        model.put("amount", results.size());
        model.put("books", results);
        return ViewUtil.render(req, model, Path.Template.INDEX_RESULTS);
    };

}

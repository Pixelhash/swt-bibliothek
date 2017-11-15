package de.swt.bibliothek.controller;

import com.j256.ormlite.stmt.QueryBuilder;
import de.swt.bibliothek.Application;
import de.swt.bibliothek.model.Buch;
import de.swt.bibliothek.util.Path;
import de.swt.bibliothek.util.RequestUtil;
import de.swt.bibliothek.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

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
        model.put("query", searchQuery);
        QueryBuilder<Buch, Integer> queryBuilder = Application.buchDao.getRawDao().queryBuilder();
        queryBuilder.where().like("titel", "%" + searchQuery + "%");
        List<Buch> results = Application.buchDao.getRawDao().query(queryBuilder.prepare());
        model.put("amount", results.size());
        model.put("books", results);
        return ViewUtil.render(req, model, Path.Template.INDEX_RESULTS);
    };

}

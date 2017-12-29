package de.swt.bibliothek.controller;

import de.swt.bibliothek.Application;
import de.swt.bibliothek.model.Benutzer;
import de.swt.bibliothek.model.BuchExemplar;
import de.swt.bibliothek.util.*;
import org.mindrot.jbcrypt.BCrypt;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BuchExemplarController {

    public static Route getAusleihen = (Request req, Response res) -> {
        Map<String, Object> model = new HashMap<>();
        model.put("title", "Ausleihe | Bibliothek");
        return ViewUtil.render(req, model, Path.Template.LEND);
    };

    public static Route postAusleihen = (Request req, Response res) -> {
        //TODO: Check if already logged in!
        Map<String, Object> model = new HashMap<>();
        String userId = RequestUtil.getBenutzerId(req);
        String bookExemplarId = RequestUtil.getBuchExemplarId(req);

        if (Validation.isValidId(userId) && Validation.isValidId(bookExemplarId)) {
            // Input fields are valid
            Benutzer benutzer = Application.benutzerDao.getBenutzer(Integer.parseInt(userId));
            BuchExemplar buchExemplar = Application.buchExemplarDao.getBuchExemplar(Integer.parseInt(bookExemplarId));

            if (benutzer == null) {
                return ViewUtil.returnError(
                        req,
                        model,
                        Path.Template.LEND,
                        "Ausleihe | Bibliothek",
                        new MessageBundle().get("ERROR_USER_NOT_FOUND")
                );
            } else if (buchExemplar == null) {
                return ViewUtil.returnError(
                        req,
                        model,
                        Path.Template.LEND,
                        "Ausleihe | Bibliothek",
                        new MessageBundle().get("ERROR_BOOKEXEMPLAR_NOT_FOUND")
                );
            }
            // User and book exemplar exist

            boolean success = Application.buchExemplarDao.borrow(benutzer, buchExemplar);
            if (!success) { // Book already borrowed
                return ViewUtil.returnError(
                        req,
                        model,
                        Path.Template.LEND,
                        "Ausleihe | Bibliothek",
                        new MessageBundle().get("ERROR_BOOKEXEMPLAR_BORROWED")
                );
            }

            // Book exemplar is not borrowed yet
            return ViewUtil.returnSuccess(
                    req,
                    model,
                    Path.Template.LEND,
                    "Ausleihe | Bibliothek",
                    new MessageBundle().get("SUCCESS_BOOK_BORROWED")
            );
        }
        Application.LOGGER.warn("Invalid borrow data!");
        // Invalid or missing login parameters
        return ViewUtil.returnError(
                req,
                model,
                Path.Template.LEND,
                "Ausleihe | Bibliothek",
                new MessageBundle().get("ERROR_INVALID_BORROW_DATA")
        );
    };
}

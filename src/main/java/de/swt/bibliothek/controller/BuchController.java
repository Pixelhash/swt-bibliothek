package de.swt.bibliothek.controller;

import de.swt.bibliothek.Application;
import de.swt.bibliothek.util.Validation;
import de.swt.bibliothek.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

public class BuchController {

    private static final int WRONG_CONTENT_TYPE = 1;
    private static final int ID_MISSING = 2;
    private static final int ID_INVALID = 3;
    private static final int BOOK_NOT_FOUND = 4;
    private static final int BOOK_CANT_DELETE = 5;
    private static final int BOOK_DELETED = 6;

    public static Route postLoeschen = (Request req, Response res) -> {
        if (!req.contentType().equals("application/json")) {
            return ViewUtil.returnJsonError("Wrong content-type!", WRONG_CONTENT_TYPE);
        }
        String idString = req.params(":id");
        if (idString == null || idString.isEmpty()) {
            return ViewUtil.returnJsonError("ID field is missing!", ID_MISSING);
        }
        if (!Validation.isValidId(idString)) {
            return ViewUtil.returnJsonError("ID is not valid!", ID_INVALID);
        }
        int id = Integer.parseInt(idString);
        boolean success = Application.getBuchDao().delete(id);
        if (!success) {
            return ViewUtil.returnJsonError("Could not delete book!", BOOK_CANT_DELETE);
        } else {
            return ViewUtil.returnJsonError("Book deleted!", BOOK_DELETED);
        }
    };

}
